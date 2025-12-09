package ee.markh.webshopbackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Configuration
public class AppConfig {

    @Bean
    public Random getRandom() {
        return new Random();
        // tänu beanile teeb selle ühe korra
    }

//    @Bean
//    public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
//        //seadistus siin
//        builder.headers();
//        builder.timeout(10);
//        return builder.build();
//        return new RestTemplate(); build mitte see returnib siis
        // tänu beanile teeb selle ühe korra
        // maksed, smart-id, mid, pakiautomaadid -- > teise rakendusse päringud RestTemplate-iga
        // konstruktoris seadistada ei saa
    }
//}
