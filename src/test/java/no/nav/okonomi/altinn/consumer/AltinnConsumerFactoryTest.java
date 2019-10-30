package no.nav.okonomi.altinn.consumer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AltinnConsumerFactoryTest {


    private AltinnConsumerFactory altinnConsumerFactory = new AltinnConsumerFactory(new TestPropertiesReader());

    @Test
    void createAltinnCorrespondenceConsumerService() {
        assertNotNull(altinnConsumerFactory.createAltinnCorrespondenceConsumerService());

    }

    @Test
    void createAltinnFormSubmitConsumerService() {
        assertNotNull(altinnConsumerFactory.createAltinnFormSubmitConsumerService());
    }

    @Test
    void createltinnReceiptConsumerService() {
        assertNotNull(altinnConsumerFactory.createltinnReceiptConsumerService());
    }
}