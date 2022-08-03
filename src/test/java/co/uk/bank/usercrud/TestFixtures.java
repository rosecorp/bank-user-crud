package co.uk.bank.usercrud;

import co.uk.bank.usercrud.user.User;
import co.uk.bank.usercrud.user.UserTitle;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestFixtures {
    public static LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    public static User user(UUID uuid, UserTitle title, String firstName, String lastName) {
        User u =new User();
        u.setId(uuid);
        u.setTitle(title);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setJobTitle("Nurse");
        u.setDateOfBirth(parseDate("1982-03-03"));
        u.setDeleted(Boolean.FALSE);
        u.setCreatedStamp(new Date());

        return u;
    }

    public static List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection);
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        return sorts;
    }
}
