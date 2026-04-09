package org.sys.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PaymentServiceExceptionMapper implements ExceptionMapper<PaymentServiceException> {
  @Override
  public Response toResponse(PaymentServiceException exception) {
    return Response.status(Response.Status.BAD_GATEWAY).entity(exception.getMessage()).build();
  }
}
