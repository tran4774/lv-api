package com.lv.api.storage.repository;

import com.lv.api.storage.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>, JpaSpecificationExecutor<ProductCategory> {
    List<ProductCategory> findAllByStatus(Integer status);

    Optional<ProductCategory> findByStatusAndId(Integer status, Long id);
}
