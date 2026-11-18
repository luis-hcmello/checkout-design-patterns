package br.com.portfolio.checkout.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CheckoutRequest(
        @NotBlank(message = "O código do pedido é obrigatório.")
        String codigo,

        @NotBlank(message = "O nome do cliente é obrigatório.")
        String nomeCliente,

        @NotEmpty(message = "O pedido precisa ter pelo menos um item.")
        List<@Valid ItemPedidoRequest> itens,

        @NotBlank(message = "A forma de pagamento é obrigatória.")
        String formaPagamento
) {
}
