package hrms.service;

import hrms.model.HardwareRequest;
import hrms.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepo;

    // Employee: create a new draft request
    public HardwareRequest createRequest(int userId, int hardwareId,
                                         int quantity, String justification,
                                         String priority, LocalDate requiredDate) {
        HardwareRequest req = new HardwareRequest();
        req.setRequestNo("REQ-" + System.currentTimeMillis());
        req.setRequestedBy(userId);
        req.setHardwareId(hardwareId);
        req.setQuantity(quantity);
        req.setJustification(justification);
        req.setPriority(priority);
        req.setRequiredDate(requiredDate);
        req.setStatus("DRAFT");
        req.setCreatedOn(LocalDateTime.now());
        req.setUpdatedOn(LocalDateTime.now());
        return requestRepo.save(req);
    }

    // Employee: submit a draft
    public boolean submitRequest(String requestNo, int userId) {
        Optional<HardwareRequest> opt = requestRepo.findByRequestNo(requestNo);
        if (opt.isEmpty()) return false;
        HardwareRequest req = opt.get();
        if (req.getRequestedBy() != userId) return false;
        if (!req.getStatus().equals("DRAFT")) return false;
        req.setStatus("SUBMITTED");
        req.setUpdatedOn(LocalDateTime.now());
        requestRepo.save(req);
        return true;
    }

    // Employee: view their own requests
    public List<HardwareRequest> getMyRequests(int userId) {
        return requestRepo.findByRequestedByOrderByCreatedOnDesc(userId);
    }

    // IS GM: view all pending
    public List<HardwareRequest> getPendingApprovals() {
        return requestRepo.findByStatus("SUBMITTED");
    }

    // IS GM: approve
    public boolean approveRequest(String requestNo, String gmName, String remarks) {
        Optional<HardwareRequest> opt = requestRepo.findByRequestNo(requestNo);
        if (opt.isEmpty()) return false;
        HardwareRequest req = opt.get();
        if (!req.getStatus().equals("SUBMITTED")) return false;
        req.setStatus("APPROVED");
        req.setApprovedBy(gmName);
        req.setApprovedDate(LocalDate.now());
        req.setUpdatedOn(LocalDateTime.now());
        requestRepo.save(req);
        return true;
    }

    // IS GM: reject
    public boolean rejectRequest(String requestNo, String gmName) {
        Optional<HardwareRequest> opt = requestRepo.findByRequestNo(requestNo);
        if (opt.isEmpty()) return false;
        HardwareRequest req = opt.get();
        if (!req.getStatus().equals("SUBMITTED")) return false;
        req.setStatus("REJECTED");
        req.setApprovedBy(gmName);
        req.setUpdatedOn(LocalDateTime.now());
        requestRepo.save(req);
        return true;
    }

    // Processing team: get approved requests
    public List<HardwareRequest> getApprovedRequests() {
        return requestRepo.findByStatus("APPROVED");
    }

    // Processing team: mark processed
    public boolean processRequest(String requestNo) {
        Optional<HardwareRequest> opt = requestRepo.findByRequestNo(requestNo);
        if (opt.isEmpty()) return false;
        HardwareRequest req = opt.get();
        if (!req.getStatus().equals("APPROVED")) return false;
        req.setStatus("PROCESSED");
        req.setUpdatedOn(LocalDateTime.now());
        requestRepo.save(req);
        return true;
    }
}
