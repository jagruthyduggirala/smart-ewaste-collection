import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM users WHERE email=? AND password=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                HttpSession session = request.getSession();

                session.setAttribute(
                    "user_id",
                    rs.getInt("user_id")
                );

                session.setAttribute(
                    "user_name",
                    rs.getString("name")
                );

                response.sendRedirect("frontend/user-dashboard.html");

            } else {

                out.println("<h2>Invalid email or password!</h2>");
                out.println("<a href='frontend/login.html'>Try Again</a>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Login Failed!</h2>");
        }
    }
}