import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateStatusServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String requestId = request.getParameter("request_id");

        response.setContentType("text/html");

        try {

            Connection con = DBConnection.getConnection();

            String sql = "UPDATE ewaste_requests " +
                         "SET status='Collected' " +
                         "WHERE request_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(requestId));

            int result = ps.executeUpdate();

            ps.close();
            con.close();

            if (result > 0) {

                response.getWriter().println(
                    "<html>" +
                    "<head>" +
                    "<title>Collection Successful</title>" +
                    "</head>" +
                    "<body style='font-family:Arial;text-align:center;" +
                    "background:#f4f8f5;padding-top:100px;'>" +

                    "<script>" +
                    "alert(' E-waste request collected successfully!');" +
                    "window.location.href='requests';" +
                    "</script>" +

                    "<h2> Request Collected Successfully!</h2>" +
                    "<p>The request status has been updated to Collected.</p>" +

                    "</body>" +
                    "</html>"
                );

            } else {

                response.getWriter().println(
                    "<h2>Request not found!</h2>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "<h2>Error updating request</h2>"
            );
        }
    }
}