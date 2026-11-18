package br.com.portfolio.checkout.facade;

import br.com.portfolio.checkout.auditoria.RegistroAuditoria;
import br.com.portfolio.checkout.domain.Pedido;
import br.com.portfolio.checkout.pagamento.PagamentoStrategy;
import br.com.portfolio.checkout.pagamento.PagamentoStrategyResolver;
import br.com.portfolio.checkout.pagamento.ResultadoPagamento;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

/**
 * Facade: coordena as etapas do checkout e oferece uma operação de alto nível
 * para a camada REST, sem expor os detalhes de cada componente.
 */
@Service
public class CheckoutFacade {

    private final PagamentoStrategyResolver strategyResolver;
    private final RegistroAuditoria registroAuditoria;

    public CheckoutFacade(PagamentoStrategyResolver strategyResolver,
                          RegistroAuditoria registroAuditoria) {
        this.strategyResolver = Objects.requireNonNull(strategyResolver,
                "O resolvedor de pagamento é obrigatório.");
        this.registroAuditoria = Objects.requireNonNull(registroAuditoria,
                "O registro de auditoria é obrigatório.");
    }

    public ResultadoCheckout finalizarCompra(Pedido pedido, String codigoFormaPagamento) {
        Objects.requireNonNull(pedido, "O pedido é obrigatório.");

        PagamentoStrategy estrategia = strategyResolver.resolver(codigoFormaPagamento);
        ResultadoPagamento pagamento = estrategia.calcular(pedido.calcularTotal());
        Instant processadoEm = Instant.now();

        registroAuditoria.registrar("Checkout concluído"
                + " | pedido=" + pedido.codigo()
                + " | cliente=" + pedido.nomeCliente()
                + " | pagamento=" + pagamento.formaPagamento()
                + " | valorFinal=" + pagamento.valorFinal().toPlainString());

        return new ResultadoCheckout(pedido, pagamento, processadoEm);
    }
}
