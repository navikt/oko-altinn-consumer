package no.nav.okonomi.altinn.consumer;

import no.nav.okonomi.altinn.consumer.interceptor.BadContextTokenInFaultInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.CookiesInInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.CookiesOutInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.HeaderInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.MessageInterceptor;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.security.auth.callback.CallbackHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AbstractConfig {

    @Value("${altinn-consumer.virksomhet.password}")
    private String virksomhetUserName;

    @Value("${altinn-consumer.virksomhet.username}")
    private String virksomhetPassord;


    @Value("${altinn-consumer.security.appcert.keystorealias}")
    private String appcertKeystorealias;

    @Value("${altinn-consumer.security.appcert.password}")
    private String appcertSecret;

    @Value("${altinn-consumer.security.appcert.keystore:app-key}")
    private String appcertKeystore;

    private final SpringBusFactory busFactory = new SpringBusFactory();

    protected void setRequestContext(Object port, SecurityCredentials credentials) {
        try (Client client = ClientProxy.getClient(port)) {
            client.getRequestContext().put("security.must-understand", Boolean.TRUE);
            client.getRequestContext().put("org.apache.cxf.message.Message.MAINTAIN_SESSION", Boolean.TRUE);
            client.getRequestContext().put("javax.xml.ws.session.maintain", Boolean.TRUE);
            client.getRequestContext().put("security.cache.issued.token.in.endpoint", Boolean.TRUE); // default: true
            client.getRequestContext().put("security.issue.after.failed.renew", Boolean.TRUE); // This must be set to true (default: true)
            client.getRequestContext().put("security.signature.properties", credentials.getKeyStoreProperties());
        } catch (Exception e) {
            throw new AltinnConsumerException("Altinn-consumer feilet ved opprettelse av proxy-client", e);
        }
    }

    @Bean
    public SecurityCredentials securityCredentials() {
        Properties keyStoreProperties = new Properties();
        keyStoreProperties.setProperty("keystore", appcertKeystore);
        keyStoreProperties.setProperty("keystorepassword", appcertSecret);
        keyStoreProperties.setProperty("keystorealias", appcertKeystore);
        return new SecurityCredentials(virksomhetUserName, virksomhetPassord, keyStoreProperties);
    }

    @SuppressWarnings("unchecked")
    protected Bus createBus() {
        Bus bus = busFactory.createBus();
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);
        loggingFeature.initialize(bus);
        bus.getFeatures().add(loggingFeature);
        bus.getInInterceptors().add(new CookiesInInterceptor());
        bus.getOutInterceptors().add(new CookiesOutInterceptor());
        bus.getOutInterceptors().add(new HeaderInterceptor());
        bus.getInFaultInterceptors().add(new BadContextTokenInFaultInterceptor());
        bus.getInInterceptors().add(new MessageInterceptor());

        return bus;
    }

    protected Map<String, Object> cryptoProperties() {
        Properties signingCryptProperties = new Properties();
        signingCryptProperties.put("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
        signingCryptProperties.put("org.apache.ws.security.crypto.merlin.keystore.password", appcertSecret);
        signingCryptProperties.put("org.apache.ws.security.crypto.merlin.keystore.file", appcertKeystore);
        signingCryptProperties.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        signingCryptProperties.put("org.apache.ws.security.crypto.merlin.keystore.private.password", appcertSecret);
        Map<String, Object> properties = new HashMap<>();
        properties.put("security.signature.properties.sct", signingCryptProperties);
        return properties;
    }

    protected WSS4JOutInterceptor wss4JOutInterceptor(String userName, CallbackHandler clientCallBackHandler) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        properties.put(WSHandlerConstants.USER, userName);
        properties.put(WSHandlerConstants.PW_CALLBACK_REF, clientCallBackHandler);

        return new WSS4JOutInterceptor(properties);
    }

}
