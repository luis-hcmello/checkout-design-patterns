package br.com.portfolio.checkout.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/** Pedido imutável: os itens são copiados para evitar alterações externas. */
public record Pedido(String codigo, String nomeCliente, List<ItemPedido> itens) {

    public Pedido {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código do pedido é obrigatório.");
        }
        if (nomeCliente == null || nomeCliente.isBlank()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }

        Objects.requireNonNull(itens, "A lista de itens é obrigatória.");
        itens = List.copyOf(itens);
        if (itens.isEmpty()) {
            throw new IllegalArgumentException("O pedido precisa ter pelo menos um item.");
        }
    }

    public BigDecimal calcularTotal() {
        return itens.stream()
                .map(ItemPedido::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
