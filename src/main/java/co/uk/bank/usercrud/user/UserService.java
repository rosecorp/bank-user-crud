package co.uk.bank.usercrud.user;

import co.uk.bank.usercrud.user.dto.UserRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

import static co.uk.bank.usercrud.user.dto.UserDtoAssembler.toUser;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Retryable( value = SQLException.class, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    public User saveUser(UserRequestDto userRequestDto) {
        User savedUser = userRepository.save(toUser(userRequestDto));
        logger.debug("User has been created: {}", savedUser);
        return savedUser;
    }

    @Transactional
    @Retryable( value = SQLException.class, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    public User updateUser (User user, UserRequestDto userDetails) {
        User updatedUser = userRepository.save(toUser(user, userDetails));
        logger.debug("User has been updated: {}", updatedUser);
        return updatedUser;
    }

    @Transactional
    @Retryable( value = SQLException.class, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    public void deleteUser(UUID id) {
        // soft delete user
        logger.debug("About to delete user with id: '{}'", id);
        userRepository.deleteById(id);
        logger.debug("User with id: '{}' has been deleted ", id);
    }

    public List<User> fetchFilteredUserDataAsList(String firstNameFilter, String lastNameFilter, UUID idFilter) {
        // Apply the filter for the id, firstName and lastName
        List<User> foundUsers = userRepository.findByFirstNameLikeAndLastNameLikeOrId(firstNameFilter, lastNameFilter, idFilter != null ? idFilter.toString() : "");
        logger.debug("Found total users: {}", foundUsers.size());

        return foundUsers;
    }

    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Page<User> fetchUserDataAsPageWithFilteringAndSorting(String firstNameFilter, String lastNameFilter, UUID idFilter, int page, int size, List<String> sortList, String sortOrder) {
        // create Pageable object using the page, size and sort details
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        // fetch the page object by additionally passing pageable with the filters
        Page<User> foundPagedUsers = userRepository.findByFirstNameLikeAndLastNameLikeOrId(firstNameFilter, lastNameFilter, idFilter != null ? idFilter.toString() : "", pageable);
        logger.debug("Found total users: {}", foundPagedUsers.getTotalElements());
        return foundPagedUsers;
    }

    private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
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
