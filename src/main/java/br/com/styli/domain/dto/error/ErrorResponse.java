package br.com.styli.domain.dto.error;

import br.com.styli.domain.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private ErrorCode errorCode;
    private String message;

}
