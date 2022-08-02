package com.tmkr.legion.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestUtils {

  public static String getJsonBody(HttpServletRequest request) {

    String body;
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader bufferedReader;

    try (InputStream inputStream = request.getInputStream()) {
      bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      char[] charBuffer = new char[128];
      int bytesRead;
      while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
        stringBuilder.append(charBuffer, 0, bytesRead);
      }
    } catch (IOException ex) {
      log.error("Could not read request body", ex);
    }

    body = stringBuilder.toString();
    return body;
  }

}
