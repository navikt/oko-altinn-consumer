package no.nav.okonomi.altinn.consumer.receiptservice;

import no.altinn.receiptexternalec.IReceiptExternalEC2;
import no.altinn.receiptexternalec.v201506.Receipt;
import no.altinn.receiptexternalec.v201506.ReceiptSearch;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import no.nav.okonomi.altinn.consumer.correspondenceservice.AltinnCorrespondenceServiceException;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Created by Levent Demir (Capgemini)
 */
public class AltinnReceiptConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AltinnReceiptConsumerService.class);

    private final IReceiptExternalEC2 iReceiptExternalEC;
    private SecurityCredentials credentials;
    private ReceiptService receiptService;

    public AltinnReceiptConsumerService(IReceiptExternalEC2 iReceiptExternalEC,
                                        SecurityCredentials securityCredentials,
                                        ReceiptService receiptService) {
        Objects.requireNonNull(iReceiptExternalEC, "iReceiptExternalEC must not be null");
        Objects.requireNonNull(securityCredentials, "securityCredentials must not be null");
        Objects.requireNonNull(receiptService, "receiptService must not be null");
        this.credentials = securityCredentials;
        this.iReceiptExternalEC = iReceiptExternalEC;
        this.receiptService = receiptService;
    }

    public synchronized SubmitFormTask getReceiptWithSubmitForm(SubmitFormTask submitFormTask) {
        try {
            ReceiptSearch receiptSearch = receiptService.createReceipt(
                    submitFormTask.getReceiptId(),
                    submitFormTask.getExternalShipmentReference());

            Receipt receipt = iReceiptExternalEC.getReceiptECV2(
                    credentials.getVirksomhetsbruker(),
                    credentials.getVirksomhetsbrukerPassord(),
                    receiptSearch);

            return receiptService.updateReceipt(
                    receipt,
                    submitFormTask);
        } catch (Exception e) {
            throw new AltinnReceiptServiceException("Henting av kvittering fra Altinn feilet ", e);
        }
    }

    public synchronized void test() {
        try {
            iReceiptExternalEC.test();
        } catch (Exception e) {
            LOGGER.warn("Henting av kvittering fra Altinn feilet", e);
            throw new AltinnCorrespondenceServiceException("Henting av kvittering fra Altinn feilet ", e);
        }
    }

}