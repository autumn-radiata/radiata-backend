package radiata.service.user.core.domain.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

  CUSTOMER(Authority.CUSTOMER),
  BRAND_OWNER(Authority.BRAND_OWNER),
  ADMIN(Authority.ADMIN);


  private final String authority;
  public static class Authority {

    private static String CUSTOMER = "ROLE_CUSTOMER";
    private static String BRAND_OWNER = "ROLE_BRAND_OWNER";
    private static String ADMIN = "ROLE_ADMIN";
  }
}
