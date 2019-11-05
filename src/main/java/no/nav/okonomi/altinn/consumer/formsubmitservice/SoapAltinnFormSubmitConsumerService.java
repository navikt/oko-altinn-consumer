package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.AltinnFault;
import no.altinn.intermediaryinboundexternalec.FormTaskShipmentBE;
import no.altinn.intermediaryinboundexternalec.IIntermediaryInboundExternalEC2;
import no.altinn.intermediaryinboundexternalec.IIntermediaryInboundExternalEC2SubmitFormTaskECAltinnFaultFaultFaultMessage;
import no.altinn.intermediaryinboundexternalec.IIntermediaryInboundExternalEC2TestAltinnFaultFaultFaultMessage;
import no.altinn.intermediaryinboundexternalec.ReceiptExternalBE;
import no.altinn.intermediaryinboundexternalec.ReceiptStatusExternal;
import no.altinn.intermediaryinboundexternalec.ReferenceExternalBE;
import no.altinn.intermediaryinboundexternalec.ReferenceTypeExternal;
import no.nav.okonomi.altinn.consumer.AltinnConsumerInternalException;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBElement;
import java.io.StringWriter;
import java.util.Objects;

/**
 * Created by Levent Demir (Capgemini)
 */
public class SoapAltinnFormSubmitConsumerService implements AltinnFormSubmitConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoapAltinnFormSubmitConsumerService.class);

    private static final String FEIL_VED_AA_SENDE = "Feil ved å sende skjema til Altinn: ";

    private final IIntermediaryInboundExternalEC2 iIntermediaryInboundExternalEC2;

    private final SecurityCredentials credentials;

    private final FormTaskShipmentService formTaskShipmentService;

    public SoapAltinnFormSubmitConsumerService(IIntermediaryInboundExternalEC2 iIntermediaryInboundExternalEC2,
                                               SecurityCredentials securityCredentials,
                                               FormTaskShipmentService formTaskShipmentService) {
        Objects.requireNonNull(iIntermediaryInboundExternalEC2, "iIntermediaryInboundExternalEC2 must not be null");
        Objects.requireNonNull(securityCredentials, "securityCredentials must not be null");
        Objects.requireNonNull(formTaskShipmentService, "formTaskShipmentService must not be null");
        this.iIntermediaryInboundExternalEC2 = iIntermediaryInboundExternalEC2;
        this.credentials = securityCredentials;
        this.formTaskShipmentService = formTaskShipmentService;
    }

    public synchronized SubmitFormTask submitForm(AltinnMessage altinnMessage) throws AltinnFormSubmitServiceException, AltinnConsumerInternalException {
        FormTaskShipmentBE formTaskShipment = formTaskShipmentService.createFormTaskShipment(altinnMessage);
        ReceiptExternalBE receipt = submitFormTaskEC(formTaskShipment);
        return insertReceipt(receipt, altinnMessage.getOrderId());
    }

    public synchronized SubmitFormTask submitFormWithoutAttachment(AltinnMessage altinnMessage) throws AltinnFormSubmitServiceException, AltinnConsumerInternalException {
        FormTaskShipmentBE formTaskShipment = formTaskShipmentService.createFormTaskShipmentWithoutAttachments(altinnMessage);
        ReceiptExternalBE receipt = submitFormTaskEC(formTaskShipment);
        return insertReceipt(receipt, altinnMessage.getOrderId());
    }

    public synchronized void test() throws AltinnFormSubmitServiceException {
        try {
            iIntermediaryInboundExternalEC2.test();
        } catch (IIntermediaryInboundExternalEC2TestAltinnFaultFaultFaultMessage altinnFaultFaultFaultMessage) {
            AltinnFault faultInfo = altinnFaultFaultFaultMessage.getFaultInfo();
            LOGGER.error("Feil ved å sende skjema test til Altinn: {}", getAltinnErrorMessage(faultInfo));
            throw new AltinnFormSubmitServiceException(
                    "Feil ved å sende skjema test til Altinn: ",
                    getSafeString(faultInfo.getAltinnErrorMessage()),
                    faultInfo.getErrorID(),
                    altinnFaultFaultFaultMessage);
        }
    }

    private ReceiptExternalBE submitFormTaskEC(FormTaskShipmentBE formTaskShipment) throws AltinnFormSubmitServiceException {
        try {
            logFormTaskShipment(formTaskShipment);
            return iIntermediaryInboundExternalEC2.submitFormTaskEC(
                    credentials.getVirksomhetsbruker(),
                    credentials.getVirksomhetsbrukerPassord(),
                    formTaskShipment);
        } catch (IIntermediaryInboundExternalEC2SubmitFormTaskECAltinnFaultFaultFaultMessage e) {
            AltinnFault faultInfo = e.getFaultInfo();
            LOGGER.error(getAltinnErrorMessage(faultInfo));
            throw new AltinnFormSubmitServiceException(
                    FEIL_VED_AA_SENDE,
                    getSafeString(faultInfo.getAltinnErrorMessage()),
                    faultInfo.getErrorID(),
                    e);
        }
    }

    private String getAltinnErrorMessage(AltinnFault fault) {
        return fault == null ? "Ingen FaultInfo" : getAltinnFaultAsString(fault);
    }

    private String getAltinnFaultAsString(AltinnFault fault) {
        return "ErrorMessage:" + getSafeString(fault.getAltinnErrorMessage()) + '/' +
                "ExtendedErrorMessage:" + getSafeString(fault.getAltinnExtendedErrorMessage()) + '/' +
                "LocalizedErrorMessage:" + getSafeString(fault.getAltinnLocalizedErrorMessage()) + '/' +
                "ErrorGuid:" + getSafeString(fault.getErrorGuid()) + '/' +
                "ErrorID:" + fault.getErrorID() + '/' +
                "UserGuid:" + getSafeString(fault.getUserGuid()) + '/' +
                "UserId:" + getSafeString(fault.getUserId());
    }

    private String getSafeString(JAXBElement<String> element) {
        return element != null ? element.getValue() : "null";
    }

    private void logFormTaskShipment(FormTaskShipmentBE formTaskShipment) {
        StringWriter xml = new StringWriter();
        JAXB.marshal(formTaskShipment, xml);
        LOGGER.debug("Formtaskshipment til Altinn: {}", xml);
    }

    private SubmitFormTask insertReceipt(ReceiptExternalBE receipt, String externalShipmentReference) throws AltinnConsumerInternalException {
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
            throw new AltinnConsumerInternalException("ReceiptStatus er ikke OK, var " + status + " (" + receipt.getReceiptText() + ")");
            //TODO burde ikke kaste exception her
        }
    }

}