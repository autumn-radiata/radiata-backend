package radiata.service.user.core.service.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import radiata.common.domain.user.dto.response.PointHistoryGetResponseDto;
import radiata.common.domain.user.dto.response.UserGetInfoResponseDto;
import radiata.service.user.core.domain.model.entity.PointHistory;
import radiata.service.user.core.domain.model.entity.User;

@Mapper(componentModel = SPRING)
public interface UserMapper {

    @Mapping(source = "address.roadAddress", target = "roadAddress")
    @Mapping(source = "address.detailAddress", target = "detailAddress")
    @Mapping(source = "address.zipcode", target = "zipcode")
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "user.totalPoint.totalPoint", target = "totalPoint")
    UserGetInfoResponseDto userToUserGetInfoResponseDto(User user);

    @Mapping(source = "history.id", target = "pointHistoryId")
    @Mapping(source = "history.pointType", target = "pointType")
    PointHistoryGetResponseDto userToPointHistoriesGetResponseDto(PointHistory history);
}
