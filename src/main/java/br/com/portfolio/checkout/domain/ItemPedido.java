package br.com.portfolio.checkout.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Representa uma linha de produto dentro de um pedido. */
public record ItemPedido(String descricao, int quantidade, BigDecimal precoUnitario) {

    public ItemPedido {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição do item é obrigatória.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        Objects.requireNonNull(precoUnitario, "O preço unitário é obrigatório.");
        precoUnitario = precoUnitario.setScale(2, RoundingMode.HALF_UP);
        if (precoUnitario.signum() <= 0) {
            throw new IllegalArgumentException("O preço unitário deve ser maior que zero.");
        }
    }

    public BigDecimal calcularSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
