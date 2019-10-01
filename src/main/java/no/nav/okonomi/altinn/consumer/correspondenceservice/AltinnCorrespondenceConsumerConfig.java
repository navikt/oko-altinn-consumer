package no.nav.okonomi.altinn.consumer.correspondenceservice;

import no.altinn.correspondenceexternalec.CorrespondenceExternalEC2SF;
import no.altinn.correspondenceexternalec.ICorrespondenceExternalEC2;
import no.nav.okonomi.altinn.consumer.AbstractConfig;
import no.nav.okonomi.altinn.consumer.security.ClientCallBackHandler;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

@Configuration
public class AltinnCorrespondenceConsumerConfig extends AbstractConfig {

    @Value("${altinn-consumer.srvuser-sbs.username}")
    private String userName;

    @Value("${altinn-consumer.correspondence.url}")
    private String endpointAddress;

    @Bean
    public ICorrespondenceExternalEC2 getAltinnCorrespondencePortType(
            ClientCallBackHandler clientCallBackHandler, SecurityCredentials securityCredentials) {
        CorrespondenceExternalEC2SF service = new CorrespondenceExternalEC2SF();
        ICorrespondenceExternalEC2 port = service.getCustomBindingICorrespondenceExternalEC2();
        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
        Client client = ClientProxy.getClient(port);
        Bus bus = createBus();
        setRequestContext(client, securityCredentials);
        bus.getOutInterceptors().add(wss4JOutInterceptor(userName, clientCallBackHandler));
        return port;
    }

}