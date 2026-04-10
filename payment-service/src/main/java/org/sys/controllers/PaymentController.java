package org.sys.controllers;

import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.sys.repositories.PaymentStatus;
import org.sys.repositories.Payments;
import org.sys.services.PaymentService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/payments")
@Tag(name = "Pagamentos", description = "Operações relacionadas a pagamentos")
public class PaymentController {

  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @GET
  @Path("/list")
  @Operation(summary = "Listar pagamentos", description = "Retorna todos os pagamentos com paginação. Opcionalmente filtra por status.")
  public Response getAllPayments(
      @QueryParam("page") @DefaultValue("0") Integer page,
      @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,
      @QueryParam("status") String status) {
    if (status != null && !status.isBlank()) {
      PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
      var payments = paymentService.findPaymentsByStatus(paymentStatus, page, pageSize);
      return Response.ok(payments).build();
    }
    var payments = paymentService.findAllPayments(page, pageSize);
    return Response.ok(payments).build();
  }

  @GET
  @Path("/list/{id}")
  @Operation(summary = "Buscar pagamento por ID", description = "Retorna um pagamento específico pelo seu identificador")
  public Response getPaymentById(@PathParam("id") UUID paymentId) {
    var payment = paymentService.findPaymentById(paymentId);
    return Response.ok(payment).build();
  }

  @GET
  @Path("/order/{orderId}")
  @Operation(summary = "Buscar pagamentos por pedido", description = "Retorna todos os pagamentos associados a um pedido")
  public Response getPaymentsByOrderId(@PathParam("orderId") String orderId) {
    var payments = paymentService.findPaymentsByOrderId(orderId);
    return Response.ok(payments).build();
  }

  @POST
  @Path("/create")
  @Transactional
  @Operation(summary = "Criar pagamento", description = "Cria um novo pagamento com status PENDING")
  public Response createPayment(@Valid Payments payment) {
    var created = paymentService.createPayment(payment);
    return Response.status(Response.Status.CREATED).entity(created).build();
  }

  @PUT
  @Path("/{id}/approve")
  @Transactional
  @Operation(summary = "Aprovar pagamento", description = "Altera o status do pagamento para APPROVED")
  public Response approvePayment(@PathParam("id") UUID paymentId) {
    var payment = paymentService.approvePayment(paymentId);
    return Response.ok(payment).build();
  }

  @PUT
  @Path("/{id}/reject")
  @Transactional
  @Operation(summary = "Rejeitar pagamento", description = "Altera o status do pagamento para REJECTED")
  public Response rejectPayment(@PathParam("id") UUID paymentId) {
    var payment = paymentService.rejectPayment(paymentId);
    return Response.ok(payment).build();
  }
}
