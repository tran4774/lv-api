package com.lv.api.mapper;

import com.lv.api.dto.customer.CustomerAddressDto;
import com.lv.api.dto.customer.CustomerAdminDto;
import com.lv.api.dto.customer.CustomerDto;
import com.lv.api.form.customer.CreateAddressForm;
import com.lv.api.form.customer.RegisterCustomerForm;
import com.lv.api.form.customer.UpdateAddressForm;
import com.lv.api.form.customer.UpdateCustomerForm;
import com.lv.api.storage.model.Customer;
import com.lv.api.storage.model.CustomerAddress;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AccountMapper.class}
)
public interface CustomerMapper {

    @Named("fromCustomerRegisterFormToEntity")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "username", target = "account.username")
    @Mapping(source = "password", target = "account.password", qualifiedByName = "passwordEncoder")
    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "birthday", target = "birthday")
    Customer fromCustomerRegisterFormToEntity(RegisterCustomerForm registerCustomerForm);

    @Named("fromCustomerEntityToDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "account", target = "account", qualifiedByName = "fromEntityToAccountDtoMapper")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "note", target = "note")
    CustomerDto fromCustomerEntityToDto(Customer customer);

    @Named("fromListCustomerEntityToListDtoMapper")
    @IterableMapping(elementTargetType = CustomerDto.class, qualifiedByName = "fromCustomerEntityToDtoMapper")
    List<CustomerDto> fromListCustomerEntityToListDto(List<Customer> customers);

    @Named("fromCustomerEntityToAdminDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "account", target = "account", qualifiedByName = "adminGetMapping")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "note", target = "note")
    CustomerAdminDto fromCustomerEntityToAdminDto(Customer customer);

    @Named("fromListCustomerEntityToListAdminDtoMapper")
    @IterableMapping(elementTargetType = CustomerAdminDto.class, qualifiedByName = "fromCustomerEntityToAdminDtoMapper")
    List<CustomerAdminDto> fromListCustomerEntityToListAdminDto(List<Customer> customers);

    @Named("fromUpdateCustomerFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "avatar", target = "account.avatarPath")
    @Mapping(source = "birthday", target = "birthday")
    void fromUpdateCustomerFormToEntity(UpdateCustomerForm updateCustomerForm, @MappingTarget Customer customer);

    @Named("fromCustomerAddressEntityToDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "province.name", target = "province")
    @Mapping(source = "district.name", target = "district")
    @Mapping(source = "ward.name", target = "ward")
    @Mapping(source = "addressDetails", target = "addressDetails")
    @Mapping(source = "receiverFullName", target = "receiverFullName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "isDefault", target = "isDefault")
    CustomerAddressDto fromCustomerAddressEntityToDto(CustomerAddress customerAddress);

    @Named("fromListCustomerAddressEntityToListDtoMapper")
    @IterableMapping(elementTargetType = CustomerAddressDto.class, qualifiedByName = "fromCustomerAddressEntityToDtoMapper")
    List<CustomerAddressDto> fromListCustomerAddressEntityToListDto(List<CustomerAddress> customerAddresses);

    @Named("fromCreateAddressFormToCustomerAddressMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "addressDetails", target = "addressDetails")
    @Mapping(source = "receiverFullName", target = "receiverFullName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "isDefault", target = "isDefault")
    CustomerAddress fromCreateAddressFormToCustomerAddress(CreateAddressForm createAddressForm);

    @Named("fromUpdateAddressFormToCustomerAddressMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "addressDetails", target = "addressDetails")
    @Mapping(source = "receiverFullName", target = "receiverFullName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "isDefault", target = "isDefault")
    void fromUpdateAddressFormToCustomerAddress(UpdateAddressForm updateAddressForm, @MappingTarget CustomerAddress customerAddress);
}
