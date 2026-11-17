package br.com.portfolio.checkout.facade;

import br.com.portfolio.checkout.domain.Pedido;
import br.com.portfolio.checkout.pagamento.ResultadoPagamento;

import java.time.Instant;
import java.util.Objects;

/** Informações retornadas ao cliente depois da conclusão do checkout. */
public record ResultadoCheckout(Pedido pedido, ResultadoPagamento pagamento, Instant processadoEm) {

    public ResultadoCheckout {
        Objects.requireNonNull(pedido, "O pedido é obrigatório.");
        Objects.requireNonNull(pagamento, "O resultado do pagamento é obrigatório.");
        Objects.requireNonNull(processadoEm, "A data do processamento é obrigatória.");
    }
}
