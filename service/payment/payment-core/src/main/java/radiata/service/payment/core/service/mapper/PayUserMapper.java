package radiata.service.payment.core.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import radiata.common.domain.payment.dto.response.PayUserResponseDto;
import radiata.service.payment.core.domain.model.entity.PayUser;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PayUserMapper {

    @Mapping(target = "money", source = "balance.amount")
    @Mapping(target = "payUserId", source = "id")
    PayUserResponseDto toPayUserResponseDto(PayUser payUser);
}
