package com.taskagile.domain.application.commands;

import com.taskagile.domain.model.user.UserId;

public class CreateTeamCommand extends UserCommand {

    private String name;

    public CreateTeamCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
