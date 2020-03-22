package com.taskagile.domain.application.impl;

import com.taskagile.domain.application.commands.RegistrationCommand;
import com.taskagile.domain.model.common.event.DomainEventPublisher;
import com.taskagile.domain.model.common.mail.MailManager;
import com.taskagile.domain.model.common.mail.MessageVariable;
import com.taskagile.domain.model.user.EmailAddressExistsException;
import com.taskagile.domain.model.user.RegistrationException;
import com.taskagile.domain.model.user.RegistrationManagement;
import com.taskagile.domain.model.user.User;
import com.taskagile.domain.model.user.events.UserRegisteredEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceImplTests {

    private RegistrationManagement registrationManagementMock;
    private DomainEventPublisher eventPublisherMock;
    private MailManager mailManagerMock;
    private UserServiceImpl instance;

    @BeforeEach
    public void setUp() {
        registrationManagementMock = mock(RegistrationManagement.class);
        eventPublisherMock = mock(DomainEventPublisher.class);
        mailManagerMock = mock(MailManager.class);
        instance = new UserServiceImpl(registrationManagementMock, eventPublisherMock, mailManagerMock);
    }

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
        doThrow(EmailAddressExistsException.class).when(registrationManagementMock)
            .register(username, emailAddress, password);

        RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);

        assertThatThrownBy(() -> {
            instance.register(command);
        }).isInstanceOf(RegistrationException.class);
    }

    @Test
    public void register_validCommand_shouldSucceed() throws RegistrationException {
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        String password = "MyPassword!";
        User newUser = User.create(username, emailAddress, password);

        when(registrationManagementMock.register(username, emailAddress, password))
            .thenReturn(newUser);
        RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);

        instance.register(command);

        verify(mailManagerMock).send(
            emailAddress,
            "Welcome to TaskAgile",
            "welcome.ftl",
            MessageVariable.from("user", newUser)
        );

        verify(eventPublisherMock).publish(new UserRegisteredEvent(newUser));
    }

}
