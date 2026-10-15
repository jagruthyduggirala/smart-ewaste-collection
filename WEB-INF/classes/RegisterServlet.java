import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String location = request.getParameter("location");

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO users " +
                         "(name, email, password, phone, location) " +
                         "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, phone);
            ps.setString(5, location);

            int result = ps.executeUpdate();

            if (result > 0) {
                out.println("<h2>Registration Successful!</h2>");
                out.println("<p>User registered successfully.</p>");
            }

            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Registration Failed!</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }
}