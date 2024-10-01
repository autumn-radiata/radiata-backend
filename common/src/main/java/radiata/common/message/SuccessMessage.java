package radiata.common.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum SuccessMessage {

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
