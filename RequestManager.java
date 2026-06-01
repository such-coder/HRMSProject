import java.util.ArrayList;
import java.sql.*;
import java.time.LocalDate;

public class RequestManager {

    private ArrayList<HardwareRequest> requests;
    private ArrayList<ApprovalHistory> history;

    private int nextRequestId = 1001;
    private int nextHistoryId = 1;

    public RequestManager() {
        requests = new ArrayList<>();
        history  = new ArrayList<>();
        loadSampleHardware();
    }

    private ArrayList<String[]> hardwareMaster = new ArrayList<>();

    private void loadSampleHardware() {
        hardwareMaster.add(new String[]{"1", "Laptop",       "Computing"});
        hardwareMaster.add(new String[]{"2", "Desktop PC",   "Computing"});
        hardwareMaster.add(new String[]{"3", "iPad/Tablet",  "Mobile"});
        hardwareMaster.add(new String[]{"4", "Pendrive",     "Storage"});
        hardwareMaster.add(new String[]{"5", "External HDD", "Storage"});
        hardwareMaster.add(new String[]{"6", "VC Setup",     "Conference"});
        hardwareMaster.add(new String[]{"7", "Accessories",  "Peripherals"});
    }

    public User login(String empCode, String password) {
        return loginFromDatabase(empCode, password);
    }

