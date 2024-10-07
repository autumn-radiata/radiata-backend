package radiata.service.user.core.service.Mapper;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import radiata.common.domain.user.dto.response.GetUserInfoResponseDto;
import radiata.common.domain.user.dto.response.PointHistoryGetResponseDto;
import radiata.service.user.core.domain.model.entity.PointHistory;
import radiata.service.user.core.domain.model.entity.User;
import radiata.service.user.core.domain.model.vo.Address;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-07T10:01:51+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public GetUserInfoResponseDto userToGetUserInfoResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        String roadAddress = null;
        String detailAddress = null;
        String zipcode = null;
        String email = null;
        String nickname = null;
        String phone = null;

        roadAddress = userAddressRoadAddress( user );
        detailAddress = userAddressDetailAddress( user );
        zipcode = userAddressZipcode( user );
        email = user.getEmail();
        nickname = user.getNickname();
        phone = user.getPhone();

        String userId = null;

        GetUserInfoResponseDto getUserInfoResponseDto = new GetUserInfoResponseDto( userId, email, nickname, phone, roadAddress, detailAddress, zipcode );

        return getUserInfoResponseDto;
    }

    @Override
    public PointHistoryGetResponseDto userToPointHistoriesGetResponseDto(PointHistory history) {
        if ( history == null ) {
            return null;
        }

        int rewardPoint = 0;
        String pointType = null;
        LocalDateTime issueAt = null;

        rewardPoint = history.getRewardPoint();
        if ( history.getPointType() != null ) {
            pointType = history.getPointType().name();
        }
        issueAt = history.getIssueAt();

        String pointHistoryId = null;

        PointHistoryGetResponseDto pointHistoryGetResponseDto = new PointHistoryGetResponseDto( pointHistoryId, rewardPoint, pointType, issueAt );

        return pointHistoryGetResponseDto;
    }

    private String userAddressRoadAddress(User user) {
        if ( user == null ) {
            return null;
        }
        Address address = user.getAddress();
        if ( address == null ) {
            return null;
        }
        String roadAddress = address.getRoadAddress();
        if ( roadAddress == null ) {
            return null;
        }
        return roadAddress;
    }

    private String userAddressDetailAddress(User user) {
        if ( user == null ) {
            return null;
        }
        Address address = user.getAddress();
        if ( address == null ) {
            return null;
        }
        String detailAddress = address.getDetailAddress();
        if ( detailAddress == null ) {
            return null;
        }
        return detailAddress;
    }

    private String userAddressZipcode(User user) {
        if ( user == null ) {
            return null;
        }
        Address address = user.getAddress();
        if ( address == null ) {
            return null;
        }
        String zipcode = address.getZipcode();
        if ( zipcode == null ) {
            return null;
        }
        return zipcode;
    }
}
