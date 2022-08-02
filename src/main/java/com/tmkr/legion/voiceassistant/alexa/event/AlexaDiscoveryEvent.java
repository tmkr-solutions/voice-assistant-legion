package com.tmkr.legion.voiceassistant.alexa.event;

import com.tmkr.legion.core.model.Device;
import java.util.List;

public interface AlexaDiscoveryEvent {

  List<Device> getDevices();

  void setDevices(List<Device> devices);
}
