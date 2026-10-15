import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM ewaste_requests " +
                         "WHERE status = 'Pending' " +
                         "ORDER BY request_date DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Pending Requests</title>");
            out.println("</head>");

            out.println("<body style='font-family:Arial; background:#f4f8f5;'>");

            out.println("<h1 style='text-align:center; color:#1b5e20;'>");
            out.println(" Pending E-Waste Requests");
            out.println("</h1>");

            boolean found = false;

            while (rs.next()) {

                found = true;

                out.println("<div style='background:white; width:500px; margin:20px auto; padding:25px; border-radius:12px; box-shadow:0 3px 12px rgba(0,0,0,0.1);'>");

                out.println("<h2>" +
                        rs.getString("item_name") +
                        "</h2>");

                out.println("<p><b>Request ID:</b> " +
                        rs.getInt("request_id") +
                        "</p>");

                out.println("<p><b>Category:</b> " +
                        rs.getString("category") +
                        "</p>");

                out.println("<p><b>Quantity:</b> " +
                        rs.getInt("quantity") +
                        "</p>");

                out.println("<p><b>Location:</b> " +
                        rs.getString("location") +
                        "</p>");

                out.println("<p><b>Status:</b> " +
                        rs.getString("status") +
                        "</p>");

                out.println("<form action='updateStatus' method='post' " +
                        "onsubmit=\"return confirm('Are you sure you want to mark this request as collected?');\">");

                out.println("<input type='hidden' name='request_id' value='" +
                        rs.getInt("request_id") + "'>");

                out.println("<button type='submit' " +
                    "style='background:#2e7d32;color:white;border:none;" +
                    "padding:10px 18px;border-radius:6px;cursor:pointer;'>" +
                    " Mark as Collected" +
                    "</button>");

                out.println("</form>");

                out.println("<p><b>Submitted:</b> " +
                        rs.getTimestamp("request_date") +
                        "</p>");

                out.println("</div>");
            }

            if (!found) {
                out.println("<h3 style='text-align:center;'>");
                out.println("No pending requests found.");
                out.println("</h3>");
            }

            out.println("</body>");
            out.println("</html>");

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Error loading requests</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }
}