package org.sys.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PaymentResponseExceptionMapper implements ExceptionMapper<PaymentNotFoundException> {
  @Override
  public Response toResponse(PaymentNotFoundException exception) {
    return Response.status(Response.Status.NOT_FOUND).entity("Payment not found").build();
  }
}
