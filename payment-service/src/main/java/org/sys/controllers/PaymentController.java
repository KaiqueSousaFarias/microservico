package org.sys.controllers;

import java.util.UUID;

import org.sys.repositories.Payments;
import org.sys.services.PaymentService;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/payments")
public class PaymentController {

  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @GET
  @Path("/list")
  public Response getAllPayments(@QueryParam("page") @DefaultValue("0") Integer page,
      @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
    var payments = paymentService.findAllPayments(page, pageSize);
    return Response.ok(payments).build();
  }

  @GET
  @Path("/list/{id}")
  public Response getPaymentById(@PathParam("id") UUID paymentId) {
    var payment = paymentService.findPaymentById(paymentId);
    return Response.ok(payment).build();
  }

  @GET
  @Path("/order/{orderId}")
  public Response getPaymentsByOrderId(@PathParam("orderId") String orderId) {
    var payments = paymentService.findPaymentsByOrderId(orderId);
    return Response.ok(payments).build();
  }

  @POST
  @Path("/create")
  @Transactional
  public Response createPayment(Payments payment) {
    var created = paymentService.createPayment(payment);
    return Response.status(Response.Status.CREATED).entity(created).build();
  }

  @PUT
  @Path("/{id}/approve")
  @Transactional
  public Response approvePayment(@PathParam("id") UUID paymentId) {
    var payment = paymentService.approvePayment(paymentId);
    return Response.ok(payment).build();
  }

  @PUT
  @Path("/{id}/reject")
  @Transactional
  public Response rejectPayment(@PathParam("id") UUID paymentId) {
    var payment = paymentService.rejectPayment(paymentId);
    return Response.ok(payment).build();
  }
}
