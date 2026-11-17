package br.com.portfolio.checkout.facade;

import br.com.portfolio.checkout.auditoria.RegistroAuditoria;
import br.com.portfolio.checkout.domain.ItemPedido;
import br.com.portfolio.checkout.domain.Pedido;
import br.com.portfolio.checkout.pagamento.PixPagamentoStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckoutFacadeTest {

    @Test
    void deveFinalizarCompraCalcularPagamentoERegistrarAuditoria() {
        Pedido pedido = new Pedido(
                "TESTE-FACADE-001",
                "Pessoa Teste",
                List.of(new ItemPedido("Livro", 2, new BigDecimal("50.00")))
        );
        int eventosAntes = RegistroAuditoria.INSTANCE.listarEventos().size();
        CheckoutFacade facade = new CheckoutFacade(new PixPagamentoStrategy(), RegistroAuditoria.INSTANCE);

        ResultadoCheckout resultado = facade.finalizarCompra(pedido);

        assertEquals("TESTE-FACADE-001", resultado.pedido().codigo());
        assertEquals(0, new BigDecimal("100.00").compareTo(resultado.pagamento().valorOriginal()));
        assertEquals(0, new BigDecimal("95.00").compareTo(resultado.pagamento().valorFinal()));
        assertNotNull(resultado.processadoEm());
        assertEquals(eventosAntes + 1, RegistroAuditoria.INSTANCE.listarEventos().size());
        assertTrue(RegistroAuditoria.INSTANCE.listarEventos().get(eventosAntes)
                .contains("pedido=TESTE-FACADE-001"));
    }

    @Test
    void naoDeveFinalizarCompraComPedidoNulo() {
        CheckoutFacade facade = new CheckoutFacade(new PixPagamentoStrategy(), RegistroAuditoria.INSTANCE);

        assertThrows(NullPointerException.class, () -> facade.finalizarCompra(null));
    }
}
