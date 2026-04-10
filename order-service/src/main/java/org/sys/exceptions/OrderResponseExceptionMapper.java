package org.sys.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import main.java.org.sys.exceptions.OrderNotFoundException;

@Provider
public class OrderResponseExceptionMapper implements ExceptionMapper<OrderNotFoundException> {
  @Override
  public Response toResponse(OrderNotFoundException exception) {
    return Response.status(Response.Status.NOT_FOUND).entity("Pedido não encontrado").build();
  }
}
