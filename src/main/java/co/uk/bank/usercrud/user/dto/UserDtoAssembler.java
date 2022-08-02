package co.uk.bank.usercrud.user.dto;

import co.uk.bank.usercrud.user.User;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserDtoAssembler {

    public static User toUser(User user, UserRequestDto userRequestDto) {
        BeanUtils.copyProperties(userRequestDto, user);
        return user;
    }

    public static User toUser(UserRequestDto userRequestDto) {
        User user = new User();
        BeanUtils.copyProperties(userRequestDto, user);
        return user;
    }

    public static UserResponseDto toUserResponseDto(User entity) {
        UserResponseDto userResponseDto = new UserResponseDto();
        BeanUtils.copyProperties(entity, userResponseDto);
        return userResponseDto;
    }

    public static List<UserResponseDto> toUserResponseDtos(List<User> users) {
        return users.stream()
            .map( user -> new UserResponseDto(user.getId(), user.getTitle(), user.getFirstName(), user.getLastName(), user.getDateOfBirth(), user.getJobTitle(), user.getCreatedStamp()))
            .collect(Collectors.toList());
    }
}
