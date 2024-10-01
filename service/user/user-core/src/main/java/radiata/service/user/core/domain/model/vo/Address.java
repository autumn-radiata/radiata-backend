package radiata.service.user.core.domain.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Address {

  @Column(nullable = false)
  private String roadAddress;

  @Column(nullable = false)
  private String detailAddress;

  @Column(nullable = false)
  private String zipcode;

  @Builder
  private Address(String roadAddress, String detailAddress, String zipcode) {
    this.roadAddress = roadAddress;
    this.detailAddress = detailAddress;
    this.zipcode = zipcode;
  }

}