package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.AltinnFault;
import no.altinn.intermediaryinboundexternalec.FormTaskShipmentBE;
import no.altinn.intermediaryinboundexternalec.IIntermediaryInboundExternalEC;
import no.altinn.intermediaryinboundexternalec.IIntermediaryInboundExternalECSubmitFormTaskECAltinnFaultFaultFaultMessage;
import no.altinn.intermediaryinboundexternalec.ReceiptExternalBE;
import no.altinn.intermediaryinboundexternalec.ReceiptStatusExternal;
import no.altinn.intermediaryinboundexternalec.ReferenceExternalBE;
import no.altinn.intermediaryinboundexternalec.ReferenceTypeExternal;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.xml.bind.JAXB;
import java.io.StringWriter;

/**
 * Created by Levent Demir (Capgemini)
 */
@Component
public class DefaultAltinnFormSubmitConsumerService implements AltinnFormSubmitConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAltinnFormSubmitConsumerService.class);

    private static final String FEIL_VED_AA_SENDE = "Feil ved å sende skjema til Altinn: ";

    private final IIntermediaryInboundExternalEC formSubmitService;

    private SecurityCredentials credentials;

    private FormTaskShipmentService formTaskShipmentService;

    @Inject
    public DefaultAltinnFormSubmitConsumerService(IIntermediaryInboundExternalEC formSubmitService,
                                                  SecurityCredentials credentials,
                                                  FormTaskShipmentService formTaskShipmentService) {
        this.formSubmitService = formSubmitService;
        this.credentials = credentials;
        this.formTaskShipmentService = formTaskShipmentService;
    }

    @Override
    public synchronized SubmitFormTask submitForm(AltinnMessage altinnMessage) {
        FormTaskShipmentBE formTaskShipment = formTaskShipmentService.createFormTaskShipment(altinnMessage);
        ReceiptExternalBE receipt = submitFormTaskEC(formTaskShipment);

        return insertReceipt(receipt, altinnMessage.getOrderId());
    }

    @Override
    public synchronized SubmitFormTask submitFormWithoutAttachment(AltinnMessage altinnMessage) {
        FormTaskShipmentBE formTaskShipment = formTaskShipmentService.createFormTaskShipmentWithoutAttachments(altinnMessage);
        ReceiptExternalBE receipt = submitFormTaskEC(formTaskShipment);

        return insertReceipt(receipt, altinnMessage.getOrderId());
    }

    @Override
    public synchronized void test() {
        try {
            formSubmitService.test();
        } catch (Exception e) {
            LOGGER.warn("Feil ved å sende skjema test til Altinn: {}", e.getMessage());
            throw new AltinnFormSubmitServiceException("Feil ved å sende skjema test til Altinn: ", e);
        }
    }

    private ReceiptExternalBE submitFormTaskEC(FormTaskShipmentBE formTaskShipment) {
        try {
            logFormTaskShipment(formTaskShipment);
            return formSubmitService.submitFormTaskEC(credentials.getVirksomhetsbruker(), credentials.getVirksomhetsbrukerPassord(), formTaskShipment);
        } catch (IIntermediaryInboundExternalECSubmitFormTaskECAltinnFaultFaultFaultMessage e) {
            LOGGER.error(getAltinnErrorMessage(e));
            throw new AltinnFormSubmitServiceException(FEIL_VED_AA_SENDE, e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Melding feilet hos mottakeren: {}", e);
            throw new AltinnFormSubmitServiceException(FEIL_VED_AA_SENDE, e);
        } catch (Exception e) {
            LOGGER.error("Ukjent feil oppstått: {}", e);
            throw new AltinnFormSubmitServiceException(FEIL_VED_AA_SENDE, e);
        }
    }

    private void logFormTaskShipment(FormTaskShipmentBE formTaskShipment) {
        StringWriter xml = new StringWriter();
        JAXB.marshal(formTaskShipment, xml);
        LOGGER.debug("Formtaskshipment til Altinn: {}", xml);
    }

    private String getAltinnErrorMessage(IIntermediaryInboundExternalECSubmitFormTaskECAltinnFaultFaultFaultMessage altinne) {
        AltinnFault fault = altinne.getFaultInfo();
        String errMsg = fault.getAltinnErrorMessage().getValue();
        Integer errId = fault.getErrorID();
        return "ErrorMessage:" + errMsg + '/' +
                "ExtendedErrorMessage:" + fault.getAltinnExtendedErrorMessage().getValue() + '/' +
                "LocalizedErrorMessage:" + fault.getAltinnLocalizedErrorMessage().getValue() + '/' +
                "ErrorGuid:" + fault.getErrorGuid().getValue() + '/' +
                "ErrorID:" + errId + '/' +
                "UserGuid:" + fault.getUserGuid().getValue() + '/' +
                "UserId:" + fault.getUserId().getValue();
    }

    private SubmitFormTask insertReceipt(ReceiptExternalBE receipt, String externalShipmentReference) {
        ReceiptStatusExternal status = receipt.getReceiptStatusCode();
        String recRef = null;
        String archRef = null;
        if (ReceiptStatusExternal.OK == status) {
            if (receipt.getReferences() != null) {
                for (ReferenceExternalBE ref : receipt.getReferences().getReferenceBE()) {
                    if (ReferenceTypeExternal.ARCHIVE_REFERENCE == ref.getReferenceTypeName()) {
                        archRef = ref.getReferenceValue();
                    } else if (ReferenceTypeExternal.RECEIVERS_REFERENCE == ref.getReferenceTypeName()) {
                        recRef = ref.getReferenceValue();
                    }
                }
            }
            return new SubmitFormTask(receipt.getReceiptId(), externalShipmentReference, archRef, recRef);
        } else {
            throw new IllegalArgumentException("ReceiptStatus er ikke OK, var " + status + " (" + receipt.getReceiptText() + ")");
        }
    }

}