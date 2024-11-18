package vn.com.atomi.loyalty.gift.service.impl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.gift.dto.input.GiftInput;
import vn.com.atomi.loyalty.gift.dto.output.GiftApplyAddressOutput;
import vn.com.atomi.loyalty.gift.dto.output.GiftOutput;
import vn.com.atomi.loyalty.gift.dto.output.InternalGiftOutput;
import vn.com.atomi.loyalty.gift.dto.output.PreviewGiftOutput;
import vn.com.atomi.loyalty.gift.entity.CategoryApproval;
import vn.com.atomi.loyalty.gift.entity.GiftApplyAddress;
import vn.com.atomi.loyalty.gift.enums.ErrorCode;
import vn.com.atomi.loyalty.gift.enums.Status;
import vn.com.atomi.loyalty.gift.repository.CategoryRepository;
import vn.com.atomi.loyalty.gift.repository.GiftApplyAddressRepository;
import vn.com.atomi.loyalty.gift.repository.GiftRepository;
import vn.com.atomi.loyalty.gift.repository.redis.GiftCacheRepository;
import vn.com.atomi.loyalty.gift.service.GiftService;
import vn.com.atomi.loyalty.gift.utils.Utils;

@Service
@RequiredArgsConstructor
public class GiftServiceImpl extends BaseService implements GiftService {
  private final CategoryRepository categoryRepository;
  private final GiftRepository giftRepository;
  private final GiftCacheRepository giftCacheRepository;
  private final GiftApplyAddressRepository giftApplyAddressRepository;

