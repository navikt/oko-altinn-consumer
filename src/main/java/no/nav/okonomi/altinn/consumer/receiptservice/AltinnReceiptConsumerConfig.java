package no.nav.okonomi.altinn.consumer.receiptservice;

import no.altinn.receiptexternalec.IReceiptExternalEC2;
import no.altinn.receiptexternalec.ReceiptExternalEC2;
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
public class AltinnReceiptConsumerConfig extends AbstractConfig {

    @Value("${altinn-consumer.srvuser-sbs.username}")
    private String userName;

    @Value("${altinn-consumer.receipt.url}")
    private String endpointAddress;

    @Bean
    public IReceiptExternalEC2 getAltinnReceiptPortType(
            ClientCallBackHandler clientCallBackHandler, SecurityCredentials securityCredentials) {
        ReceiptExternalEC2 service = new ReceiptExternalEC2();
        IReceiptExternalEC2 port = service.getCustomBindingIReceiptExternalEC2();
        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpointAddress);
        Client client = ClientProxy.getClient(port);
        Bus bus = createBus();
        setRequestContext(client, securityCredentials);
        bus.getOutInterceptors().add(wss4JOutInterceptor(userName, clientCallBackHandler));
        return port;
    }

}