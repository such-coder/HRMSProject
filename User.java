public class User {
    private int userId;
    private String empCode;
    private String empName;
    private String email;
    private String password;
    private String designation;
    private String department;
    private Role role;
    private String location;
    private String mobileNo;
    private String status;       // ACTIVE / INACTIVE
    private String createdOn;

    public User(int userId, String empCode, String empName, String email,
                String password, String designation, String department,
                Role role, String location, String mobileNo) {
        this.userId      = userId;
        this.empCode     = empCode;
        this.empName     = empName;
        this.email       = email;
        this.password    = password;
        this.designation = designation;
        this.department  = department;
        this.role        = role;
        this.location    = location;
        this.mobileNo    = mobileNo;
        this.status      = "ACTIVE";
        this.createdOn   = java.time.LocalDate.now().toString();
    }

    // Getters
    public int    getUserId()     { return userId; }
    public String getEmpCode()    { return empCode; }
    public String getEmpName()    { return empName; }
    public String getEmail()      { return email; }
    public String getPassword()   { return password; }
    public String getDepartment() { return department; }
    public Role   getRole()       { return role; }
    public String getStatus()     { return status; }

    @Override
    public String toString() {
        return "[" + userId + "] " + empName + " (" + empCode + ") | "
               + role + " | " + department;
    }
}
