package com.securemessaging;

import com.securemessaging.sm.enums.SMRequestMethod;
import org.springframework.http.HttpEntity;

import java.util.Map;

/**
 * SMRequestInterface defines all methods necessary for the ClientRequestHandler to properly process a client SDK
 * request. The request includes methods defining the route, any parameters, the method and the request as a generic
 * entity. For JSON request attributes, the implementing class can define public attributes which will then be
 * serialized
 */
public interface SMRequestInterface {



    String getRequestRoute();

    boolean requestRouteHasApiPath();

   Map<String,String> getRequestParams();

    SMRequestMethod getRequestMethod();

    HttpEntity<?> getRequestAsEntity();
}
