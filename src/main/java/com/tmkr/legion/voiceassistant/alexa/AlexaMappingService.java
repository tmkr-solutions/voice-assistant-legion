package com.tmkr.legion.voiceassistant.alexa;

import com.tmkr.legion.core.event.DiscoveryEvent;
import com.tmkr.legion.core.event.Event;
import com.tmkr.legion.core.event.PowerEvent;
import com.tmkr.legion.core.model.Capability;
import com.tmkr.legion.core.model.Category;
import com.tmkr.legion.core.model.Device;
import com.tmkr.legion.core.model.states.PowerState;
import com.tmkr.legion.core.model.states.State;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlexaMappingService {

  public String mapToAlexaJson(List<Event> responseEvents) {
    String responseJson = "{}";

    Event responseEvent = responseEvents.get(0); //TODO alexa schickt immer nur ein event und erwartet auch nur eins zurück
    if (responseEvent instanceof DiscoveryEvent discoveryEvent) {
      responseJson = mapDiscoveryEvent(discoveryEvent);
    }

    if (responseEvent instanceof PowerEvent powerEvent) {
      responseJson = mapPowerEvent(powerEvent);
    }

    return normalizeJson(responseJson);
  }

  private String mapDiscoveryEvent(DiscoveryEvent discoveryEvent) {

    List<String> devices = new ArrayList<>();
    for (Device device : discoveryEvent.getDevices()) {
      String deviceJson = DEVICE_FRAME;
      deviceJson = deviceJson.replace("$endpointId", device.getDeviceId());
      deviceJson = deviceJson.replace("$manufacturerName", device.getDeviceId());
      deviceJson = deviceJson.replace("$description", device.getDeviceId());
      deviceJson = deviceJson.replace("$name", device.getDeviceId());
      deviceJson = deviceJson.replace("$manufacturerName", device.getDeviceId());
      deviceJson = deviceJson.replace("$categories", device.getCategories().stream().map(this::mapCategory).collect(Collectors.joining(", ")));
      deviceJson = deviceJson.replace("$capabilities", device.getCapabilities().stream().map(this::mapCapability).collect(Collectors.joining(", ")));
      devices.add(deviceJson);
    }

    String discoveryJson = DISCOVERY_FRAME;
    discoveryJson = discoveryJson.replace("$messageId", discoveryEvent.getMessageId());
    discoveryJson = discoveryJson.replace("$endpoints", String.join(", ", devices));

    return discoveryJson;
  }

  private String mapCategory(Category category) {
    switch (category) {
      case LIGHT -> {
        return "LIGHT";
      }
    }

    return null; //TODO
  }

  private String mapCapability(Capability capability) {
    switch (capability) {
      case POWER -> {
        return CAPABILITY_POWER;
      }
    }

    return null; //TODO
  }

  private String mapPowerEvent(PowerEvent powerEvent) {

    String base = EVENT_BASE_FRAME;
    base = base.replace("$messageId", powerEvent.getMessageId());
    base = base.replace("$correlationToken", UUID.randomUUID().toString()); //TODO
    base = base.replace("$bearerToken", powerEvent.getToken());
    base = base.replace("$deviceId", powerEvent.getDeviceId());

    List<String> propertyList = new ArrayList<>();
    for (State state : powerEvent.getDeviceStates()) {

      String properties = PROPERTIES_FRAME;
      properties = properties.replace("$date", new Date().toString());

      if (state instanceof PowerState powerState) {
        properties = properties.replace("$namespace", "Alexa.PowerController");
        properties = properties.replace("$name", "powerState");

        String additionalProperties = ADDITIONAL_PROPERTIES_POWER_FRAME;
        String value = powerState.isOn() ? "ON" : "OFF";
        additionalProperties = additionalProperties.replace("$value", value);
        properties = properties.replace("$additionalProperties", additionalProperties);
      }

      propertyList.add(properties);
    }

    String propertiesString = String.join(", ", propertyList);
    return base.replace("$properties", propertiesString);
  }

  private String normalizeJson(String jsonString) {
    JSONObject json = new JSONObject(jsonString);
    return json.toString();
  }

  private final String DEVICE_FRAME = """
      {
                "endpointId": "$endpointId",
      		      "manufacturerName": "$manufacturerName",
                "description": "$description",
                "friendlyName": "$name",
                "displayCategories": [$categories],
                "capabilities": [
                  $capabilities
                ]
              }
      """;

  private final String DISCOVERY_FRAME = """
      {
        "event": {
          "header": {
            "namespace": "Alexa.Discovery",
            "name": "Discover.Response",
            "payloadVersion": "3",
            "messageId": "$messageId"
          },
          "payload": {
            "endpoints": [
              $endpoints
            ]
          }
        }
      }
      """;

  private final String CAPABILITY_POWER = """
      {
         "type": "AlexaInterface",
         "interface": "Alexa.PowerController",
         "version": "3"
      }
      """;
  private final String EVENT_BASE_FRAME = """
      {
        "event": {
          "header": {
            "namespace": "Alexa",
            "name": "Response",
            "messageId": "$messageId",
            "correlationToken": "$correlationToken",
            "payloadVersion": "3"
          },
          "endpoint": {
            "scope": {
              "type": "BearerToken",
              "token": "$bearerToken"
            },
            "endpointId": "$deviceId"
          },
          "payload": {}
        },
        "context": {
          "properties": [
            $properties
          ]
        }
      }
      """;

  private final String PROPERTIES_FRAME = """
      {
          "namespace": "$namespace",
          "name": "$name",
          $additionalProperties
          "timeOfSample": "$date",
          "uncertaintyInMilliseconds": 500
      }
      """;

  private final String ADDITIONAL_PROPERTIES_POWER_FRAME = """
      "value": "$value",
      """;
}
