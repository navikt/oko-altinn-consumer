package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.nav.okonomi.altinn.consumer.SubmitFormTask;

public interface AltinnFormSubmitConsumerService {

    SubmitFormTask submitForm(AltinnMessage altinnMessage);
    SubmitFormTask submitFormWithoutAttachment(AltinnMessage altinnMessage);
    void test();
}
