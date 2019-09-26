//package no.nav.okonomi.altinn.consumer.formsubmitservice;
//
//import no.altinn.intermediaryinboundexternalec.IIntermediaryInboundExternalEC;
//import no.nav.okonomi.altinn.consumer.AbstractConfig;
//import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
//import org.apache.cxf.Bus;
//import org.apache.cxf.ext.logging.LoggingFeature;
//import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
//import org.apache.cxf.ws.addressing.WSAddressingFeature;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//import org.springframework.core.io.ClassPathResource;
//
//import javax.xml.namespace.QName;
//import java.io.IOException;
//import java.util.Properties;
//
//import static org.junit.Assert.fail;
//
//public class FormSubmitConsumerTestConfig extends AbstractConfig {
//
//    @Value("${no.nav.os.eskatt.altinnconsumer.intermediary.external.ec.url}")
//    private String endpointAddress;
//
//    @Value("${no.nav.os.eskatt.srvoseskatt.username}")
//    private String userName;
//
//    @Value("${no.nav.os.eskatt.srvoseskatt.password}")
//    private String password;
//
//    static SecurityCredentials credentials;
//
//    public FormSubmitConsumerTestConfig() {
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
//            "http://www.altinn.no/services/Intermediary/Shipment/IntermediaryInbound/2010/10",
//            "IIntermediaryInboundExternalEC");
//
//    private static final QName PORT = new QName(
//            "http://www.altinn.no/services/Intermediary/Shipment/IntermediaryInbound/2010/10",
//            "Intermediary_Port");
//
//    @Bean
//    public IIntermediaryInboundExternalEC getPortType() {
//        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
//        factoryBean.setWsdlURL("wsdl/IntermediaryInboundExternalEC.wsdl");
//        factoryBean.setServiceName(SERVICE);
//        factoryBean.setEndpointName(PORT);
//        factoryBean.setServiceClass(IIntermediaryInboundExternalEC.class);
//        factoryBean.setAddress(endpointAddress);
//        factoryBean.getFeatures().add(new WSAddressingFeature());
//        factoryBean.getFeatures().add(new LoggingFeature());
//        IIntermediaryInboundExternalEC port = factoryBean.create(IIntermediaryInboundExternalEC.class);
//        setRequestContext(port, credentials);
//
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
//}