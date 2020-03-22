package com.taskagile.infrastructure.repository;

import com.taskagile.domain.model.user.User;
import com.taskagile.domain.model.user.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class HibernateUserRepositoryTest {

    @TestConfiguration
    public static class UserRepositoryTestContextConfiguration {
        @Bean
        public UserRepository userRepository(EntityManager entityManager) {
            return new HibernateUserRepository(entityManager);
        }
    }

    @Autowired
    private UserRepository repository;

    @Test
    public void save_nullUsernameUser_shouldFail() {
        User inavlidUser = User.create(null, "sunny@taskagile.com", "MyPassword!");
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> repository.save(inavlidUser));
    }

    @Test
    public void save_nullEmailAddressUser_shouldFail() {
        User inavlidUser = User.create("sunny", null, "MyPassword!");
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> repository.save(inavlidUser));
    }

    @Test
    public void save_nullPasswordUser_shouldFail() {
        User inavlidUser = User.create("sunny", "sunny@taskagile.com", null);
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> repository.save(inavlidUser));
    }

    @Test
    public void save_validUser_shouldSuccess() {
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        User newUser = User.create(username, emailAddress, "MyPassword!");
        repository.save(newUser);
        assertNotNull(newUser.getId(), "New user's id should be generated");
        assertNotNull(newUser.getCreatedDate(), "New user's created date should be generated");
        assertEquals(username, newUser.getUsername());
        assertEquals(emailAddress, newUser.getEmailAddress());
        assertEquals("", newUser.getFirstName());
        assertEquals("", newUser.getLastName());
    }

    @Test
    public void save_usernameAlreadyExist_shouldFail() {
        // Create already exist user
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        User alreadyExist = User.create(username, emailAddress, "MyPassword!");
        repository.save(alreadyExist);

        try {
            User newUser = User.create(username, "new@taskagile.com", "MyPassword!");
            repository.save(newUser);
        } catch (Exception e) {
            assertEquals(ConstraintViolationException.class.toString(), e.getCause().getClass().toString());
        }
    }

    @Test
    public void save_emailAddressAlreadyExist_shouldFail() {
        // Create already exist user
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        User alreadyExist = User.create(username, emailAddress, "MyPassword!");
        repository.save(alreadyExist);

        try {
            User newUser = User.create("new", emailAddress, "MyPassword!");
            repository.save(newUser);
        } catch (Exception e) {
            assertEquals(ConstraintViolationException.class.toString(), e.getCause().getClass().toString());
        }
    }

    @Test
    public void findByEmailAddress_notExist_shouldReturnEmptyResult() {
        String emailAddress = "sunny@taskagile.com";
        User user = repository.findByEmailAddress(emailAddress);
        assertNull(user, "No user should by found");
    }

    @Test
    public void findByEmailAddress_exist_shouldReturnResult() {
        String emailAddress = "sunny@taskagile.com";
        String username = "sunny";
        User newUser = User.create(username, emailAddress, "MyPassword!");
        repository.save(newUser);
        User found = repository.findByEmailAddress(emailAddress);
        assertEquals(username, found.getUsername(), "Username should match");
    }

    @Test
    public void findByUsername_notExist_shouldReturnEmptyResult() {
        String username = "sunny";
        User user = repository.findByUsername(username);
        assertNull(user, "No user should by found");
    }

    @Test
    public void findByUsername_exist_shouldReturnResult() {
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        User newUser = User.create(username, emailAddress, "MyPassword!");
        repository.save(newUser);
        User found = repository.findByUsername(username);
        assertEquals(emailAddress, found.getEmailAddress(), "Email address should match");
    }
}
