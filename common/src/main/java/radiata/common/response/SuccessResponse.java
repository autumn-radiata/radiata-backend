package radiata.common.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.NonNull;

@Builder(access = PRIVATE)
public record SuccessResponse<T> (
        @NonNull
        String message,
        @NonNull
        String code,
        @JsonInclude(NON_NULL) T data
) implements CommonResponse {

    public static <T> SuccessResponse<T> success(String message, T data) {

        return SuccessResponse.<T>builder().message(message).code("0").data(data).build();
    }

    public static <T> SuccessResponse<Object> success(String message) {

        return SuccessResponse.builder().message(message).code("0").build();
    }

}
