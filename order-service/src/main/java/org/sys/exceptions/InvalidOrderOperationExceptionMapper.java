package org.sys.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidOrderOperationExceptionMapper implements ExceptionMapper<InvalidOrderOperationException> {
    @Override
    public Response toResponse(InvalidOrderOperationException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
