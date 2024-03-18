package vn.com.atomi.loyalty.gift.feign;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.gift.dto.input.TransactionInput;
import vn.com.atomi.loyalty.gift.dto.output.CustomerBalanceOutput;
import vn.com.atomi.loyalty.gift.feign.fallback.LoyaltyCoreClientFallbackFactory;

/**
 * @author haidv
 * @version 1.0
 */
@FeignClient(
    name = "loyalty-core-service",
    url = "${custom.properties.loyalty-core-service-url}",
    fallbackFactory = LoyaltyCoreClientFallbackFactory.class)
public interface LoyaltyCoreClient {

  @Operation(summary = "Api (nội bộ) tiêu điểm")
  @PostMapping("/internal/transactions-minus")
  ResponseData<Void> executeTransactionMinus(
      @RequestHeader(RequestConstant.REQUEST_ID) String requestId,
      @RequestBody TransactionInput transactionInput);

  @Operation(summary = "Api (nội bộ) lấy thông tin số dư hiện tại của khách hàng")
  @GetMapping("/internal/customers/balances")
  ResponseData<CustomerBalanceOutput> getCurrentBalance(
      @RequestHeader(RequestConstant.REQUEST_ID) String requestId,
      @Parameter(description = "Mã định danh của khách hàng trên bank")
          @RequestParam(required = false)
          String cifBank,
      @Parameter(description = "Mã định danh của khách hàng trên ví")
          @RequestParam(required = false)
          String cifWallet);
}
