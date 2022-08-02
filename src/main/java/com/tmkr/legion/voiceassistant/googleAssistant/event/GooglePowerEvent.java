package com.tmkr.legion.voiceassistant.googleAssistant.event;

public interface GooglePowerEvent {

  String getDeviceId();

  void setDeviceId(String deviceId);

  boolean isOn();

  void setOn(boolean on);
}
