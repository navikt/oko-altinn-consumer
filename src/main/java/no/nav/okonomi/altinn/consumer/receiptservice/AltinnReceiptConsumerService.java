package no.nav.okonomi.altinn.consumer.receiptservice;

import no.nav.okonomi.altinn.consumer.SubmitFormTask;

public interface AltinnReceiptConsumerService {

    SubmitFormTask getReceiptWithSubmitForm(SubmitFormTask submitFormTask) throws AltinnReceiptServiceException;
    void test() throws AltinnReceiptServiceException;
}
