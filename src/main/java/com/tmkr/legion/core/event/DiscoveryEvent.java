package com.tmkr.legion.core.event;

import com.tmkr.legion.core.model.Device;
import com.tmkr.legion.voiceassistant.alexa.event.AlexaDiscoveryEvent;
import com.tmkr.legion.voiceassistant.googleAssistant.event.GoogleDiscoveryEvent;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DiscoveryEvent extends Event implements AlexaDiscoveryEvent, GoogleDiscoveryEvent {

  private List<Device> devices;

}
