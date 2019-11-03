package no.nav.okonomi.altinn.consumer.receiptservice;

import no.altinn.receiptexternalec.AltinnFault;
import no.altinn.receiptexternalec.IReceiptExternalEC2;
import no.altinn.receiptexternalec.IReceiptExternalEC2GetReceiptECV2AltinnFaultFaultFaultMessage;
import no.altinn.receiptexternalec.IReceiptExternalEC2TestAltinnFaultFaultFaultMessage;
import no.altinn.receiptexternalec.v201506.Receipt;
import no.altinn.receiptexternalec.v201506.ReceiptSearch;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBElement;
import java.util.Objects;

/**
 * Created by Levent Demir (Capgemini)
 */
public class SoapAltinnReceiptConsumerService implements AltinnReceiptConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoapAltinnReceiptConsumerService.class);

    private final IReceiptExternalEC2 iReceiptExternalEC;
    private SecurityCredentials credentials;
    private ReceiptService receiptService;

    public SoapAltinnReceiptConsumerService(IReceiptExternalEC2 iReceiptExternalEC,
                                            SecurityCredentials securityCredentials,
                                            ReceiptService receiptService) {
        Objects.requireNonNull(iReceiptExternalEC, "iReceiptExternalEC must not be null");
        Objects.requireNonNull(securityCredentials, "securityCredentials must not be null");
        Objects.requireNonNull(receiptService, "receiptService must not be null");
        this.credentials = securityCredentials;
        this.iReceiptExternalEC = iReceiptExternalEC;
        this.receiptService = receiptService;
    }

    public synchronized SubmitFormTask getReceiptWithSubmitForm(SubmitFormTask submitFormTask) throws AltinnReceiptServiceException {
        try {
            ReceiptSearch receiptSearch = receiptService.createReceipt(
                    submitFormTask.getReceiptId(),
                    submitFormTask.getExternalShipmentReference());

            Receipt receipt = iReceiptExternalEC.getReceiptECV2(
                    credentials.getVirksomhetsbruker(),
                    credentials.getVirksomhetsbrukerPassord(),
                    receiptSearch);

            return receiptService.updateReceipt(receipt,submitFormTask);


        } catch (IReceiptExternalEC2GetReceiptECV2AltinnFaultFaultFaultMessage faultMessage) {
            AltinnFault faultInfo = faultMessage.getFaultInfo();
            throw new AltinnReceiptServiceException(
                    "Henting av kvittering fra Altinn feilet ",
                    getSafeString(faultInfo.getAltinnErrorMessage()),
                    faultInfo.getErrorID(),
                    faultMessage);
        }
    }

    public synchronized void test() throws AltinnReceiptServiceException {
        try {
            iReceiptExternalEC.test();
        } catch (IReceiptExternalEC2TestAltinnFaultFaultFaultMessage faultMessage) {
            AltinnFault faultInfo = faultMessage.getFaultInfo();
             LOGGER.warn("Henting av kvittering fra Altinn feilet", faultMessage);
            throw new AltinnReceiptServiceException(
                    "Henting av kvittering fra Altinn feilet ",
                    getSafeString(faultInfo.getAltinnErrorMessage()),
                    faultInfo.getErrorID(),
                    faultMessage);

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

}