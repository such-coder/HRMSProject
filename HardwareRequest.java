import java.time.LocalDate;

public class HardwareRequest {

    
    public static final String DRAFT       = "DRAFT";
    public static final String SUBMITTED   = "SUBMITTED";
    public static final String APPROVED    = "APPROVED";
    public static final String REJECTED    = "REJECTED";
    public static final String PROCESSED   = "PROCESSED";
    public static final String CLOSED      = "CLOSED";

    private int    requestId;
    private String requestNo;      
    private String empCode;        // who raised it
    private String empName;
    private String department;
    private String designation;
    private int    hardwareId;
    private String hardwareName;
    private int    quantity;
    private String justification;
    private String priority;       // HIGH / MEDIUM / LOW
    private String requiredDate;
    private String attachmentPath;
    private String status;
    private String createdOn;
    private String approvedBy;
    private String approvedDate;
    private String remarks;        // approval/rejection remarks

    public HardwareRequest(int requestId, String empCode, String empName,
                           String department, String designation,
                           int hardwareId, String hardwareName,
                           int quantity, String justification,
                           String priority, String requiredDate) {
        this.requestId     = requestId;
        this.requestNo     = "REQ-" + requestId;
        this.empCode       = empCode;
        this.empName       = empName;
        this.department    = department;
        this.designation   = designation;
        this.hardwareId    = hardwareId;
        this.hardwareName  = hardwareName;
        this.quantity      = quantity;
        this.justification = justification;
        this.priority      = priority;
        this.requiredDate  = requiredDate;
        this.status        = DRAFT;
        this.createdOn     = LocalDate.now().toString();
    }

    // Getters
    public int    getRequestId()    { return requestId; }
    public String getRequestNo()    { return requestNo; }
    public String getEmpCode()      { return empCode; }
    public String getEmpName()      { return empName; }
    public String getDepartment()   { return department; }
    public String getHardwareName() { return hardwareName; }
    public int    getQuantity()     { return quantity; }
    public String getJustification(){ return justification; }
    public String getPriority()     { return priority; }
    public String getRequiredDate() { return requiredDate; }
    public String getStatus()       { return status; }
    public String getCreatedOn()    { return createdOn; }
    public String getApprovedBy()   { return approvedBy; }
    public String getRemarks()      { return remarks; }

    // Setters for status updates
    public void setStatus(String status)       { this.status = status; }
    public void setApprovedBy(String name)     { this.approvedBy = name; }
    public void setApprovedDate(String date)   { this.approvedDate = date; }
    public void setRemarks(String remarks)     { this.remarks = remarks; }
    public void setAttachmentPath(String path) { this.attachmentPath = path; }

    @Override
    public String toString() {
        return "\n====================================" +
               "\nRequest No    : " + requestNo +
               "\nEmployee      : " + empName + " (" + empCode + ")" +
               "\nDepartment    : " + department +
               "\nHardware      : " + hardwareName +
               "\nQuantity      : " + quantity +
               "\nPriority      : " + priority +
               "\nRequired By   : " + requiredDate +
               "\nJustification : " + justification +
               "\nStatus        : " + status +
               (approvedBy != null ? "\nApproved By   : " + approvedBy : "") +
               (remarks    != null ? "\nRemarks       : " + remarks    : "") +
               "\nCreated On    : " + createdOn +
               "\n====================================";
    }
}