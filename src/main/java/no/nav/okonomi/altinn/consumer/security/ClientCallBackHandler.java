package no.nav.okonomi.altinn.consumer.security;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.springframework.beans.factory.annotation.Value;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

public class ClientCallBackHandler implements CallbackHandler {

    @Value("${altinn-consumer.srvuser-sbs.password}")
    private String password;

    @Override
    public void handle(Callback[] callbacks) {
        WSPasswordCallback wsPasswordCallback = (WSPasswordCallback) callbacks[0];
        wsPasswordCallback.setPassword(password);
    }

}