package no.nav.okonomi.altinn.consumer;

import no.nav.okonomi.altinn.consumer.correspondenceservice.AltinnCorrespondenceConsumerConfig;
import no.nav.okonomi.altinn.consumer.formsubmitservice.AltinnFormSubmitConsumerConfig;
import no.nav.okonomi.altinn.consumer.receiptservice.AltinnReceiptConsumerConfig;
import no.nav.okonomi.altinn.consumer.security.ClientCallBackHandler;
import no.nav.okonomi.altinn.consumer.security.KeyStore;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan("no.nav.okonomi.altinn.consumer")
@Import({AltinnCorrespondenceConsumerConfig.class, AltinnFormSubmitConsumerConfig.class, AltinnReceiptConsumerConfig.class})
public class AltinnConsumerConfig {

    @Value("${altinn-consumer.virksomhet.username}")
    private String virksomhetUserName;

    @Value("${altinn-consumer.virksomhet.password}")
    private String virksomhetPassord;

    @Value("${altinn-consumer.security.appcert.keystorealias}")
    private String appcertKeystorealias;

    @Value("${altinn-consumer.security.appcert.password}")
    private String appcertSecret;

    @Value("${altinn-consumer.security.appcert.keystore:app-key}")
    private String appcertKeystore;

    @Bean
    public KeyStore keyStore() {
        return new KeyStore(appcertKeystore, appcertKeystorealias, appcertSecret, "jks");
    }

    @Bean
    public SecurityCredentials securityCredentials(KeyStore keyStore) {
        return new SecurityCredentials(virksomhetUserName, virksomhetPassord, keyStore);
    }

    @Bean
    public ClientCallBackHandler clientCallBackHandler() {
        return new ClientCallBackHandler();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
