package co.uk.bank.usercrud.user;

import co.uk.bank.usercrud.TestFixtures;
import co.uk.bank.usercrud.user.dto.UserRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindUser() {
        UUID id = UUID.randomUUID();
        UserRequestDto u = new UserRequestDto(UserTitle.DR, "John", "Silver", TestFixtures.parseDate("2000-02-02"), "Nurse");
        List<User> userList = List.of(TestFixtures.user(id, UserTitle.DR, "John", "Silver"));

        when(userRepository.findByFirstNameLikeAndLastNameLikeOrId(null, null, id.toString())).thenReturn(userList);

        List<User> users = userService.fetchFilteredUserDataAsList(null, null, id);

        assertEquals(1, users.size());
        verify(userRepository, times(1)).findByFirstNameLikeAndLastNameLikeOrId(null, null, id.toString());
    }

}