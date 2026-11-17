package br.com.portfolio.checkout.facade;

import br.com.portfolio.checkout.auditoria.RegistroAuditoria;
import br.com.portfolio.checkout.domain.Pedido;
import br.com.portfolio.checkout.pagamento.PagamentoStrategy;
import br.com.portfolio.checkout.pagamento.ResultadoPagamento;

import java.time.Instant;
import java.util.Objects;

/**
 * Facade: oferece uma operação simples para concluir a compra,
 * escondendo a sequência de cálculo, registro e montagem da resposta.
 */
public class CheckoutFacade {

    private final PagamentoStrategy pagamentoStrategy;
    private final RegistroAuditoria registroAuditoria;

    public CheckoutFacade(PagamentoStrategy pagamentoStrategy, RegistroAuditoria registroAuditoria) {
        this.pagamentoStrategy = Objects.requireNonNull(pagamentoStrategy,
                "A estratégia de pagamento é obrigatória.");
        this.registroAuditoria = Objects.requireNonNull(registroAuditoria,
                "O registro de auditoria é obrigatório.");
    }

    public ResultadoCheckout finalizarCompra(Pedido pedido) {
        Objects.requireNonNull(pedido, "O pedido é obrigatório.");

        ResultadoPagamento pagamento = pagamentoStrategy.calcular(pedido.calcularTotal());
        Instant processadoEm = Instant.now();

        registroAuditoria.registrar("Checkout concluído"
                + " | pedido=" + pedido.codigo()
                + " | cliente=" + pedido.nomeCliente()
                + " | pagamento=" + pagamento.formaPagamento()
                + " | valorFinal=" + pagamento.valorFinal().toPlainString());

        return new ResultadoCheckout(pedido, pagamento, processadoEm);
    }
}
