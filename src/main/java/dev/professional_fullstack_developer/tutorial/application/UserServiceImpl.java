package dev.professional_fullstack_developer.tutorial.application;

import dev.professional_fullstack_developer.tutorial.adapter.repository.UserRepository;
import dev.professional_fullstack_developer.tutorial.domain.dto.CreateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.dto.UpdateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import dev.professional_fullstack_developer.tutorial.domain.exception.BadRequestException;
import dev.professional_fullstack_developer.tutorial.domain.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(@Qualifier("userRepositoryBean") UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserById(long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public User getUserByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(NotFoundException::new);
    }

    @Override
    public User getUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(NotFoundException::new);
    }

    @Override
    public User createUser(CreateUserRequest userDto) {
        if (repository.existsByUsername(userDto.username())) {
            throw new BadRequestException("Username already exists");
        }
        if (repository.existsByEmail(userDto.email())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.from(userDto);
        return repository.save(user);
    }

    @Override
    public User updateUser(UpdateUserRequest userDto) {
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
        return repository.save(user);
    }

    @Override
    public User deleteUser(long id) {
        User deletedUser = repository.findById(id)
                .orElseThrow(NotFoundException::new);

        repository.delete(deletedUser);
        return deletedUser;
    }

}
