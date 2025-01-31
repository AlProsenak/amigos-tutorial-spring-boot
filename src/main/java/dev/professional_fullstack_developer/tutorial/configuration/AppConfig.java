package dev.professional_fullstack_developer.tutorial.configuration;

import dev.professional_fullstack_developer.tutorial.adapter.repository.UserJpaRepository;
import dev.professional_fullstack_developer.tutorial.adapter.repository.UserRepository;
import dev.professional_fullstack_developer.tutorial.adapter.repository.UserStaticRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class AppConfig {

    @Bean
    @Primary
    @Profile("!static")
    public UserRepository userRepository(UserJpaRepository repository) {
        return repository;
    }

    @Bean
    @Profile("static")
    public UserRepository userStaticRepository() {
        return new UserStaticRepository();
    }

}
