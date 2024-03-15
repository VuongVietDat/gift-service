package vn.com.atomi.loyalty.gift.dto.projection;

import vn.com.atomi.loyalty.gift.enums.ApprovalStatus;
import vn.com.atomi.loyalty.gift.enums.ApprovalType;
import vn.com.atomi.loyalty.gift.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CategoryApprovalProjection {
  Long getId();

  String getCode();

  String getName();

  String getDescription();


  Status getStatus();

  LocalDate getStartDate();

  LocalDate getEndDate();

  String getCreator();

  LocalDateTime getCreationDate();

  ApprovalStatus getApprovalStatus();

  ApprovalType getApprovalType();

  String getApprover();
}
