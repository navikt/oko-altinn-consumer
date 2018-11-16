package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.nav.okonomi.altinn.consumer.SubmitFormTask;

/**
 * Created by Levent Demir (Capgemini)
 */
public interface AltinnFormSubmitConsumerService {

    SubmitFormTask submitForm(AltinnMessage altinnMessage);

    SubmitFormTask submitFormWithoutAttachment(AltinnMessage altinnMessage);

    void test();

}