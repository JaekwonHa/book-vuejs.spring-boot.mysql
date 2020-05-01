package com.taskagile.web.payload;

import com.taskagile.domain.application.commands.CreateTeamCommand;
import com.taskagile.domain.model.user.UserId;

public class CreateTeamPayload {

    private String name;

    public CreateTeamCommand toCommand() {
        return new CreateTeamCommand(name);
    }

    public void setName(String name) {
        this.name = name;
    }
}
