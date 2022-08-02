package co.uk.bank.usercrud;

import co.uk.bank.usercrud.user.User;
import co.uk.bank.usercrud.user.UserTitle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
        u.setCreatedStamp(new Date());

        return u;
    }
}
