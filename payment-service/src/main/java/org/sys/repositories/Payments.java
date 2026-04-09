package org.sys.repositories;

import java.util.List;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_payments")
public class Payments extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  public UUID id;

  @Column(name = "order_id")
  public String orderId;

  public Integer value;

  @Enumerated(EnumType.STRING)
  public PaymentStatus status;

  public static List<Payments> findByOrderId(String orderId) {
    return list("orderId", orderId);
  }
}
