package dev.professional_fullstack_developer.tutorial.integration;

import dev.professional_fullstack_developer.tutorial.TutorialApplication;
import dev.professional_fullstack_developer.tutorial.domain.dto.CreateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.dto.UsersResponse;
import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import dev.professional_fullstack_developer.tutorial.testcontainer.PostgresTestcontainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TutorialApplication.class)
public class UserControllerTest extends PostgresTestcontainer {

    @Autowired
    private WebTestClient client;

    @Test
    void canCreateUser() {
        String username = "test";
        String email = "test@gmail.com";
        LocalDate birthdate = LocalDate.of(1999, 02, 02);

        CreateUserRequest request = new CreateUserRequest(username, email, birthdate);

        // Save user
        client.post()
                .uri("/api/v1/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateUserRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // Get all users
        EntityExchangeResult<UsersResponse> getAllResponse = client.get()
                .uri("/api/v1/user")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<UsersResponse>() {
                })
                .returnResult();

        User user1 = User.from(new CreateUserRequest(username, email, birthdate));
        List<User> users = List.of(user1);
        UsersResponse expectedResponse = new UsersResponse(users);

        assertThat(getAllResponse.getResponseBody())
                .usingRecursiveComparison()
                .ignoringFields("users.id", "users.createdAt", "users.updatedAt")
                .isEqualTo(expectedResponse);
    }

}
