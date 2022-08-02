package com.tmkr.legion.core.event;

import com.tmkr.legion.voiceassistant.alexa.event.AlexaPowerEvent;
import com.tmkr.legion.voiceassistant.googleAssistant.event.GooglePowerEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PowerEvent extends DeviceStateEvent implements AlexaPowerEvent, GooglePowerEvent {

  private String deviceId;

  private boolean on;

}
