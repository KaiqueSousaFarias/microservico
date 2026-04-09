package org.sys.controllers;

import java.util.UUID;

import org.sys.repositories.Orders;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import main.java.org.sys.services.OrderService;

@Path("/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GET
  @Path("/list")
  public Response getAllOrders(@QueryParam("page") @DefaultValue("0") Integer page,
      @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
    var orders = orderService.findAllOrders(page, pageSize);
    return Response.ok(orders).build();
  }

  @GET
  @Path("/list/{id}")
  public Response getOrderById(@PathParam("id") UUID orderId) {
    var order = orderService.findOrderById(orderId);
    return Response.ok(order).build();
  }

  @POST
  @Path("/create")
  @Transactional
  public Response createOrder(Orders order) {
    var created = orderService.createOrder(order);
    return Response.status(Response.Status.CREATED).entity(created).build();
  }

  @PUT
  @Path("/{id}/pay")
  @Transactional
  public Response processPayment(@PathParam("id") UUID orderId) {
    var order = orderService.processPayment(orderId);
    return Response.ok(order).build();
  }
}
