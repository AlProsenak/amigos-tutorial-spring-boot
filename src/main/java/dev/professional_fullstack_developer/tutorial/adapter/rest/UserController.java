package dev.professional_fullstack_developer.tutorial.adapter.rest;

import dev.professional_fullstack_developer.tutorial.adapter.repository.UserRepository;
import dev.professional_fullstack_developer.tutorial.domain.dto.CreateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.dto.SimpleResponse;
import dev.professional_fullstack_developer.tutorial.domain.dto.UpdateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.dto.UserResponse;
import dev.professional_fullstack_developer.tutorial.domain.dto.UsersResponse;
import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import dev.professional_fullstack_developer.tutorial.domain.exception.BadRequestException;
import dev.professional_fullstack_developer.tutorial.domain.exception.NotFoundException;
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

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping(
            path = {"/user", "/user/{id}"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Object getUsers(@PathVariable(name = "id", required = false) Long id) {
        if (id == null) {
            List<User> users = repository.findAll();
            return new UsersResponse(users);
        }

        User user = repository.findById(id)
                .orElseThrow(NotFoundException::new);

        return new UserResponse(user);
    }

    @PostMapping(
            path = "/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Object createUser(@RequestBody CreateUserRequest userDto) {
        User user = User.from(userDto);
        if (repository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        if (repository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User createdUser = repository.save(user);

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
        User user = repository.findById(userDto.id())
                .orElseThrow(NotFoundException::new);

        Optional<User> existingUser = repository.findByUsername(userDto.username());
        if (existingUser.isPresent() && existingUser.get().getId() != user.getId()) {
            throw new BadRequestException("Username already exists");
        }
        existingUser = repository.findByEmail(userDto.email());
        if (existingUser.isPresent() && existingUser.get().getId() != user.getId()) {
            throw new BadRequestException("Email already exists");
        }

        user.accept(userDto);
        User updatedUser = repository.save(user);

        return new UserResponse(updatedUser);
    }

    @DeleteMapping(
            path = "/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Object deleteUser(@RequestParam(name = "id") long id) {
        User deletedUser = repository.findById(id)
                .orElseThrow(NotFoundException::new);

        repository.delete(deletedUser);

        return new UserResponse(deletedUser);
    }

}
