package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.nav.okonomi.altinn.consumer.AltinnConsumerInternalException;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;

public interface AltinnFormSubmitConsumerService {

    SubmitFormTask submitForm(AltinnMessage altinnMessage) throws AltinnFormSubmitServiceException, AltinnConsumerInternalException;
    SubmitFormTask submitFormWithoutAttachment(AltinnMessage altinnMessage) throws AltinnFormSubmitServiceException, AltinnConsumerInternalException;
    void test() throws AltinnFormSubmitServiceException;
}
