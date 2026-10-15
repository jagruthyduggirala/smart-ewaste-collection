public class TestDB {

    public static void main(String[] args) {

        if (DBConnection.getConnection() != null) {
            System.out.println("SUCCESS! MySQL connection is working.");
        } else {
            System.out.println("Connection failed.");
        }
    }
}