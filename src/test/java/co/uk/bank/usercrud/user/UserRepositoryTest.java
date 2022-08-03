package co.uk.bank.usercrud.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static co.uk.bank.usercrud.TestFixtures.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateReadDelete() {
        User user1 = user(UUID.randomUUID(), UserTitle.MR, "Jake", "Black");
        User user2 = user(UUID.randomUUID(), UserTitle.MR, "Sally", "Black");
        userRepository.save(user1);
        userRepository.save(user2);

        Iterable<User> users = userRepository.findByFirstNameLikeAndLastNameLikeOrId("Jake", "", "");
        Assertions.assertThat(users).extracting(User::getFirstName).containsOnly("Jake");

        userRepository.deleteAll();
        Assertions.assertThat(userRepository.findAll()).isEmpty();

    }

    @Test
    public void testSearchNotFound() {
        User user1 = user(UUID.randomUUID(), UserTitle.MR, "Jake", "Black");
        User user2 = user(UUID.randomUUID(), UserTitle.MR, "Sally", "Black");
        userRepository.save(user1);
        userRepository.save(user2);

        Iterable<User> users = userRepository.findByFirstNameLikeAndLastNameLikeOrId("Danny", "", "");
        Assertions.assertThat(users).isEmpty();

        userRepository.deleteAll();
        Assertions.assertThat(userRepository.findAll()).isEmpty();

    }

    @Test
    public void testUpdate() {
        User user1 = user(UUID.randomUUID(), UserTitle.MR, "Jake", "Black");
        userRepository.save(user1);

        Iterable<User> users = userRepository.findByFirstNameLikeAndLastNameLikeOrId("Jake", "", "");
        Assertions.assertThat(users).extracting(User::getFirstName).containsOnly("Jake");

        User user = users.iterator().next();

        user.setFirstName("Danny");
        userRepository.save(user);

        List<User> foundUsers = userRepository.findAll();
        Assertions.assertThat(foundUsers).extracting(User::getFirstName).containsOnly("Danny");

        userRepository.deleteAll();
        Assertions.assertThat(userRepository.findAll()).isEmpty();

    }

    @Test
    public void testSearchPaged() {
        User user1 = user(UUID.randomUUID(), UserTitle.MR, "Jake", "Black");
        User user2 = user(UUID.randomUUID(), UserTitle.MR, "Jake", "Black");
        User user3 = user(UUID.randomUUID(), UserTitle.MR, "Jake", "Black");
        User user4 = user(UUID.randomUUID(), UserTitle.MR, "Jake", "Black");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        Pageable pageable = PageRequest.of(2, 2, Sort.by(createSortOrder(new ArrayList<>(), null)));

        Page<User> pagedUsers = userRepository.findByFirstNameLikeAndLastNameLikeOrId("Jake", "", "", pageable);

        Assertions.assertThat(pagedUsers.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(pagedUsers.getTotalElements()).isEqualTo(4);

        userRepository.deleteAll();
        Assertions.assertThat(userRepository.findAll()).isEmpty();

    }

}