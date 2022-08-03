package co.uk.bank.usercrud.user.dto;

import co.uk.bank.usercrud.user.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDtoAssembler {

    public User toUser(User user, UserRequestDto userRequestDto) {
        user.setTitle(userRequestDto.getTitle());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setJobTitle(userRequestDto.getJobTitle());
        user.setDateOfBirth(parseDate(userRequestDto.getDateOfBirth()));

        return user;
    }

    public User toUser(UserRequestDto userRequestDto) {
        User user = new User();
        user.setTitle(userRequestDto.getTitle());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setJobTitle(userRequestDto.getJobTitle());
        user.setDateOfBirth(parseDate(userRequestDto.getDateOfBirth()));
        return user;
    }

    public static UserResponseDto toUserResponseDto(User entity) {
        UserResponseDto userResponseDto = new UserResponseDto();
        BeanUtils.copyProperties(entity, userResponseDto);
        return userResponseDto;
    }

    public List<UserResponseDto> toUserResponseDtos(List<User> users) {
        return users.stream()
            .map( user -> new UserResponseDto(user.getId(), user.getTitle(), user.getFirstName(), user.getLastName(), user.getDateOfBirth(), user.getJobTitle(), user.getCreatedStamp()))
            .collect(Collectors.toList());
    }

    private static LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }
}
