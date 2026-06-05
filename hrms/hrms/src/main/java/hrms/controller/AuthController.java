package hrms.controller;

import hrms.model.User;
import hrms.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> body) {

        String empCode  = body.get("empCode");
        String password = body.get("password");

        User user = authService.login(empCode, password);
        Map<String, Object> response = new HashMap<>();

        if (user != null) {
            String role = switch (user.getRoleId()) {
                case 1 -> "EMPLOYEE";
                case 2 -> "IS_GM";
                case 3 -> "PROCESSING_TEAM";
                case 4 -> "ADMIN";
                default -> "EMPLOYEE";
            };
            response.put("success", true);
            response.put("userId",  user.getUserId());
            response.put("empCode", user.getEmpCode());
            response.put("empName", user.getEmpName());
            response.put("role",    role);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody Map<String, String> body) {

        String result = authService.register(
                body.get("empCode"),
                body.get("empName"),
                body.get("email"),
                body.get("password"),
                body.get("department"),
                body.get("designation"),
                body.get("location"),
                body.get("mobileNo")
        );

        Map<String, Object> res = new HashMap<>();
        if (result.equals("SUCCESS")) {
            res.put("success", true);
            res.put("message", "Registration submitted! Awaiting admin approval.");
        } else {
            res.put("success", false);
            res.put("message", result);
        }
        return ResponseEntity.ok(res);
    }

    // PENDING REGISTRATIONS
    @GetMapping("/registrations/pending")
    public ResponseEntity<List<User>> getPendingRegistrations() {
        return ResponseEntity.ok(authService.getPendingRegistrations());
    }

    // APPROVE REGISTRATION
    @PostMapping("/registrations/approve")
    public ResponseEntity<Map<String, Object>> approveReg(
            @RequestBody Map<String, Integer> body) {

        boolean ok = authService.approveRegistration(body.get("userId"));
        Map<String, Object> res = new HashMap<>();
        res.put("success", ok);
        res.put("message", ok ? "User approved and activated" : "Could not approve");
        return ResponseEntity.ok(res);
    }

    // REJECT REGISTRATION
    @PostMapping("/registrations/reject")
    public ResponseEntity<Map<String, Object>> rejectReg(
            @RequestBody Map<String, Integer> body) {

        boolean ok = authService.rejectRegistration(body.get("userId"));
        Map<String, Object> res = new HashMap<>();
        res.put("success", ok);
        res.put("message", ok ? "Registration rejected" : "Could not reject");
        return ResponseEntity.ok(res);
    }

    // ALL USERS
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }
}