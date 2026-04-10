package org.sys.repositories;

import java.time.LocalDateTime;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_payments")
public class Payments extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  public UUID id;

  @NotBlank(message = "O ID do pedido é obrigatório")
  @Column(name = "order_id")
  public String orderId;

  @NotNull(message = "O valor é obrigatório")
  @Min(value = 1, message = "O valor deve ser maior que zero")
  public Integer value;

  @Enumerated(EnumType.STRING)
  public PaymentStatus status;

  @Column(name = "created_at", updatable = false)
  public LocalDateTime createdAt;

  @Column(name = "updated_at")
  public LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public static List<Payments> findByOrderId(String orderId) {
    return list("orderId", orderId);
  }

  public static List<Payments> findByStatus(PaymentStatus status, Integer page, Integer pageSize) {
    return find("status", status).page(page, pageSize).list();
  }
}
