package testtask.exptracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testtask.exptracker.domain.Role;
import testtask.exptracker.domain.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User getOne(Long id);

    List<User> findByUsernameContaining(String filter);

    User findByUsernameAndIdNot(String username, Long id);

    List<User> findByRolesNotContaining(Role admin);

    List<User> findByUsernameContainingAndRolesNotContaining(String filter, Role admin);
}
