package com.baozistore.api.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErroResposta(
            LocalDateTime timestamp,
            int status,
            String erro,
            String mensagem,
            List<String> detalhes
    ) {
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        return montarResposta(HttpStatus.NOT_FOUND, ex.getMessage(), List.of());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResposta> tratarRegraNegocio(RegraNegocioException ex) {
        return montarResposta(HttpStatus.BAD_REQUEST, ex.getMessage(), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> tratarValidacao(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        return montarResposta(HttpStatus.BAD_REQUEST, "Erro de validação nos dados enviados.", erros);
    }

    private ResponseEntity<ErroResposta> montarResposta(HttpStatus status, String mensagem, List<String> detalhes) {
        ErroResposta body = new ErroResposta(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                detalhes
        );
        return ResponseEntity.status(status).body(body);
    }
}
