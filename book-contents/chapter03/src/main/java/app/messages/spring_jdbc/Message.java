package app.messages.spring_jdbc;

import java.time.LocalDateTime;

public class Message {
    private Integer id;
    private String text;
    private LocalDateTime createdDate;

    public Message(Integer id, String text, LocalDateTime createdDate) {
        this.id = id;
        this.text = text;
        this.createdDate = createdDate;
    }

    public Message(String text) {
        this.text = text;
        this.createdDate = LocalDateTime.now();
    }

    public String getText() {
        return this.text;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
