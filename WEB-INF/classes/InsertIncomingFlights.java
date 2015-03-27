import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
 
public class InsertIncomingFlights extends HttpServlet{

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
				PreparedStatement deleteFlightStatement = conn.prepareStatement(
						"DELETE FROM IncomingFlight " +
						"WHERE num = ?");
				deleteFlightStatement.setInt(1,deletecodeval);
				deleteFlightStatement.executeUpdate();
				deleteFlightStatement.close();
				response.sendRedirect("/csc370p1/incomingflights");
			}
			
			out.println("<html>" +
						"<head>" +
						"<title>Insert Incoming Flight</title>" +
						"</head>" +
						"<body><font size=\"4\">");
			if(errorcode1 != null) {
				out.println("ERROR NUM AND ARRIVALID MUST BE INTS");
			}


			out.println("Insert an Incoming Flight:<br>" +
						"<form method=\"POST\" action=\"/csc370p1/incomingflights\" >" +
						"Flight Number: <input type=\"text\" name=\"num\" value=\"\" /> <br>" +
						"Arrives At: <input type=\"text\" name=\"arrives_at\" value=\"\" /> <br>" +
						"Arrival ID: <input type=\"text\" name=\"arrivalid\" value=\"\" /> <br>" +
						"<input type=\"submit\" value=\"Submit\" > <br>" +
						"</form>" + 
						"<hr>");


			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(
					"SELECT * " +
					"FROM IncomingFlight");
			out.println("<table border=\"1\"><tr>" +
  						"<th>num</th>" +
    					"<th>arrives_at</th>" +
    					"<th>arrivalid</th>" +
   	 					"<th>Delete?</th>" +
  						"</tr>");

			while (rset.next()) {
				out.println("<tr>");
				out.print (
					"<td>"+rset.getString("num")+"</td>" +
					"<td>"+rset.getString("arrives_at")+"</td>" +
					"<td>"+rset.getString("arrivalid")+"</td>" +
					"<td>" + "<a href=\"/csc370p1/incomingflights?delete="+rset.getString("num")+"\">X</a>" + "</td>");
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
			String arrives_at = request.getParameter("arrives_at");
			String arrivalid = request.getParameter("arrivalid");


			int numval = 0;
			int arrivalidval = 0;

			try {
   				numval = Integer.parseInt(num);
   				arrivalidval = Integer.parseInt(arrivalid);
			} catch (NumberFormatException e) {
    			response.sendRedirect("/csc370p1/incomingflights?er=true");
				return;
			}


			PreparedStatement insertFlightStatement = conn.prepareStatement(
						"INSERT INTO IncomingFlight(num,arrives_at,arrivalid,) " +
						"VALUES(?,?,?)");
			insertFlightStatement.setInt(1,numval);
			insertFlightStatement.setString(2,arrives_at);
			insertFlightStatement.setInt(3,arrivalidval);
			insertFlightStatement.executeUpdate();
			insertFlightStatement.close();
			
			response.sendRedirect("/csc370p1/incomingflights");
			

			
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}