import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
 
public class GetBags extends HttpServlet{

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
						"<title>Where are my bags yo</title>" +
						"</head>" +
						"<body><font size=\"4\">");
			
			out.println("Get Bags" +
						"<form method=\"GET\" action=\"/csc370p1/getbags\">"
						);

			out.println(
						"Select a Passenger:" +
						"<select name=\"pid\">");
						Statement stmt = conn.createStatement();
						ResultSet rset = stmt.executeQuery("SELECT * FROM Passenger");
						while (rset.next()) {
							out.print (
							"<option value=\"" + rset.getString("pid") + "\">" + rset.getString("name") + "</option>");
						}
						stmt.close();


			out.println("</select>" +
						"Select a Flight:" +
						"<select name=\"num\">");
						stmt = conn.createStatement();
						rset = stmt.executeQuery("SELECT * FROM Flight ");
						while (rset.next()) {
							out.print (
							"<option value=\"" + rset.getString("num") + "\">" + rset.getString("num") + "</option>");
						}
						stmt.close();

			out.println("</select>" +
						"<input type=\"submit\" value=\"Submit\" > <br>" +
						"</form>" + 
						"<hr>");


			String pid = request.getParameter("pid");
			String fnum = request.getParameter("num");

			stmt = conn.createStatement();
			rset = stmt.executeQuery("SELECT * FROM Baggage WHERE fnum = " + fnum + " and pid = " + pid);
			out.println("<table border=\"1\"><tr>" +
  						"<th>Bag ID</th>" +
    					"<th>Passenger ID</th>" +
    					"<th>Flight Number</th>" +
  						"</tr>");

			while (rset.next()) {
				out.println("<tr>");
				out.print (
					"<td>"+rset.getString("bid")+"</td>" +
					"<td>"+rset.getString("pid")+"</td>" +
					"<td>"+rset.getString("fnum")+"</td>"
					);
				out.println("</tr>");
			}
			out.println("</table>");
			stmt.close();
			out.println("<a href=\"/csc370p1\">Back</a>");

			out.println("</body>" + "</html>");
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}