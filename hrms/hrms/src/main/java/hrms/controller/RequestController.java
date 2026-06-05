package hrms.controller;

import hrms.model.HardwareRequest;
import hrms.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class RequestController {

    @Autowired
    private RequestService requestService;

    // GET /api/requests/my?userId=1
    @GetMapping("/my")
    public ResponseEntity<List<HardwareRequest>> getMyRequests(
            @RequestParam int userId) {
        return ResponseEntity.ok(requestService.getMyRequests(userId));
    }

    // POST /api/requests/create
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createRequest(
            @RequestBody Map<String, Object> body) {

        int    userId        = (int) body.get("userId");
        int    hardwareId    = (int) body.get("hardwareId");
        int    quantity      = (int) body.get("quantity");
        String justification = (String) body.get("justification");
        String priority      = (String) body.get("priority");
        LocalDate reqDate    = LocalDate.parse((String) body.get("requiredDate"));

        HardwareRequest req = requestService.createRequest(
                userId, hardwareId, quantity, justification, priority, reqDate
        );

        Map<String, Object> res = new HashMap<>();
        res.put("success",   true);
        res.put("requestNo", req.getRequestNo());
        res.put("message",   "Request created successfully");
        return ResponseEntity.ok(res);
    }

    // POST /api/requests/submit
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitRequest(
            @RequestBody Map<String, Object> body) {

        String requestNo = (String) body.get("requestNo");
        int    userId    = (int) body.get("userId");
        boolean ok       = requestService.submitRequest(requestNo, userId);

        Map<String, Object> res = new HashMap<>();
        res.put("success", ok);
        res.put("message", ok ? "Request submitted!" : "Could not submit request");
        return ResponseEntity.ok(res);
    }

    // GET /api/requests/pending
    @GetMapping("/pending")
    public ResponseEntity<List<HardwareRequest>> getPending() {
        return ResponseEntity.ok(requestService.getPendingApprovals());
    }

    // POST /api/requests/approve
    @PostMapping("/approve")
    public ResponseEntity<Map<String, Object>> approve(
            @RequestBody Map<String, Object> body) {

        String requestNo = (String) body.get("requestNo");
        String gmName    = (String) body.get("gmName");
        String remarks   = (String) body.get("remarks");
        boolean ok       = requestService.approveRequest(requestNo, gmName, remarks);

        Map<String, Object> res = new HashMap<>();
        res.put("success", ok);
        res.put("message", ok ? "Approved!" : "Could not approve");
        return ResponseEntity.ok(res);
    }

    // POST /api/requests/reject
    @PostMapping("/reject")
    public ResponseEntity<Map<String, Object>> reject(
            @RequestBody Map<String, Object> body) {

        String requestNo = (String) body.get("requestNo");
        String gmName    = (String) body.get("gmName");
        boolean ok       = requestService.rejectRequest(requestNo, gmName);

        Map<String, Object> res = new HashMap<>();
        res.put("success", ok);
        res.put("message", ok ? "Rejected" : "Could not reject");
        return ResponseEntity.ok(res);
    }

    // GET /api/requests/approved
    @GetMapping("/approved")
    public ResponseEntity<List<HardwareRequest>> getApproved() {
        return ResponseEntity.ok(requestService.getApprovedRequests());
    }

    // POST /api/requests/process
    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> process(
            @RequestBody Map<String, Object> body) {

        String requestNo = (String) body.get("requestNo");
        boolean ok       = requestService.processRequest(requestNo);

        Map<String, Object> res = new HashMap<>();
        res.put("success", ok);
        res.put("message", ok ? "Marked as processed" : "Could not process");
        return ResponseEntity.ok(res);
    }
}