package com.taskagile.domain.model.common.mail;

import com.taskagile.domain.common.mail.DefaultMailManager;
import com.taskagile.domain.common.mail.Mailer;
import com.taskagile.domain.common.mail.Message;
import com.taskagile.domain.common.mail.MessageVariable;
import freemarker.template.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class DefaultMailManagerTest {

    @TestConfiguration
    static class DefaultMessageCreatorConfiguration {
        @Bean
        public FreeMarkerConfigurationFactoryBean getFreemarkerConfiguration() {
            FreeMarkerConfigurationFactoryBean factoryBean = new FreeMarkerConfigurationFactoryBean();
            factoryBean.setTemplateLoaderPath("/mail-templates/");
            return factoryBean;
        }
    }

    @Autowired
    private Configuration configuration;
    private Mailer mailerMock;
    private DefaultMailManager instance;

    @BeforeEach
    public void setUp() {
        mailerMock = mock(Mailer.class);
        instance = new DefaultMailManager("noreply@taskagile.com", mailerMock, configuration);
    }

    @Test
    public void send_nullEmailAddress_shouldFail() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> instance.send(null, "Test subject", "test.ftl"));
    }

    @Test
    public void send_emptyEmailAddress_shouldFail() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> instance.send("", "Test subject", "test.ftl"));
    }

    @Test
    public void send_nullSubject_shouldFail() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> instance.send("test@taskagile.com", null, "test.ftl"));
    }

    @Test
    public void send_emptySubject_shouldFail() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> instance.send("test@taskagile.com", "", "test.ftl"));
    }

    @Test
    public void send_nullTemplateName_shouldFail() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> instance.send("test@taskagile.com", "Test subject", null));
    }

    @Test
    public void send_emptyTemplateName_shouldFail() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> instance.send("test@taskagile.com", "Test subject", ""));
    }

    @Test
    public void send_validParameters_shouldSucceed() {
        String to = "user@example.com";
        String subject = "Test subject";
        String templateName = "test.ftl";

        instance.send(to, subject, templateName, MessageVariable.from("name", "test"));
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(mailerMock).send(messageArgumentCaptor.capture());

        Message messageSent = messageArgumentCaptor.getValue();
        assertEquals(to, messageSent.getTo());
        assertEquals(subject, messageSent.getSubject());
        assertEquals("noreply@taskagile.com", messageSent.getFrom());
        assertEquals("Hello, test\n", messageSent.getBody());
    }

}
