package no.nav.okonomi.altinn.consumer.security;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

public class ClientCallBackHandler implements CallbackHandler {

    private String password;

    public ClientCallBackHandler(String password) {
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) {
        WSPasswordCallback wsPasswordCallback = (WSPasswordCallback) callbacks[0];
        wsPasswordCallback.setPassword(password);
    }

}