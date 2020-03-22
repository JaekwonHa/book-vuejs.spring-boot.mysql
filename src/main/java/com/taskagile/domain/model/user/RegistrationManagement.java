package com.taskagile.domain.model.user;

import com.taskagile.domain.model.common.security.PasswordEncryptor;
import org.springframework.stereotype.Component;

@Component
public class RegistrationManagement {

    private UserRepository repository;
    private PasswordEncryptor passwordEncryptor;

    public RegistrationManagement(UserRepository repository, PasswordEncryptor passwordEncryptor) {
        this.repository = repository;
        this.passwordEncryptor = passwordEncryptor;
    }

    public User register(String username, String emailAddress, String password) throws RegistrationException {
        User existingUser = repository.findByUsername(username);
        if (existingUser != null) {
            throw new UsernameExistsException();
        }

        existingUser = repository.findByEmailAddress(emailAddress);
        if (existingUser != null) {
            throw new EmailAddressExistsException();
        }

        String encrytedPassword = passwordEncryptor.encrypt(password);
        User newUser = User.create(username, emailAddress.toLowerCase(), encrytedPassword);
        repository.save(newUser);
        return newUser;
    }
}
