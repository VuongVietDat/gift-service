package vn.com.atomi.loyalty.gift.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.exception.CommonErrorCode;
import vn.com.atomi.loyalty.gift.dto.input.TransactionInput;
import vn.com.atomi.loyalty.gift.dto.output.CustomerBalanceOutput;
import vn.com.atomi.loyalty.gift.feign.LoyaltyCoreClient;

/**
 * @author haidv
 * @version 1.0
 */
@Component
@Slf4j
public class LoyaltyCoreClientFallbackFactory implements FallbackFactory<LoyaltyCoreClient> {
  @Override
  public LoyaltyCoreClient create(Throwable cause) {
    log.error("An exception occurred when calling the LoyaltyCommonClient", cause);

    return new LoyaltyCoreClient() {
      @Override
      public ResponseData<Void> executeTransactionMinus(
          String requestId, TransactionInput transactionInput) {
        throw new BaseException(CommonErrorCode.EXECUTE_THIRTY_SERVICE_ERROR, cause);
      }

      @Override
      public ResponseData<CustomerBalanceOutput> getCurrentBalance(
          String requestId, String cifBank, String cifWallet) {
        throw new BaseException(CommonErrorCode.EXECUTE_THIRTY_SERVICE_ERROR, cause);
      }
    };
  }
}
