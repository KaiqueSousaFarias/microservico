package org.sys.clients;

public class PaymentRequest {
  public String orderId;
  public Integer value;

  public PaymentRequest() {
  }

  public PaymentRequest(String orderId, Integer value) {
    this.orderId = orderId;
    this.value = value;
  }
}
