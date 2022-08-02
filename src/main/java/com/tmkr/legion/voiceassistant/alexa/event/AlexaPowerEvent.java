package com.tmkr.legion.voiceassistant.alexa.event;

public interface AlexaPowerEvent {

  String getDeviceId();

  void setDeviceId(String deviceId);

  boolean isOn();

  void setOn(boolean on);
}
