package dev.professional_fullstack_developer.tutorial.configuration;

import dev.professional_fullstack_developer.tutorial.adapter.repository.UserJdbcTemplateRepository;
import dev.professional_fullstack_developer.tutorial.adapter.repository.UserJpaRepository;
import dev.professional_fullstack_developer.tutorial.adapter.repository.UserRepository;
import dev.professional_fullstack_developer.tutorial.adapter.repository.UserStaticRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {

    @Bean(name = "userRepositoryBean")
    @Profile("!static")
    public UserRepository userRepository(UserJpaRepository repository) {
        return repository;
    }

    @Bean(name = "userRepositoryBean")
    @Profile("static")
    public UserRepository userStaticRepository() {
        return new UserStaticRepository();
    }

    @Bean(name = "userJdbcTemplateRepositoryBean")
    public UserRepository userJdbcTemplateRepository(JdbcTemplate template) {
        return new UserJdbcTemplateRepository(template);
    }

}
