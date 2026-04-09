package org.sys.services;

import java.util.List;
import java.util.UUID;

import org.sys.exceptions.PaymentNotFoundException;
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

  public Payments createPayment(Payments payments) {
    Payments.persist(payments);
    return payments;
  }
}
