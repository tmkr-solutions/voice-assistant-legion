package com.tmkr.legion.voiceassistant.googleAssistant.event;

import com.tmkr.legion.core.event.DeviceStateEvent;
import java.util.List;

public interface GoogleStatusEvent {

  List<DeviceStateEvent> getDeviceStates();

  void setDeviceStates(List<DeviceStateEvent> deviceStates);
}
