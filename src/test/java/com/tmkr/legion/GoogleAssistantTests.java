package com.tmkr.legion;

import com.tmkr.legion.voiceassistant.googleAssistant.GoogleAssistant;

public class GoogleAssistantTests extends VoiceAssistantTest<GoogleAssistant> {

  @Override
  protected GoogleAssistant createInstance() {
    return new GoogleAssistant();
  }

  @Override
  public String getTestDiscoveryJson() {
    return """
        {
          "requestId": "ff36a3cc-ec34-11e6-b1a0-64510650abcf",
          "inputs": [
            {
              "intent": "action.devices.SYNC"
            }
          ]
        }
        """;
  }

  @Override
  public String getTestStatusJson() {
    return """
        {
          "requestId": "ff36a3cc-ec34-11e6-b1a0-64510650abcf",
          "inputs": [
            {
              "intent": "action.devices.QUERY",
              "payload": {
                "devices": [
                  {
                    "id": "123"
                  }
                ]
              }
            }
          ]
        }
        """;
  }

  @Override
  public String getTestDeviceStateJson() {
    return """
        {
          "requestId": "ff36a3cc-ec34-11e6-b1a0-64510650abcf",
          "inputs": [
            {
              "intent": "action.devices.EXECUTE",
              "payload": {
                "commands": [
                  {
                    "devices": [
                      {
                        "id": "123"
                      }
                    ],
                    "execution": [
                      {
                        "command": "action.devices.commands.OnOff",
                        "params": {
                          "on": true
                        }
                      }
                    ]
                  }
                ]
              }
            }
          ]
        }
        """;
  }
}
