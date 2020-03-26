package com.taskagile.domain.model.user;

import com.taskagile.domain.common.security.PasswordEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RegistrationManagementTest {

    private UserRepository repositoryMock;
    private PasswordEncryptor passwordEncryptorMock;
    private RegistrationManagement instance;

    @BeforeEach
    public void setUp() {
        repositoryMock = mock(UserRepository.class);
        passwordEncryptorMock = mock(PasswordEncryptor.class);
        instance = new RegistrationManagement(repositoryMock, passwordEncryptorMock);
    }

    @Test
    public void register_existedUsername_shouldFail() throws RegistrationException {
        String username = "existUsername";
        String emailAddress = "sunny@taskagile.com";
        String password = "MyPassword!";
        // We just return an empty user object to insdicate an existing user
        when(repositoryMock.findByUsername(username)).thenReturn(new User());
        assertThatExceptionOfType(UsernameExistsException.class)
            .isThrownBy(() -> instance.register(username, emailAddress, password));
    }

    @Test
    public void register_existedEmailAddress_shouldFail() throws RegistrationException {
        String username = "sunny";
        String emailAddress = "exist@taskagile.com";
        String password = "MyPassword!";
        // We just return an empty user object to indicate an existing user
        when(repositoryMock.findByEmailAddress(emailAddress)).thenReturn(new User());
        assertThatExceptionOfType(EmailAddressExistsException.class)
            .isThrownBy(() -> instance.register(username, emailAddress, password));
    }

    @Test
    public void register_uppercaseEmailAddress_shouldSucceedAndBecomeLowercase() throws RegistrationException {
        String username = "sunny";
        String emailAddress = "Sunny@TaskAgile.com";
        String password = "MyPassword!";
        instance.register(username, emailAddress, password);
        User userToSave = User.create(username, emailAddress.toLowerCase(), password);
        verify(repositoryMock).save(userToSave);
    }

    @Test
    public void register_newUser_shouldSucceed() throws RegistrationException {
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        String password = "MyPassword!";
        String encryptedPassword = "EncryptedPassword";
        User newUser = User.create(username, emailAddress, encryptedPassword);

        // Setup repository mock
        // Return null to indicate no user exists
        when(repositoryMock.findByUsername(username)).thenReturn(null);
        when(repositoryMock.findByEmailAddress(emailAddress)).thenReturn(null);
        doNothing().when(repositoryMock).save(newUser);

        // Setup passwordEncryptor mock
        when(passwordEncryptorMock.encrypt(password)).thenReturn(encryptedPassword);

        User savedUser = instance.register(username, emailAddress, password);
        InOrder inOrder = inOrder(repositoryMock);
        inOrder.verify(repositoryMock).findByUsername(username);
        inOrder.verify(repositoryMock).findByEmailAddress(emailAddress);
        inOrder.verify(repositoryMock).save(newUser);
        verify(passwordEncryptorMock).encrypt(password);
        assertEquals(encryptedPassword, savedUser.getPassword(), "Saved user's password should be encrypted");
    }

}
