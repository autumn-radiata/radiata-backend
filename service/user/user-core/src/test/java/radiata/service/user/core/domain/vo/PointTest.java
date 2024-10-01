package radiata.service.user.core.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.user.core.domain.model.vo.Point;

class PointTest {

    private Point point;

    @BeforeEach
    void setUp() {
        point = Point.builder()
            .point(1000)
            .build();
    }

    @Test
    @DisplayName("포인트 지급")
    void testAddPoint() {
        Point updatedPoint = point.addPoint(500);

        assertThat(updatedPoint.getPoint()).isEqualTo(1500);
    }

    @Test
    @DisplayName("포인트 차감")
    void testSubPoint() {
        Point updatedPoint = point.subPoint(300);

        assertThat(updatedPoint.getPoint()).isEqualTo(700);
    }

    @Test
    @DisplayName("포인트 부족")
    void testSubPoint_Issue_Lack() {
        assertThatThrownBy(() -> point.subPoint(1500))
            .isInstanceOf(BusinessException.class)
            .hasMessage(ExceptionMessage.POINT_ISSUE_LACK.getMessage());
    }

}