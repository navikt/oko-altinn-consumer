package no.nav.okonomi.altinn.consumer.receiptservice;

import no.altinn.receiptexternalec.IReceiptExternalEC;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import no.nav.okonomi.altinn.consumer.SubmitFormTaskBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

@Ignore
@ContextConfiguration(classes = {ReceiptConsumerTestConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource({"classpath:properties/altinntjenester-uri.properties", "classpath:properties/navtestklient-credentials.properties", "classpath:properties/navtestklient-certificate.properties"})
public class ReceiptITest {

    @Inject
    private IReceiptExternalEC receiptPortType;

    @Inject
    private ReceiptService receiptService;

    private AltinnReceiptConsumerService service;

    @Before
    public void setup() {
        service = new DefaultAltinnReceiptConsumerService(receiptPortType, ReceiptConsumerTestConfig.credentials, receiptService);
    }

    @Test
    public void receiptServiceTest() throws Exception {
        try {
            service.test();
        } catch (Exception e) {
            fail("Caught exception");
        }
    }

    @Test
    @Ignore
    public void testGetReceipt() {
        try {
            SubmitFormTask sft = new SubmitFormTaskBuilder()
                    .receipdId(11663407)
                    .externalShipmentReference("90f70a46-cb4e-4dcc-9138-45bc3fdf8f91")
                    .build();

            sft = service.getReceiptWithSubmitForm(sft);
            assertThat(sft, notNullValue());
        } catch (Exception e) {
            fail("Caught exception");
        }
    }

}