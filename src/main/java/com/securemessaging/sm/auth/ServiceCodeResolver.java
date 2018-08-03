package com.securemessaging.sm.auth;

import com.securemessaging.ccc.request.PublicGetServiceRequest;
import com.securemessaging.ccc.response.PublicGetServiceResponse;
import com.securemessaging.client.ClientRequestHandler;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;

/**
 * ServiceCodeResolver is a helper class that resolves service codes to their appropriate secure messaging api
 * endpoint using the CCC API
 */
public class ServiceCodeResolver {

    private static String cccBaseURL = Endpoints.CCCAPI;
    private static final String resolveRoute = "/public/services/single";

    private static String versionedRoutesBase = "/v1";
    private static boolean includeVersionedRoutePathInBaseUrl = true;

    public static String resolve(String serviceCode) throws SecureMessengerClientException, SecureMessengerException {

        ClientRequestHandler client = new ClientRequestHandler(cccBaseURL);
        PublicGetServiceResponse response = client.makeRequest(resolveRoute + "?serviceCode=" + serviceCode, new PublicGetServiceRequest(), PublicGetServiceResponse.class);
        if(includeVersionedRoutePathInBaseUrl){
            return response.urls.SecMsgAPI + versionedRoutesBase;
        }else{
            return response.urls.SecMsgAPI;
        }

    }

    public static void includeVersionedRoutePathInBaseUrl(boolean includeVersionPathInBaseUrl){
        ServiceCodeResolver.includeVersionedRoutePathInBaseUrl = includeVersionPathInBaseUrl;
    }

    public static void setApiVersionedRoutesBase(String versionedRouteBase){
        ServiceCodeResolver.versionedRoutesBase = versionedRouteBase;
    }

    public static void setResolverUrl(String resolverURL){
        ServiceCodeResolver.cccBaseURL = resolverURL;
    }
}
