package com.tmkr.legion.core.model;

import com.tmkr.legion.voiceassistant.alexa.model.AlexaDevice;
import com.tmkr.legion.voiceassistant.googleAssistant.model.GoogleDevice;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Device implements AlexaDevice, GoogleDevice {

  private String deviceId;

  private List<Category> categories;

  private List<Capability> capabilities;

  private String name;

  private String description;

  private String manufacturerName;

  private boolean willReportState;
}
