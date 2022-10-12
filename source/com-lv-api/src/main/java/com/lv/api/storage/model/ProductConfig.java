package com.lv.api.storage.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = TablePrefix.PREFIX_TABLE + "product_config")
public class ProductConfig extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "choice_kind")
    private Integer choiceKind;

    @Column(name = "is_required")
    private Boolean isRequired;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false)
    private Product product;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true, targetEntity = ProductVariant.class)
    @JoinColumn(name = "product_config_id")
    List<ProductVariant> variants = new ArrayList<>();
}
