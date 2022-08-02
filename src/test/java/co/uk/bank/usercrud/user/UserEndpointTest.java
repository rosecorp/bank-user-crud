package co.uk.bank.usercrud.user;

import co.uk.bank.usercrud.TestFixtures;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import co.uk.bank.usercrud.user.dto.UserSearchDto;
import co.uk.bank.usercrud.user.dto.UserUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockService;

    @Test
    public void shouldFindUser() throws Exception {
        User user = TestFixtures.user(UUID.randomUUID(), UserTitle.DR, "Jake", "Man");
        when(mockService.fetchFilteredUserDataAsList(user.getFirstName(), "", null)).thenReturn(List.of(user));

        UserSearchDto criteria = new UserSearchDto();
        criteria.setFirstName("Jake");

        this.mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(asJsonString(criteria)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"lastName\":\"Man\"")))
                .andExpect(content().string(containsString("\"firstName\":\"Jake\"")));
    }

    @Test
    public void shouldFailValidationOnSearch() throws Exception {
        UserUpdateRequestDto updateRequestDto = new UserUpdateRequestDto();
        updateRequestDto.setDateOfBirth("1990-03-03");

        this.mockMvc.perform(post("/api/v1/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(updateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName", is("First name is mandatory")))
                .andExpect(jsonPath("$.lastName", is("Last name is mandatory")))
                .andExpect(jsonPath("$.jobTitle", is("Job title is mandatory")))
                .andExpect(jsonPath("$.title", is("User title is mandatory")));
    }

    @Test
    public void shouldSearchWithCriteria() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID());
        UserUpdateRequestDto updateRequestDto = new UserUpdateRequestDto(UserTitle.MISS, "Betty", "Clark", "2000-01-02","IT Dev");
        when(mockService.saveUser(any())).thenReturn(user);

        this.mockMvc.perform(post("/api/v1/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(updateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId())));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID());
        when(mockService.findUserById(any())).thenReturn(Optional.of(user));

        this.mockMvc.perform(delete("/api/v1/user/{id}", "ba5c9ff7-4c32-44b6-9eed-9d99e81c5c43"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("deleted", is(Boolean.TRUE)));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
