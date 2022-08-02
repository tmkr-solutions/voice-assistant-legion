package com.tmkr.legion.voiceassistant.googleAssistant.model;

import com.tmkr.legion.core.model.Capability;
import com.tmkr.legion.core.model.Category;
import java.util.List;

public interface GoogleDevice {

  String getDeviceId();

  void setDeviceId(String deviceId);

  List<Category> getCategories();

  void setCategories(List<Category> categories);

  List<Capability> getCapabilities();

  void setCapabilities(List<Capability> capabilities);

  String getName();

  void setName(String name);

  boolean isWillReportState();

  void setWillReportState(boolean willReportState);
}
