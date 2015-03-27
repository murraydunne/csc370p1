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
 
public class PassengerRegistration extends HttpServlet{

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
			String errorcode2 = request.getParameter("er2");

			String deletecode = request.getParameter("delete");
			if(deletecode != null) {
				int deletecodeval = Integer.parseInt(deletecode);
				PreparedStatement deletePassengerStatement = conn.prepareStatement(
						"DELETE FROM Passenger " +
						"WHERE pid = ?");
				deletePassengerStatement.setInt(1,deletecodeval);
				deletePassengerStatement.executeUpdate();
				deletePassengerStatement.close();
				response.sendRedirect("/csc370p1/registrations");

			}
			
			out.println("<html>" +
						"<head>" +
						"<title>Insert Passenger</title>" +
						"</head>" +
						"<body><font size=\"4\">");
			if(errorcode1 != null) {
				out.println("ERROR PID MUST BE INT");
			}

			if(errorcode2 != null){
				out.println("ERROR INVALID DATE FORMAT");
			}

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

			out.println(
						"</select><br>" +
						"Select a Flight:" +
						"<select name=\"num\">");
						stmt = conn.createStatement();
						rset = stmt.executeQuery("SELECT * FROM Flight");
						while (rset.next()) {
							out.print (
							"<option value=\"" + rset.getString("num") + "\">" + rset.getString("num") + "</option>");
						}
						stmt.close();

  						out.println(
						"</select><br>" +
						"<input type=\"submit\" value=\"Submit\" > <br>" +
						"</form>" + 
						"<hr>");


			stmt = conn.createStatement();
			rset = stmt.executeQuery(
					"SELECT * " +
					"FROM Passenger");
			out.println("<table border=\"1\"><tr>" +
  						"<th>Pid</th>" +
    					"<th>Name</th>" +
    					"<th>Birthdate</th>" +
    					"<th>Birthplace</th>" +
    					"<th>Citizenship</th>" +
   	 					"<th>Delete?</th>" +
  						"</tr>");

			while (rset.next()) {
				out.println("<tr>");
				out.print (
					"<td>"+rset.getString("pid")+"</td>" +
					"<td>"+rset.getString("name")+"</td>" +
					"<td>"+rset.getString("birthdate")+"</td>" +
					"<td>"+rset.getString("birthplace")+"</td>" +
					"<td>"+rset.getString("citizenship")+"</td>" +
					"<td>" + "<a href=\"/csc370p1/registrations?delete="+rset.getString("pid")+"\">X</a>" + "</td>");
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
			String pid = request.getParameter("pid");
			String fnum = request.getParameter("fnum");

			int pidval = 0;
			int fnumval = 0;

			try {
   				pidval = Integer.parseInt(pid);
   				fnumval = Integer.parseInt(fnum);
			} catch (NumberFormatException e) {
    			response.sendRedirect("/csc370p1/registrations?er1=true");
				return;
			}


			Statement test = conn.createStatement();
			ResultSet results = test.executeQuery("SELECT * FROM IncomingFlight WHERE num = " + fnum);

			if(results.next()) {
				PreparedStatement insertPassengerStatement = conn.prepareStatement(
						"INSERT INTO ArrivesOn(pid,arrivalid) " +
						"VALUES( ?,?)");
				insertPassengerStatement.setInt(1,pidval);
				insertPassengerStatement.setInt(2,fnumval);
				insertPassengerStatement.executeUpdate();
				insertPassengerStatement.close();

			} else {
				PreparedStatement insertPassengerStatement = conn.prepareStatement(
						"INSERT INTO DepartsOn(pid,departureid) " +
						"VALUES( ?,?)");
				insertPassengerStatement.setInt(1,pidval);
				insertPassengerStatement.setInt(2,fnumval);
				insertPassengerStatement.executeUpdate();
				insertPassengerStatement.close();
			}

			
			
			response.sendRedirect("/csc370p1/registrations");
			
	
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}