package hrms.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "REQUEST_MASTER")
public class HardwareRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private int requestId;

    @Column(name = "request_no")
    private String requestNo;

    @Column(name = "requested_by")
    private int requestedBy;

    @Column(name = "hardware_id")
    private int hardwareId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "justification")
    private String justification;

    @Column(name = "priority")
    private String priority;

    @Column(name = "required_date")
    private LocalDate requiredDate;

    @Column(name = "status")
    private String status;

    @Column(name = "attachment_path")
    private String attachmentPath;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_date")
    private LocalDate approvedDate;

    // Getters
    public int         getRequestId()    { return requestId; }
    public String      getRequestNo()    { return requestNo; }
    public int         getRequestedBy()  { return requestedBy; }
    public int         getHardwareId()   { return hardwareId; }
    public int         getQuantity()     { return quantity; }
    public String      getJustification(){ return justification; }
    public String      getPriority()     { return priority; }
    public LocalDate   getRequiredDate() { return requiredDate; }
    public String      getStatus()       { return status; }
    public LocalDateTime getCreatedOn()  { return createdOn; }
    public String      getApprovedBy()   { return approvedBy; }

    // Setters (only what we need to update)
    public void setRequestNo(String v)    { this.requestNo = v; }
    public void setRequestedBy(int v)     { this.requestedBy = v; }
    public void setHardwareId(int v)      { this.hardwareId = v; }
    public void setQuantity(int v)        { this.quantity = v; }
    public void setJustification(String v){ this.justification = v; }
    public void setPriority(String v)     { this.priority = v; }
    public void setRequiredDate(LocalDate v){ this.requiredDate = v; }
    public void setStatus(String v)       { this.status = v; }
    public void setCreatedOn(LocalDateTime v){ this.createdOn = v; }
    public void setUpdatedOn(LocalDateTime v){ this.updatedOn = v; }
    public void setApprovedBy(String v)   { this.approvedBy = v; }
    public void setApprovedDate(LocalDate v){ this.approvedDate = v; }
    public void setAttachmentPath(String v){ this.attachmentPath = v; }
}