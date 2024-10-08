package radiata.common.domain.user.dto.response;

import java.time.LocalDateTime;

public record PointHistoryGetResponseDto(
    String PointHistoryId,
    int rewardPoint,
    String pointType,
    LocalDateTime issueAt
) {
}
