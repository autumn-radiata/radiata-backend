package radiata.service.user.core.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point implements Serializable {

    @Column
    private Integer point;

    @Builder
    public Point(int point) {
        this.point = point;
    }

    /**
     * 적립금 지급
     */
    public Point addPoint(int addPoint) {
        return new Point(this.point + addPoint);
    }

    /**
     * 적립금 사용
     */
    public Point subPoint(int subPoint) {
        hasAvailablePoint(subPoint);
        return new Point(this.point - subPoint);
    }

    /**
     * 적립금 사용 가능 여부
     */
    public void hasAvailablePoint(int requirePoint){
        if (this.point < requirePoint) {
            throw new BusinessException(ExceptionMessage.POINT_ISSUE_LACK);
        }
    }



}
