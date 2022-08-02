package com.tmkr.legion.voiceassistant.googleAssistant;

import com.tmkr.legion.core.event.DeviceStateEvent;
import com.tmkr.legion.core.model.states.PowerState;
import com.tmkr.legion.core.model.states.State;
import java.util.ArrayList;
import java.util.List;

public class StateMapper {

  private static final String STATE_FRAME = """
      {
        "requestId": "$messageId",
        "payload": {
          "devices": {
              $devices
           }
        }
      }
      """;

  private static final String DEVICE_FRAME = """
      "$deviceId": {
         "online": $online,
         $additionalProperties
         "status": "$status"
      }
      """;

  private static final String ADDITIONAL_PROPERTIES_POWER_FRAME = """
      "on": $on,
      """;

  public static String mapStateEvents(String messageId, List<DeviceStateEvent> stateEvents) {
    List<String> devices = new ArrayList<>();

    String stateBaseFrame = STATE_FRAME;
    stateBaseFrame = stateBaseFrame.replace("$messageId", messageId);

    for (DeviceStateEvent device : stateEvents) {

      String deviceFrame = DEVICE_FRAME;
      deviceFrame = deviceFrame.replace("$deviceId", device.getDeviceId());
      deviceFrame = deviceFrame.replace("$online", String.valueOf(device.isOnline()));
      deviceFrame = deviceFrame.replace("$status", device.getStatus().name());

      List<String> additionalProperties = new ArrayList<>();
      for (State state : device.getDeviceStates()) {
        if (state instanceof PowerState powerState) {
          String devicePowerStateJson = ADDITIONAL_PROPERTIES_POWER_FRAME;
          devicePowerStateJson = devicePowerStateJson.replace("$on", String.valueOf(powerState.isOn()));
          additionalProperties.add(devicePowerStateJson);
        }
      }

      String properties = additionalProperties.isEmpty() ? "" : String.join(", ", additionalProperties);
      deviceFrame = deviceFrame.replace("$additionalProperties", properties);

      devices.add(deviceFrame);
    }

    return stateBaseFrame.replace("$devices", String.join(", ", devices));
  }

}
