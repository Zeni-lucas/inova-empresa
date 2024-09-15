package com.inova_evento.app.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiExceptionHandlerTest {

    private ApiExceptionHandler apiExceptionHandler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        apiExceptionHandler = new ApiExceptionHandler();
        request = mock(HttpServletRequest.class);
    }

    @Test
    void testMethodArgumentNotValidException() {
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorMessage> response = apiExceptionHandler.MethodArgumentNotValidException(ex, request, bindingResult);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("campo(s) inválido(s)", response.getBody().getMessage());
    }

    @Test
    void testUniqueEmailException() {
        UniqueEmailException ex = new UniqueEmailException("Email já está em uso");

        ResponseEntity<ErrorMessage> response = apiExceptionHandler.UniqueEmailException(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email já está em uso", response.getBody().getMessage());
    }

    @Test
    void testEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("Entidade não encontrada");

        ResponseEntity<ErrorMessage> response = apiExceptionHandler.EntityNotFoundException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entidade não encontrada", response.getBody().getMessage());
    }

    @Test
    void testUserUnauthorizedException() {
        AccessDeniedException ex = new AccessDeniedException("Acesso negado");

        ResponseEntity<ErrorMessage> response = apiExceptionHandler.UserUnauthorizedException(ex, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Acesso negado", response.getBody().getMessage());
    }

    @Test
    void testHttpMessageNotReadableException() {
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);

        ResponseEntity<ErrorMessage> response = apiExceptionHandler.HttpMessageNotReadableException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("O formato da requisição está incorreto", response.getBody().getMessage());
    }

    @Test
    void testBusinnesException() {
        BusinnesException ex = new BusinnesException("Erro de negócio");

        ResponseEntity<ErrorMessage> response = apiExceptionHandler.BusinnesException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro de negócio", response.getBody().getMessage());
    }
}