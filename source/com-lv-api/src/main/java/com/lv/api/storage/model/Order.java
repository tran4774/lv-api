package com.lv.api.storage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "orders")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Audited
@NoArgsConstructor
@AllArgsConstructor
@AuditOverrides({
        @AuditOverride(forClass = Auditable.class, name = "modifiedBy"),
        @AuditOverride(forClass = Auditable.class, name = "modifiedDate"),
        @AuditOverride(forClass = Auditable.class, name = "status")
})
public class Order extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "sub_total")
    private Double subTotal;

    @Column(name = "shipping_charge")
    private Double shippingCharge;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "province_id")
    private Location province;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "district_id")
    private Location district;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Location ward;

    @NotAudited
    @Column(name = "address_details")
    private String addressDetails;

    @NotAudited
    @Column(name = "receiver_full_name")
    private String receiverFullName;

    @NotAudited
    @Column(name = "phone", columnDefinition = "varchar(10)")
    private String phone;

    @Column(name = "payment_method")
    private Integer paymentMethod;

    @Column(name = "note")
    private String note;

    @NotAudited
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = OrderItem.class)
    @JoinColumn(name = "order_id")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<OrderItem> orderItems = new ArrayList<>();
}
