package com.tmkr.legion.core.event;

import com.tmkr.legion.core.model.RequestStatus;
import com.tmkr.legion.core.model.states.State;
import com.tmkr.legion.voiceassistant.alexa.event.AlexaDeviceStateEvent;
import com.tmkr.legion.voiceassistant.googleAssistant.event.GoogleDeviceStateEvent;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceStateEvent extends Event implements AlexaDeviceStateEvent, GoogleDeviceStateEvent {

  private String deviceId;

  private boolean online;

  private RequestStatus status;

  private List<State> deviceStates;

}
