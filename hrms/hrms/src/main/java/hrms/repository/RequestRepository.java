package hrms.repository;

import hrms.model.HardwareRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<HardwareRequest, Integer> {

    // All requests by a specific user
    List<HardwareRequest> findByRequestedByOrderByCreatedOnDesc(int userId);

    // All requests with status SUBMITTED (pending approval)
    List<HardwareRequest> findByStatus(String status);

    // Find by request number e.g. REQ-1001
    Optional<HardwareRequest> findByRequestNo(String requestNo);
}