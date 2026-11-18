package br.com.portfolio.checkout.api.controller;

import br.com.portfolio.checkout.api.dto.response.FormaPagamentoResponse;
import br.com.portfolio.checkout.pagamento.PagamentoStrategyResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoStrategyResolver strategyResolver;

    public PagamentoController(PagamentoStrategyResolver strategyResolver) {
        this.strategyResolver = strategyResolver;
    }

    @GetMapping
    public List<FormaPagamentoResponse> listarFormasPagamento() {
        return strategyResolver.listar().stream()
                .map(estrategia -> new FormaPagamentoResponse(estrategia.codigo(), estrategia.nome()))
                .toList();
    }
}
