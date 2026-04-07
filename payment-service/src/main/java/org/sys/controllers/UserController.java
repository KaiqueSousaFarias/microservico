package org.sys.controllers;

import java.util.UUID;

import org.sys.repositories.Users;
import org.sys.services.UserService;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/users")

public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GET
  public Response getAllUsers(@QueryParam("page") @DefaultValue("0") Integer page,
                              @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
      var users = userService.findAllUsers(page, pageSize);
    return Response.ok(users).build();
  }

  @GET
  @Path("/{id}")
  public Response getUserById(@PathParam("id") UUID userId){
    return Response.ok(userService.findUserById(userId)).build();
  }

  @POST
  @Transactional
  public Response createUser(Users users) {
    return Response.ok(userService.createUsers(users)).build();
  }

}
