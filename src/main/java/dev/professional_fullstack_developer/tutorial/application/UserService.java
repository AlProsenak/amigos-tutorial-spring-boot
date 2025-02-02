package dev.professional_fullstack_developer.tutorial.application;

import dev.professional_fullstack_developer.tutorial.domain.dto.CreateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.dto.UpdateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(long id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    User createUser(CreateUserRequest createUserRequest);

    User updateUser(UpdateUserRequest userDto);

    User deleteUser(long id);

}
