package br.com.portfolio.checkout.api.exception;

import br.com.portfolio.checkout.api.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> tratarValidacao(MethodArgumentNotValidException exception) {
        List<String> detalhes = exception.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .distinct()
                .toList();

        return criarResposta(HttpStatus.BAD_REQUEST, "Verifique os dados enviados.", detalhes);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> tratarArgumentoInvalido(IllegalArgumentException exception) {
        return criarResposta(HttpStatus.BAD_REQUEST, exception.getMessage(), List.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> tratarCorpoInvalido(HttpMessageNotReadableException exception) {
        return criarResposta(HttpStatus.BAD_REQUEST,
                "O corpo da requisição está ausente ou contém JSON inválido.", List.of());
    }

    private ResponseEntity<ApiErrorResponse> criarResposta(
            HttpStatus status, String mensagem, List<String> detalhes) {
        ApiErrorResponse resposta = new ApiErrorResponse(
                Instant.now(), status.value(), status.getReasonPhrase(), mensagem, detalhes);
        return ResponseEntity.status(status).body(resposta);
    }
}
