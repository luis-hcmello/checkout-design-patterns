package br.com.portfolio.checkout.pagamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Contrato comum para as diferentes regras de cálculo de pagamento. */
public interface PagamentoStrategy {

    String codigo();

    String nome();

    ResultadoPagamento calcular(BigDecimal valorPedido);

    default BigDecimal validarEArredondar(BigDecimal valorPedido) {
        Objects.requireNonNull(valorPedido, "O valor do pedido é obrigatório.");
        BigDecimal valor = valorPedido.setScale(2, RoundingMode.HALF_UP);
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException("O valor do pedido deve ser maior que zero.");
        }
        return valor;
    }

    default BigDecimal calcularPercentual(BigDecimal valor, String percentual) {
        return valor.multiply(new BigDecimal(percentual))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
