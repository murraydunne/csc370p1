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
 
public class InsertDepartures extends HttpServlet{

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
				PreparedStatement deleteDeparturestatement = conn.prepareStatement(
						"DELETE FROM Departure " +
						"WHERE id = ?");
				deleteDeparturestatement.setInt(1,deletecodeval);
				deleteDeparturestatement.executeUpdate();
				deleteDeparturestatement.close();
				response.sendRedirect("/csc370p1/departures");

			}
			
			out.println("<html>" +
						"<head>" +
						"<title>Insert Departure</title>" +
						"</head>" +
						"<body><font size=\"4\">");
			if(errorcode1 != null) {
				out.println("ERROR ID AND FNUM MUST BE INTS");
			}

			if(errorcode2 != null){
				out.println("ERROR INVALID DATE FORMAT");
			}

			out.println("Insert a Departure:<br>" +
						"<form method=\"POST\" action=\"/csc370p1/departures\" >" +
						"Departure ID: <input type=\"text\" name=\"id\" value=\"\" /> <br>" +
						"Gate: <input type=\"text\" name=\"gate\" value=\"\" /> <br>" +
						"Departing Date: <input type=\"text\" name=\"departing_date\" value=\"\" /> <br>" +
						"Status: <input type=\"text\" name=\"status\" value=\"\" /> <br>" +
						"Flight Number: <input type=\"text\" name=\"fnum\" value=\"\" /> <br>" +
						"<input type=\"submit\" value=\"Submit\" > <br>" +
						"</form>" + 
						"<hr>");


			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(
					"SELECT * " +
					"FROM Departure");
			out.println("<table border=\"1\"><tr>" +
  						"<th>ID</th>" +
    					"<th>Gate</th>" +
    					"<th>Departing_Date</th>" +
    					"<th>Status</th>" +
    					"<th>Flight Number</th>" +
   	 					"<th>Delete?</th>" +
  						"</tr>");

			while (rset.next()) {
				out.println("<tr>");
				out.print (
					"<td>"+rset.getString("id")+"</td>" +
					"<td>"+rset.getString("gate")+"</td>" +
					"<td>"+rset.getString("departing_date")+"</td>" +
					"<td>"+rset.getString("status")+"</td>" +
					"<td>"+rset.getString("fnum")+"</td>" +
					"<td>" + "<a href=\"/csc370p1/departures?delete="+rset.getString("id")+"\">X</a>" + "</td>");
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
			String id = request.getParameter("id");
			String gate = request.getParameter("gate");
			String departing_date = request.getParameter("departing_date");
			String status = request.getParameter("status");
			String fnum = request.getParameter("fnum");

			//Format Date
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date tempdate = null;

			try{
				tempdate = formatter.parse(departing_date);
			} catch (ParseException e){
				response.sendRedirect("/csc370p1/departures?er2=true");
				return;
			}
			
	
			java.sql.Date departing_dateval = new java.sql.Date(tempdate.getTime());


			int idval = 0;
			int fnumval = 0;

			try {
   				idval = Integer.parseInt(id);
   				fnumval = Integer.parseInt(fnum);
			} catch (NumberFormatException e) {
    			response.sendRedirect("/csc370p1/departures?er1=true");
				return;
			}


			PreparedStatement insertDeparturestatement = conn.prepareStatement(
						"INSERT INTO Departure(id,gate,departing_date,status,fnum) " +
						"VALUES( ?,?,?,?,?)");
			insertDeparturestatement.setInt(1,idval);
			insertDeparturestatement.setString(2,gate);
			insertDeparturestatement.setDate(3,departing_dateval);
			insertDeparturestatement.setString(4,status);
			insertDeparturestatement.setInt(5,fnumval);
			insertDeparturestatement.executeUpdate();
			insertDeparturestatement.close();
			
			response.sendRedirect("/csc370p1/departures");
			

			
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}