package radiata.common.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static radiata.common.message.ExceptionMessage.SYSTEM_ERROR;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import radiata.common.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {

        return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.error(e));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ErrorResponse.error(SYSTEM_ERROR.getMessage(),
            SYSTEM_ERROR.getCode()));
    }
}
