package com.lv.api.mapper;

import com.lv.api.dto.employee.EmployeeAdminDto;
import com.lv.api.dto.employee.EmployeeDto;
import com.lv.api.form.employee.CreateEmployeeForm;
import com.lv.api.form.employee.UpdateEmployeeForm;
import com.lv.api.storage.model.Employee;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AccountMapper.class, CategoryMapper.class}
)
public interface EmployeeMapper {

    @Named("fromCreateEmployeeFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "username", target = "account.username")
    @Mapping(source = "password", target = "account.password", qualifiedByName = "passwordEncoder")
    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "avatar", target = "account.avatarPath")
    @Mapping(source = "status", target = "account.status")
    Employee fromCreateEmployeeFormToEntity(CreateEmployeeForm createEmployeeForm);

    @Named("fromEmployeeEntityToDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "account", target = "account", qualifiedByName = "adminGetMapping")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "department", target = "department", qualifiedByName = "adminGetMapping")
    @Mapping(source = "job", target = "job", qualifiedByName = "adminGetMapping")
    EmployeeDto fromEmployeeEntityToDto(Employee employee);

    @Named("fromListEmployeeEntityToListDto")
    @IterableMapping(elementTargetType = EmployeeDto.class, qualifiedByName = "fromEmployeeEntityToDtoMapper")
    List<EmployeeDto> fromListEmployeeEntityToListDto(List<Employee> employees);

    @Named("fromEmployeeEntityToAdminDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "account", target = "account", qualifiedByName = "adminGetMapping")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "department", target = "department", qualifiedByName = "adminGetMapping")
    @Mapping(source = "job", target = "job", qualifiedByName = "adminGetMapping")
    EmployeeAdminDto fromEmployeeEntityToAdminDto(Employee employee);

    @Named("fromEmployeeEntityListToAdminDtoListMapper")
    @IterableMapping(elementTargetType = EmployeeAdminDto.class, qualifiedByName = "fromEmployeeEntityToAdminDtoMapper")
    List<EmployeeAdminDto> fromEmployeeEntityListToAdminDtoList(List<Employee> employees);


    @Named("fromUpdateEmployeeFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "password", target = "account.password", qualifiedByName = "passwordEncoder")
    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "avatar", target = "account.avatarPath")
    @Mapping(source = "status", target = "account.status")
    void fromUpdateEmployeeFormToEntity(UpdateEmployeeForm updateEmployeeForm, @MappingTarget Employee employee);
}
