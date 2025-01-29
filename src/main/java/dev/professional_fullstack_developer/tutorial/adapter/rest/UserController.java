package dev.professional_fullstack_developer.tutorial.adapter.rest;

import dev.professional_fullstack_developer.tutorial.adapter.repository.UserRepository;
import dev.professional_fullstack_developer.tutorial.domain.dto.CreateUser;
import dev.professional_fullstack_developer.tutorial.domain.dto.SimpleResponse;
import dev.professional_fullstack_developer.tutorial.domain.dto.UserResponse;
import dev.professional_fullstack_developer.tutorial.domain.dto.UsersResponse;
import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import dev.professional_fullstack_developer.tutorial.domain.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@RequestMapping(
        path = "/api/v1"
)
@Controller
@ResponseBody
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping(
            path = {"/user", "/user/{id}"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Object getUsers(@PathVariable(name = "id", required = false) Long id) {
        if (id == null) {
            List<User> users = repository.getUsers();
            return new UsersResponse(users);
        }
        Optional<User> user = repository.getUserById(id);
        if (user.isEmpty()) {
            throw new NotFoundException();
        }
        return new UserResponse(user.orElseThrow(() -> new RuntimeException("Unexpected error")));
    }

    @PostMapping(
            path = "/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Object createUser(@RequestBody CreateUser user) {
        long id = repository.createUser(user.name(), user.email());
        return new ResponseEntity<>(new SimpleResponse("User created with ID: %s".formatted(id)), HttpStatus.CREATED);
    }

    @DeleteMapping(
            path = "/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Object deleteUser(@RequestParam(name = "id") long id) {
        Optional<User> deletedUser = repository.deleteUser(id);
        if (deletedUser.isEmpty()) {
            throw new NotFoundException();
        }
        return new UserResponse(deletedUser.orElseThrow(() -> new RuntimeException("Unexpected error")));
    }

}
