package dev.professional_fullstack_developer.tutorial.application;

import dev.professional_fullstack_developer.tutorial.adapter.repository.UserRepository;
import dev.professional_fullstack_developer.tutorial.domain.dto.CreateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.dto.UpdateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import dev.professional_fullstack_developer.tutorial.domain.exception.BadRequestException;
import dev.professional_fullstack_developer.tutorial.domain.exception.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static dev.professional_fullstack_developer.tutorial.application.local.TestDataInitializer.getStaticTestUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    // Alternative way of instantiating, instead of manually supplying dependency mocks.
    @InjectMocks
    private UserServiceImpl testSubject;

    @Mock
    private UserRepository repository;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        this.autoCloseable = MockitoAnnotations.openMocks(this);
        this.testSubject = new UserServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Close mocks in order to begin tests with fresh mocks.
        // To avoid writing this boilerplate code, we can annotate class with @ExtendWith({MockitoExtension.class})
        this.autoCloseable.close();
    }

    @Test
    void getAllUsers() {
        testSubject.getAllUsers();

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    void getUserById() {
        long notFoundId = 999L;

        Mockito.when(repository.findById(notFoundId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> testSubject.getUserById(notFoundId));

        assertThatThrownBy(() -> testSubject.getUserById(notFoundId))
                .isInstanceOf(NotFoundException.class);

        long foundId = 1L;

        Mockito.when(repository.findById(foundId)).thenReturn(Optional.of(getStaticTestUsers().getFirst()));
        User actual = testSubject.getUserById(foundId);

        assertThat(actual).isNotNull();
    }

    @Test
    void getUserByUsername() {
    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void createUser() {
        CreateUserRequest request = new CreateUserRequest("test", "test@mail.com", LocalDate.of(2000, 1, 1));

        Mockito.when(repository.existsByUsername(ArgumentMatchers.anyString()))
                .thenReturn(true);

        assertThrows(BadRequestException.class, () -> testSubject.createUser(request));

        // Better alternative to testing throwable
        assertThatThrownBy(() -> testSubject.createUser(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Username already exists");
    }

    @Test
    void updateUser() {
        UpdateUserRequest request = new UpdateUserRequest(1L, "test", "test@mail.com", LocalDate.of(2000, 1, 1));
        User expected = getStaticTestUsers().getFirst(); // Too lazy to write custom new User
        expected.accept(request);
        expected.setId(1L);

        Mockito.when(repository.findById(request.id())).thenReturn(Optional.of(expected));
        Mockito.when(repository.findByEmail(request.email())).thenReturn(Optional.of(expected));
        Mockito.when(repository.findByEmail(request.email())).thenReturn(Optional.of(expected));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        testSubject.updateUser(request);

        // Could validate that return of this method is correct, but we are trying this test with ArgumentCaptor for practice sake.
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());

        // Validates that user entity was not tampered during processing.
        User actual = captor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(request.id());
        assertThat(actual.getUsername()).isEqualTo(request.username());
        assertThat(actual.getEmail()).isEqualTo(request.email());
    }

    @Test
    void deleteUser() {
    }

}