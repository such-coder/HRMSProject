import java.util.Scanner;

public class MenuHandler {

    private RequestManager manager;
    private Scanner        scanner;
    private User           currentUser;

    public MenuHandler() {
        manager = new RequestManager();
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║  IOCL Hardware Request Management System     ║");
        System.out.println("║  Indian Oil Corporation Ltd.                 ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        System.out.println("\nDemo Logins:");
        System.out.println("  Employee      : EMP001 / pass123");
        System.out.println("  IS GM         : GM001  / pass123");
        System.out.println("  Processing    : PT001  / pass123");
        System.out.println("  Admin         : ADM001 / admin123");

        login();

        if (currentUser == null) {
            System.out.println("Too many failed attempts. Exiting.");
            return;
        }

        switch (currentUser.getRole()) {
            case EMPLOYEE:        employeeMenu();   break;
            case IS_GM:           gmMenu();         break;
            case PROCESSING_TEAM: processingMenu(); break;
            case ADMIN:           adminMenu();      break;
        }
    }

    private void login() {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("\nEmployee Code : ");
            String empCode  = scanner.nextLine().trim();
            System.out.print("Password      : ");
            String password = scanner.nextLine().trim();

            currentUser = manager.login(empCode, password);
            if (currentUser != null) {
                System.out.println("\n✔ Welcome, " + currentUser.getEmpName() +
                                   "! Role: " + currentUser.getRole());
                return;
            }
            attempts++;
            System.out.println("✘ Invalid credentials. Attempts left: " + (3 - attempts));
        }
        currentUser = null;
    }

    private void employeeMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n--- EMPLOYEE MENU ---");
            System.out.println("1. Create New Request");
            System.out.println("2. Submit a Draft Request");
            System.out.println("3. View My Requests");
            System.out.println("4. Search Request by No.");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = readInt();
            switch (choice) {
                case 1: handleCreateRequest(); break;
                case 2: handleSubmitRequest(); break;
                case 3: manager.viewMyRequests(currentUser); break;
                case 4: handleSearch(); break;
                case 0: System.out.println("Logged out."); break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void handleCreateRequest() {
        System.out.println("\n--- New Hardware Request ---");
        System.out.println("Available Hardware:");
        int i = 1;
        for (String[] hw : manager.getHardwareMaster()) {
            System.out.println("  " + i++ + ". " + hw[1] + " (" + hw[2] + ")");
        }
        System.out.print("Select Hardware (number): ");
        int hwChoice = readInt();
        System.out.print("Quantity      : ");
        int qty = readInt();
        System.out.print("Justification : ");
        String justification = scanner.nextLine().trim();
        System.out.print("Priority (HIGH / MEDIUM / LOW): ");
        String priority = scanner.nextLine().trim().toUpperCase();
        System.out.print("Required By (YYYY-MM-DD)       : ");
        String reqDate = scanner.nextLine().trim();
        manager.createRequest(currentUser, hwChoice, qty, justification, priority, reqDate);
    }

    private void handleSubmitRequest() {
        System.out.print("Enter Request No. to submit (e.g. REQ-1001): ");
        String reqNo = scanner.nextLine().trim();
        manager.submitRequest(reqNo, currentUser);
    }

    private void gmMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n--- IS GM MENU ---");
            System.out.println("1. View Pending Approvals");
            System.out.println("2. Approve a Request");
            System.out.println("3. Reject a Request");
            System.out.println("4. Search Request");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = readInt();
            switch (choice) {
                case 1: manager.viewPendingApprovals(); break;
                case 2: handleApprove(); break;
                case 3: handleReject();  break;
                case 4: handleSearch();  break;
                case 0: System.out.println("Logged out."); break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void handleApprove() {
        System.out.print("Enter Request No. to approve: ");
        String reqNo   = scanner.nextLine().trim();
        System.out.print("Remarks (optional)          : ");
        String remarks = scanner.nextLine().trim();
        manager.approveRequest(reqNo, currentUser, remarks);
    }

    private void handleReject() {
        System.out.print("Enter Request No. to reject : ");
        String reqNo   = scanner.nextLine().trim();
        System.out.print("Reason for rejection        : ");
        String remarks = scanner.nextLine().trim();
        manager.rejectRequest(reqNo, currentUser, remarks);
    }

    private void processingMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n--- PROCESSING TEAM MENU ---");
            System.out.println("1. View Approved Requests");
            System.out.println("2. Mark Request as Processed");
            System.out.println("3. Close a Processed Request");
            System.out.println("4. Search Request");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = readInt();
            switch (choice) {
                case 1: manager.viewApprovedRequests(); break;
                case 2: handleProcess(); break;
                case 3: handleClose();   break;
                case 4: handleSearch();  break;
                case 0: System.out.println("Logged out."); break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void handleProcess() {
        System.out.print("Enter Request No. to process: ");
        String reqNo   = scanner.nextLine().trim();
        System.out.print("Asset Tag                   : ");
        String assetTag = scanner.nextLine().trim();
        System.out.print("Serial Number               : ");
        String serialNo = scanner.nextLine().trim();
        manager.processRequest(reqNo, currentUser, assetTag, serialNo);
    }

    private void handleClose() {
        System.out.print("Enter Request No. to close: ");
        String reqNo = scanner.nextLine().trim();
        manager.closeRequest(reqNo, currentUser);
    }

    private void adminMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Generate Report");
            System.out.println("2. View Audit Log");
            System.out.println("3. View All Users");
            System.out.println("4. Search Request");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = readInt();
            switch (choice) {
                case 1: manager.generateReport(); break;
                case 2: manager.viewAuditLog();   break;
                case 3: manager.viewAllUsers();   break;
                case 4: handleSearch();           break;
                case 0: System.out.println("Logged out."); break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void handleSearch() {
        System.out.print("Enter Request No. (e.g. REQ-1001): ");
        String reqNo = scanner.nextLine().trim();
        manager.searchByRequestNo(reqNo);
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return -1;
        }
    }
}