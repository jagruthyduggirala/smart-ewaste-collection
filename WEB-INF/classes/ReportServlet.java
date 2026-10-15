import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ReportServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String itemName = request.getParameter("item_name");
        String category = request.getParameter("category");
        String quantity = request.getParameter("quantity");
        String location = request.getParameter("location");
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");

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

            String sql = "INSERT INTO ewaste_requests " +
                         "(user_id, item_name, category, quantity, location, latitude, longitude) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setString(2, itemName);
            ps.setString(3, category);
            ps.setInt(4, Integer.parseInt(quantity));
            ps.setString(5, location);
            ps.setDouble(6, Double.parseDouble(latitude));
            ps.setDouble(7, Double.parseDouble(longitude));

            int result = ps.executeUpdate();

            if (result > 0) {

                out.println("<html>");
                out.println("<head>");
                out.println("<title>Request Submitted</title>");
                out.println("</head>");

                out.println("<body style='font-family:Arial;" +
                            "text-align:center;" +
                            "padding-top:100px;'>");

                out.println("<h2>Request Submitted Successfully!</h2>");

                out.println("<p>Your e-waste collection request has been recorded.</p>");

                out.println("<br>");

                out.println("<a href='frontend/my-requests.html'>");
                out.println("View My Requests");
                out.println("</a>");

                out.println("</body>");
                out.println("</html>");
            }

            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Request Failed!</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }
}