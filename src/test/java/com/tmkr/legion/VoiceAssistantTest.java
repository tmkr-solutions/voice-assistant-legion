package com.tmkr.legion;

import com.tmkr.legion.core.VoiceAssistant;
import com.tmkr.legion.core.event.DeviceStateEvent;
import com.tmkr.legion.core.event.DiscoveryEvent;
import com.tmkr.legion.core.event.Event;
import com.tmkr.legion.core.event.StatusEvent;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class VoiceAssistantTest<T extends VoiceAssistant> {

  private T voiceAssistant;

  protected abstract T createInstance();

  @BeforeAll
  public void setup() {
    voiceAssistant = createInstance();
  }

  @Test
  @DisplayName("Tests whether the voiceAssistant instance implemented the discoveryEvent")
  public void test_discovery_event_is_implemented() {
    List<Event> events = voiceAssistant.mapRequestToEvents(getTestDiscoveryJson());
    assert !events.isEmpty();
    Event event = events.get(0);
    assert event instanceof DiscoveryEvent;
  }

  @Test
  @DisplayName("Tests whether the voiceAssistant instance implemented the statusEvent")
  public void test_status_event_is_implemented() {
    List<Event> events = voiceAssistant.mapRequestToEvents(getTestStatusJson());
    assert !events.isEmpty();
    Event event = events.get(0);
    assert event instanceof StatusEvent;
  }

  @Test
  @DisplayName("Tests whether the voiceAssistant instance implemented the deviceStateEvent")
  public void test_device_state_event_is_implemented() {
    List<Event> events = voiceAssistant.mapRequestToEvents(getTestDeviceStateJson());
    assert !events.isEmpty();
    Event event = events.get(0);
    assert event instanceof DeviceStateEvent;
  }

  public abstract String getTestDiscoveryJson();
  public abstract String getTestStatusJson();
  public abstract String getTestDeviceStateJson();

}
