package cl.duoc.sanosysalvos.mascotas.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFoundDeberiaRetornar404ConJson() {
        ResponseEntity<Map<String, String>> response = handler.handleNotFound(new RuntimeException("No encontrado"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No encontrado", response.getBody().get("error"));
    }

    @Test
    void handleBadRequestDeberiaRetornar400ConJson() {
        ResponseEntity<Map<String, String>> response = handler.handleBadRequest(new IllegalArgumentException("Dato inválido"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dato inválido", response.getBody().get("error"));
    }
}
