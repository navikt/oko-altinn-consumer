package no.nav.okonomi.altinn.consumer;

import no.nav.okonomi.altinn.consumer.correspondenceservice.AltinnCorrespondenceConsumerConfig;
import no.nav.okonomi.altinn.consumer.formsubmitservice.AltinnFormSubmitConsumerConfig;
import no.nav.okonomi.altinn.consumer.receiptservice.AltinnReceiptConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan("no.nav.okonomi.altinn.consumer")
@Import({AltinnCorrespondenceConsumerConfig.class, AltinnFormSubmitConsumerConfig.class, AltinnReceiptConsumerConfig.class})
public class AltinnConsumerConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
