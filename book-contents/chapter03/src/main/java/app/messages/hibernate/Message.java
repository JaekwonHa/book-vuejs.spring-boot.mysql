package app.messages.hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "text", nullable = false, length = 128)
    private String text;

    @Column(name = "created_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdDate;

    public Message() {
    }

    public Message(String text) {
        this.text = text;
        this.createdDate = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return this.text;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;

    }
}
