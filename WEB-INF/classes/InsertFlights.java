import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
 
public class InsertFlights extends HttpServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		PrintWriter out = response.getWriter();

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			
			Connection conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@csc370p1.cii93vwsfsde.us-east-1.rds.amazonaws.com:1521:ORCL",
				"csc370p1",
				"csc370p1");

			String errorcode1 = request.getParameter("er1");

			String deletecode = request.getParameter("delete");
			if(deletecode != null) {
				int deletecodeval = Integer.parseInt(deletecode);
				PreparedStatement deleteFlightStatement = conn.prepareStatement(
						"DELETE FROM Flight " +
						"WHERE num = ?");
				deleteFlightStatement.setInt(1,deletecodeval);
				deleteFlightStatement.executeUpdate();
				deleteFlightStatement.close();
				response.sendRedirect("/csc370p1/flights");

			}
			
			out.println("<html>" +
						"<head>" +
						"<title>Insert Flight</title>" +
						"</head>" +
						"<body><font size=\"4\">");
			if(errorcode1 != null) {
				out.println("ERROR NUM, ACODE, AND PMCODE MUST BE INTS");
			}


			out.println("Insert a Flight:<br>" +
						"<form method=\"POST\" action=\"/csc370p1/flights\" >" +
						"Flight Number: <input type=\"text\" name=\"num\" value=\"\" /> <br>" +
						"Source: <input type=\"text\" name=\"src\" value=\"\" /> <br>" +
						"Destination: <input type=\"text\" name=\"destination\" value=\"\" /> <br>" +
						"Airline Code: <input type=\"text\" name=\"acode\" value=\"\" /> <br>" +
						"Plane Code: <input type=\"text\" name=\"pmcode\" value=\"\" /> <br>" +
						"<input type=\"submit\" value=\"Submit\" > <br>" +
						"</form>" + 
						"<hr>");


			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(
					"SELECT * " +
					"FROM Flight");
			out.println("<table border=\"1\"><tr>" +
  						"<th>num</th>" +
    					"<th>src</th>" +
    					"<th>destination</th>" +
    					"<th>acode</th>" +
    					"<th>pmcode</th>" +
   	 					"<th>Delete?</th>" +
  						"</tr>");

			while (rset.next()) {
				out.println("<tr>");
				out.print (
					"<td>"+rset.getString("num")+"</td>" +
					"<td>"+rset.getString("src")+"</td>" +
					"<td>"+rset.getString("destination")+"</td>" +
					"<td>"+rset.getString("acode")+"</td>" +
					"<td>"+rset.getString("pmcode")+"</td>" +
					"<td>" + "<a href=\"/csc370p1/flights?delete="+rset.getString("num")+"\">X</a>" + "</td>");
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
			String src = request.getParameter("src");
			String destination = request.getParameter("destination");
			String acode = request.getParameter("acode");
			String pmcode = request.getParameter("pmcode");


			int numval = 0;
			int acodeval = 0;
			int pmcodeval =0;

			try {
   				numval = Integer.parseInt(num);
   				acodeval = Integer.parseInt(acode);
   				pmcodeval = Integer.parseInt(pmcode);
			} catch (NumberFormatException e) {
    			response.sendRedirect("/csc370p1/flights?er1=true");
				return;
			}


			PreparedStatement insertFlightStatement = conn.prepareStatement(
						"INSERT INTO Flight(num,src,destination,acode,pmcode) " +
						"VALUES( ?,?,?,?,?)");
			insertFlightStatement.setInt(1,numval);
			insertFlightStatement.setString(2,src);
			insertFlightStatement.setString(3,destination);
			insertFlightStatement.setInt(4,acodeval);
			insertFlightStatement.setInt(5,pmcodeval);
			insertFlightStatement.executeUpdate();
			insertFlightStatement.close();
			
			response.sendRedirect("/csc370p1/flights");
			

			
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}