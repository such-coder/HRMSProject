import java.time.LocalDateTime;

public class ApprovalHistory {
    private int    historyId;
    private int    requestId;
    private String actionBy;    // emp code of who acted
    private String actionType;  // APPROVED / REJECTED / PROCESSED / CLOSED
    private String remarks;
    private String actionDate;

    public ApprovalHistory(int historyId, int requestId,
                           String actionBy, String actionType, String remarks) {
        this.historyId  = historyId;
        this.requestId  = requestId;
        this.actionBy   = actionBy;
        this.actionType = actionType;
        this.remarks    = remarks;
        this.actionDate = LocalDateTime.now().toString();
    }

    @Override
    public String toString() {
        return "[" + actionDate + "] " + actionType +
               " by " + actionBy +
               (remarks != null && !remarks.isEmpty() ? " — Remarks: " + remarks : "");
    }

    public int    getRequestId() { return requestId; }
    public String getActionType(){ return actionType; }
}