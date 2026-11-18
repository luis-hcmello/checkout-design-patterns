package br.com.portfolio.checkout.api.dto.response;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String erro,
        String mensagem,
        List<String> detalhes
) {
}
