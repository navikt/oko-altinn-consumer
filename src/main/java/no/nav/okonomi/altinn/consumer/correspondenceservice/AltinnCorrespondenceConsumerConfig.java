package no.nav.okonomi.altinn.consumer.correspondenceservice;

import no.altinn.correspondenceexternalec.ICorrespondenceExternalEC;
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
public class AltinnCorrespondenceConsumerConfig extends AltinnConsumerConfig {

    private static final String NAMESPACE = "http://www.altinn.no/services/ServiceEngine/Correspondence/2010/10";
    private static final String SERVICE_LOCAL_PART = "CorrespondenceExternalECSF";
    private static final String PORT_LOCAL_PART = "CustomBinding_ICorrespondenceExternalEC";

    @Value("${altinn-consumer.srvuser-sbs.username}")
    private String userName;

    @Value("${altinn-consumer.correspondence.url}")
    private String endpointAddress;

    private Bus bus = createBus();

    @Bean
    public ICorrespondenceExternalEC getAltinnCorrespondencePortType() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setWsdlURL("wsdl/CorrespondenceExternalEC.wsdl");
        factoryBean.setServiceName(new QName(NAMESPACE, SERVICE_LOCAL_PART));
        factoryBean.setEndpointName(new QName(NAMESPACE, PORT_LOCAL_PART));
        factoryBean.setServiceClass(ICorrespondenceExternalEC.class);
        factoryBean.setAddress(endpointAddress);
        factoryBean.getFeatures().add(new WSAddressingFeature());
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);
        loggingFeature.initialize(bus);
        factoryBean.getFeatures().add(loggingFeature);
        ICorrespondenceExternalEC port = factoryBean.create(ICorrespondenceExternalEC.class);
        setRequestContext(port, securityCredentials());

        bus.getOutInterceptors().add(wss4JOutInterceptor(userName, new ClientCallBackHandler()));

        return port;
    }

}