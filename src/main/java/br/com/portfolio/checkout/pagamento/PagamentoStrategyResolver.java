package br.com.portfolio.checkout.pagamento;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Localiza a estratégia de pagamento a partir do código recebido pela API. */
@Component
public class PagamentoStrategyResolver {

    private final Map<String, PagamentoStrategy> estrategias;

    public PagamentoStrategyResolver(List<PagamentoStrategy> estrategiasDisponiveis) {
        this.estrategias = estrategiasDisponiveis.stream()
                .collect(Collectors.toUnmodifiableMap(
                        estrategia -> normalizar(estrategia.codigo()),
                        Function.identity()
                ));
    }

    public PagamentoStrategy resolver(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("A forma de pagamento é obrigatória.");
        }

        PagamentoStrategy estrategia = estrategias.get(normalizar(codigo));
        if (estrategia == null) {
            throw new IllegalArgumentException("Forma de pagamento não suportada: " + codigo);
        }
        return estrategia;
    }

    public List<PagamentoStrategy> listar() {
        return estrategias.values().stream()
                .sorted((primeira, segunda) -> primeira.codigo().compareTo(segunda.codigo()))
                .toList();
    }

    private static String normalizar(String codigo) {
        return Objects.requireNonNull(codigo, "O código da estratégia é obrigatório.")
                .trim()
                .toUpperCase(Locale.ROOT);
    }
}
