package no.nav.okonomi.altinn.consumer.correspondenceservice;

import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import org.w3c.dom.Document;

public interface AltinnCorrespondenceConsumerService {

    Document retrieveDocument(SubmitFormTask submitFormTask) throws AltinnCorrespondenceConsumerServiceException;

    void test() throws AltinnCorrespondenceConsumerServiceException;
}
