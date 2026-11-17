package br.com.portfolio.checkout.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PedidoTest {

    @Test
    void deveManterUmaCopiaImutavelDosItensRecebidos() {
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido("Caderno", 1, new BigDecimal("20.00")));
        Pedido pedido = new Pedido("PED-IMUTAVEL", "Cliente", itens);

        itens.clear();

        assertEquals(1, pedido.itens().size());
        assertThrows(UnsupportedOperationException.class,
                () -> pedido.itens().add(new ItemPedido("Caneta", 1, new BigDecimal("3.00"))));
    }

    @Test
    void naoDeveCriarPedidoSemItens() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pedido("PED-VAZIO", "Cliente", List.of()));
    }
}
