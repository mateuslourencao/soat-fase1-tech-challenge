package com.oficina.common.infrastructure.rest;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveTratarIllegalArgumentExceptionComoBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Dados inválidos");
        ResponseEntity<Object> response = handler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(400, body.get("status"));
        assertEquals("Dados inválidos", body.get("message"));
    }

    @Test
    void deveTratarIllegalStateExceptionComoUnprocessableEntity() {
        IllegalStateException ex = new IllegalStateException("Estado inválido");
        ResponseEntity<Object> response = handler.handleIllegalStateException(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(422, body.get("status"));
        assertEquals("Estado inválido", body.get("message"));
    }

    @Test
    void deveTratarRuntimeExceptionComoNotFoundQuandoContemMensagemEspecifica() {
        RuntimeException ex = new RuntimeException("Cliente não encontrado");
        ResponseEntity<Object> response = handler.handleRuntimeException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(404, body.get("status"));
        assertEquals("Cliente não encontrado", body.get("message"));
    }

    @Test
    void deveTratarRuntimeExceptionComoInternalServerErrorParaOutrasMensagens() {
        RuntimeException ex = new RuntimeException("Erro genérico");
        ResponseEntity<Object> response = handler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(500, body.get("status"));
        assertEquals("Erro interno no servidor", body.get("message"));
    }

    @Test
    void deveTratarRuntimeExceptionComMensagemNulaComoInternalServerError() {
        RuntimeException ex = new RuntimeException((String) null);
        ResponseEntity<Object> response = handler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(500, body.get("status"));
    }
}
