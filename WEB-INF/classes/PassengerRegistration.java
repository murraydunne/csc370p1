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


			String deletepid = request.getParameter("delpid");
			String deletefnum = request.getParameter("delfnum");
			if(deletepid != null) {
				int deletepidval = Integer.parseInt(deletepid);
				int deletefnumval = Integer.parseInt(deletefnum);
				PreparedStatement deletePassengerStatement = conn.prepareStatement(
						"DELETE FROM ArrivesOn " +
						"WHERE (pid = ?) AND (fnum = ?)");
				deletePassengerStatement.setInt(1,deletepidval);
				deletePassengerStatement.setInt(2,deletefnumval);
				deletePassengerStatement.executeUpdate();
				deletePassengerStatement.close();
				deletePassengerStatement = conn.prepareStatement(
						"DELETE FROM DepartsOn " +
						"WHERE (pid = ?) AND (fnum = ?)");
				deletePassengerStatement.setInt(1,deletepidval);
				deletePassengerStatement.setInt(2,deletefnumval);
				deletePassengerStatement.executeUpdate();
				deletePassengerStatement.close();
				response.sendRedirect("/csc370p1/registrations");

			}
			
			out.println("<html>" +
						"<head>" +
						"<title>Insert Passenger</title>" +
						"</head>" +
						"<body><font size=\"4\">");
			out.println(
						"<form method=\"POST\" action=\"/csc370p1/registrations\" >" +
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
						"<select name=\"fnum\">");
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
					"FROM ArrivesOn");
			out.println("Incoming Flights<br><table border=\"1\"><tr>" +
  						"<th>Pid</th>" +
    					"<th>Fid</th>" +
    					"<th>Delete?</th>" +
  						"</tr>");

			while (rset.next()) {
				out.println("<tr>");
				out.print (
					"<td>"+rset.getString("pid")+"</td>" +
					"<td>"+rset.getString("fnum")+"</td>" +
					"<td>" + "<a href=\"/csc370p1/registrations?delpid="+rset.getString("pid")+"&delfnum="+rset.getString("fnum")+"\">X</a>" + "</td>");
				out.println("</tr>");
			}
			rset = stmt.executeQuery(
					"SELECT * " +
					"FROM DepartsOn");
			out.println("</table><br>Outgoing Flights<table border=\"1\"><tr>" +
  						"<th>Pid</th>" +
    					"<th>Fid</th>" +
    					"<th>Delete?</th>" +
  						"</tr>");
			while (rset.next()) {
				out.println("<tr>");
				out.print (
					"<td>"+rset.getString("pid")+"</td>" +
					"<td>"+rset.getString("fnum")+"</td>" +
					"<td>" + "<a href=\"/csc370p1/registrations?delpid="+rset.getString("pid")+"&delfnum="+rset.getString("fnum")+"\">X</a>" + "</td>");
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
						"INSERT INTO ArrivesOn(pid,fnum) " +
						"VALUES( ?,?)");
				insertPassengerStatement.setInt(1,pidval);
				insertPassengerStatement.setInt(2,fnumval);
				insertPassengerStatement.executeUpdate();
				insertPassengerStatement.close();

			} else {
				PreparedStatement insertPassengerStatement = conn.prepareStatement(
						"INSERT INTO DepartsOn(pid,fnum) " +
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