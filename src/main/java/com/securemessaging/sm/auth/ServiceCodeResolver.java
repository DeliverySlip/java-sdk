package com.securemessaging.sm.auth;

import com.securemessaging.ccc.request.PublicGetServiceRequest;
import com.securemessaging.ccc.response.PublicGetServiceResponse;
import com.securemessaging.client.ClientRequestHandler;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;

public class ServiceCodeResolver {

    private static String cccBaseURL = Endpoints.CCCAPI;
    private static final String resolveRoute = "/public/services/single";

    public static String resolve(String serviceCode) throws SecureMessengerClientException, SecureMessengerException {

        ClientRequestHandler client = new ClientRequestHandler(cccBaseURL);
        PublicGetServiceResponse response = client.makeRequest(resolveRoute + "?serviceCode=" + serviceCode, new PublicGetServiceRequest(), PublicGetServiceResponse.class);
        return response.urls.SecMsgAPI;
    }

    public static void setResolverUrl(String resolverURL){
        ServiceCodeResolver.cccBaseURL = resolverURL;
    }
}
