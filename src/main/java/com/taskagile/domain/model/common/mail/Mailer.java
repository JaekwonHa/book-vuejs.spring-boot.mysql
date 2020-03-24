package com.taskagile.domain.model.common.mail;

public interface Mailer {
    /**
     * Send a message
     *
     * @param message the message instance
     */
    void send(Message message);
}
