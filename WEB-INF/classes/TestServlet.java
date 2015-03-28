import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
 
public class TestServlet extends HttpServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		PrintWriter out = response.getWriter();

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			
			Connection conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@csc370p1.cii93vwsfsde.us-east-1.rds.amazonaws.com:1521:ORCL",
				"csc370p1",
				"csc370p1");

			out.println("<html>");
			out.println("<body>");
			out.println("<h1>Hello Servlet!</h1>");
		
			if(conn.isValid(0)) {
				out.println("<h2>Yes!</h2>");
			} else {
				out.println("<h2>RIP</h2>");
			}

			out.println("<b>Part 3:</b><br>")
			out.println("<a href=\"/csc370p1/planes\">Insert Planes</a><br>");
			out.println("<a href=\"/csc370p1/airlines\">Insert Airlines</a><br>");
			out.println("<a href=\"/csc370p1/flights\">Insert Flights</a><br>");
			out.println("<a href=\"/csc370p1/passengers\">Insert Passengers</a><br>");
			out.println("<a href=\"/csc370p1/bags\">Insert Bags</a><br>");
			//out.println("<a href=\"/csc370p1/incomingflights\">Insert Incoming Flights</a><br>");
			//out.println("<a href=\"/csc370p1/outgoingflights\">Insert Outgoing Flights</a><br>");
			//out.println("<a href=\"/csc370p1/arrivals\">Insert Arrivals</a><br>");
			//out.println("<a href=\"/csc370p1/departures\">Insert Departures</a><br>");
			out.println("<a href=\"/csc370p1/registrations\">Passenger Registration</a><br>");

			out.println("<b>Part 4:</b><br>")
			out.println("<a href=\"/csc370p1/flightstofrom\">Find Flights</a><br>");



			out.println("</body>");
			out.println("</html>");	
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}
