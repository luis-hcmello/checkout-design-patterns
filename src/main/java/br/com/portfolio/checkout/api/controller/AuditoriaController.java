package br.com.portfolio.checkout.api.controller;

import br.com.portfolio.checkout.auditoria.RegistroAuditoria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    private final RegistroAuditoria registroAuditoria;

    public AuditoriaController(RegistroAuditoria registroAuditoria) {
        this.registroAuditoria = registroAuditoria;
    }

    @GetMapping
    public List<String> listarEventos() {
        return registroAuditoria.listarEventos();
    }
}
