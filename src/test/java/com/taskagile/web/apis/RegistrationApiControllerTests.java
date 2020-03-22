package com.taskagile.web.apis;

import com.taskagile.config.SecurityConfiguration;
import com.taskagile.domain.application.UserService;
import com.taskagile.domain.model.user.EmailAddressExistsException;
import com.taskagile.domain.model.user.UsernameExistsException;
import com.taskagile.utils.JsonUtils;
import com.taskagile.web.payload.RegistrationPayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SecurityConfiguration.class, RegistrationApiController.class })
@WebMvcTest(RegistrationApiController.class)
@ActiveProfiles("test")
class RegistrationApiControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService serviceMock;

    @Test
    public void register_blackPayload_shouldFailAndReturn400() throws Exception {
        mockMvc.perform(post("/api/registrations")).andExpect(status().is(400));
    }

    @Test
    public void register_existsUsername_shouldFailAndReturn400() throws Exception {
        RegistrationPayload payload = new RegistrationPayload("exist", "test@tasksgile.com", "MyPassword!");

        doThrow(UsernameExistsException.class)
            .when(serviceMock)
            .register(payload.toCommand());

        mockMvc.perform(
            post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(payload)))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.message").value("Username already exists"));
    }

    @Test
    public void register_existsEmail_shouldFailAndReturn400() throws Exception {
        RegistrationPayload payload = new RegistrationPayload("userName", "exists@tasksgile.com", "MyPassword!");

        doThrow(EmailAddressExistsException.class)
            .when(serviceMock)
            .register(payload.toCommand());

        mockMvc.perform(
            post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(payload)))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.message").value("Email address already exists"));
    }

    @Test
    public void register_validPayload_shouldSucceedAndReturn201() throws Exception {
        RegistrationPayload payload = new RegistrationPayload("sunny", "sunny@tasksgile.com", "MyPassword!");

        doNothing().when(serviceMock)
            .register(payload.toCommand());

        mockMvc.perform(
            post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(payload)))
            .andExpect(status().is(201));
    }
}
