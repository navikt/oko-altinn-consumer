package no.nav.okonomi.altinn.consumer.receiptservice;

import no.nav.okonomi.altinn.consumer.SubmitFormTask;

/**
 * Created by Levent Demir (Capgemini)
 */
public interface AltinnReceiptConsumerService {

    SubmitFormTask getReceiptWithSubmitForm(SubmitFormTask submitFormTask);

    void test();

}