package pe.edu.vallegrande.appsscript.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import pe.edu.vallegrande.appsscript.model.CertificateModel;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AppsScriptService {
    private final String SCRIPT_ID = "AKfycbwf1uT79MqsGqPKzGT1kyYvSEy66SDrf7xrCjV1mJHBjhWJqVxUurIdqnBcIxmdJwJ_Fw";
    private final String URL = "https://script.google.com/macros/s/" + SCRIPT_ID + "/exec";

    public Flux<CertificateModel> getCertificates() {

        ExchangeFilterFunction eff = ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().is3xxRedirection()) {
                return WebClient.create(String.valueOf(clientResponse.headers().asHttpHeaders().getLocation()))
                        .get()
                        .exchange();
            }
            return Mono.just(clientResponse);
        });

        return WebClient.builder()
                .baseUrl(URL)
                .filter(eff)
                .build()
                .get()
                .retrieve()
                .bodyToFlux(CertificateModel.class);
    }

    public Mono<Void> generatePdf() {
        return getCertificates().collectList().flatMap(certificatesList ->
                WebClient.create(URL)
                        .post()
                        .body(BodyInserters.fromValue(certificatesList))
                        .retrieve()
                        .bodyToMono(Void.class)
        );
    }
}
