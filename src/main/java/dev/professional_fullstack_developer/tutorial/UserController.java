package dev.professional_fullstack_developer.tutorial;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
public class UserController {

    private final StaticUserRepository repository;

    public UserController() {
        this.repository = new StaticUserRepository();
    }

    // @ResponseBody automatically wraps return type, as if it was ResponseEntity<String>
    @GetMapping(
            path = "/user"
    )
    @ResponseBody
    public Object getUsers(@RequestParam(name = "id", required = false) Long id) {
        if (id == null) {
            List<User> users = repository.getUsers();
            return new UsersResponse(users);
        }
        Optional<User> user = repository.getUserById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(new ExceptionResponse("Not found"), HttpStatus.NOT_FOUND);
        }
        return new UserResponse(user.orElseThrow(() -> new RuntimeException("Unexpected error")));
    }

    public record UsersResponse(List<User> users) {
    }

    public record UserResponse(User user) {
    }

    public record ExceptionResponse(String message) {
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

        public StaticUserRepository() {
            this.users.add(new User(1, "Alex", "alex@gmail.com"));
            this.users.add(new User(2, "Alice", "alice@gmail.com"));
            this.users.add(new User(3, "Bob", "bob@gmail.com"));
            this.users.add(new User(4, "Robert", "robert@gmail.com"));
            this.users.add(new User(5, "Denise", "denise@gmail.com"));
        }

        public List<User> getUsers() {
            return this.users;
        }

        public Optional<User> getUserById(long id) {
            return this.users.stream()
                    .filter(user -> user.id == id)
                    .findFirst();
        }

    }

}
