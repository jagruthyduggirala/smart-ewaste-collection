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

public class MyRequestsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        try {

            HttpSession session = request.getSession(false);

            if (session == null ||
                session.getAttribute("user_id") == null) {

                out.println("<h2>Please login first!</h2>");
                out.println("<a href='frontend/login.html'>Login</a>");
                return;
            }

            int userId =
                (Integer) session.getAttribute("user_id");

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM ewaste_requests " +
                         "WHERE user_id=? " +
                         "ORDER BY request_date DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>My Requests</title>");

            out.println("<style>");
            out.println("body{font-family:Arial;background:#f4f8f5;}");
            out.println("h1{text-align:center;color:#1b5e20;}");
            out.println(".container{width:70%;margin:30px auto;}");
            out.println(".card{background:white;padding:20px;margin:15px 0;" +
                        "border-radius:10px;" +
                        "box-shadow:0 3px 10px rgba(0,0,0,0.1);}");
            out.println("</style>");

            out.println("</head>");
            out.println("<body>");

            out.println("<h1>My E-Waste Requests</h1>");
            out.println("<div class='container'>");

            boolean found = false;

            while (rs.next()) {

                found = true;

                out.println("<div class='card'>");

                out.println("<h3>Request ID: " +
                        rs.getInt("request_id") + "</h3>");

                out.println("<p><b>Item:</b> " +
                        rs.getString("item_name") + "</p>");

                out.println("<p><b>Category:</b> " +
                        rs.getString("category") + "</p>");

                out.println("<p><b>Quantity:</b> " +
                        rs.getInt("quantity") + "</p>");

                out.println("<p><b>Location:</b> " +
                        rs.getString("location") + "</p>");

                out.println("<p><b>Status:</b> " +
                        rs.getString("status") + "</p>");

                out.println("<p><b>Submitted:</b> " +
                        rs.getTimestamp("request_date") + "</p>");

                out.println("</div>");
            }

            if (!found) {

                out.println("<div class='card'>");
                out.println("<h3>No requests found.</h3>");
                out.println("<p>Submit an e-waste request to see it here.</p>");
                out.println("</div>");
            }

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Error loading requests</h2>");
        }
    }
}