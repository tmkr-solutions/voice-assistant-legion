package com.tmkr.legion.core.event;

import com.tmkr.legion.voiceassistant.alexa.event.AlexaStatusEvent;
import com.tmkr.legion.voiceassistant.googleAssistant.event.GoogleStatusEvent;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusEvent extends Event implements AlexaStatusEvent, GoogleStatusEvent {

  private List<DeviceStateEvent> deviceStates;
}
