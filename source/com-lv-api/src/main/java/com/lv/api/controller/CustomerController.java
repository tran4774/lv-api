package com.lv.api.controller;

import com.lv.api.constant.Constants;
import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.customer.CustomerAddressDto;
import com.lv.api.dto.customer.CustomerAdminDto;
import com.lv.api.dto.customer.CustomerDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.customer.*;
import com.lv.api.mapper.CustomerMapper;
import com.lv.api.service.AccountService;
import com.lv.api.service.CommonApiService;
import com.lv.api.storage.criteria.CustomerAddressCriteria;
import com.lv.api.storage.criteria.CustomerCriteria;
import com.lv.api.storage.model.Customer;
import com.lv.api.storage.model.CustomerAddress;
import com.lv.api.storage.model.Group;
import com.lv.api.storage.model.Location;
import com.lv.api.storage.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/v1/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class CustomerController extends ABasicController {
    private final AccountRepository accountRepository;
    private final GroupRepository groupRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final LocationRepository locationRepository;
    private final CustomerMapper customerMapper;
    private final CommonApiService commonApiService;
    private final AccountService accountService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<CustomerAdminDto>> list(CustomerCriteria customerCriteria, BindingResult bindingResult, Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(customerCriteria.getSpecification(), pageable);
        List<CustomerAdminDto> customerDtoList = customerMapper.fromListCustomerEntityToListAdminDto(customerPage.getContent());
        return new ApiMessageDto<>(new ResponseListObj<>(customerDtoList, customerPage), "Get list successfully");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<CustomerDto>> autoComplete(CustomerCriteria customerCriteria) {
        Page<Customer> customerPage = customerRepository.findAll(customerCriteria.getSpecification(), Pageable.unpaged());
        return new ApiMessageDto<>(
                customerMapper.fromListCustomerEntityToListDto(customerPage.getContent()),
                "Auto complete customer successfully"
        );
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CustomerAdminDto> get(@PathVariable("id") Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found"));
        return new ApiMessageDto<>(customerMapper.fromCustomerEntityToAdminDto(customer), "Get customer successfully");
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CustomerDto> profile() {
        Customer customer = customerRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found"));
        return new ApiMessageDto<>(customerMapper.fromCustomerEntityToDto(customer), "Get customer successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateCustomerForm createCustomerForm, BindingResult bindingResult) {
        // 0 = not exist
        accountService.checkAccountCreate(createCustomerForm.getUsername(), createCustomerForm.getPhone(), createCustomerForm.getEmail());
        Group groupCustomer = groupRepository.findFirstByKind(Constants.GROUP_KIND_CUSTOMER);
        if (groupCustomer == null) {
            throw new RequestException(ErrorCode.GROUP_ERROR_NOT_FOUND);
        }
        Customer customer = customerMapper.fromCustomerCreateFormToEntity(createCustomerForm);
        customer.getAccount().setGroup(groupCustomer);
        customer.getAccount().setKind(Constants.USER_KIND_CUSTOMER);
        //rank?
        customerRepository.save(customer);
        return new ApiMessageDto<>("Create customer successfully");
    }

    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> register(@Valid @RequestBody RegisterCustomerForm registerCustomerForm, BindingResult bindingResult) {
        accountService.checkAccountCreate(registerCustomerForm.getUsername(), registerCustomerForm.getPhone(), registerCustomerForm.getEmail());
        Group groupCustomer = groupRepository.findFirstByKind(Constants.GROUP_KIND_CUSTOMER);
        if (groupCustomer == null) {
            throw new RequestException(ErrorCode.GROUP_ERROR_NOT_FOUND);
        }
        Customer customer = customerMapper.fromCustomerRegisterFormToEntity(registerCustomerForm);
        customer.getAccount().setGroup(groupCustomer);
        customer.getAccount().setKind(Constants.USER_KIND_CUSTOMER);
        //rank?
        customerRepository.save(customer);
        return new ApiMessageDto<>("Create customer successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCustomerForm updateCustomerForm, BindingResult bindingResult) {
        Customer customer = customerRepository.findById(updateCustomerForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found"));
        accountService.checkAccountUpdate(customer.getAccount().getId(), updateCustomerForm.getPhone(), updateCustomerForm.getEmail());
        if (StringUtils.isNoneBlank(updateCustomerForm.getAvatar()) && !updateCustomerForm.getAvatar().equals(customer.getAccount().getAvatarPath()))
            commonApiService.deleteFile(customer.getAccount().getAvatarPath());
        customerMapper.fromUpdateCustomerFormToEntity(updateCustomerForm, customer);
        customerRepository.save(customer);
        accountRepository.save(customer.getAccount());
        return new ApiMessageDto<>("Update customer successfully");
    }

    @PutMapping(value = "/update-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updateProfile(@Valid @RequestBody UpdateProfileCustomerForm updateProfileCustomerForm, BindingResult bindingResult) {
        if (!isCustomer()) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed get list.");
        }
        Customer customer = customerRepository.findById(updateProfileCustomerForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found"));
        if (StringUtils.isNoneBlank(updateProfileCustomerForm.getAvatar()) && !updateProfileCustomerForm.getAvatar().equals(customer.getAccount().getAvatarPath()))
            commonApiService.deleteFile(customer.getAccount().getAvatarPath());
        customerMapper.fromUpdateProfileCustomerFormToEntity(updateProfileCustomerForm, customer);
        customerRepository.save(customer);
        accountRepository.save(customer.getAccount());
        return new ApiMessageDto<>("Update customer successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found"));
        commonApiService.deleteFile(customer.getAccount().getAvatarPath());
        customerRepository.delete(customer);
        return new ApiMessageDto<>("Delete customer successfully");
    }

    @GetMapping(value = "/address/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<CustomerAddressDto>> listAddressAdmin(CustomerAddressCriteria customerAddressCriteria, Pageable pageable) {
        Page<CustomerAddress> customerAddressPage = customerAddressRepository.findAll(customerAddressCriteria.getSpecification(), pageable);
        List<CustomerAddressDto> customerAddressDtos = customerMapper.fromListCustomerAddressEntityToListDto(customerAddressPage.getContent());
        return new ApiMessageDto<>(new ResponseListObj<>(customerAddressDtos, customerAddressPage), "Get list successfully");
    }

    @GetMapping(value = "/address/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CustomerAddressDto> getAddress(@PathVariable("id") Long id) {
        Optional<CustomerAddress> customerAddress;
        if (isCustomer()) {
            customerAddress = customerAddressRepository.findCustomerAddressByIdAndCustomerId(id, getCurrentUserId());
        } else {
            customerAddress = customerAddressRepository.findById(id);
        }
        return new ApiMessageDto<>(
                customerMapper.fromCustomerAddressEntityToDto(
                        customerAddress.orElseThrow(() -> new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_NOT_FOUND, "Customer's address not found"))
                ),
                "Get list successfully");
    }

    @Transactional
    @PostMapping(value = "/address/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> createAddress(@Valid @RequestBody CreateAddressForm createAddressForm, BindingResult bindingResult) {
        Customer customer = customerRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found"));
        Location ward = locationRepository.findById(createAddressForm.getWardId())
                .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Ward not found"));
        Location district = ward.getParent();
        Location province = district.getParent();
        if (!Objects.equals(district.getId(), createAddressForm.getDistrictId()))
            throw new RequestException(ErrorCode.LOCATION_ERROR_INVALID, "Invalid district");
        if (!Objects.equals(province.getId(), createAddressForm.getProvinceId()))
            throw new RequestException(ErrorCode.LOCATION_ERROR_INVALID, "Invalid province");
        CustomerAddress customerAddress = customerMapper.fromCreateAddressFormToCustomerAddress(createAddressForm);
        customerAddress.setProvince(province);
        customerAddress.setDistrict(district);
        customerAddress.setWard(ward);
        customerAddress.setCustomer(customer);
        if (createAddressForm.getIsDefault()) {
            customerAddressRepository.updateCustomerAddressWithDefault(customer.getId(), false);
        }
        customerAddressRepository.save(customerAddress);
        return new ApiMessageDto<>("Add address success");
    }

    @Transactional
    @PutMapping(value = "/address/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updateAddress(@Valid @RequestBody UpdateAddressForm updateAddressForm, BindingResult bindingResult) {
        CustomerAddress customerAddress = customerAddressRepository.findById(updateAddressForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_NOT_FOUND, "Customer's address not found"));
        if (!customerAddress.getCustomer().getId().equals(getCurrentUserId()))
            throw new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_NOT_FOUND, "Customer's address not found");
        Location ward = locationRepository.findById(updateAddressForm.getWardId())
                .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Ward not found"));
        Location district = ward.getParent();
        Location province = district.getParent();
        if (!Objects.equals(district.getId(), updateAddressForm.getDistrictId()))
            throw new RequestException(ErrorCode.LOCATION_ERROR_INVALID, "Invalid district");
        if (!Objects.equals(province.getId(), updateAddressForm.getProvinceId()))
            throw new RequestException(ErrorCode.LOCATION_ERROR_INVALID, "Invalid province");
        if (updateAddressForm.getIsDefault()) {
            customerAddressRepository.updateCustomerAddressWithDefault(getCurrentUserId(), false);
        }
        customerMapper.fromUpdateAddressFormToCustomerAddress(updateAddressForm, customerAddress);
        customerAddressRepository.save(customerAddress);
        return new ApiMessageDto<>("Update address successfully");
    }

    @DeleteMapping(value = "/address/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> deleteAddress(@PathVariable("id") Long id) {
        CustomerAddress customerAddress = customerAddressRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_NOT_FOUND, "Customer's address not found"));
        if (!customerAddress.getCustomer().getId().equals(getCurrentUserId()))
            throw new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_NOT_FOUND, "Customer's address not found");
        if (customerAddress.getIsDefault())
            throw new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_CANNOT_DELETE, "Customer's address can not delete because it is set by default");
        customerAddressRepository.delete(customerAddress);
        return new ApiMessageDto<>("Delete customer's address successfully");
    }

    @PutMapping(value = "/address/default/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> setDefaultAddress(@PathVariable("id") Long id) {
        CustomerAddress customerAddress = customerAddressRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_NOT_FOUND, "Customer's address not found"));
        if (!customerAddress.getCustomer().getId().equals(getCurrentUserId()))
            throw new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_NOT_FOUND, "Customer's address not found");
        customerAddressRepository.updateCustomerAddressWithDefault(getCurrentUserId(), false);
        customerAddress.setIsDefault(true);
        customerAddressRepository.save(customerAddress);
        return new ApiMessageDto<>("Set default customer's address successfully");
    }
}
