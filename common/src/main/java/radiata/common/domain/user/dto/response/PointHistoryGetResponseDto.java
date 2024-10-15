package radiata.common.domain.user.dto.response;

import java.time.LocalDateTime;

public record PointHistoryGetResponseDto(
    String pointHistoryId,
    int rewardPoint,
    String pointType,
    LocalDateTime issueAt
) {

}
