package org.sys.services;

import java.util.List;
import java.util.UUID;

import org.sys.exceptions.UserNotFoundException;
import org.sys.repositories.Users;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {
  public Users createUsers(Users users){
    Users.persist(users);
    return users;
  }

  public List<Users> findAllUsers(Integer page, Integer pageSize) {
    return Users.findAll().page(page, pageSize).list();
  }

  public Users findUserById(UUID userId) {
    return (Users) Users.findByIdOptional(userId).orElseThrow(UserNotFoundException::new);
  }

  public Users updateUserById(UUID userId, Users users) {
    var user = findUserById(userId);

    user.username = users.username;
    user.email = users.email;

    Users.persist(user);

    return(user);
  }
}
