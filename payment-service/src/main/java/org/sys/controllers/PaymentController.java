package org.sys.controllers;

import java.util.UUID;

import org.sys.repositories.Payments;
import org.sys.services.PaymentService;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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
}
