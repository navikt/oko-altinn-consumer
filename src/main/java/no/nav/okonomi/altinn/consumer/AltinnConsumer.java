package no.nav.okonomi.altinn.consumer;

import no.nav.okonomi.altinn.consumer.correspondenceservice.AltinnCorrespondenceConsumerService;
import no.nav.okonomi.altinn.consumer.formsubmitservice.AltinnFormSubmitConsumerService;
import no.nav.okonomi.altinn.consumer.formsubmitservice.AltinnMessage;
import no.nav.okonomi.altinn.consumer.receiptservice.AltinnReceiptConsumerService;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.inject.Inject;

@Component
public class AltinnConsumer {

    private AltinnCorrespondenceConsumerService correspondenceConsumerService;
    private AltinnFormSubmitConsumerService formSubmitConsumerService;
    private AltinnReceiptConsumerService receiptConsumerService;

    @Inject
    public AltinnConsumer(AltinnCorrespondenceConsumerService correspondenceConsumerService,
                          AltinnFormSubmitConsumerService formSubmitConsumerService,
                          AltinnReceiptConsumerService receiptConsumerService) {
        this.correspondenceConsumerService = correspondenceConsumerService;
        this.formSubmitConsumerService = formSubmitConsumerService;
        this.receiptConsumerService = receiptConsumerService;
    }

    public SubmitFormTask sendBestilling(AltinnMessage altinnMessage) {
        return formSubmitConsumerService.submitForm(altinnMessage);
    }

    public SubmitFormTask hentKvittering(SubmitFormTask submitFormTask) {
        return receiptConsumerService.getReceiptWithSubmitForm(submitFormTask);
    }

    public Document hentMelding(SubmitFormTask submitFormTask) {
        return correspondenceConsumerService.retrieveDocument(submitFormTask);
    }

}
