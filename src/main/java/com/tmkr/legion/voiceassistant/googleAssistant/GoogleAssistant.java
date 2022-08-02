package com.tmkr.legion.voiceassistant.googleAssistant;

import com.tmkr.legion.core.VoiceAssistant;
import com.tmkr.legion.core.event.DeviceStateEvent;
import com.tmkr.legion.core.event.DiscoveryEvent;
import com.tmkr.legion.core.event.Event;
import com.tmkr.legion.core.event.PowerEvent;
import com.tmkr.legion.core.event.StatusEvent;
import com.tmkr.legion.core.exception.NoSuitableEventFound;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleAssistant extends VoiceAssistant {

  @Autowired
  private GoogleMappingService goggleMappingService = new GoogleMappingService();

  @Override
  public String mapEventsToResponseString(List<Event> responseEvents) {
    return goggleMappingService.mapToGoogleJson(responseEvents);
  }

  @Override
  public List<Event> mapRequestToEvents(String json) {
    JSONObject jsonObject = new JSONObject(json);
    String messageId = (String) jsonObject.get("requestId");

    JSONArray inputs = jsonObject.getJSONArray("inputs");
    List<Event> events = new ArrayList<>();
    for (Object input : inputs) {
      events.addAll(mapRequestToEvent(messageId, (JSONObject) input));
    }

    return events;
  }

  private List<Event> mapRequestToEvent(String messageId, JSONObject jsonObject) {

    String intent = (String) jsonObject.get("intent");

    if (intent.equals("action.devices.SYNC")) {
      DiscoveryEvent discoveryEvent = new DiscoveryEvent();
      discoveryEvent.setMessageId(messageId);
      return Collections.singletonList(discoveryEvent);
    }

    if (intent.contains("action.devices.QUERY")) {
      JSONArray jsonArray = jsonObject.getJSONObject("payload").getJSONArray("devices");

      StatusEvent statusEvent = new StatusEvent();
      statusEvent.setMessageId(messageId);

      List<DeviceStateEvent> stateEvents = new ArrayList<>();
      jsonArray.iterator().forEachRemaining(device -> {
        JSONObject jsonDevice = (JSONObject) device;
        String deviceId = (String) jsonDevice.get("id");

        DeviceStateEvent stateEvent = new DeviceStateEvent();
        stateEvent.setDeviceId(deviceId);
        stateEvents.add(stateEvent);
      });

      statusEvent.setDeviceStates(stateEvents);
      return Collections.singletonList(statusEvent);
    }

    if (intent.contains("action.devices.EXECUTE")) {
      List<Event> events = new ArrayList<>();

      JSONObject payload = jsonObject.getJSONObject("payload");
      JSONArray commands = payload.getJSONArray("commands");

      commands.iterator().forEachRemaining(command -> {
        JSONObject commandJson = (JSONObject) command;

        JSONArray devices = commandJson.getJSONArray("devices");
        JSONArray executions = commandJson.getJSONArray("execution");

        devices.iterator().forEachRemaining(device -> {

          JSONObject jsonDevice = (JSONObject) device;
          String deviceId = (String) jsonDevice.get("id");

          executions.iterator().forEachRemaining(execution -> {

            Event event = mapToEvent(messageId, (JSONObject) execution, deviceId);
            events.add(event);

          });
        });
      });

      return events;
    }

    throw new NoSuitableEventFound();
  }

  private Event mapToEvent(String messageId, JSONObject execution, String deviceId) {
    String command = (String) execution.get("command");

    if (command.equals("action.devices.commands.OnOff")) {
      boolean on = (Boolean) execution.getJSONObject("params").get("on");
      PowerEvent powerEvent = new PowerEvent();
      powerEvent.setMessageId(messageId);
      powerEvent.setDeviceId(deviceId);
      powerEvent.setOn(on);
      return powerEvent;
    }

    throw new NoSuitableEventFound();
  }
}
