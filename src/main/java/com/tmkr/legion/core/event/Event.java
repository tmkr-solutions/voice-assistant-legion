package com.tmkr.legion.core.event;

import com.tmkr.legion.voiceassistant.alexa.event.AlexaEvent;
import com.tmkr.legion.voiceassistant.googleAssistant.event.GoogleEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event implements AlexaEvent, GoogleEvent {

  private String token;

  private String refreshToken;

  private String messageId;

}
