package com.tmkr.legion.voiceassistant.googleAssistant.event;

import com.tmkr.legion.core.model.Device;
import java.util.List;

public interface GoogleDiscoveryEvent {

  List<Device> getDevices();

  void setDevices(List<Device> devices);
}
