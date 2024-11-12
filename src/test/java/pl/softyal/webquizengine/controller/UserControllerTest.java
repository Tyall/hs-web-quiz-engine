package pl.softyal.webquizengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.softyal.webquizengine.users.model.RegistrationRequest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/register creates user and returns OK")
    void register_returnSuccess() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("correct@email.com");
        request.setPassword("correct_password");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/register using invalid email returns BAD REQUEST")
    void register_returnBadRequestValidationError_InvalidEmail() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("incorrect@emailcom");
        request.setPassword("correct_password");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/register using invalid password returns BAD REQUEST")
    void register_returnBadRequestValidationError_InvalidPassword() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("correct@email.com");
        request.setPassword("pwd");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}