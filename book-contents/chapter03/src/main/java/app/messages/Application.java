package app.messages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
//        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//        MessageService messageService = context.getBean(MessageService.class);
//        messageService.save("Hello, Spring!");
        SpringApplication.run(Application.class);
    }
}
