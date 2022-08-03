package co.uk.bank.usercrud.user;

import co.uk.bank.usercrud.TestFixtures;
import co.uk.bank.usercrud.user.dto.UserDtoAssembler;
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

    @Mock
    private UserDtoAssembler userDtoAssembler;

    @Test
    public void testFindUser() {
        UUID id = UUID.randomUUID();
        List<User> userList = List.of(TestFixtures.user(id, UserTitle.DR, "John", "Silver"));

        when(userRepository.findByFirstNameLikeAndLastNameLikeOrId(null, null, id.toString())).thenReturn(userList);

        List<User> users = userService.fetchFilteredUserDataAsList(null, null, id);

        assertEquals(1, users.size());
        verify(userRepository, times(1)).findByFirstNameLikeAndLastNameLikeOrId(null, null, id.toString());
    }

    @Test
    public void testCreateUser() {
        UUID id = UUID.randomUUID();
        UserRequestDto userRequestDto = new UserRequestDto(UserTitle.DR, "John", "Silver", "2000-02-02", "Nurse");
        User user = TestFixtures.user(id, UserTitle.DR, "John", "Silver");

        when(userDtoAssembler.toUser(userRequestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.saveUser(userRequestDto);

        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser() {
        UUID id = UUID.randomUUID();
        UserRequestDto userRequestDto = new UserRequestDto(UserTitle.DR, "John", "Silver", "2000-02-02", "Nurse");
        User user = TestFixtures.user(id, UserTitle.DR, "John", "Silver");

        when(userDtoAssembler.toUser(user, userRequestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.updateUser(user, userRequestDto);

        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        UUID id = UUID.randomUUID();

        userService.deleteUser(id);
        verify(userRepository, times(1)).deleteById(id);
    }

}