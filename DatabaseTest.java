public class DatabaseTest {

    public static void main(String[] args) {
        RequestManager manager = new RequestManager();
        User user = manager.loginFromDatabase("EMP001", "pass123");

        if (user != null) {
            System.out.println("Welcome " + user.getEmpName());
        } else {
            System.out.println("Login Failed");
        }
    }
}