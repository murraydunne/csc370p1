import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
 
public class InsertOutgoingFlights extends HttpServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		PrintWriter out = response.getWriter();

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			
			Connection conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@csc370p1.cii93vwsfsde.us-east-1.rds.amazonaws.com:1521:ORCL",
				"csc370p1",
				"csc370p1");

			String errorcode1 = request.getParameter("er");

			String deletecode = request.getParameter("delete");
			if(deletecode != null) {
				int deletecodeval = Integer.parseInt(deletecode);
				PreparedStatement deleteOutgoingFlightStatement = conn.prepareStatement(
						"DELETE FROM OutgoingFlight " +
						"WHERE num = ?");
				deleteOutgoingFlightStatement.setInt(1,deletecodeval);
				deleteOutgoingFlightStatement.executeUpdate();
				deleteOutgoingFlightStatement.close();
				response.sendRedirect("/csc370p1/outgoingflights");
			}
			
			out.println("<html>" +
						"<head>" +
						"<title>Insert Outgoing Flight</title>" +
						"</head>" +
						"<body><font size=\"4\">");
			if(errorcode1 != null) {
				out.println("ERROR NUM AND DEPARTUREID MUST BE INTS");
			}


			out.println("Insert an Outgoing Flight:<br>" +
						"<form method=\"POST\" action=\"/csc370p1/outgoingflights\" >" +
						"Flight Number: <input type=\"text\" name=\"num\" value=\"\" /> <br>" +
						"Departs At: <input type=\"text\" name=\"departs_at\" value=\"\" /> <br>" +
						"Departure ID: <input type=\"text\" name=\"departureid\" value=\"\" /> <br>" +
						"<input type=\"submit\" value=\"Submit\" > <br>" +
						"</form>" + 
						"<hr>");


			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(
					"SELECT * " +
					"FROM OutgoingFlight");
			out.println("<table border=\"1\"><tr>" +
  						"<th>num</th>" +
    					"<th>departs_at</th>" +
    					"<th>departureid</th>" +
   	 					"<th>Delete?</th>" +
  						"</tr>");

			while (rset.next()) {
				out.println("<tr>");
				out.print (
					"<td>"+rset.getString("num")+"</td>" +
					"<td>"+rset.getString("departs_at")+"</td>" +
					"<td>"+rset.getString("departureid")+"</td>" +
					"<td>" + "<a href=\"/csc370p1/outgoingflights?delete="+rset.getString("num")+"\">X</a>" + "</td>");
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


	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		PrintWriter out = response.getWriter();

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			
			Connection conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@csc370p1.cii93vwsfsde.us-east-1.rds.amazonaws.com:1521:ORCL",
				"csc370p1",
				"csc370p1");
			String num = request.getParameter("num");
			String departs_at = request.getParameter("departs_at");
			String departureid = request.getParameter("departureid");


			int numval = 0;
			int departureidval = 0;

			try {
   				numval = Integer.parseInt(num);
   				departureidval = Integer.parseInt(departureid);
			} catch (NumberFormatException e) {
    			response.sendRedirect("/csc370p1/outgoingflights?er=true");
				return;
			}


			PreparedStatement insertOutgoingFlightStatement = conn.prepareStatement(
						"INSERT INTO OutgoingFlight(num,departs_at,departureid,) " +
						"VALUES(?,?,?)");
			insertOutgoingFlightStatement.setInt(1,numval);
			insertOutgoingFlightStatement.setString(2,departs_at);
			insertOutgoingFlightStatement.setInt(3,departureidval);
			insertOutgoingFlightStatement.executeUpdate();
			insertOutgoingFlightStatement.close();
			
			response.sendRedirect("/csc370p1/outgoingflights");
			

			
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}