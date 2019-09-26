//package no.nav.okonomi.altinn.consumer.correspondenceservice;
//
//import no.altinn.correspondenceexternalec.ICorrespondenceExternalEC;
//import no.nav.okonomi.altinn.consumer.AbstractConfig;
//import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
//import org.apache.cxf.Bus;
//import org.apache.cxf.feature.LoggingFeature;
//import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
//import org.apache.cxf.ws.addressing.WSAddressingFeature;
//import org.apache.wss4j.common.ext.WSPasswordCallback;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//import org.springframework.core.io.ClassPathResource;
//
//import javax.inject.Inject;
//import javax.security.auth.callback.Callback;
//import javax.security.auth.callback.CallbackHandler;
//import javax.security.auth.callback.UnsupportedCallbackException;
//import javax.xml.namespace.QName;
//import java.io.IOException;
//import java.util.Properties;
//
//import static org.junit.Assert.fail;
//
//public class CorrespondenceConsumerTestConfig extends AbstractConfig {
//
//    @Inject
//    Bus bus;
//
//    @Value("${no.nav.os.eskatt.altinnconsumer.correspondence.external.ec.url}")
//    private String endpointAddress;
//
//    @Value("${no.nav.os.eskatt.srvoseskatt.username}")
//    private String userName;
//
//    @Value("${no.nav.os.eskatt.srvoseskatt.password}")
//    private String password;
//
//    public static SecurityCredentials credentials;
//
//    public CorrespondenceConsumerTestConfig() {
//        Properties properties = new Properties();
//        try {
//            properties.loadFromXML(new ClassPathResource("properties/navtestklient-properties.xml").getInputStream());
//        } catch (IOException e) {
//            fail("Unable to load properties");
//        }
//
//        credentials = new SecurityCredentials(properties.getProperty("testklient.virksomhetsbruker"),
//                properties.getProperty("testklient.virksomhetsbrukerpassord"),
//                properties.getProperty("testklient.certificate"),
//                properties.getProperty("testklient.entitypassword"),
//                properties.getProperty("testklient.alias"));
//    }
//
//    private static final QName SERVICE = new QName(
//            "http://www.altinn.no/services/ServiceEngine/Correspondence/2010/10",
//            "CorrespondenceExternalECSF");
//
//    private static final QName PORT = new QName(
//            "http://www.altinn.no/services/ServiceEngine/Correspondence/2010/10",
//            "CustomBinding_ICorrespondenceExternalEC");
//
//    @Bean
//    public ICorrespondenceExternalEC getAltinnCorrespondencePortType() {
//        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
//        factoryBean.setWsdlURL("wsdl/CorrespondenceExternalEC.wsdl");
//        factoryBean.setServiceName(SERVICE);
//        factoryBean.setEndpointName(PORT);
//        factoryBean.setServiceClass(ICorrespondenceExternalEC.class);
//        factoryBean.setAddress(endpointAddress);
//        factoryBean.getFeatures().add(new WSAddressingFeature());
//        factoryBean.getFeatures().add(new LoggingFeature());
//        ICorrespondenceExternalEC port = factoryBean.create(ICorrespondenceExternalEC.class);
//
//        return port;
//    }
//
//    @Bean
//    public Bus springBus() {
//        return createBus();
//    }
//
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }
//
//    private class TestClientCallBackHandler implements CallbackHandler {
//
//        @Override
//        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
//            WSPasswordCallback wsPasswordCallback = (WSPasswordCallback) callbacks[0];
//            wsPasswordCallback.setPassword(password);
//        }
//    }
//
//}