package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.nav.okonomi.altinn.consumer.SubmitFormTask;

public interface AltinnFormSubmitConsumerService {

    SubmitFormTask submitForm(AltinnMessage altinnMessage) throws AltinnFormSubmitServiceException;
    SubmitFormTask submitFormWithoutAttachment(AltinnMessage altinnMessage) throws AltinnFormSubmitServiceException;
    void test() throws AltinnFormSubmitServiceException;
}
