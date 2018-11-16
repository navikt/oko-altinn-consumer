package no.nav.okonomi.altinn.consumer.security;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.springframework.beans.factory.annotation.Value;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

public class ClientCallBackHandler implements CallbackHandler {

    @Value("${no.nav.os.eskatt.srvoseskattsbs.password}")
    private String password;

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback wsPasswordCallback = (WSPasswordCallback) callbacks[0];
        wsPasswordCallback.setPassword(password);
    }

}