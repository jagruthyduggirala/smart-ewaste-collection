import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RouteServlet extends HttpServlet {

    static class RequestPoint {
        int id;
        String item;
        String location;
        double latitude;
        double longitude;

        RequestPoint(int id, String item, String location,
                     double latitude, double longitude) {

            this.id = id;
            this.item = item;
            this.location = location;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                "SELECT request_id, item_name, location, latitude, longitude " +
                "FROM ewaste_requests " +
                "WHERE status='Pending' " +
                "AND latitude IS NOT NULL " +
                "AND longitude IS NOT NULL";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            List<RequestPoint> points = new ArrayList<>();

            while (rs.next()) {

                points.add(
                    new RequestPoint(
                        rs.getInt("request_id"),
                        rs.getString("item_name"),
                        rs.getString("location"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                    )
                );
            }

            /*
             * Collector starting point.
             * For our demo, Bhimavaram is used as the starting point.
             */
            double currentLat = 16.5449;
            double currentLon = 81.5212;

            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Optimized Collection Route</title>");

            out.println("<style>");

            out.println("body{font-family:Arial;background:#f4f8f5;margin:0;}");

            out.println("h1{text-align:center;color:#1b5e20;margin-top:25px;}");

            out.println(".container{width:75%;margin:30px auto;}");

            out.println(".stop{background:white;padding:25px;margin:18px 0;" +
                        "border-radius:12px;" +
                        "box-shadow:0 3px 12px rgba(0,0,0,0.1);}");

            out.println(".stop h2{color:#1b5e20;}");

            out.println(".distance{color:#555;font-weight:bold;}");

            out.println(".start{text-align:center;background:#dff3e3;" +
                        "padding:15px;border-radius:10px;}");

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            out.println("<h1>Optimized Collection Route</h1>");

            out.println("<div class='container'>");

            out.println("<div class='start'>");
            out.println("<b>START</b><br>");
            out.println("Collector Location: Bhimavaram");
            out.println("</div>");

            if (points.size() == 0) {

                out.println("<h2 style='text-align:center;'>");
                out.println("No pending requests with location coordinates.");
                out.println("</h2>");

            } else {

                boolean[] visited = new boolean[points.size()];

                double totalDistance = 0;

                for (int step = 0; step < points.size(); step++) {

                    int nearest = -1;
                    double shortestDistance = Double.MAX_VALUE;

                    for (int i = 0; i < points.size(); i++) {

                        if (!visited[i]) {

                            double distance = calculateDistance(
                                currentLat,
                                currentLon,
                                points.get(i).latitude,
                                points.get(i).longitude
                            );

                            if (distance < shortestDistance) {

                                shortestDistance = distance;
                                nearest = i;
                            }
                        }
                    }

                    RequestPoint selected = points.get(nearest);

                    visited[nearest] = true;

                    totalDistance += shortestDistance;

                    out.println("<div class='stop'>");

                    out.println("<h2>Stop " + (step + 1) + "</h2>");

                    out.println("<p><b>Request ID:</b> " +
                            selected.id + "</p>");

                    out.println("<p><b>Item:</b> " +
                            selected.item + "</p>");

                    out.println("<p><b>Location:</b> " +
                            selected.location + "</p>");

                    out.println("<p><b>Coordinates:</b> " +
                            selected.latitude + ", " +
                            selected.longitude + "</p>");

                    out.println("<p class='distance'>Distance from previous point: " +
                            String.format("%.2f", shortestDistance) +
                            " km</p>");

                    out.println("</div>");

                    currentLat = selected.latitude;
                    currentLon = selected.longitude;
                }

                out.println("<div class='start'>");

                out.println("<h2>Route Completed</h2>");

                out.println("<p><b>Total approximate distance:</b> " +
                        String.format("%.2f", totalDistance) +
                        " km</p>");

                out.println("<p>Nearest Neighbor algorithm used.</p>");

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

            out.println("<h2>Error generating optimized route</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }

    private double calculateDistance(
            double lat1,
            double lon1,
            double lat2,
            double lon2) {

        final double EARTH_RADIUS = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2)
            +
            Math.cos(Math.toRadians(lat1))
            * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLon / 2)
            * Math.sin(dLon / 2);

        double c =
            2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}