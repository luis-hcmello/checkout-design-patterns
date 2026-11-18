package br.com.portfolio.checkout.api.dto.response;

import br.com.portfolio.checkout.facade.ResultadoCheckout;

import java.math.BigDecimal;
import java.time.Instant;

public record CheckoutResponse(
        String codigoPedido,
        String nomeCliente,
        int quantidadeItens,
        BigDecimal valorOriginal,
        String formaPagamento,
        BigDecimal desconto,
        BigDecimal taxa,
        BigDecimal valorFinal,
        Instant processadoEm
) {
    public static CheckoutResponse from(ResultadoCheckout resultado) {
        var pedido = resultado.pedido();
        var pagamento = resultado.pagamento();

        return new CheckoutResponse(
                pedido.codigo(),
                pedido.nomeCliente(),
                pedido.itens().size(),
                pagamento.valorOriginal(),
                pagamento.formaPagamento(),
                pagamento.desconto(),
                pagamento.taxa(),
                pagamento.valorFinal(),
                resultado.processadoEm()
        );
    }
}
