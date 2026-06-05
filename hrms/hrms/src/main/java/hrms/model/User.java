package hrms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "USER_MASTER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "emp_code")
    private String empCode;

    @Column(name = "emp_name")
    private String empName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "designation")
    private String designation;

    @Column(name = "department")
    private String department;

    @Column(name = "role_id")
    private int roleId;

    @Column(name = "location")
    private String location;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "status")
    private String status;

    // Getters
    public int    getUserId()     { return userId; }
    public String getEmpCode()    { return empCode; }
    public String getEmpName()    { return empName; }
    public String getEmail()      { return email; }
    public String getPassword()   { return password; }
    public String getDepartment() { return department; }
    public String getDesignation(){ return designation; }
    public String getLocation()   { return location; }
    public String getMobileNo()   { return mobileNo; }
    public String getStatus()     { return status; }
    public int    getRoleId()     { return roleId; }

    public void setEmpCode(String v)    { this.empCode = v; }
    public void setEmpName(String v)    { this.empName = v; }
    public void setEmail(String v)      { this.email = v; }
    public void setPassword(String v)   { this.password = v; }
    public void setDepartment(String v) { this.department = v; }
    public void setDesignation(String v){ this.designation = v; }
    public void setLocation(String v)   { this.location = v; }
    public void setMobileNo(String v)   { this.mobileNo = v; }
    public void setRoleId(int v)        { this.roleId = v; }
    public void setStatus(String v)     { this.status = v; }
}
