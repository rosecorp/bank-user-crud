package co.uk.bank.usercrud.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    String FILTER_USER_ON_FIRST_NAME_AND_LAST_NAME_QUERY = "select u from User u where UPPER(u.firstName) like CONCAT('%',UPPER(?1),'%') and UPPER(u.lastName) like CONCAT('%',UPPER(?2),'%') and UPPER(u.id) like CONCAT('%',UPPER(?3),'%')";

    @Query(FILTER_USER_ON_FIRST_NAME_AND_LAST_NAME_QUERY)
    List<User> findByFirstNameLikeAndLastNameLikeOrId(String firstNameFilter, String lastNameFilter, String idFilter);

    @Query(FILTER_USER_ON_FIRST_NAME_AND_LAST_NAME_QUERY)
    Page<User> findByFirstNameLikeAndLastNameLikeOrId(String firstNameFilter, String lastNameFilter, String idFilter, Pageable pageable);

}
