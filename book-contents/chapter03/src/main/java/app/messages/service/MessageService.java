package app.messages.service;

import app.messages.hibernate.Message;
import app.messages.hibernate.MessageRepository;
import app.messages.security.SecurityCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MessageService {
    private MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @SecurityCheck
    @Transactional(noRollbackFor = {UnsupportedOperationException.class})
    public Message save(String text) {
        Message message = repository.saveMessage(new Message(text));
        logger.debug("New message[id={}] saved", message.getId());
//        updateStatistics();
        return message;
    }

    private void updateStatistics() {
        throw new UnsupportedOperationException("This method is not implemented yet");
    }

    @Transactional(readOnly = true)
    public List<Message> getMessages() {
        return repository.getMessages();
    }
}
