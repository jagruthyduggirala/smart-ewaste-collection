import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminDashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        try {

            Connection con = DBConnection.getConnection();

            int users = getCount(con, "SELECT COUNT(*) FROM users");

            int requests = getCount(con,
                    "SELECT COUNT(*) FROM ewaste_requests");

            int pending = getCount(con,
                    "SELECT COUNT(*) FROM ewaste_requests WHERE status='Pending'");

            int collected = getCount(con,
                    "SELECT COUNT(*) FROM ewaste_requests WHERE status='Collected'");

            int collectors = getCount(con,
                    "SELECT COUNT(*) FROM collectors");

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Admin Dashboard</title>");

            out.println("<style>");

            out.println("body{font-family:Arial;background:#f4f8f5;margin:0;}");

            out.println("header{background:#1b5e20;color:white;padding:20px;text-align:center;}");

            out.println(".container{width:90%;margin:40px auto;}");

            out.println(".cards{display:flex;gap:20px;justify-content:center;flex-wrap:wrap;}");

            out.println(".card{background:white;width:200px;padding:25px;text-align:center;border-radius:12px;box-shadow:0 3px 12px rgba(0,0,0,.1);}");

            out.println(".number{font-size:35px;font-weight:bold;color:#2e7d32;}");

            out.println("table{width:100%;margin-top:40px;background:white;border-collapse:collapse;}");

            out.println("th,td{padding:12px;border:1px solid #ddd;text-align:center;}");

            out.println("th{background:#1b5e20;color:white;}");

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            out.println("<header>");
            out.println("<h1> Admin Dashboard</h1>");
            out.println("</header>");

            out.println("<div class='container'>");

            out.println("<div class='cards'>");

            out.println("<div class='card'>");
            out.println("<h3> Users</h3>");
            out.println("<div class='number'>" + users + "</div>");
            out.println("</div>");

            out.println("<div class='card'>");
            out.println("<h3> Total Requests</h3>");
            out.println("<div class='number'>" + requests + "</div>");
            out.println("</div>");

            out.println("<div class='card'>");
            out.println("<h3> Pending</h3>");
            out.println("<div class='number'>" + pending + "</div>");
            out.println("</div>");

            out.println("<div class='card'>");
            out.println("<h3> Collected</h3>");
            out.println("<div class='number'>" + collected + "</div>");
            out.println("</div>");

            out.println("<div class='card'>");
            out.println("<h3> Collectors</h3>");
            out.println("<div class='number'>" + collectors + "</div>");
            out.println("</div>");

            out.println("</div>");

            // Requests table

            out.println("<h2> Recent Requests</h2>");

            out.println("<table>");

            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Item</th>");
            out.println("<th>Category</th>");
            out.println("<th>Quantity</th>");
            out.println("<th>Location</th>");
            out.println("<th>Status</th>");
            out.println("</tr>");

            String sql = "SELECT * FROM ewaste_requests " +
                         "ORDER BY request_date DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                out.println("<tr>");

                out.println("<td>" +
                        rs.getInt("request_id") +
                        "</td>");

                out.println("<td>" +
                        rs.getString("item_name") +
                        "</td>");

                out.println("<td>" +
                        rs.getString("category") +
                        "</td>");

                out.println("<td>" +
                        rs.getInt("quantity") +
                        "</td>");

                out.println("<td>" +
                        rs.getString("location") +
                        "</td>");

                out.println("<td>" +
                        rs.getString("status") +
                        "</td>");

                out.println("</tr>");
            }

            out.println("</table>");

            out.println("</div>");

            out.println("</body>");
            out.println("</html>");

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Dashboard Error</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }

    private int getCount(Connection con, String sql)
            throws Exception {

        PreparedStatement ps = con.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        rs.next();

        int count = rs.getInt(1);

        rs.close();
        ps.close();

        return count;
    }
}