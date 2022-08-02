package com.tmkr.legion.core;

import com.tmkr.legion.core.event.Event;

public abstract class Infrastructure {

  public abstract Event executeEvent(Event event);

}
