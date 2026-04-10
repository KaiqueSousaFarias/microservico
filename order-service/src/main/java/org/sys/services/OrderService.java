package org.sys.services;

import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.sys.clients.PaymentRequest;
import org.sys.clients.PaymentRestClient;
import org.sys.exceptions.InvalidOrderOperationException;
import org.sys.exceptions.PaymentServiceException;
import org.sys.repositories.OrderStatus;
import org.sys.repositories.Orders;

import jakarta.enterprise.context.ApplicationScoped;
import main.java.org.sys.clients.PaymentResponse;
import main.java.org.sys.exceptions.OrderNotFoundException;

@ApplicationScoped
public class OrderService {

  private final PaymentRestClient paymentRestClient;

  public OrderService(@RestClient PaymentRestClient paymentRestClient) {
    this.paymentRestClient = paymentRestClient;
  }

  public Orders createOrder(Orders order) {
    order.status = OrderStatus.CREATED;
    Orders.persist(order);
    return order;
  }

  public List<Orders> findAllOrders(Integer page, Integer pageSize) {
    return Orders.findAll().page(page, pageSize).list();
  }

  public List<Orders> findOrdersByStatus(OrderStatus status, Integer page, Integer pageSize) {
    return Orders.findByStatus(status, page, pageSize);
  }

  public Orders findOrderById(UUID orderId) {
    return (Orders) Orders.findByIdOptional(orderId).orElseThrow(OrderNotFoundException::new);
  }

  public Orders cancelOrder(UUID orderId) {
    Orders order = findOrderById(orderId);

    if (order.status != OrderStatus.CREATED) {
      throw new InvalidOrderOperationException(
          "Somente pedidos com status CREATED podem ser cancelados. Status atual: " + order.status);
    }

    order.status = OrderStatus.CANCELED;
    Orders.persist(order);
    return order;
  }

  public void deleteOrder(UUID orderId) {
    Orders order = findOrderById(orderId);

    if (order.status != OrderStatus.CREATED) {
      throw new InvalidOrderOperationException(
          "Somente pedidos com status CREATED podem ser removidos. Status atual: " + order.status);
    }

    Orders.deleteById(order.id);
  }

  public Orders processPayment(UUID orderId) {
    Orders order = findOrderById(orderId);

    if (order.status != OrderStatus.CREATED) {
      throw new InvalidOrderOperationException(
          "Pedido já processado com status: " + order.status);
    }

    try {
      PaymentRequest request = new PaymentRequest(order.id.toString(), order.value);
      PaymentResponse payment = paymentRestClient.createPayment(request);

      PaymentResponse approved = paymentRestClient.approvePayment(payment.id);

      if ("APPROVED".equals(approved.status)) {
        order.status = OrderStatus.PAID;
      } else {
        order.status = OrderStatus.CANCELED;
      }
    } catch (Exception e) {
      throw new PaymentServiceException("Serviço de pagamento indisponível");
    }

    Orders.persist(order);
    return order;
  }
}
