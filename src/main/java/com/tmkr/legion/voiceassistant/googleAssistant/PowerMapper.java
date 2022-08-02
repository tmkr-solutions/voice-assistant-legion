package com.tmkr.legion.voiceassistant.googleAssistant;

import com.tmkr.legion.core.event.PowerEvent;
import com.tmkr.legion.core.model.states.PowerState;
import com.tmkr.legion.core.model.states.State;
import java.util.ArrayList;
import java.util.List;

public class PowerMapper {

  private static final String POWER_FRAME = """
      {
        "requestId": "$messageId",
        "payload": {
          "commands": [
            $commands
          ]
        }
      }  
      """;

  private static final String DEVICE_FRAME = """
      {
        "ids": [
            "$id"
          ],
          "status": "$status",
          "states": {
            $additionalProperties
            "online": $online
          }
      }
      """;

  private static final String ADDTIONAL_PROPERTIES_POWER = """
      "on": $on,
       """;

  public static String mapPowerEvents(String messageId, List<PowerEvent> powerEvents) {
    String base = POWER_FRAME;
    base = base.replace("$messageId", messageId);

    List<String> devices = new ArrayList<>();
    for (PowerEvent device : powerEvents) {
      String deviceFrame = DEVICE_FRAME;
      deviceFrame = deviceFrame.replace("$id", device.getDeviceId());
      deviceFrame = deviceFrame.replace("$status", device.getStatus().name());
      deviceFrame = deviceFrame.replace("$online", String.valueOf(device.isOnline()));

      List<String> additionalProperties = new ArrayList<>();
      for (State state : device.getDeviceStates()) {
        if (state instanceof PowerState powerState) {
          String devicePowerStateJson = ADDTIONAL_PROPERTIES_POWER;
          devicePowerStateJson = devicePowerStateJson.replace("$on", String.valueOf(powerState.isOn()));
          additionalProperties.add(devicePowerStateJson);
        }
      }
      String properties = additionalProperties.isEmpty() ? "" : String.join(", " + additionalProperties);
      deviceFrame = deviceFrame.replace("$additionalProperties", properties);
      devices.add(deviceFrame);
    }

    return base.replace("$commands", String.join(", ", devices));
  }
}
