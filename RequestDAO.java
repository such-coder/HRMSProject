import java.sql.*;

public class RequestDAO {

    public void viewMyRequests(int userId) {
        String sql =
            "SELECT request_no, hardware_id, quantity, priority, status, required_date, created_on " +
            "FROM REQUEST_MASTER WHERE requested_by=? ORDER BY created_on DESC";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== MY REQUESTS =====");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getString("request_no") + " | " +
                    rs.getInt("hardware_id") + " | Qty: " +
                    rs.getInt("quantity") + " | " +
                    rs.getString("priority") + " | " +
                    rs.getString("status")
                );
            }
            if (!found) System.out.println("No requests found.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}