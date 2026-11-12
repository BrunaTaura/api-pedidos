package br.com.bilheteria.api_pedidos.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void configurar() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void deveRetornarBadRequestComMensagem() {

        ResponseStatusException exception =
                new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Status inválido"
                );

        ResponseEntity<Map<String, Object>> response =
                handler.tratarErro(exception);

        assertEquals(HttpStatus.BAD_REQUEST,
                response.getStatusCode());

        assertEquals(400,
                response.getBody().get("status"));

        assertEquals("Status inválido",
                response.getBody().get("mensagem"));
    }

    @Test
    void deveRetornarNotFoundComMensagem() {

        ResponseStatusException exception =
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pedido não encontrado"
                );

        ResponseEntity<Map<String, Object>> response =
                handler.tratarErro(exception);

        assertEquals(HttpStatus.NOT_FOUND,
                response.getStatusCode());

        assertEquals(404,
                response.getBody().get("status"));

        assertEquals("Pedido não encontrado",
                response.getBody().get("mensagem"));
    }

    @Test
    void deveRetornarMensagemPadrao() {

        ResponseStatusException exception =
                new ResponseStatusException(
                        HttpStatus.BAD_REQUEST
                );

        ResponseEntity<Map<String, Object>> response =
                handler.tratarErro(exception);

        assertEquals(
                "Erro ao processar a solicitação",
                response.getBody().get("mensagem"));
    }

    @Test
    void devePreservarStatusDaExcecao() {

        ResponseStatusException exception =
                new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Conflito na operação"
                );

        ResponseEntity<Map<String, Object>> response =
                handler.tratarErro(exception);

        assertEquals(HttpStatus.CONFLICT,
                response.getStatusCode());

        assertEquals(409,
                response.getBody().get("status"));
    }
}