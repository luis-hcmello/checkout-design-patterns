package br.com.portfolio.checkout.pagamento;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PagamentoStrategyTest {

    private final BigDecimal valor = new BigDecimal("400.00");

    @Test
    void pixDeveAplicarCincoPorCentoDeDesconto() {
        ResultadoPagamento resultado = new PixPagamentoStrategy().calcular(valor);

        assertEquals(0, new BigDecimal("20.00").compareTo(resultado.desconto()));
        assertEquals(0, new BigDecimal("0.00").compareTo(resultado.taxa()));
        assertEquals(0, new BigDecimal("380.00").compareTo(resultado.valorFinal()));
    }

    @Test
    void cartaoDeveAplicarDoisPorCentoDeTaxa() {
        ResultadoPagamento resultado = new CartaoCreditoPagamentoStrategy().calcular(valor);

        assertEquals(0, new BigDecimal("0.00").compareTo(resultado.desconto()));
        assertEquals(0, new BigDecimal("8.00").compareTo(resultado.taxa()));
        assertEquals(0, new BigDecimal("408.00").compareTo(resultado.valorFinal()));
    }

    @Test
    void boletoDeveAplicarDoisPorCentoDeDesconto() {
        ResultadoPagamento resultado = new BoletoPagamentoStrategy().calcular(valor);

        assertEquals(0, new BigDecimal("8.00").compareTo(resultado.desconto()));
        assertEquals(0, new BigDecimal("0.00").compareTo(resultado.taxa()));
        assertEquals(0, new BigDecimal("392.00").compareTo(resultado.valorFinal()));
    }

    @Test
    void naoDeveAceitarDescontoMaiorQueOValorOriginal() {
        assertThrows(IllegalArgumentException.class, () -> new ResultadoPagamento(
                "Teste",
                new BigDecimal("10.00"),
                new BigDecimal("11.00"),
                BigDecimal.ZERO,
                new BigDecimal("-1.00")
        ));
    }

    @Test
    void naoDeveCalcularPagamentoComValorZeroOuNegativo() {
        PagamentoStrategy estrategia = new PixPagamentoStrategy();

        assertThrows(IllegalArgumentException.class,
                () -> estrategia.calcular(new BigDecimal("0.00")));
        assertThrows(IllegalArgumentException.class,
                () -> estrategia.calcular(new BigDecimal("-10.00")));
    }
}
