package dev.professional_fullstack_developer.tutorial.adapter.rest;

import dev.professional_fullstack_developer.tutorial.application.UserService;
import dev.professional_fullstack_developer.tutorial.domain.dto.CreateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.dto.SimpleResponse;
import dev.professional_fullstack_developer.tutorial.domain.dto.UpdateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.dto.UserResponse;
import dev.professional_fullstack_developer.tutorial.domain.dto.UsersResponse;
import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

@RequestMapping(
        path = "/api/v1"
)
@Controller
@ResponseBody
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(
            path = {"/user", "/user/{id}"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Object getUsers(@PathVariable(name = "id", required = false) Optional<Long> id) {
        if (id.isEmpty()) {
            List<User> users = service.getAllUsers();
            return new UsersResponse(users);
        }

        User user = service.getUserById(id.get());

        return new UserResponse(user);
    }

    @PostMapping(
            path = "/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Object createUser(@RequestBody CreateUserRequest userDto) {
        User createdUser = service.createUser(userDto);

        return new ResponseEntity<>(new SimpleResponse("User created with ID: %s"
                .formatted(createdUser.getId())), HttpStatus.CREATED);
    }

    @PutMapping(
            path = "/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Object updateUser(@RequestBody UpdateUserRequest userDto) {
        User updatedUser = service.updateUser(userDto);

        return new UserResponse(updatedUser);
    }

    @DeleteMapping(
            path = "/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Object deleteUser(@RequestParam(name = "id") long id) {
        User deletedUser = service.deleteUser(id);

        return new UserResponse(deletedUser);
    }

}
