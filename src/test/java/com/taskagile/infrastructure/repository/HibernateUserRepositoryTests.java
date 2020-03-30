package com.taskagile.infrastructure.repository;

import com.taskagile.domain.model.user.User;
import com.taskagile.domain.model.user.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
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
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class HibernateUserRepositoryTests {

    @TestConfiguration
    public static class UserRepositoryTestContextConfiguration {
        @Bean
        public UserRepository userRepository(EntityManager entityManager) {
            return new HibernateUserRepository(entityManager);
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    public void save_nullUsernameUser_shouldFail() {
        User inavlidUser = User.create(null, "sunny@taskagile.com", "Sunny", "Hu", "MyPassword!");
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> userRepository.save(inavlidUser));
    }

    @Test
    public void save_nullEmailAddressUser_shouldFail() {
        User inavlidUser = User.create("sunny", null, "Sunny", "Hu", "MyPassword!");
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> userRepository.save(inavlidUser));
    }

    @Test
    public void save_nullPasswordUser_shouldFail() {
        User inavlidUser = User.create("sunny", "sunny@taskagile.com", "Sunny", "Hu", null);
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> userRepository.save(inavlidUser));
    }

    @Test
    public void save_validUser_shouldSuccess() {
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        String firstName = "Sunny";
        String lastName = "Hu";
        User newUser = User.create(username, emailAddress, firstName, lastName, "MyPassword!");

        userRepository.save(newUser);
        assertNotNull(newUser.getId(), "New user's id should be generated");
        assertNotNull(newUser.getCreatedDate(), "New user's created date should be generated");
        assertEquals(username, newUser.getUsername());
        assertEquals(emailAddress, newUser.getEmailAddress());
        assertEquals(firstName, newUser.getFirstName());
        assertEquals(lastName, newUser.getLastName());
    }

    @Test
    public void save_usernameAlreadyExist_shouldFail() {
        // Create already exist user
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        User alreadyExist = User.create(username, emailAddress, "Sunny", "Hu", "MyPassword!");
        userRepository.save(alreadyExist);

        try {
            User newUser = User.create(username, "new@taskagile.com", "Sunny", "Hu", "MyPassword!");
            userRepository.save(newUser);
        } catch (Exception e) {
            assertEquals(ConstraintViolationException.class.toString(), e.getCause().getClass().toString());
        }
    }

    @Test
    public void save_emailAddressAlreadyExist_shouldFail() {
        // Create already exist user
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        User alreadyExist = User.create(username, emailAddress, "Sunny", "Hu", "MyPassword!");
        userRepository.save(alreadyExist);

        try {
            User newUser = User.create("new", emailAddress, "Sunny", "Hu", "MyPassword!");
            userRepository.save(newUser);
        } catch (Exception e) {
            assertEquals(ConstraintViolationException.class.toString(), e.getCause().getClass().toString());
        }
    }

    @Test
    public void findByEmailAddress_notExist_shouldReturnEmptyResult() {
        String emailAddress = "sunny@taskagile.com";
        User user = userRepository.findByEmailAddress(emailAddress);
        assertNull(user, "No user should by found");
    }

    @Test
    public void findByEmailAddress_exist_shouldReturnResult() {
        String emailAddress = "sunny@taskagile.com";
        String username = "sunny";
        User newUser = User.create(username, emailAddress, "Sunny", "Hu", "MyPassword!");
        userRepository.save(newUser);
        User found = userRepository.findByEmailAddress(emailAddress);
        assertEquals(username, found.getUsername(), "Username should match");
    }

    @Test
    public void findByUsername_notExist_shouldReturnEmptyResult() {
        String username = "sunny";
        User user = userRepository.findByUsername(username);
        assertNull(user, "No user should by found");
    }

    @Test
    public void findByUsername_exist_shouldReturnResult() {
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        User newUser = User.create(username, emailAddress, "Sunny", "Hu", "MyPassword!");
        userRepository.save(newUser);
        User found = userRepository.findByUsername(username);
        assertEquals(emailAddress, found.getEmailAddress(), "Email address should match");
    }
}
