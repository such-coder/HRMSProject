package hrms.repository;

import hrms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmpCodeAndStatus(String empCode, String status);
    boolean existsByEmpCode(String empCode);
    boolean existsByEmail(String email);
    List<User> findByStatus(String status);
}