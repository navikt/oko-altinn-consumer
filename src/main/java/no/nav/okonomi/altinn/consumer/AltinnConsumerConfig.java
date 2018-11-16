package no.nav.okonomi.altinn.consumer;

import no.nav.okonomi.altinn.consumer.correspondenceservice.AltinnCorrespondenceConsumerConfig;
import no.nav.okonomi.altinn.consumer.formsubmitservice.AltinnFormSubmitConsumerConfig;
import no.nav.okonomi.altinn.consumer.interceptor.BadContextTokenInFaultInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.CookiesInInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.CookiesOutInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.HeaderInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.MessageInterceptor;
import no.nav.okonomi.altinn.consumer.receiptservice.AltinnReceiptConsumerConfig;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.security.auth.callback.CallbackHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@ComponentScan("no.nav.okonomi.altinn.consumer")
@Import({AltinnCorrespondenceConsumerConfig.class, AltinnFormSubmitConsumerConfig.class, AltinnReceiptConsumerConfig.class})
public class AltinnConsumerConfig {

    private static final String SYSTEM_PROPERTY_APPCERT_ALIAS = "no.nav.modig.security.appcert.keystorealias";

    private static final String SYSTEM_PROPERTY_APPCERT_SECRET = "no.nav.modig.security.appcert.password";

    private static final String SYSTEM_PROPERTY_APPCERT_FILE = "no.nav.modig.security.appcert.keystore";

    @Value("${nav_altinn_consumer_virksomhet_password}")
    private String virksomhetUserName;

    @Value("${nav_altinn_consumer_virksomhet_username}")
    private String virksomhetPassord;

    private final SpringBusFactory busFactory = new SpringBusFactory();

    protected void setRequestContext(Object port, SecurityCredentials credentials) {
        Client client = ClientProxy.getClient(port);
        client.getRequestContext().put("security.must-understand", Boolean.TRUE);
        client.getRequestContext().put("org.apache.cxf.message.Message.MAINTAIN_SESSION", Boolean.TRUE);
        client.getRequestContext().put("javax.xml.ws.session.maintain", Boolean.TRUE);
        client.getRequestContext().put("security.cache.issued.token.in.endpoint", Boolean.TRUE); // default: true
        client.getRequestContext().put("security.issue.after.failed.renew", Boolean.TRUE); // This must be set to true (default: true)
        client.getRequestContext().put("security.signature.properties", credentials.getKeyStoreProperties());
    }

    @Bean
    public SecurityCredentials securityCredentials() {
        Properties keyStoreProperties = new Properties();
        keyStoreProperties.setProperty("keystore", System.getProperty(SYSTEM_PROPERTY_APPCERT_FILE));
        keyStoreProperties.setProperty("keystorepassword", System.getProperty(SYSTEM_PROPERTY_APPCERT_SECRET));
        keyStoreProperties.setProperty("keystorealias", System.getProperty(SYSTEM_PROPERTY_APPCERT_ALIAS, "app-key"));
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

    protected WSS4JOutInterceptor wss4JOutInterceptor(String userName, CallbackHandler clientCallBackHandler) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        properties.put(WSHandlerConstants.USER, userName);
        properties.put(WSHandlerConstants.PW_CALLBACK_REF, clientCallBackHandler);

        return new WSS4JOutInterceptor(properties);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
