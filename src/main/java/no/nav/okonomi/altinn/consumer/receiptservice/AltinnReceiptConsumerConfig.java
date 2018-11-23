package no.nav.okonomi.altinn.consumer.receiptservice;

import no.altinn.receiptexternalec.IReceiptExternalEC;
import no.nav.okonomi.altinn.consumer.AbstractConfig;
import no.nav.okonomi.altinn.consumer.security.ClientCallBackHandler;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.apache.cxf.Bus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;

@Configuration
public class AltinnReceiptConsumerConfig extends AbstractConfig {

    private static final String NAMESPACE = "http://www.altinn.no/services/Intermediary/Receipt/2009/10";
    private static final String SERVICE_LOCAL_PART = "ReceiptExternalEC";
    private static final String PORT_LOCAL_PART = "CustomBinding_IReceiptExternalEC";

    @Value("${altinn-consumer.srvuser-sbs.username}")
    private String userName;

    @Value("${altinn-consumer.receipt.url}")
    private String endpointAddress;

    @Bean
    public IReceiptExternalEC getAltinnReceiptPortType(
            ClientCallBackHandler clientCallBackHandler, SecurityCredentials securityCredentials) {
        Bus bus = createBus();
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setWsdlURL("classpath:wsdl/ReceiptExternalEC.wsdl");
        factoryBean.setServiceName(new QName(NAMESPACE, SERVICE_LOCAL_PART));
        factoryBean.setEndpointName(new QName(NAMESPACE, PORT_LOCAL_PART));
        factoryBean.setServiceClass(IReceiptExternalEC.class);
        factoryBean.setAddress(endpointAddress);
        factoryBean.getFeatures().add(new WSAddressingFeature());
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);
        loggingFeature.initialize(bus);
        factoryBean.getFeatures().add(loggingFeature);
        factoryBean.setProperties(cryptoProperties(securityCredentials));
        IReceiptExternalEC port = factoryBean.create(IReceiptExternalEC.class);
        setRequestContext(port, securityCredentials);
        bus.getOutInterceptors().add(wss4JOutInterceptor(userName, clientCallBackHandler));
        return port;
    }

}