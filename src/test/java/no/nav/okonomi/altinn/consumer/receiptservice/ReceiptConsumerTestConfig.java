package no.nav.okonomi.altinn.consumer.receiptservice;

import no.altinn.receiptexternalec.IReceiptExternalEC;
import no.nav.okonomi.altinn.consumer.AbstractConfig;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.apache.cxf.Bus;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import javax.inject.Inject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.fail;

public class ReceiptConsumerTestConfig extends AbstractConfig {

    @Inject
    Bus bus;

    @Value("${no.nav.os.eskatt.altinnconsumer.receipt.external.ec.url}")
    private String endpointAddress;

    @Value("${no.nav.os.eskatt.srvoseskatt.username}")
    private String userName;

    @Value("${no.nav.os.eskatt.srvoseskatt.password}")
    private String password;

    static SecurityCredentials credentials;

    public ReceiptConsumerTestConfig() {
        Properties properties = new Properties();
        try {
            properties.loadFromXML(new ClassPathResource("properties/navtestklient-properties.xml").getInputStream());
        } catch (IOException e) {
            fail("Unable to load properties");
        }

        credentials = new SecurityCredentials(properties.getProperty("testklient.virksomhetsbruker"),
                properties.getProperty("testklient.virksomhetsbrukerpassord"),
                properties.getProperty("testklient.certificate"),
                properties.getProperty("testklient.entitypassword"),
                properties.getProperty("testklient.alias"));
    }

//    @Bean
//    public IReceiptExternalEC getAltinnReceiptPortType() {
//        ReceiptExternalEC service = new ReceiptExternalEC();
//        IReceiptExternalEC port = service.getCustomBindingIReceiptExternalEC();
//         bus.getOutInterceptors().add(wss4JOutInterceptor(userName, new TestClientCallBackHandler()));
//        BindingProvider bindingProvider = (BindingProvider) port;
//        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
//        setRequestContext(port, credentials);
//
//        return port;
//    }

    private static final QName SERVICE = new QName(
            "http://www.altinn.no/services/Intermediary/Receipt/2009/10",
            "ReceiptExternalEC");

    private static final QName PORT = new QName(
            "http://www.altinn.no/services/Intermediary/Receipt/2009/10",
            "CustomBinding_IReceiptExternalEC");

    @Bean
    public IReceiptExternalEC getPortType() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setWsdlURL("wsdl/ReceiptExternalEC.wsdl");
        factoryBean.setServiceName(SERVICE);
        factoryBean.setEndpointName(PORT);
        factoryBean.setServiceClass(IReceiptExternalEC.class);
        factoryBean.setAddress(endpointAddress);
        factoryBean.getFeatures().add(new WSAddressingFeature());
        factoryBean.getFeatures().add(new LoggingFeature());
        IReceiptExternalEC port = factoryBean.create(IReceiptExternalEC.class);

        return port;
    }

    @Bean
    public Bus springBus() {
        return createBus();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    private class TestClientCallBackHandler implements CallbackHandler {
        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            WSPasswordCallback wsPasswordCallback = (WSPasswordCallback) callbacks[0];
            wsPasswordCallback.setPassword(password);
        }
    }

}