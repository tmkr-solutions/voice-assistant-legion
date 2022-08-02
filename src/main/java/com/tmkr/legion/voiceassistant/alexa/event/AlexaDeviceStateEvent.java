package com.tmkr.legion.voiceassistant.alexa.event;

import com.tmkr.legion.core.model.states.State;
import java.util.List;

public interface AlexaDeviceStateEvent {

  String getDeviceId();

  void setDeviceId(String deviceId);

  List<State> getDeviceStates();

  void setDeviceStates(List<State> deviceStates);
}
