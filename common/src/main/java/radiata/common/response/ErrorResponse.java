package radiata.common.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.NonNull;
import radiata.common.exception.BusinessException;

@Builder(access = PRIVATE)
public record ErrorResponse<T>(
        @NonNull
        String message,
        @NonNull
        String code,
        @JsonInclude(NON_NULL) T data
) implements CommonResponse {

    public static <T> ErrorResponse<T> error(String message, String code, T data) {

        return ErrorResponse.<T> builder()
            .message(message)
            .code(code)
            .data(data)
            .build();
    }

    public static <T> ErrorResponse<Object> error(String message, String code) {

        return ErrorResponse.builder()
            .message(message)
            .code(code)
            .build();
    }

    public static <T> ErrorResponse<Object> error(BusinessException e) {

        return ErrorResponse.builder()
            .message(e.getMessage())
            .code(e.getCode())
            .build();
    }

}
