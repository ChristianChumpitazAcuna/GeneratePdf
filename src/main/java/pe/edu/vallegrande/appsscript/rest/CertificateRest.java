package pe.edu.vallegrande.appsscript.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.vallegrande.appsscript.model.CertificateModel;
import pe.edu.vallegrande.appsscript.service.AppsScriptService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/appsscript")
public class CertificateRest {

    @Autowired
    private AppsScriptService service;

    @RequestMapping()
    public Flux<CertificateModel> getAll() {
        return service.getCertificates();
    }

    @PostMapping()
    public Mono<Void> generatePdf() {
        return service.generatePdf();
    }
}
