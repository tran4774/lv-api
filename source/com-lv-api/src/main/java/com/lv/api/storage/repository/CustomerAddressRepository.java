package com.lv.api.storage.repository;

import com.lv.api.storage.model.CustomerAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long>, JpaSpecificationExecutor<CustomerAddress> {
    Page<CustomerAddress> findCustomerAddressByCustomerId(Long customerId, Pageable pageable);
    List<CustomerAddress> findCustomerAddressByCustomerIdAndIsDefault(Long customerId, Boolean isDefault);
}
