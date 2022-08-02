package co.uk.bank.usercrud.user;

import co.uk.bank.usercrud.user.dto.UserRequestDto;
import co.uk.bank.usercrud.user.dto.UserSearchDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static co.uk.bank.usercrud.TestFixtures.parseDate;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserEndpointIntegrationTest {
    @Autowired
    private UserEndpoint userEndpoint;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {

        //given:
        @Valid UserRequestDto user = new UserRequestDto(UserTitle.MR, "Jacob", "Black", parseDate("2000-02-02"), "IT dude");

        //when:
        Map<String, UUID> createdUser = userEndpoint.createUser(user);

        //then:
        userExistsInRepository(createdUser);

        //and:
        userEndpoint.deleteUser(createdUser.get("id"));

        //then:
        UserSearchDto userSearchDto = new UserSearchDto();
        userSearchDto.setId(createdUser.get("id"));

        assertThrows(ResourceNotFoundException.class, () -> userEndpoint.fetchUsersAsFilteredList(userSearchDto));

    }

    @Test
    public void testUpdateUser() {


    }



    private void userExistsInRepository(Map<String, UUID> createdUser) {
        //when:
        List<User> allUsers = userRepository.findAll();

        //then:
        Assertions.assertThat(allUsers).first()
                .hasFieldOrPropertyWithValue("id", createdUser.get("id"))
                .hasFieldOrPropertyWithValue("firstName", "Jacob")
                .hasFieldOrPropertyWithValue("lastName", "Black")
                .hasFieldOrPropertyWithValue("jobTitle", "IT dude")
                .hasFieldOrPropertyWithValue("dateOfBirth", parseDate("2000-02-02"))
                .hasFieldOrPropertyWithValue("deleted", false);
    }

}