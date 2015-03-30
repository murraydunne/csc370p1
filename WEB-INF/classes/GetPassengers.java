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
 
public class GetPassengers extends HttpServlet{

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
						"<form method=\"GET\" action=\"/csc370p1/passengerlist\" >");
						out.println("</select><br>" +
						"Select a Flight:" +
						"<select name=\"fnum\">");
						Statement fstmt = conn.createStatement();
						ResultSet frset = fstmt.executeQuery("SELECT * FROM Flight");
						while (frset.next()) {
							out.print (
							"<option value=\"" + frset.getString("num") + "\">" + frset.getString("num") + "</option>");
						}
						fstmt.close();

						
						out.println("<input type=\"submit\" value=\"Submit\" > <br>" +
						"</form>" + 
						"<hr>");

			String fnum = request.getParameter("fnum");


			if (fnum != null) {
				int fnumval = Integer.parseInt(fnum);
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Passenger WHERE pid IN" +  
					"(SELECT pid FROM ArrivesOn WHERE fnum = ?) OR pid IN (SELECT pid FROM DepartsOn WHERE fnum = ?)");
						//"OR (d.date IS NOT NULL AND ABS(EXTRACT(HOUR FROM CAST(ddate AS TIMESTAMP)) - ?) < 3)");
				stmt.setInt(1,fnumval);
				stmt.setInt(2,fnumval);
				ResultSet rset = stmt.executeQuery();
					
				out.println("<br><table border=\"1\"><tr>" +
							"<th>pid</th>" +
							"<th>name</th>" +
							"<th>birthdate</th>" +
							"<th>birthplace</th>" +
							"<th>citizenship</th>" +
	  						"</tr>");


				while (rset.next()) {

					out.println("<tr>");
					out.print (
						"<td>"+rset.getString("pid")+"</td>" +
						"<td>"+rset.getString("name")+"</td>" +
						"<td>"+rset.getString("birthdate")+"</td>" +
						"<td>"+rset.getString("birthplace")+"</td>" +
						"<td>"+rset.getString("citizenship")+"</td>");
					out.println("</tr>");
					
					
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