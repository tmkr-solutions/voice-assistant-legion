package com.tmkr.legion.voiceassistant.googleAssistant.event;

import com.tmkr.legion.core.model.RequestStatus;
import com.tmkr.legion.core.model.states.State;
import java.util.List;

public interface GoogleDeviceStateEvent {

  String getDeviceId();

  void setDeviceId(String deviceId);

  boolean isOnline();

  void setOnline(boolean online);

  RequestStatus getStatus();

  void setStatus(RequestStatus requestStatus);

  List<State> getDeviceStates();

  void setDeviceStates(List<State> deviceStates);
}
