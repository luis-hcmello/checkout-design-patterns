package br.com.portfolio.checkout.auditoria;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class RegistroAuditoriaTest {

    @Test
    void deveDisponibilizarSempreAMesmaInstancia() {
        assertSame(RegistroAuditoria.INSTANCE, RegistroAuditoria.valueOf("INSTANCE"));
    }
}