  @Override
  public void create(GiftInput input) {
    var startDate = Utils.convertToLocalDate(input.getStartDate());
    var endDate = Utils.convertToLocalDate(input.getEndDate());

    // check category
    categoryRepository
        .findByDeletedFalseAndIdAndStatus(input.getCategoryId(), Status.ACTIVE)
        .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_EXISTED));

    // check user group
    // todo: core API

    // tạo code
    var id = giftRepository.getSequence();
    var code = Utils.generateCode(id, CategoryApproval.class.getSimpleName());

    // lưu bản ghi
    var gift = modelMapper.convertToGift(input, startDate, endDate, id, code);
    giftRepository.save(gift);

    // clear cache
    giftCacheRepository.clear();
  }

  @Override
  public ResponsePage<GiftOutput> gets(Status status, String name, String code, Pageable pageable) {
    var page = giftRepository.findByCondition(code, name, status, pageable);
    var giftOutputs = modelMapper.convertToGiftOutputs(page.getContent());
    return new ResponsePage<>(page, giftOutputs);
  }  @Override
  public ResponsePage<InternalGiftOutput> getsI(Status status, String name, String code, Pageable pageable) {
    var page = giftRepository.findByCondition(code, name, status, pageable);
    var giftOutputs = modelMapper.convertToInternalGiftOutputs(page.getContent());
    return new ResponsePage<>(page, giftOutputs);
  }

  @Override
  public GiftOutput get(Long id) {
    var gift =
            giftRepository
                    .findByDeletedFalseAndId(id)
                    .orElseThrow(() -> new BaseException(ErrorCode.GIFT_NOT_EXISTED));
    return super.modelMapper.convertToGiftOutput(gift);
  }

  @Override
  public InternalGiftOutput getI(Long id) {
    var gift =
        giftRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.GIFT_NOT_EXISTED));
    List<GiftApplyAddress> applyAddress = giftApplyAddressRepository.findAllByGiftId(gift.getId());
    InternalGiftOutput giftOutput = super.modelMapper.convertToInternalGiftOutput(gift);
    List<GiftApplyAddressOutput> applyAddressOutput = applyAddress.stream()
            .map(applyAddr -> modelMapper.convertToOutput(applyAddr)) // convertToOutput() transforms GiftApplyAddress to GiftApplyAddressOutput
            .collect(Collectors.toList());

    giftOutput.setApplyAddress(applyAddressOutput);
    return giftOutput;
  }

  @Override
  public void update(Long id, GiftInput input) {
    // lấy record hiện tại
    var record =
        giftRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.GIFT_NOT_EXISTED));

    // check category
    categoryRepository
        .findByDeletedFalseAndIdAndStatus(input.getCategoryId(), Status.ACTIVE)
        .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_EXISTED));

    // check user group
    // todo: core API

    // mapping new values
    var newCampaign = super.modelMapper.mappingToGift(record, input);
    // lưu
    giftRepository.save(newCampaign);

    // clear cache
    giftCacheRepository.clear();
  }

  @Override
  public ResponsePage<InternalGiftOutput> getInternalGift(Long categoryId, Pageable pageable) {
    // load cache
//        var cache = giftCacheRepository.gets(categoryId);
//        if (cache.isPresent()) return cache.get();
//
//        // load DB
//        var page =
//            categoryId == null
//                ? giftRepository.findAllBy(pageable)
//                : giftRepository.findAllByCategoryId(categoryId, pageable);
//
//        var outputs = modelMapper.convertToInternalGiftOutputs(page.getContent());
//
//        var outputPage = new ResponsePage<>(page, outputs);
//
//        // save cache
//        if (!outputs.isEmpty()) giftCacheRepository.put(categoryId, outputPage);
//
//        return outputPage;
    return new ResponsePage<>(
        1,
        10,
        2L,
        1,
        Arrays.asList(
            InternalGiftOutput.builder()
                .id(2L)
                .name("(TEST) Phòng khách hạng thương gia - Chuyến bay Quốc nội")
                .totalRemaining(9999L)
                .giftCount(10000L)
                .description(
                    "Quy định và điều khoản:\\n- E-code tương ứng với 01 lượt/khách/3 giờ sử dụng dịch vụ Phòng khách hạng thương gia.\\n- Áp dụng cho các chuyến bay Quốc nội tại các sân bay: Nội Bài, Tân Sơn Nhất, Đà Nẵng, Cam Ranh, Phú Quốc.\\n- E-code có thời hạn: 3 tháng kể từ ngày nhận code")
                .applyAddress(
                    Arrays.asList(
                        GiftApplyAddressOutput.builder()
                            .id(1L)
                            .giftId(4L)
                            .address(
                                "Sảnh A, tầng 3, nhà ga T1, Khu hành khách Quốc nội, Sân bay quốc tế Nội Bài")
                            .name("Sông Hồng Premium Quốc nội")
                            .build(),
                        GiftApplyAddressOutput.builder()
                            .id(2L)
                            .giftId(4L)
                            .address(
                                "Sảnh A, tầng 3, nhà ga T1, Khu hành khách Quốc nội, Sân bay quốc tế Nội Bài")
                            .name("Bông Sen Quốc Nội Nội Bài")
                            .build()))
                .guide(
                    "Hướng dẫn sử dụng voucher\\n-\\tKhách hàng trình diện thẻ MB Private và e-voucher tại quầy lễ tân phòng khách hạng thương gia để sử dụng phòng chờ.\\n-\\tDanh sách phòng chờ quốc nội áp dụng:\\n+ Bông Sen Quốc Nội Nội Bài - Tầng 3 khu D, khu vực hành khách nội địa đi Nhà ga T1 - Sân bay quốc tế Nội Bài (Nếu đi Vietjet - áp dụng tại phòng Bussiness lounge tầng 2 sảnh E)\\n+ Sông Hồng Premium Quốc nội - Tầng 3 khu D, khu vực hành khách nội địa đi Nhà ga T1 - Sân bay quốc tế Nội Bài.\\n+ Phòng khách Quốc nội C2 – Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Đà Nẵng\\n+ Phòng khách The Champ Lounge – Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Cam Ranh\\n+ Phòng khách Lotus Lounge - Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Cam Ranh (phục vụ duy nhất khách hàng của Vietnam Airlines).\\n+ Phòng khách Quốc nội CIP Lounge – Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Phú Quốc")
                .termsOfUse(
                    "\\tÁp dụng cho khách hàng MB Private.\\n-\\tVoucher chỉ có thể sử dụng 01 lần, không quy đổi thành tiền mặt\\n-\\t01 Voucher tương ứng với 01 lượt/khách/3 giờ sử dụng dịch vụ Phòng khách hạng thương gia.")
                .price(1000L)
                .limitGiftPerUser(1L)
                .categoryId(1L)
                .customerGroupIds(Arrays.asList(1L, 2L))
//                .images(
//                    Arrays.asList(
//                        "https://lounge.mpoint.vn/images/aff216ed-b9e3-445e-a59f-5611f2687a59.jpg",
//                        "https://lounge.mpoint.vn/images/3b1ab02c-9a34-4e5c-9f89-65727349d581.jpg",
//                        "https://lounge.mpoint.vn/images/c397190c-7e55-4772-9b67-fa62d5760cd1.jpg"))
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(30))
                .thumbnail(
                    "https://lounge.mpoint.vn/images/eda5fcca-9884-4c07-88db-aba172277011.jpg")
                .support("Liên hệ tổng đài ....")
                .build(),
            InternalGiftOutput.builder()
                .id(4L)
                .name("(TEST) Phòng khách hạng thương gia - Chuyến bay Quốc nội")
                .totalRemaining(1000L)
                .giftCount(1000L)
                .description(
                    "Quy định và điều khoản:\\n- E-code tương ứng với 01 lượt/khách/3 giờ sử dụng dịch vụ Phòng khách hạng thương gia.\\n- Áp dụng cho các chuyến bay Quốc nội tại các sân bay: Nội Bài, Tân Sơn Nhất, Đà Nẵng, Cam Ranh, Phú Quốc.\\n- E-code có thời hạn: 3 tháng kể từ ngày nhận code")
                .applyAddress(
                    Arrays.asList(
                        GiftApplyAddressOutput.builder()
                            .id(1L)
                            .giftId(4L)
                            .address(
                                "Sảnh A, tầng 3, nhà ga T1, Khu hành khách Quốc nội, Sân bay quốc tế Nội Bài")
                            .name("Sông Hồng Premium Quốc nội")
                            .build(),
                        GiftApplyAddressOutput.builder()
                            .id(2L)
                            .giftId(4L)
                            .address(
                                "Sảnh A, tầng 3, nhà ga T1, Khu hành khách Quốc nội, Sân bay quốc tế Nội Bài")
                            .name("Bông Sen Quốc Nội Nội Bài")
                            .build()))
                .guide(
                    "Hướng dẫn sử dụng voucher\\n-\\tKhách hàng trình diện thẻ MB Private và e-voucher tại quầy lễ tân phòng khách hạng thương gia để sử dụng phòng chờ.\\n-\\tDanh sách phòng chờ quốc nội áp dụng:\\n+ Bông Sen Quốc Nội Nội Bài - Tầng 3 khu D, khu vực hành khách nội địa đi Nhà ga T1 - Sân bay quốc tế Nội Bài (Nếu đi Vietjet - áp dụng tại phòng Bussiness lounge tầng 2 sảnh E)\\n+ Sông Hồng Premium Quốc nội - Tầng 3 khu D, khu vực hành khách nội địa đi Nhà ga T1 - Sân bay quốc tế Nội Bài.\\n+ Phòng khách Quốc nội C2 – Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Đà Nẵng\\n+ Phòng khách The Champ Lounge – Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Cam Ranh\\n+ Phòng khách Lotus Lounge - Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Cam Ranh (phục vụ duy nhất khách hàng của Vietnam Airlines).\\n+ Phòng khách Quốc nội CIP Lounge – Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Phú Quốc")
                .termsOfUse(
                    "\\tÁp dụng cho khách hàng MB Private.\\n-\\tVoucher chỉ có thể sử dụng 01 lần, không quy đổi thành tiền mặt\\n-\\t01 Voucher tương ứng với 01 lượt/khách/3 giờ sử dụng dịch vụ Phòng khách hạng thương gia.")
                .price(1000L)
                .limitGiftPerUser(1L)
                .categoryId(1L)
                .customerGroupIds(Arrays.asList(1L, 2L))
//                .images(
//                    Arrays.asList(
//                        "https://lounge.mpoint.vn/images/aff216ed-b9e3-445e-a59f-5611f2687a59.jpg",
//                        "https://lounge.mpoint.vn/images/3b1ab02c-9a34-4e5c-9f89-65727349d581.jpg",
//                        "https://lounge.mpoint.vn/images/c397190c-7e55-4772-9b67-fa62d5760cd1.jpg"))
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(30))
                .thumbnail(
                    "https://lounge.mpoint.vn/images/eda5fcca-9884-4c07-88db-aba172277011.jpg")
                .support("Liên hệ tổng đài ....")
                .build(),
            InternalGiftOutput.builder()
                .id(5L)
                .name("(TEST KHÔNG THUỘC GROUP) Phòng khách hạng thương gia - Chuyến bay Quốc nội")
                .totalRemaining(1000L)
                .giftCount(1000L)
                .applyAddress(
                    Arrays.asList(
                        GiftApplyAddressOutput.builder()
                            .id(1L)
                            .giftId(4L)
                            .address(
                                "Sảnh A, tầng 3, nhà ga T1, Khu hành khách Quốc nội, Sân bay quốc tế Nội Bài")
                            .name("Sông Hồng Premium Quốc nội")
                            .build(),
                        GiftApplyAddressOutput.builder()
                            .id(2L)
                            .giftId(4L)
                            .address(
                                "Sảnh A, tầng 3, nhà ga T1, Khu hành khách Quốc nội, Sân bay quốc tế Nội Bài")
                            .name("Bông Sen Quốc Nội Nội Bài")
                            .build()))
                .description(
                    "Quy định và điều khoản:\\n- E-code tương ứng với 01 lượt/khách/3 giờ sử dụng dịch vụ Phòng khách hạng thương gia.\\n- Áp dụng cho các chuyến bay Quốc nội tại các sân bay: Nội Bài, Tân Sơn Nhất, Đà Nẵng, Cam Ranh, Phú Quốc.\\n- E-code có thời hạn: 3 tháng kể từ ngày nhận code")
                .guide(
                    "Hướng dẫn sử dụng voucher\\n-\\tKhách hàng trình diện thẻ MB Private và e-voucher tại quầy lễ tân phòng khách hạng thương gia để sử dụng phòng chờ.\\n-\\tDanh sách phòng chờ quốc nội áp dụng:\\n+ Bông Sen Quốc Nội Nội Bài - Tầng 3 khu D, khu vực hành khách nội địa đi Nhà ga T1 - Sân bay quốc tế Nội Bài (Nếu đi Vietjet - áp dụng tại phòng Bussiness lounge tầng 2 sảnh E)\\n+ Sông Hồng Premium Quốc nội - Tầng 3 khu D, khu vực hành khách nội địa đi Nhà ga T1 - Sân bay quốc tế Nội Bài.\\n+ Phòng khách Quốc nội C2 – Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Đà Nẵng\\n+ Phòng khách The Champ Lounge – Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Cam Ranh\\n+ Phòng khách Lotus Lounge - Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Cam Ranh (phục vụ duy nhất khách hàng của Vietnam Airlines).\\n+ Phòng khách Quốc nội CIP Lounge – Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Phú Quốc")
                .termsOfUse(
                    "\\tÁp dụng cho khách hàng MB Private.\\n-\\tVoucher chỉ có thể sử dụng 01 lần, không quy đổi thành tiền mặt\\n-\\t01 Voucher tương ứng với 01 lượt/khách/3 giờ sử dụng dịch vụ Phòng khách hạng thương gia.")
                .price(1000L)
                .limitGiftPerUser(1L)
                .categoryId(1L)
                .customerGroupIds(Arrays.asList(3L))
//                .images(
//                    Arrays.asList(
//                        "https://lounge.mpoint.vn/images/aff216ed-b9e3-445e-a59f-5611f2687a59.jpg",
//                        "https://lounge.mpoint.vn/images/3b1ab02c-9a34-4e5c-9f89-65727349d581.jpg",
//                        "https://lounge.mpoint.vn/images/c397190c-7e55-4772-9b67-fa62d5760cd1.jpg"))
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(30))
                .thumbnail(
                    "https://lounge.mpoint.vn/images/eda5fcca-9884-4c07-88db-aba172277011.jpg")
                .support("Liên hệ tổng đài ....")
                .build()));
  }

  @Override
  public List<PreviewGiftOutput> getPartnerGift(String partnerCode) {
    return null;
  }

  @Override
  public InternalGiftOutput getInternalGift(Long id) {
    return InternalGiftOutput.builder()
        .id(5L)
        .name("(TEST KHÔNG THUỘC GROUP) Phòng khách hạng thương gia - Chuyến bay Quốc nội")
        .totalRemaining(1000L)
        .giftCount(1000L)
        .description(
            "Quy định và điều khoản:\\n- E-code tương ứng với 01 lượt/khách/3 giờ sử dụng dịch vụ Phòng khách hạng thương gia.\\n- Áp dụng cho các chuyến bay Quốc nội tại các sân bay: Nội Bài, Tân Sơn Nhất, Đà Nẵng, Cam Ranh, Phú Quốc.\\n- E-code có thời hạn: 3 tháng kể từ ngày nhận code")
        .applyAddress(
            Arrays.asList(
                GiftApplyAddressOutput.builder()
                    .id(1L)
                    .giftId(4L)
                    .address(
                        "Sảnh A, tầng 3, nhà ga T1, Khu hành khách Quốc nội, Sân bay quốc tế Nội Bài")
                    .name("Sông Hồng Premium Quốc nội")
                    .build(),
                GiftApplyAddressOutput.builder()
                    .id(2L)
                    .giftId(4L)
                    .address(
                        "Sảnh A, tầng 3, nhà ga T1, Khu hành khách Quốc nội, Sân bay quốc tế Nội Bài")
                    .name("Bông Sen Quốc Nội Nội Bài")
                    .build()))
        .guide(
            "Hướng dẫn sử dụng voucher\\n-\\tKhách hàng trình diện thẻ MB Private và e-voucher tại quầy lễ tân phòng khách hạng thương gia để sử dụng phòng chờ.\\n-\\tDanh sách phòng chờ quốc nội áp dụng:\\n+ Bông Sen Quốc Nội Nội Bài - Tầng 3 khu D, khu vực hành khách nội địa đi Nhà ga T1 - Sân bay quốc tế Nội Bài (Nếu đi Vietjet - áp dụng tại phòng Bussiness lounge tầng 2 sảnh E)\\n+ Sông Hồng Premium Quốc nội - Tầng 3 khu D, khu vực hành khách nội địa đi Nhà ga T1 - Sân bay quốc tế Nội Bài.\\n+ Phòng khách Quốc nội C2 – Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Đà Nẵng\\n+ Phòng khách The Champ Lounge – Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Cam Ranh\\n+ Phòng khách Lotus Lounge - Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Cam Ranh (phục vụ duy nhất khách hàng của Vietnam Airlines).\\n+ Phòng khách Quốc nội CIP Lounge – Khu vực hành khách nội địa, Ga đi Quốc nội, Sân bay Quốc tế Phú Quốc")
        .termsOfUse(
            "\\tÁp dụng cho khách hàng MB Private.\\n-\\tVoucher chỉ có thể sử dụng 01 lần, không quy đổi thành tiền mặt\\n-\\t01 Voucher tương ứng với 01 lượt/khách/3 giờ sử dụng dịch vụ Phòng khách hạng thương gia.")
        .price(1000L)
        .limitGiftPerUser(1L)
        .categoryId(1L)
        .customerGroupIds(Arrays.asList(3L))
//        .images(
//            Arrays.asList(
//                "https://lounge.mpoint.vn/images/aff216ed-b9e3-445e-a59f-5611f2687a59.jpg",
//                "https://lounge.mpoint.vn/images/3b1ab02c-9a34-4e5c-9f89-65727349d581.jpg",
//                "https://lounge.mpoint.vn/images/c397190c-7e55-4772-9b67-fa62d5760cd1.jpg"))
        .startDate(LocalDate.now().minusDays(10))
        .endDate(LocalDate.now().plusDays(30))
        .thumbnail("https://lounge.mpoint.vn/images/eda5fcca-9884-4c07-88db-aba172277011.jpg")
        .support("Liên hệ tổng đài ....")
        .voucherCodes(Arrays.asList("VC0001", "VC0002"))
        .build();
  }
}
