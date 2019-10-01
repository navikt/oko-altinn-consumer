package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.IIntermediaryInboundExternalEC2;
import no.altinn.intermediaryinboundexternalec.IntermediaryInboundExternalEC2;
import no.nav.okonomi.altinn.consumer.AbstractConfig;
import no.nav.okonomi.altinn.consumer.security.ClientCallBackHandler;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.BindingProvider;

@Configuration
public class AltinnIntermediaryInboundConsumerConfig extends AbstractConfig {

    @Value("${altinn-consumer.srvuser-sbs.username}")
    private String userName;

    @Value("${altinn-consumer.intermediaryinbound.url}")
    private String endpointAddress;

    @Bean
    public IIntermediaryInboundExternalEC2 getAltinnIntermediaryInboundPortType(
            ClientCallBackHandler clientCallBackHandler, SecurityCredentials securityCredentials) {
        IntermediaryInboundExternalEC2 service = new IntermediaryInboundExternalEC2();
        IIntermediaryInboundExternalEC2 port = service.getCustomBindingIIntermediaryInboundExternalEC2();
        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpointAddress);
        Client client = ClientProxy.getClient(port);
        Bus bus = createBus();
        setRequestContext(client, securityCredentials);
        bus.getOutInterceptors().add(wss4JOutInterceptor(userName, clientCallBackHandler));
        return port;
    }

}