    public User loginFromDatabase(String empCode, String password) {
        String sql =
            "SELECT u.*, r.role_name " +
            "FROM USER_MASTER u " +
            "JOIN ROLE_MASTER r ON u.role_id = r.role_id " +
            "WHERE u.emp_code=? AND u.password=? AND u.status='ACTIVE'";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, empCode);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("emp_code"),
                    rs.getString("emp_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("designation"),
                    rs.getString("department"),
                    Role.valueOf(rs.getString("role_name")),
                    rs.getString("location"),
                    rs.getString("mobile_no")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createRequest(User emp, int hardwareChoice, int quantity,
                              String justification, String priority, String requiredDate) {
        try {
            String requestNo = "REQ-" + System.currentTimeMillis();
            String sql =
                "INSERT INTO REQUEST_MASTER " +
                "(request_no, requested_by, hardware_id, quantity, " +
                "justification, priority, required_date, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, requestNo);
            ps.setInt(2,    emp.getUserId());
            ps.setInt(3,    hardwareChoice);
            ps.setInt(4,    quantity);
            ps.setString(5, justification);
            ps.setString(6, priority);
            ps.setString(7, requiredDate);
            ps.setString(8, "DRAFT");
            ps.executeUpdate();

            System.out.println("Request Created Successfully");
            System.out.println("Request Number : " + requestNo);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submitRequest(String requestNo, User emp) {
        HardwareRequest req = findByRequestNo(requestNo);
        if (req == null) { System.out.println("✘ Request not found."); return; }
        if (!req.getEmpCode().equals(emp.getEmpCode())) {
            System.out.println("✘ You can only submit your own requests.");
            return;
        }
        if (!req.getStatus().equals(HardwareRequest.DRAFT)) {
            System.out.println("✘ Only DRAFT requests can be submitted.");
            return;
        }
        req.setStatus(HardwareRequest.SUBMITTED);
        System.out.println("\n✔ Request " + requestNo + " submitted successfully!");
        System.out.println("  IS GM will be notified for approval.");
    }

    public void viewMyRequests(User emp) {
        System.out.println("VIEW MY REQUESTS METHOD CALLED");
        RequestDAO dao = new RequestDAO();
        dao.viewMyRequests(emp.getUserId());
        System.out.println("DEBUG userId = " + emp.getUserId());
    }

    public void viewPendingApprovals() {
        System.out.println("\n===== PENDING APPROVALS =====");
        boolean found = false;
        for (HardwareRequest r : requests) {
            if (r.getStatus().equals(HardwareRequest.SUBMITTED)) {
                System.out.println(r);
                found = true;
            }
        }
        if (!found) System.out.println("No pending approvals.");
    }

    public void approveRequest(String requestNo, User gm, String remarks) {
        HardwareRequest req = findByRequestNo(requestNo);
        if (req == null) { System.out.println("✘ Request not found."); return; }
        if (!req.getStatus().equals(HardwareRequest.SUBMITTED)) {
            System.out.println("✘ Only SUBMITTED requests can be approved.");
            return;
        }
        req.setStatus(HardwareRequest.APPROVED);
        req.setApprovedBy(gm.getEmpName());
        req.setApprovedDate(LocalDate.now().toString());
        req.setRemarks(remarks);
        history.add(new ApprovalHistory(nextHistoryId++,
            req.getRequestId(), gm.getEmpCode(), "APPROVED", remarks));
        System.out.println("\n✔ Request " + requestNo + " APPROVED.");
        System.out.println("  Processing team notified.");
    }

    public void rejectRequest(String requestNo, User gm, String remarks) {
        HardwareRequest req = findByRequestNo(requestNo);
        if (req == null) { System.out.println("✘ Request not found."); return; }
        if (!req.getStatus().equals(HardwareRequest.SUBMITTED)) {
            System.out.println("✘ Only SUBMITTED requests can be rejected.");
            return;
        }
        req.setStatus(HardwareRequest.REJECTED);
        req.setApprovedBy(gm.getEmpName());
        req.setRemarks(remarks);
        history.add(new ApprovalHistory(nextHistoryId++,
            req.getRequestId(), gm.getEmpCode(), "REJECTED", remarks));
        System.out.println("\n✔ Request " + requestNo + " REJECTED.");
        System.out.println("  Employee has been notified.");
    }

    public void viewApprovedRequests() {
        System.out.println("\n===== APPROVED REQUESTS (Ready to Process) =====");
        boolean found = false;
        for (HardwareRequest r : requests) {
            if (r.getStatus().equals(HardwareRequest.APPROVED)) {
                System.out.println(r);
                found = true;
            }
        }
        if (!found) System.out.println("No approved requests pending processing.");
    }

    public void processRequest(String requestNo, User processor,
                               String assetTag, String serialNo) {
        HardwareRequest req = findByRequestNo(requestNo);
        if (req == null) { System.out.println("✘ Request not found."); return; }
        if (!req.getStatus().equals(HardwareRequest.APPROVED)) {
            System.out.println("✘ Only APPROVED requests can be processed.");
            return;
        }
        req.setStatus(HardwareRequest.PROCESSED);
        history.add(new ApprovalHistory(nextHistoryId++,
            req.getRequestId(), processor.getEmpCode(),
            "PROCESSED", "Asset Tag: " + assetTag + " | Serial: " + serialNo));
        System.out.println("\n✔ Request " + requestNo + " marked as PROCESSED.");
        System.out.println("  Asset Tag: " + assetTag + " | Serial No: " + serialNo);
    }

    public void closeRequest(String requestNo, User processor) {
        HardwareRequest req = findByRequestNo(requestNo);
        if (req == null) { System.out.println("✘ Request not found."); return; }
        if (!req.getStatus().equals(HardwareRequest.PROCESSED)) {
            System.out.println("✘ Only PROCESSED requests can be closed.");
            return;
        }
        req.setStatus(HardwareRequest.CLOSED);
        history.add(new ApprovalHistory(nextHistoryId++,
            req.getRequestId(), processor.getEmpCode(), "CLOSED", "Request closed."));
        System.out.println("\n✔ Request " + requestNo + " CLOSED. Employee notified.");
    }

    public void generateReport() {
        System.out.println("\n========== SYSTEM REPORT ==========");
        int total      = requests.size();
        long draft     = requests.stream().filter(r -> r.getStatus().equals(HardwareRequest.DRAFT)).count();
        long submitted = requests.stream().filter(r -> r.getStatus().equals(HardwareRequest.SUBMITTED)).count();
        long approved  = requests.stream().filter(r -> r.getStatus().equals(HardwareRequest.APPROVED)).count();
        long rejected  = requests.stream().filter(r -> r.getStatus().equals(HardwareRequest.REJECTED)).count();
        long processed = requests.stream().filter(r -> r.getStatus().equals(HardwareRequest.PROCESSED)).count();
        long closed    = requests.stream().filter(r -> r.getStatus().equals(HardwareRequest.CLOSED)).count();
        System.out.println("Total Requests  : " + total);
        System.out.println("Draft           : " + draft);
        System.out.println("Submitted       : " + submitted);
        System.out.println("Approved        : " + approved);
        System.out.println("Rejected        : " + rejected);
        System.out.println("Processed       : " + processed);
        System.out.println("Closed          : " + closed);
        System.out.println("====================================");
    }

    public void viewAuditLog() {
        System.out.println("\n===== AUDIT LOG =====");
        if (history.isEmpty()) { System.out.println("No actions yet."); return; }
        for (ApprovalHistory h : history) System.out.println(h);
    }

    public void viewAllUsers() {
        UserDAO dao = new UserDAO();
        dao.viewAllUsers();
    }

    public void searchByRequestNo(String requestNo) {
        HardwareRequest req = findByRequestNo(requestNo);
        if (req != null) {
            System.out.println(req);
            System.out.println("--- Approval History for this request ---");
            for (ApprovalHistory h : history) {
                if (h.getRequestId() == req.getRequestId()) System.out.println(h);
            }
        } else {
            System.out.println("✘ No request found: " + requestNo);
        }
    }

    public ArrayList<String[]> getHardwareMaster() { return hardwareMaster; }

    private HardwareRequest findByRequestNo(String requestNo) {
        for (HardwareRequest r : requests) {
            if (r.getRequestNo().equalsIgnoreCase(requestNo)) return r;
        }
        return null;
    }
}