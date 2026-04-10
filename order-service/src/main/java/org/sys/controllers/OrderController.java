package org.sys.controllers;

import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.sys.repositories.OrderStatus;
import org.sys.repositories.Orders;
import org.sys.services.OrderService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/orders")
@Tag(name = "Pedidos", description = "Operações relacionadas a pedidos")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GET
  @Path("/list")
  @Operation(summary = "Listar pedidos", description = "Retorna todos os pedidos com paginação. Opcionalmente filtra por status. ")
  public Response getAllOrders(
      @QueryParam("page") @DefaultValue("0") Integer page,
      @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,
      @QueryParam("status") String status) {
    if (status != null && !status.isBlank()) {
      OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
      var orders = orderService.findOrdersByStatus(orderStatus, page, pageSize);
      return Response.ok(orders).build();
    }
    var orders = orderService.findAllOrders(page, pageSize);
    return Response.ok(orders).build();
  }

  @GET
  @Path("/list/{id}")
  @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico pelo seu identificador")
  public Response getOrderById(@PathParam("id") UUID orderId) {
    var order = orderService.findOrderById(orderId);
    return Response.ok(order).build();
  }

  @POST
  @Path("/create")
  @Transactional
  @Operation(summary = "Criar pedido", description = "Cria um novo pedido com status CREATED")
  public Response createOrder(@Valid Orders order) {
    var created = orderService.createOrder(order);
    return Response.status(Response.Status.CREATED).entity(created).build();
  }

  @PUT
  @Path("/{id}/pay")
  @Transactional
  @Operation(summary = "Processar pagamento", description = "Envia o pedido para o serviço de pagamento e atualiza o status")
  public Response processPayment(@PathParam("id") UUID orderId) {
    var order = orderService.processPayment(orderId);
    return Response.ok(order).build();
  }

  @PUT
  @Path("/{id}/cancel")
  @Transactional
  @Operation(summary = "Cancelar pedido", description = "Cancela um pedido que ainda está com status CREATED")
  public Response cancelOrder(@PathParam("id") UUID orderId) {
    var order = orderService.cancelOrder(orderId);
    return Response.ok(order).build();
  }

  @DELETE
  @Path("/{id}")
  @Transactional
  @Operation(summary = "Remover pedido", description = "Remove um pedido que ainda está com status CREATED")
  public Response deleteOrder(@PathParam("id") UUID orderId) {
    orderService.deleteOrder(orderId);
    return Response.ok("Pedido removido com sucesso").build();
  }
}
