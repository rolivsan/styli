package br.com.styli.domain.handler;

import br.com.styli.domain.dto.error.ErrorResponse;
import br.com.styli.domain.exception.BusinessException;
import br.com.styli.domain.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    // Esse handler captura todas as exceções, inclusive as genéricas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        // Caso não seja uma BusinessException, definimos um erro genérico
        ErrorResponse errorResponse;

        // Ignora o erro relacionado ao favicon
        if (ex instanceof NoHandlerFoundException && ex.getMessage().contains("favicon.ico")) {
            // Não loga nem trata o erro do favicon
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

       if (ex instanceof BusinessException) {
           // Caso seja uma BusinessException, pegamos o ErrorCode dela
            BusinessException businessException = (BusinessException) ex;
            errorResponse = new ErrorResponse(
                    businessException.getErrorCode(),
                    businessException.getErrorCode().getMessage()
            );
        } else {
           log.error("error: ", ex);
            // Para outras exceções genéricas, retornamos uma mensagem genérica
            errorResponse = new ErrorResponse(
                    ErrorCode.GENERIC, // Você pode escolher um erro genérico para outras exceções
                    "An unexpected error occurred: " + ex.getMessage()
            );
        }

        // Retorna uma resposta com um status HTTP adequado
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

