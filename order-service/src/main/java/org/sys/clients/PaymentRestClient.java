package org.sys.clients;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@RegisterRestClient(configKey = "payment-api")
@Path("/payments")
public interface PaymentRestClient {

  @POST
  @Path("/create")
  PaymentResponse createPayment(PaymentRequest request);

  @PUT
  @Path("/{id}/approve")
  PaymentResponse approvePayment(@jakarta.ws.rs.PathParam("id") UUID paymentId);

  @PUT
  @Path("/{id}/reject")
  PaymentResponse rejectPayment(@jakarta.ws.rs.PathParam("id") UUID paymentId);
}
