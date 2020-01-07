package no.nav.okonomi.altinn.consumer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnviromentPropertiesReaderTest {

   private  EnviromentPropertiesReader enviromentPropertiesReader = new EnviromentPropertiesReader();

   @Test
   void getSbsUserName(){
       String expected = "test username";
       System.setProperty("altinn-consumer.srvuser-sbs.username", expected);

       String userName = enviromentPropertiesReader.getSbsUserName();
       assertEquals(expected, userName);
   }

   @Test
    void getSbsPassword(){
       assertThrows(AltinnConsumerException.class, () -> enviromentPropertiesReader.getSbsPassword());
   }

   @Test
    void getAppcertKeystorealias(){
       assertThrows(AltinnConsumerException.class, ()-> enviromentPropertiesReader.getAppcertKeystorealias());
   }

   @Test
    void getAppcertSecret(){
       String expected = "changeit";
       System.setProperty("NAV_TRUSTSTORE_PASSWORD", expected);
       String actual = enviromentPropertiesReader.getAppcertSecret();
       assertEquals(expected, actual);
   }
}