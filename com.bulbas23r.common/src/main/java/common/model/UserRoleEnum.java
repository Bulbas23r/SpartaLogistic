package common.model;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
  MASTER(Authority.MASTER),
  COMPANY(Authority.COMPANY),
  HUB_MANAGER(Authority.HUB_MANAGER),
  HUB_TO_HUB_DELIVERY(Authority.HUB_TO_HUB_DELIVERY),
  TO_COMPANY_DELIVERY(Authority.TO_COMPANY_DELIVERY);


  private final String authority;

  UserRoleEnum(String authority) {
    this.authority = authority;
  }

  public static class Authority {
    public static final String COMPANY = "COMPANY";
    public static final String MASTER = "MASTER";
    public static final String HUB_MANAGER = "HUB_MANAGER";
    public static final String HUB_TO_HUB_DELIVERY = "HUB_TO_HUB_DELIVERY";
    public static final String TO_COMPANY_DELIVERY = "TO_COMPANY_DELIVERY";
  }
}
