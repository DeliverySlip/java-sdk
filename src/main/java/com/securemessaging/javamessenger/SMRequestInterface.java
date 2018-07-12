package com.securemessaging.javamessenger;

import com.securemessaging.javamessenger.sm.enums.SMRequestMethod;
import org.springframework.http.HttpEntity;

import java.util.Map;

public interface SMRequestInterface {

    String getRequestRoute();

   Map<String,String> getRequestParams();

    SMRequestMethod getRequestMethod();

    HttpEntity<?> getRequestAsEntity();
}
