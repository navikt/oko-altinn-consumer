package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.IIntermediaryInboundExternalEC;
import no.nav.okonomi.altinn.consumer.AltinnConsumerConfig;
import no.nav.okonomi.altinn.consumer.security.ClientCallBackHandler;
import org.apache.cxf.Bus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;

@Configuration
public class AltinnIntermediaryInboundConsumerConfig extends AltinnConsumerConfig {

    private static final String NAMESPACE = "http://www.altinn.no/services/Intermediary/Shipment/IntermediaryInbound/2010/10";
    private static final String SERVICE_LOCAL_PART = "IIntermediaryInboundExternalEC";
    private static final String PORT_LOCAL_PART = "Intermediary_Port";

    @Value("${altinn-consumer.srvuser-sbs.username}")
    private String userName;

    @Value("${altinn-consumer.intermediaryinbound.url}")
    private String endpointAddress;

    private Bus bus = createBus();

    @Bean
    public IIntermediaryInboundExternalEC getAltinnIntermediaryInboundPortType() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setWsdlURL("wsdl/IntermediaryInboundExternalEC.wsdl");
        factoryBean.setServiceName(new QName(NAMESPACE, SERVICE_LOCAL_PART));
        factoryBean.setEndpointName(new QName(NAMESPACE, PORT_LOCAL_PART));
        factoryBean.setServiceClass(IIntermediaryInboundExternalEC.class);
        factoryBean.setAddress(endpointAddress);
        factoryBean.getFeatures().add(new WSAddressingFeature());
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);
        loggingFeature.initialize(bus);
        factoryBean.getFeatures().add(loggingFeature);
        IIntermediaryInboundExternalEC port = factoryBean.create(IIntermediaryInboundExternalEC.class);
        setRequestContext(port, securityCredentials());

        bus.getOutInterceptors().add(wss4JOutInterceptor(userName, new ClientCallBackHandler()));

        return port;
    }

}