package no.nav.okonomi.altinn.consumer;

import no.altinn.correspondenceexternalec.ICorrespondenceExternalEC;
import no.nav.okonomi.altinn.consumer.correspondenceservice.AltinnCorrespondenceConsumerService;
import no.nav.okonomi.altinn.consumer.correspondenceservice.DefaultAltinnCorrespondenceConsumerService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ReflectionUtils;
import org.w3c.dom.Document;

import javax.inject.Inject;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.lang.reflect.Field;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@Ignore
@ContextConfiguration(classes = { CorrespondenceConsumerTestConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource({ "classpath:properties/altinntjenester-uri.properties", "classpath:properties/navtestklient-credentials.properties", "classpath:properties/navtestklient-certificate.properties" })
public class CorrespondenceITest {

    @Inject
    private ICorrespondenceExternalEC correspondencePortType;

    private AltinnCorrespondenceConsumerService service;

    @Before
    public void setup() {
        Field field = ReflectionUtils.findField(DefaultAltinnCorrespondenceConsumerService.class, "languageId");
        ReflectionUtils.makeAccessible(field);

        service = new DefaultAltinnCorrespondenceConsumerService(correspondencePortType, CorrespondenceConsumerTestConfig.credentials);
        ReflectionUtils.setField(field, service, 1044);
    }

    @Test
    @Ignore
    public void correspondenceServiceTest() {
        try {
            service.test();
        } catch (Exception e) {
            fail("Caught exception");
        }
    }

    @Test
    @Ignore
    public void testGetCorrespondenceAttachment() {

        try {
            SubmitFormTask sft = new SubmitFormTask(11663407, "90f70a46-cb4e-4dcc-9138-45bc3fdf8f91", "AR3697057", "4973347");
            Document doc = service.retrieveDocument(sft);

            Transformer transfomer = TransformerFactory.newInstance().newTransformer();
            Source source = new DOMSource(doc);
            Result output = new StreamResult(System.out);
            transfomer.transform(source, output);

            assertNotNull(doc);

        } catch (Exception e) {
            fail("Caught exception");
        }
    }

}