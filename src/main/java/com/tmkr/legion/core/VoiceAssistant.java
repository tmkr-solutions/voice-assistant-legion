package com.tmkr.legion.core;

import com.tmkr.legion.core.event.Event;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class VoiceAssistant {

  private Infrastructure infrastructure = new Infrastructure() {
    @Override
    public Event executeEvent(Event event) {
      return event;
    }
  };

  public String handleRequest(String token, String refreshToken, String requestJsonString) {
    log.info("request: " + requestJsonString);
    List<Event> requestEvents = mapRequestToEvents(requestJsonString);

    List<Event> responseEvents = requestEvents.stream().map(requestEvent -> infrastructure.executeEvent(requestEvent)).collect(Collectors.toList());

    String responseJsonString = mapEventsToResponseString(responseEvents);
    log.info("response: " + responseJsonString);
    return responseJsonString;
  }

  public abstract List<Event> mapRequestToEvents(String jsonBody);

  public abstract String mapEventsToResponseString(List<Event> responseEvent);

}
