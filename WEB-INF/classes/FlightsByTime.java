import java.util.Date;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
 
public class FlightsByTime extends HttpServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		PrintWriter out = response.getWriter();

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			
			Connection conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@csc370p1.cii93vwsfsde.us-east-1.rds.amazonaws.com:1521:ORCL",
				"csc370p1",
				"csc370p1");

			
			out.println("<html>" +
						"<head>" +
						"<title></title>" +
						"</head>" +
						"<body><font size=\"4\">");

			out.println(
						"<form method=\"GET\" action=\"/csc370p1/timeflights\" >" +
						"Time: <input type=\"text\" name=\"time\" value=\"\" /> <br>" +
						
						"<input type=\"submit\" value=\"Submit\" > <br>" +
						"</form>" + 
						"<hr>");

			String time = request.getParameter("time");


			if (time != null) {
				int timeval = Integer.parseInt(time);
				PreparedStatement stmt = conn.prepareStatement("SELECT f.num AS num, d.gate AS dgate, d.departing_date AS ddate, d.status AS dstatus, a.gate AS agate, a.arrival_date AS adate, a.status AS astatus " +
						"FROM Flight f LEFT JOIN Departure d ON(f.num = d.fnum) LEFT JOIN Arrival a ON(f.num = a.fnum) " +
						"WHERE (adate IS NOT NULL AND ABS(EXTRACT(HOUR FROM CAST(adate AS TIMESTAMP)) - ?) < 3)");
						//"OR (d.date IS NOT NULL AND ABS(EXTRACT(HOUR FROM CAST(ddate AS TIMESTAMP)) - ?) < 3)");
				stmt.setInt(1,timeval);
				stmt.setInt(2,timeval);
				ResultSet rset = stmt.executeQuery();
					
				out.println("<br><table border=\"1\"><tr>" +
							"<th>Flight Number</th>" +
	    					"<th>Gate</th>" +
	    					"<th>Date</th>" +
	    					"<th>Status</th>" +
	  						"</tr>");


				while (rset.next()) {
					if(rset.getString("astatus") != null) {
						out.println("<tr>");
						out.print (
							"<td>"+rset.getString("num")+"</td>" +
							"<td>"+rset.getString("agate")+"</td>" +
							"<td>"+rset.getString("adate")+"</td>" +
							"<td>"+rset.getString("astatus")+"</td>");
						out.println("</tr>");
					} else {
						out.println("<tr>");
						out.print (
							"<td>"+rset.getString("num")+"</td>" +
							"<td>"+rset.getString("dgate")+"</td>" +
							"<td>"+rset.getString("ddate")+"</td>" +
							"<td>"+rset.getString("dstatus")+"</td>");
						out.println("</tr>");
					}
					
				}
				out.println("</table>");
				stmt.close();
			}
			out.println("<a href=\"/csc370p1\">Back</a>");

			out.println("</body>" + "</html>");
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}