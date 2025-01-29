package dev.professional_fullstack_developer.tutorial;

import org.springframework.http.HttpStatus;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping(
        path = "/api/v1"
)
@Controller
@ResponseBody
public class UserController {

    private final StaticUserRepository repository;

    public UserController() {
        this.repository = new StaticUserRepository();
    }

    // @ResponseBody automatically wraps return type, as if it was ResponseEntity<String>
    @GetMapping(
            path = {"/user", "/user/{id}"}
    )
    public Object getUsers(@PathVariable(name = "id", required = false) Long id) {
        if (id == null) {
            List<User> users = repository.getUsers();
            return new UsersResponse(users);
        }
        Optional<User> user = repository.getUserById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(new SimpleResponse("Not found"), HttpStatus.NOT_FOUND);
        }
        return new UserResponse(user.orElseThrow(() -> new RuntimeException("Unexpected error")));
    }

    public record UsersResponse(List<User> users) {
    }

    public record UserResponse(User user) {
    }

    @PostMapping(
            path = "/user",
            consumes = "application/json"
    )
    public Object createUser(@RequestBody CreateUser user) {
        long id = repository.createUser(user.name, user.email);
        return new ResponseEntity<>(new SimpleResponse("User created with ID: %s".formatted(id)), HttpStatus.CREATED);
    }

    @DeleteMapping(
            path = "/user"
    )
    public Object deleteUser(@RequestParam(name = "id") long id) {
        Optional<User> deletedUser = repository.deleteUser(id);
        if (deletedUser.isEmpty()) {
            return new ResponseEntity<>(new SimpleResponse("Not found"), HttpStatus.NOT_FOUND);
        }
        return new UserResponse(deletedUser.orElseThrow(() -> new RuntimeException("Unexpected error")));
    }

    public record CreateUser(String name, String email) {
    }

    public record SimpleResponse(String message) {
    }

    public static class User {

        private final long id;
        private final String username;
        private final String email;

        public User(long id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }

        public long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

    }

    public static class StaticUserRepository {

        private final List<User> users = new ArrayList<>();

        private long counter = 0;

        public StaticUserRepository() {
            this.users.add(new User(generateId(), "Alex", "alex@gmail.com"));
            this.users.add(new User(generateId(), "Alice", "alice@gmail.com"));
            this.users.add(new User(generateId(), "Bob", "bob@gmail.com"));
            this.users.add(new User(generateId(), "Robert", "robert@gmail.com"));
            this.users.add(new User(generateId(), "Denise", "denise@gmail.com"));
        }

        private long generateId() {
            this.counter += 1;
            return this.counter;
        }

        public List<User> getUsers() {
            return this.users;
        }

        public Optional<User> getUserById(long id) {
            return this.users.stream()
                    .filter(user -> user.id == id)
                    .findFirst();
        }

        public long createUser(String username, String email) {
            User user = new User(generateId(), username, email);
            this.users.add(user);
            return user.getId();
        }

        public Optional<User> deleteUser(long id) {
            Optional<User> deletedUser = this.users.stream()
                    .filter(user -> user.id == id)
                    .findFirst();

            deletedUser.ifPresent(this.users::remove);

            return deletedUser;
        }

    }

}
