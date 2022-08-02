package com.tmkr.legion.core.model.states;

import com.tmkr.legion.voiceassistant.alexa.model.states.AlexaPowerState;
import com.tmkr.legion.voiceassistant.googleAssistant.model.states.GooglePowerState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PowerState implements State, AlexaPowerState, GooglePowerState {

  private boolean on;
}
