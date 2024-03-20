package vn.com.atomi.loyalty.gift.dto.projection;

import vn.com.atomi.loyalty.gift.enums.Status;

public interface GiftProjection {
  Long getId();

  Long getCategoryId();

  String getName();

  String getCode();

  Status getStatus();

  String getType();

  String getService();

  String getDiscountType();

  String getDiscountValue();

  String getStartDate();

  String getEndDate();

  Long getDiscountMax();

  Long getGiftCount();

  String getDescription();

  Long getBillValueMin();

  Long getBillValueMax();

  String getSaleStartDate();

  String getSaleEndDate();

  String getRank();

  Boolean getNeedApproval();

  Long getLimitGiftPerUser();

  Long getPrice();

  Long getCustomerGroupId();
}
