package no.nav.okonomi.altinn.consumer;

import no.altinn.correspondenceexternalec.CorrespondenceExternalEC2SF;
import no.altinn.correspondenceexternalec.ICorrespondenceExternalEC2;
import no.altinn.intermediaryinboundexternalec.IIntermediaryInboundExternalEC2;
import no.altinn.intermediaryinboundexternalec.IntermediaryInboundExternalEC2;
import no.altinn.receiptexternalec.IReceiptExternalEC2;
import no.altinn.receiptexternalec.ReceiptExternalEC2;
import no.nav.okonomi.altinn.consumer.correspondenceservice.AltinnCorrespondenceConsumerService;
import no.nav.okonomi.altinn.consumer.formsubmitservice.*;
import no.nav.okonomi.altinn.consumer.interceptor.BadContextTokenInFaultInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.CookiesInInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.CookiesOutInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.HeaderInterceptor;
import no.nav.okonomi.altinn.consumer.receiptservice.AltinnReceiptConsumerService;
import no.nav.okonomi.altinn.consumer.receiptservice.ReceiptService;
import no.nav.okonomi.altinn.consumer.security.ClientCallBackHandler;
import no.nav.okonomi.altinn.consumer.security.KeyStore;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import javax.xml.ws.BindingProvider;
import java.util.HashMap;
import java.util.Map;

public class AltinnConsumerFactory {

    private final AltinnConsumerProperties altinnConsumerProperties;

    public AltinnConsumerFactory(){
        this.altinnConsumerProperties = new EnviromentPropertiesReader();
        addInterceptors();

    }

    public AltinnConsumerFactory(AltinnConsumerProperties altinnConsumerProperties){
        this.altinnConsumerProperties = altinnConsumerProperties;
        addInterceptors();

    }

    public AltinnCorrespondenceConsumerService createAltinnCorrespondenceConsumerService(){

        return new AltinnCorrespondenceConsumerService(
                        getICorrespondenceExternalEC2(getSecurityCredentials()),
                        getSecurityCredentials(),
                Integer.parseInt(altinnConsumerProperties.getLanguageId()));
    }

    public AltinnFormSubmitConsumerService createAltinnFormSubmitConsumerService(){

        return new AltinnFormSubmitConsumerService(
                        getIIntermediaryInboundExternalEC2(getSecurityCredentials()),
                        getSecurityCredentials(),
                        getFormTaskShipmentService());
    }

    public AltinnReceiptConsumerService createltinnReceiptConsumerService(){

        return new AltinnReceiptConsumerService(getIReceiptExternalEC2(getSecurityCredentials()),
                        getSecurityCredentials(),
                        getReceiptService());
    }

    private ICorrespondenceExternalEC2 getICorrespondenceExternalEC2(SecurityCredentials securityCredentials) {
        CorrespondenceExternalEC2SF service = new CorrespondenceExternalEC2SF();
        ICorrespondenceExternalEC2 port = service.getCustomBindingICorrespondenceExternalEC2();
        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, altinnConsumerProperties.getCorrespondenceEndpointAddress());
        Client client = ClientProxy.getClient(port);
        setRequestContext(client, securityCredentials);
        return port;
    }

    private IIntermediaryInboundExternalEC2 getIIntermediaryInboundExternalEC2(SecurityCredentials securityCredentials) {
        IntermediaryInboundExternalEC2 service = new IntermediaryInboundExternalEC2();
        IIntermediaryInboundExternalEC2 port = service.getCustomBindingIIntermediaryInboundExternalEC2();
        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, altinnConsumerProperties.getIntermediaryInboundEndpointAddress());
        Client client = ClientProxy.getClient(port);
        setRequestContext(client, securityCredentials);
        return port;
    }

    private IReceiptExternalEC2 getIReceiptExternalEC2(
            SecurityCredentials securityCredentials) {
        ReceiptExternalEC2 service = new ReceiptExternalEC2();
        IReceiptExternalEC2 port = service.getCustomBindingIReceiptExternalEC2();
        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, altinnConsumerProperties.getReceipEndpointAddress());
        Client client = ClientProxy.getClient(port);
        setRequestContext(client, securityCredentials);
        return port;
    }


    private SecurityCredentials getSecurityCredentials() {
        KeyStore keyStore = new KeyStore(altinnConsumerProperties.getAppcertKeystoreFilePath(),
                altinnConsumerProperties.getAppcertKeystorealias(),
                altinnConsumerProperties.getAppcertSecret(),
                "jks");
        return new SecurityCredentials(altinnConsumerProperties.getVirksomhetUserName(), altinnConsumerProperties.getVirksomhetPassord(), keyStore);
    }

    private ClientCallBackHandler getClientCallBackHandler() {
        return new ClientCallBackHandler(altinnConsumerProperties.getPassword());
    }

    private FormTaskShipmentService getFormTaskShipmentService(){
        return new FormTaskShipmentService(getAttachmentService(),getFormTaskService());
    }

    private AttachmentService getAttachmentService(){
        return new AttachmentService();
    }

    private FormSubmitServiceProperties getFormSubmitServiceProperties() {
        return new FormSubmitServiceProperties(
                altinnConsumerProperties.getServiceCode(),
                altinnConsumerProperties.getServiceEditionCode(),
                altinnConsumerProperties.getDataFormatId(),
                altinnConsumerProperties.getDataFormatVersion());
    }

    private FormTaskService getFormTaskService(){
        return new FormTaskService(getFormSubmitServiceProperties());
    }

    private ReceiptService getReceiptService(){
        return new ReceiptService();
    }


    private void setRequestContext(Client client, SecurityCredentials credentials) {
        client.getRequestContext().put("security.must-understand", Boolean.TRUE);
        client.getRequestContext().put("org.apache.cxf.message.Message.MAINTAIN_SESSION", Boolean.TRUE);
        client.getRequestContext().put("javax.xml.ws.session.maintain", Boolean.TRUE);
        client.getRequestContext().put("security.cache.issued.token.in.endpoint", Boolean.TRUE); // default: true
        client.getRequestContext().put("security.issue.after.failed.renew", Boolean.TRUE); // This must be set to true (default: true)
        client.getRequestContext().put("security.signature.properties", credentials.getKeyStoreProperties());
    }

    @SuppressWarnings("unchecked")
    private void addInterceptors() {
        Bus bus = CXFBusFactory.getDefaultBus();
        //     LoggingFeature loggingFeature = new LoggingFeature();
        //     loggingFeature.setPrettyLogging(true);
        //     loggingFeature.initialize(bus);
        //     bus.getFeatures().add(loggingFeature);
        bus.getInInterceptors().add(new CookiesInInterceptor());
        bus.getOutInterceptors().add(new CookiesOutInterceptor());
        bus.getOutInterceptors().add(new HeaderInterceptor());
        bus.getInFaultInterceptors().add(new BadContextTokenInFaultInterceptor());
        bus.getOutInterceptors().add(getWss4JOutInterceptor());
    }

    private WSS4JOutInterceptor getWss4JOutInterceptor() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        properties.put(WSHandlerConstants.USER, altinnConsumerProperties.getUserName());
        properties.put(WSHandlerConstants.PW_CALLBACK_REF, getClientCallBackHandler());

        return new WSS4JOutInterceptor(properties);
    }

}
