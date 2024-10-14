package radiata.service.brand.core.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenderType {
    ALL("전체"),
    MAN("남성"),
    WOMAN("여성");

    private final String title;
}
