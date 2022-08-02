package com.tmkr.legion.voiceassistant;

import com.tmkr.legion.core.VoiceAssistant;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class VoiceAssistantContext {

  @Setter
  private VoiceAssistant voiceAssistant;

  public String handleRequest(String token, String refreshToken, String jsonBody) {
    return voiceAssistant.handleRequest(token, refreshToken, jsonBody);
  }

}
