package com.taskagile.domain.application.impl;

import com.taskagile.domain.application.commands.RegistrationCommand;
import com.taskagile.domain.common.event.DomainEventPublisher;
import com.taskagile.domain.common.mail.MailManager;
import com.taskagile.domain.common.mail.MessageVariable;
import com.taskagile.domain.model.user.*;
import com.taskagile.domain.model.user.events.UserRegisteredEvent;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTests {

    private RegistrationManagement registrationManagementMock;
    private DomainEventPublisher eventPublisherMock;
    private MailManager mailManagerMock;
    private UserServiceImpl instance;
    private UserRepository userRepositoryMock;

    @BeforeEach
    public void setUp() {
        registrationManagementMock = mock(RegistrationManagement.class);
        eventPublisherMock = mock(DomainEventPublisher.class);
        mailManagerMock = mock(MailManager.class);
        userRepositoryMock = mock(UserRepository.class);
        instance = new UserServiceImpl(registrationManagementMock, eventPublisherMock, mailManagerMock, userRepositoryMock);
    }

    //-------------------------------------------
    // Method loadUserByUsername()
    //-------------------------------------------

    @Test
    public void loadUserByUsername_emptyUsername_shouldFail() {
//        Exception exception = null;
//        try {
//            instance.loadUserByUsername("");
//        } catch (Exception e) {
//            exception = e;
//        }
//        assertNotNull(exception);
//        assertTrue(exception instanceof UsernameNotFoundException);
        assertThatExceptionOfType(UsernameNotFoundException.class).isThrownBy(() -> {
            instance.loadUserByUsername("");
        });
        verify(userRepositoryMock, never()).findByUsername("");
        verify(userRepositoryMock, never()).findByEmailAddress("");
    }

    @Test
    public void loadUserByUsername_notExistsUsername_shouldFail() {
        String notExistUsername = "NotExistUsername";
        when(userRepositoryMock.findByUsername(notExistUsername)).thenReturn(null);
        //        Exception exception = null;
//        try {
//            instance.loadUserByUsername(notExistUsername);
//        } catch (Exception e) {
//            exception = e;
//        }
//        assertNotNull(exception);
//        assertTrue(exception instanceof UsernameNotFoundException);
        assertThatExceptionOfType(UsernameNotFoundException.class).isThrownBy(() -> {
            instance.loadUserByUsername(notExistUsername);
        });
        verify(userRepositoryMock).findByUsername(notExistUsername);
        verify(userRepositoryMock, never()).findByEmailAddress(notExistUsername);
    }

    @Test
    public void loadUserByUsername_existUsername_shouldSucceed() throws IllegalAccessException {
        String existUsername = "ExistUsername";
        User foundUser = User.create(existUsername, "user@taskagile.com", "Test", "User", "EncryptedPassword!");
        foundUser.updateName("Test", "User");

        // Found user from the database should have id. And since no setter of
        // id is available in User, we have to write the value to it using reflection
        //
        // Besides creating an actual instance of User, we can also create a user
        // mock, like the following.
        // User mockUser = Mockito.mock(User.class);
        // when(mockUser.getUsername()).thenReturn(existUsername);
        // when(mockUser.getPassword()).thenReturn("EncryptedPassword!");
        // when(mockUser.getId()).thenReturn(1L);

        FieldUtils.writeField(foundUser, "id", 1L, true);
        when(userRepositoryMock.findByUsername(existUsername)).thenReturn(foundUser);

        Exception exception = null;
        UserDetails userDetails = null;
        try {
            userDetails = instance.loadUserByUsername(existUsername);
        } catch (Exception e) {
            exception = e;
        }
        assertNull(exception);
        verify(userRepositoryMock).findByUsername(existUsername);
        verify(userRepositoryMock, never()).findByEmailAddress(existUsername);
        assertNotNull(userDetails);
        assertEquals(existUsername, userDetails.getUsername());
        assertTrue(userDetails instanceof SimpleUser);
    }

    //-------------------------------------------
    // Method register()
    //-------------------------------------------

    @Test
    public void register_nullCommand_shouldFail() throws RegistrationException {
        assertThatThrownBy(() -> {
            instance.register(null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void register_existingUsername_shouldFail() throws RegistrationException {
        String username = "sunny";
        String emailAddress = "existing@taskagile.com";
        String password = "MyPassword!";
        String firstName = "Sunny";
        String lastName = "Hu";

        doThrow(EmailAddressExistsException.class).when(registrationManagementMock)
            .register(username, emailAddress, firstName, lastName, password);

        RegistrationCommand command = new RegistrationCommand(username, emailAddress, firstName, lastName, password);

        assertThatThrownBy(() -> {
            instance.register(command);
        }).isInstanceOf(RegistrationException.class);
    }

    @Test
    public void register_validCommand_shouldSucceed() throws RegistrationException {
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        String password = "MyPassword!";
        String firstName = "Sunny";
        String lastName = "Hu";
        User newUser = User.create(username, emailAddress, firstName, lastName, password);
        when(registrationManagementMock.register(username, emailAddress, firstName, lastName, password))
            .thenReturn(newUser);

        RegistrationCommand command = new RegistrationCommand(username, emailAddress, firstName, lastName, password);

        instance.register(command);

        verify(mailManagerMock).send(
            emailAddress,
            "Welcome to TaskAgile",
            "welcome.ftl",
            MessageVariable.from("user", newUser)
        );

        verify(eventPublisherMock).publish(new UserRegisteredEvent(this, newUser));
    }

}
