package com.tmkr.legion.core.exception;

public class NoSuitableEventFound extends RuntimeException {

  public NoSuitableEventFound() {
    super("No suitable event was found. Either not implemented or request is invalid");
  }
}

