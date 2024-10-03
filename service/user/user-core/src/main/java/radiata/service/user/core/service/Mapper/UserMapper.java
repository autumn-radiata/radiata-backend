package radiata.service.user.core.service.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import radiata.common.domain.user.dto.response.GetUserInfoResponseDto;
import radiata.service.user.core.domain.model.entity.User;

@Mapper(componentModel = SPRING)
public interface UserMapper {

    @Mapping(source = "address.roadAddress", target = "roadAddress")
    @Mapping(source="address.detailAddress",target="detailAddress")
    @Mapping(source="address.zipcode",target="zipcode")
    GetUserInfoResponseDto userToGetUserInfoResponseDto(User user);

}
