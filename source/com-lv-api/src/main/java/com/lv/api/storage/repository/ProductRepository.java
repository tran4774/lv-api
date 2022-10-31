package com.lv.api.storage.repository;

import com.lv.api.storage.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findAllByStatus(Integer status);

    List<Product> findAllByStatusAndKind(Integer status, Integer kind);
    List<Product> findAllByStatusAndCategoryId(Integer status, Long categoryId);
}
