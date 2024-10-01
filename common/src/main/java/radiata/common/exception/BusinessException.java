package radiata.common.exception;

import java.io.IOException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import radiata.common.message.ExceptionMessage;

@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;

    public BusinessException(HttpStatus httpStatus, String message, String code) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
    }

    public BusinessException(ExceptionMessage exceptionMessage)  {
        this.httpStatus = exceptionMessage.getHttpStatus();
        this.message = exceptionMessage.getMessage();
        this.code = exceptionMessage.getCode();
    }
}
