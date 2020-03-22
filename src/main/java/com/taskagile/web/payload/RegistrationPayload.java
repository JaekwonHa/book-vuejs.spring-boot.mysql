package com.taskagile.web.payload;

import com.taskagile.domain.application.commands.RegistrationCommand;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegistrationPayload {

    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    @NotNull
    private String username;

    @Email(message = "Email address should be valid")
    @Size(max = 100, message = "Email address must not be more than 100 characters")
    @NotNull
    private String emailAddress;

    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    @NotNull
    private String password;

    public RegistrationPayload(@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters") @NotNull String username, @Email(message = "Email address should be valid") @Size(max = 100, message = "Email address must not be more than 100 characters") @NotNull String emailAddress, @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters") @NotNull String password) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public RegistrationCommand toCommand() {
        return new RegistrationCommand(this.username, this.emailAddress, this.password);
    }

    public String getUsername() {
        return username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

}
