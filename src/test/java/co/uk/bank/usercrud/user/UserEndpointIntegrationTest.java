package co.uk.bank.usercrud.user;

import co.uk.bank.usercrud.user.dto.UserResponseDto;
import co.uk.bank.usercrud.user.dto.UserUpdateRequestDto;
import co.uk.bank.usercrud.user.dto.UserSearchDto;
import org.junit.jupiter.api.BeforeEach;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserEndpointIntegrationTest {
    @Autowired
    private UserEndpoint userEndpoint;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser() {

        //given:
        @Valid UserUpdateRequestDto user = new UserUpdateRequestDto(UserTitle.MR, "Jacob", "Black", "2000-02-02", "IT dude");

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
        //given:
        @Valid UserUpdateRequestDto user = new UserUpdateRequestDto(UserTitle.MR, "Jacob", "Black", "2000-02-02", "IT dude");

        //when:
        Map<String, UUID> createdUser = userEndpoint.createUser(user);

        //then:
        userExistsInRepository(createdUser);

        //and:
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setTitle(UserTitle.DR);
        userUpdateRequestDto.setFirstName("Betty");
        userUpdateRequestDto.setLastName("Black");
        userUpdateRequestDto.setDateOfBirth("2000-02-02");
        userUpdateRequestDto.setJobTitle("Nurse");
        userEndpoint.updateUser(createdUser.get("id"), userUpdateRequestDto);

        //then:
        UserSearchDto search = new UserSearchDto();
        search.setFirstName("Betty");
        List<UserResponseDto> userResponseDtos = userEndpoint.fetchUsersAsFilteredList(search);

        assertThat(userResponseDtos).isNotEmpty();
    }

    @Test
    public void testFetchUser() {
        //given:
        @Valid UserUpdateRequestDto user = new UserUpdateRequestDto(UserTitle.MR, "Jacob", "Black", "2000-02-02", "IT dude");

        //when:
        Map<String, UUID> createdUser = userEndpoint.createUser(user);

        //then:
        userExistsInRepository(createdUser);

        //and:
        UserSearchDto userSearchDto = new UserSearchDto();
        userSearchDto.setFirstName("Ja");

        List<UserResponseDto> userResponseDtos = userEndpoint.fetchUsersAsFilteredList(userSearchDto);

        //then:
        assertThat(userResponseDtos).first()
                .isNotNull()
                .hasFieldOrPropertyWithValue("firstName", "Jacob");
    }

    @Test
    public void testDeleteUser() {
        //given:
        @Valid UserUpdateRequestDto user = new UserUpdateRequestDto(UserTitle.MR, "Jacob", "Black", "2000-02-02", "IT dude");

        //when:
        Map<String, UUID> createdUser = userEndpoint.createUser(user);

        //then:
        userExistsInRepository(createdUser);

        //and:
        userEndpoint.deleteUser(createdUser.get("id"));

        //then:
        assertThat(userRepository.findById(createdUser.get("id"))).isEmpty();
    }

    private void userExistsInRepository(Map<String, UUID> createdUser) {
        //when:
        List<User> allUsers = userRepository.findAll();

        //then:
        assertThat(allUsers).first()
                .hasFieldOrPropertyWithValue("id", createdUser.get("id"))
                .hasFieldOrPropertyWithValue("firstName", "Jacob")
                .hasFieldOrPropertyWithValue("lastName", "Black")
                .hasFieldOrPropertyWithValue("jobTitle", "IT dude")
                .hasFieldOrPropertyWithValue("dateOfBirth", parseDate("2000-02-02"))
                .hasFieldOrPropertyWithValue("deleted", false);
    }

}