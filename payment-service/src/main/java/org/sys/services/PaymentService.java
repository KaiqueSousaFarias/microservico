package org.sys.services;

import java.util.List;
import java.util.UUID;

import org.sys.exceptions.PaymentNotFoundException;
import org.sys.repositories.PaymentStatus;
import org.sys.repositories.Payments;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaymentService {

  public List<Payments> findAllPayments(Integer page, Integer pageSize) {
    return Payments.findAll().page(page, pageSize).list();
  }

  public Payments findPaymentById(UUID paymentId) {
    return (Payments) Payments.findByIdOptional(paymentId).orElseThrow(PaymentNotFoundException::new);
  }

  public List<Payments> findPaymentsByOrderId(String orderId) {
    return Payments.findByOrderId(orderId);
  }

  public Payments createPayment(Payments payment) {
    payment.status = PaymentStatus.PENDING;
    Payments.persist(payment);
    return payment;
  }

  public Payments approvePayment(UUID paymentId) {
    Payments payment = findPaymentById(paymentId);
    payment.status = PaymentStatus.APPROVED;
    Payments.persist(payment);
    return payment;
  }

  public Payments rejectPayment(UUID paymentId) {
    Payments payment = findPaymentById(paymentId);
    payment.status = PaymentStatus.REJECTED;
    Payments.persist(payment);
    return payment;
  }
}
