package vn.com.atomi.loyalty.gift.enums;

import org.springframework.http.HttpStatus;
import vn.com.atomi.loyalty.base.exception.AbstractError;

/**
 * @author haidv
 * @version 1.0
 */
public enum ErrorCode implements AbstractError {
  APPROVING_RECORD_NOT_EXISTED(3000, "Không tìm thấy bản ghi chờ duyệt.", HttpStatus.NOT_FOUND),
  RECORD_NOT_EXISTED(3001, "Không tìm thấy bản ghi.", HttpStatus.NOT_FOUND),
  ;

  private final int code;

  private final String message;

  private final HttpStatus httpStatus;

  ErrorCode(int code, String message, HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
