package com.lv.api.controller;

import com.lv.api.constant.Constants;
import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.order.OrderAdminDto;
import com.lv.api.dto.order.OrderDto;
import com.lv.api.dto.order.OrderStatusDto;
import com.lv.api.dto.productconfig.ProductConfigDto;
import com.lv.api.dto.productvariant.ProductVariantDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.order.ChangeOrderStatusForm;
import com.lv.api.form.order.CreateOrderForm;
import com.lv.api.form.order.CreateOrderItemForm.OrderProductConfig;
import com.lv.api.intercepter.MyIntercepter;
import com.lv.api.mapper.OrderMapper;
import com.lv.api.service.AuditService;
import com.lv.api.storage.criteria.OrderCriteria;
import com.lv.api.storage.model.*;
import com.lv.api.storage.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class OrderController extends ABasicController {

    private final OrderRepository orderRepository;
    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final AuditService auditService;
    private final MyIntercepter myIntercepter;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<OrderAdminDto>> list(@Valid OrderCriteria orderCriteria, BindingResult bindingResult, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(orderCriteria.getSpecification(), pageable);
        List<OrderAdminDto> orderAdminDtoList = orderMapper.fromOrderEntityListToAdminDtoListPage(orderPage.getContent());
        return new ApiMessageDto<>(
                new ResponseListObj<>(
                        orderAdminDtoList,
                        orderPage
                ),
                "Get list order successfully"
        );
    }

    @GetMapping(value = "/list-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<OrderDto>> listUser(@Valid OrderCriteria orderCriteria, BindingResult bindingResult, Pageable pageable) {
        orderCriteria.setCustomerId(getCurrentUserId());
        Page<Order> orderPage = orderRepository.findAll(orderCriteria.getSpecification(), pageable);
        List<OrderDto> orderDtoList = orderMapper.fromOrderEntityListToOrderDtoListPage(orderPage.getContent());
        return new ApiMessageDto<>(
                new ResponseListObj<>(
                        orderDtoList,
                        orderPage
                ),
                "Get list order successfully"
        );
    }

    @Transactional
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<OrderAdminDto> get(@PathVariable(value = "id") Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.ORDER_NOT_FOUND, "Order not found"));
        return new ApiMessageDto<>(orderMapper.fromOrderEntityToAdminDtoDetails(order), "Get order details successfully");
    }

    @Transactional
    @GetMapping(value = "/get-user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<OrderDto> getUser(@PathVariable(value = "id") Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.ORDER_NOT_FOUND, "Order not found"));
        return new ApiMessageDto<>(orderMapper.fromOrderEntityToOrderDtoDetails(order), "Get order details successfully");
    }

    @GetMapping(value = "/get-history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<OrderStatusDto>> getHistory(@PathVariable(value = "id") Long id) {
        List<Order> orderHistory = auditService.getPreviousVersion(Order.class, id);
        return new ApiMessageDto<>(orderMapper.fromOrderEntityAuditListToOrderStatusDtoList(orderHistory), "Get order audit successfully");
    }

    @Transactional
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<OrderStatusDto> create(@Valid @RequestBody CreateOrderForm createOrderForm, BindingResult bindingResult, HttpServletRequest request) {
        Boolean isLogin = myIntercepter.checkHeader(request);

        Order order = new Order();
        Customer customer = null;
        if (isLogin) {
            customer = customerRepository.findByAccountId(getCurrentUserId()).orElse(null);
        }

        order.setCustomer(customer);

        Location ward = locationRepository.findById(createOrderForm.getWardId())
                .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Ward not found"));
        Location district = ward.getParent();
        Location province = district.getParent();
        if (!Objects.equals(district.getId(), createOrderForm.getDistrictId()))
            throw new RequestException(ErrorCode.LOCATION_ERROR_INVALID, "Invalid district");
        if (!Objects.equals(province.getId(), createOrderForm.getProvinceId()))
            throw new RequestException(ErrorCode.LOCATION_ERROR_INVALID, "Invalid province");

        double subTotal = 0.0;
        for (var createOrderItemFrom : createOrderForm.getOrderItems()) {
            Product product = productRepository.findById(createOrderItemFrom.getProductId())
                    .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found"));
            OrderItem orderItem = new OrderItem();
            int counter = 0;
            double productPrice = 0.0;
            List<Object> extraVariants = new ArrayList<>();
            for (var productConfig : product.getProductConfigs()) {
                ProductConfigDto productConfigDto = new ProductConfigDto();
                OrderProductConfig orderProductConfig = createOrderItemFrom.getProductConfigs().stream()
                        .filter(cfg -> cfg.getConfigId().equals(productConfig.getId()))
                        .findFirst()
                        .orElse(null);
                if (productConfig.getIsRequired() && orderProductConfig == null) {
                    throw new RequestException(ErrorCode.ORDER_PRODUCT_CONFIG_IS_REQUIRED, "Product config is required");
                }
                if (orderProductConfig == null) {
                    continue;
                }
                if (productConfig.getChoiceKind().equals(Constants.VARIANT_SINGLE_CHOICE) && orderProductConfig.getVariantIds().size() > 1) {
                    throw new RequestException(ErrorCode.ORDER_PRODUCT_VARIANT_INVALID, "Order variant must have one value");
                }
                for (var productVariantId : orderProductConfig.getVariantIds()) {
                    ProductVariant variant = variantRepository.findById(productVariantId)
                            .orElseThrow(() -> new RequestException(ErrorCode.VARIANT_NOT_FOUND, "Product variant not found"));
                    productPrice += variant.getPrice();
                    ProductVariantDto productVariantDto = new ProductVariantDto();
                    productVariantDto.setName(variant.getName());
                    productVariantDto.setId(variant.getId());
                    productVariantDto.setPrice(variant.getPrice());
                    productConfigDto.getVariants().add(productVariantDto);
                }
                productConfigDto.setName(productConfig.getName());
                productConfigDto.setId(productConfig.getId());
                extraVariants.add(productConfigDto);
            }
            productPrice = (product.getPrice() + productPrice) * createOrderItemFrom.getQuantity();
            subTotal += productPrice;
            orderItem.setProductName(product.getName());
            orderItem.setPrice(productPrice);
            //TODO module discount with promo or another reason
            orderItem.setDiscount(0.0);
            orderItem.setQuantity(createOrderItemFrom.getQuantity());
            orderItem.setExtraVariant(extraVariants);
            orderItem.setOrderSort(++counter);
            order.getOrderItems().add(orderItem);
        }
        order.setSubTotal(subTotal);
        //TODO calculate shipping charge
        order.setShippingCharge(0.0);
        order.setProvince(province);
        order.setDistrict(district);
        order.setWard(ward);
        order.setAddressDetails(createOrderForm.getAddressDetails());
        order.setReceiverFullName(createOrderForm.getReceiverName());
        order.setPhone(createOrderForm.getReceiverPhone());
        order.setPaymentMethod(createOrderForm.getPaymentMethod());
        order.setStatus(Constants.ORDER_STATUS_NEW);
        order.setNote(createOrderForm.getNote());
        order = orderRepository.saveAndFlush(order);
        //TODO notification by mail (phone)
        return new ApiMessageDto<>(orderMapper.fromOrderEntityToOrderStatusDto(order), "Create order successfully");
    }

    @PutMapping(value = "/change-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> changeOrderStatus(@Valid @RequestBody ChangeOrderStatusForm changeOrderStatusForm, BindingResult bindingResult) {
        Order order = orderRepository.findById(changeOrderStatusForm.getOrderId())
                .orElseThrow(() -> new RequestException(ErrorCode.ORDER_NOT_FOUND, "Order not found"));
        order.setStatus(changeOrderStatusForm.getOrderStatus());
        orderRepository.save(order);
        return new ApiMessageDto<>("Change order status successfully");
    }
}
