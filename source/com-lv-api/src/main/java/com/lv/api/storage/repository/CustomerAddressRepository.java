package com.lv.api.storage.repository;

import com.lv.api.storage.model.CustomerAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long>, JpaSpecificationExecutor<CustomerAddress> {

    Optional<CustomerAddress> findCustomerAddressByIdAndCustomerId(Long id, Long customerId);

    @Modifying
    @Query(value = "UPDATE CustomerAddress ca SET ca.isDefault = false WHERE ca.customer.id = :customerId AND ca.isDefault = :isDefault")
    int updateCustomerAddressWithDefault(@Param("customerId") Long customerId, @Param("isDefault") boolean isDefault);
}
