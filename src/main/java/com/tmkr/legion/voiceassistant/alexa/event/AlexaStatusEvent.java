package com.tmkr.legion.voiceassistant.alexa.event;

import com.tmkr.legion.core.event.DeviceStateEvent;
import java.util.List;

public interface AlexaStatusEvent {

  List<DeviceStateEvent> getDeviceStates();

  void setDeviceStates(List<DeviceStateEvent> deviceStates);
}
