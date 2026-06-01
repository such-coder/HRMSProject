import java.sql.*;

public class UserDAO {

    public void viewAllUsers() {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM USER_MASTER";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(
                    rs.getInt("user_id") + " | " +
                    rs.getString("emp_name") + " | " +
                    rs.getString("email")
                );
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testLogin(String empCode, String password) {
        String sql = "SELECT * FROM USER_MASTER WHERE emp_code=? AND password=?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, empCode);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login Success");
                System.out.println("Welcome " + rs.getString("emp_name"));
            } else {
                System.out.println("Login Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}