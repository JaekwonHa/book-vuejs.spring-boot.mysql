package com.taskagile.domain.application.commands;

import com.taskagile.domain.model.board.BoardId;
import com.taskagile.domain.model.user.UserId;

public class AddCardListCommand extends UserCommand {

    private String name;
    private BoardId boardId;
    private int position;

    public AddCardListCommand(BoardId boardId, String name, int position) {
        this.name = name;
        this.boardId = boardId;
        this.position = position;
    }

    public BoardId getBoardId() {
        return boardId;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }
}
