package no.nav.okonomi.altinn.consumer.receiptservice;

import no.altinn.receiptexternalec.IReceiptExternalEC2;
import no.altinn.receiptexternalec.v201506.Receipt;
import no.altinn.receiptexternalec.v201506.ReceiptSearch;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import no.nav.okonomi.altinn.consumer.correspondenceservice.AltinnCorrespondenceServiceException;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by Levent Demir (Capgemini)
 */
@Component
public class DefaultAltinnReceiptConsumerService implements AltinnReceiptConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAltinnReceiptConsumerService.class);

    private final IReceiptExternalEC2 iReceiptExternalEC;
    private SecurityCredentials credentials;
    private ReceiptService receiptService;

    @Inject
    public DefaultAltinnReceiptConsumerService(IReceiptExternalEC2 iReceiptExternalEC,
                                               SecurityCredentials credentials,
                                               ReceiptService receiptService) {
        this.credentials = credentials;
        this.iReceiptExternalEC = iReceiptExternalEC;
        this.receiptService = receiptService;
    }

    @Override
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

    @Override
    public synchronized void test() {
        try {
            iReceiptExternalEC.test();
        } catch (Exception e) {
            LOGGER.warn("Henting av kvittering fra Altinn feilet", e);
            throw new AltinnCorrespondenceServiceException("Henting av kvittering fra Altinn feilet ", e);
        }
    }

}