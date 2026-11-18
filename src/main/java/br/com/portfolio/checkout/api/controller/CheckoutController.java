package br.com.portfolio.checkout.api.controller;

import br.com.portfolio.checkout.api.dto.request.CheckoutRequest;
import br.com.portfolio.checkout.api.dto.response.CheckoutResponse;
import br.com.portfolio.checkout.domain.ItemPedido;
import br.com.portfolio.checkout.domain.Pedido;
import br.com.portfolio.checkout.facade.CheckoutFacade;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutFacade checkoutFacade;

    public CheckoutController(CheckoutFacade checkoutFacade) {
        this.checkoutFacade = checkoutFacade;
    }

    @PostMapping
    public ResponseEntity<CheckoutResponse> finalizarCompra(@Valid @RequestBody CheckoutRequest request) {
        Pedido pedido = new Pedido(
                request.codigo(),
                request.nomeCliente(),
                request.itens().stream()
                        .map(item -> new ItemPedido(
                                item.descricao(),
                                item.quantidade(),
                                item.precoUnitario()
                        ))
                        .toList()
        );

        var resultado = checkoutFacade.finalizarCompra(pedido, request.formaPagamento());
        return ResponseEntity.ok(CheckoutResponse.from(resultado));
    }
}
