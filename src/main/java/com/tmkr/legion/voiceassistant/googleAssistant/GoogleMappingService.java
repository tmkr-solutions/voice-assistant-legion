package com.tmkr.legion.voiceassistant.googleAssistant;

import com.tmkr.legion.core.event.DiscoveryEvent;
import com.tmkr.legion.core.event.Event;
import com.tmkr.legion.core.event.PowerEvent;
import com.tmkr.legion.core.event.DeviceStateEvent;
import com.tmkr.legion.core.model.Device;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleMappingService {

  public String mapToGoogleJson(List<Event> responseEvents) {
    String responseJson = "{}";

    List<Event> events = new ArrayList<>();
    for (Event responseEvent : responseEvents) {
      if (responseEvent instanceof DiscoveryEvent discoveryEvent) {
        responseJson = mapDiscoveryEvent(discoveryEvent);
        break;
      }

      events.add(responseEvent);
    }

    if(events.isEmpty())
      return responseJson;

    Event firstEvent = events.get(0);
    String messageId = firstEvent.getMessageId();

    if (firstEvent instanceof DeviceStateEvent) {
      List<DeviceStateEvent> stateEvents = events.stream().map(event -> (DeviceStateEvent) event).collect(Collectors.toList());
      responseJson = StateMapper.mapStateEvents(messageId, stateEvents);
    }

    if (firstEvent instanceof PowerEvent) {
      List<PowerEvent> powerEvents = events.stream().map(event -> (PowerEvent) event).collect(Collectors.toList());
      responseJson = PowerMapper.mapPowerEvents(messageId, powerEvents);
    }

    return normalizeJson(responseJson);
  }

  private String mapDiscoveryEvent(DiscoveryEvent discoveryEvent) {

    List<String> devices = new ArrayList<>();
    for (Device device : discoveryEvent.getDevices()) {
      String deviceJson = DEVICE_FRAME;
      deviceJson = deviceJson.replace("$endpointId", device.getDeviceId());
      devices.add(deviceJson);
    }

    String discoveryJson = DISCOVERY_FRAME;
    discoveryJson = discoveryJson.replace("$messageId", discoveryEvent.getMessageId());
    discoveryJson = discoveryJson.replace("$userId", UUID.randomUUID().toString());
    discoveryJson = discoveryJson.replace("$endpoints", String.join(", ", devices));

    return discoveryJson;
  }

  private String normalizeJson(String jsonString) {
    JSONObject json = new JSONObject(jsonString);
    return json.toString();
  }

  private final String DISCOVERY_FRAME = """
      {
        "requestId": "$messageId",
        "payload": {
          "agentUserId": "1836.15267389",
          "devices": [
            $endpoints
          ]
        }
      }
      """;

  private final String DEVICE_FRAME = """
      {
       "id": "$endpointId",
       "type": "action.devices.types.OUTLET",
       "traits": [
         "action.devices.traits.OnOff"
       ],
       "name": {
         "name": "Light"
       },
       "willReportState": false,
      }
      """;
}
