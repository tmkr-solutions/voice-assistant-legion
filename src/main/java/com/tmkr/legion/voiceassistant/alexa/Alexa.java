package com.tmkr.legion.voiceassistant.alexa;

import com.tmkr.legion.core.VoiceAssistant;
import com.tmkr.legion.core.event.DiscoveryEvent;
import com.tmkr.legion.core.event.Event;
import com.tmkr.legion.core.event.PowerEvent;
import com.tmkr.legion.core.exception.NoSuitableEventFound;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Alexa extends VoiceAssistant {

  @Autowired
  private AlexaMappingService alexaMappingService;

  @Override
  public String mapEventsToResponseString(List<Event> responseEvent) {
    return alexaMappingService.mapToAlexaJson(responseEvent);
  }

  @Override
  public List<Event> mapRequestToEvents(String json) {
    JSONObject base = new JSONObject(json);
    JSONObject directive = base.getJSONObject("directive");
    JSONObject header = directive.getJSONObject("header");
    String messageId = (String) header.get("messageId");
    String namespace = (String) header.get("namespace");

    if (namespace.equals("Alexa.Discovery")) {
      DiscoveryEvent discoveryEvent = new DiscoveryEvent();
      discoveryEvent.setMessageId(messageId);
      return Collections.singletonList(discoveryEvent);
    }

    if (namespace.equals("Alexa.PowerController")) {
      String name = (String) header.get("name");
      boolean turnOn = name.equals("TurnOn");
      String deviceId = (String) directive.getJSONObject("endpoint").get("endpointId");

      PowerEvent powerEvent = new PowerEvent();
      powerEvent.setMessageId(messageId);
      powerEvent.setDeviceId(deviceId);
      powerEvent.setOn(turnOn);
      return Collections.singletonList(powerEvent);
    }

    throw new NoSuitableEventFound();
  }
}
