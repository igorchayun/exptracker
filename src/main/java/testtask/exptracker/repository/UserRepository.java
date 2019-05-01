package testtask.exptracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testtask.exptracker.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
