package hrms.service;

import hrms.model.User;
import hrms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    // LOGIN
    public User login(String empCode, String rawPassword) {
        Optional<User> userOpt = userRepo.findByEmpCodeAndStatus(empCode, "ACTIVE");
        if (userOpt.isEmpty()) return null;

        User user = userOpt.get();
        String stored = user.getPassword();

        boolean matches;
        if (stored.startsWith("$2a$") || stored.startsWith("$2b$")) {
            matches = encoder.matches(rawPassword, stored);
        } else {
            matches = stored.equals(rawPassword);
        }

        return matches ? user : null;
    }

    // REGISTER
    public String register(String empCode, String empName, String email,
                           String rawPassword, String department,
                           String designation, String location, String mobileNo) {

        if (userRepo.existsByEmpCode(empCode))
            return "Employee code already exists";
        if (userRepo.existsByEmail(email))
            return "Email already registered";

        User user = new User();
        user.setEmpCode(empCode);
        user.setEmpName(empName);
        user.setEmail(email);
        user.setPassword(encoder.encode(rawPassword));
        user.setDepartment(department);
        user.setDesignation(designation);
        user.setLocation(location);
        user.setMobileNo(mobileNo);
        user.setRoleId(1);
        user.setStatus("PENDING");

        userRepo.save(user);
        return "SUCCESS";
    }

    // ADMIN: pending registrations
    public List<User> getPendingRegistrations() {
        return userRepo.findByStatus("PENDING");
    }

    // ADMIN: approve
    public boolean approveRegistration(int userId) {
        Optional<User> opt = userRepo.findById(userId);
        if (opt.isEmpty()) return false;
        User user = opt.get();
        if (!user.getStatus().equals("PENDING")) return false;
        user.setStatus("ACTIVE");
        userRepo.save(user);
        return true;
    }

    // ADMIN: reject
    public boolean rejectRegistration(int userId) {
        Optional<User> opt = userRepo.findById(userId);
        if (opt.isEmpty()) return false;
        User user = opt.get();
        user.setStatus("REJECTED");
        userRepo.save(user);
        return true;
    }

    // get all users
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}