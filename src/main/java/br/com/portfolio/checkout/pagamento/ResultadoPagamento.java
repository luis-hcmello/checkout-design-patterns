package br.com.portfolio.checkout.pagamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Resultado do cálculo demonstrativo de uma estratégia de pagamento. */
public record ResultadoPagamento(
        String formaPagamento,
        BigDecimal valorOriginal,
        BigDecimal desconto,
        BigDecimal taxa,
        BigDecimal valorFinal
) {

    public ResultadoPagamento {
        if (formaPagamento == null || formaPagamento.isBlank()) {
            throw new IllegalArgumentException("A forma de pagamento é obrigatória.");
        }

        valorOriginal = normalizar(Objects.requireNonNull(valorOriginal));
        desconto = normalizar(Objects.requireNonNull(desconto));
        taxa = normalizar(Objects.requireNonNull(taxa));
        valorFinal = normalizar(Objects.requireNonNull(valorFinal));

        if (valorOriginal.signum() <= 0 || desconto.signum() < 0 || taxa.signum() < 0
                || desconto.compareTo(valorOriginal) > 0) {
            throw new IllegalArgumentException("Os valores do pagamento são inválidos.");
        }

        BigDecimal esperado = valorOriginal.subtract(desconto).add(taxa)
                .setScale(2, RoundingMode.HALF_UP);
        if (valorFinal.compareTo(esperado) != 0) {
            throw new IllegalArgumentException("O valor final não corresponde ao cálculo do pagamento.");
        }
    }

    private static BigDecimal normalizar(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }
}
