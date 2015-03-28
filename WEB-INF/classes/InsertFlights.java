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
			String errorcode2 = request.getParameter("er2");


			//DELETE STUFF
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
			

			//PAGE
			out.println("<html>" +
						"<head>" +
						"<title>Insert Flight</title>" +
						"</head>" +
						"<body><font size=\"4\">");
			if(errorcode1 != null) {
				out.println("ERROR NUM, ACODE, AND PMCODE MUST BE INTS");
			}

			if(errorcode2 != null){
				out.println("ERROR INVALID DATE FORMAT");
			}


			out.println("Insert a Flight:<br>" +
						"<form method=\"POST\" action=\"/csc370p1/flights\" >" +
						"Flight Number: <input type=\"text\" name=\"num\" value=\"\" /> <br>" +
						"Source: <input type=\"text\" name=\"src\" value=\"\" /> <br>" +
						"Destination: <input type=\"text\" name=\"destination\" value=\"\" /> <br>" +
						"Airline Code: <select name=\"acode\">");
						//airline code 
			Statement acodestmt = conn.createStatement();
			ResultSet acodes = acodestmt.executeQuery(
						"SELECT * FROM Airline");
			while (acodes.next()) {
							out.println("<option value=\"" + acodes.getString("code") + "\">" + acodes.getString("name") + "</option>");
			}
						//plane model code
			out.println("</select><br>Plane Model Code: <select name=\"pmcode\"");
			Statement pmcodestmt = conn.createStatement();
			ResultSet pmcodes = pmcodestmt.executeQuery(
						"SELECT * FROM PlaneModel");
			while (pmcodes.next()) {
							out.println("<option value=\"" + pmcodes.getString("code") + "\">" + pmcodes.getString("code") + "</option>");
			}
			out.println("</select><br>" +
						"Gate: <input type=\"text\" name=\"gate\" value=\"\" /> <br>" +
						"Date: <input type=\"text\" name=\"date\" value=\"\" /> <br>" +
						"Time: <input type=\"text\" name=\"time\" value=\"\" /> <br>" +
						"Status: <input type=\"text\" name=\"status\" value=\"\" /> <br>" +
						"Type: <select name = \"type\">" + 
						"<option value=\"dep\">Departure</option>" +
						"<option value=\"arr\">Arrival</option>" +
						"</select><br>" +
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
			String gate = request.getParameter("gate");
			String date = request.getParameter("date");
			String time = request.getParameter("time");
			String status = request.getParameter("status");
			String type = request.getParameter("type");

			//Format Date
			String fdate = date + "-" + time;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-H-m");
			Date tempdate = null;

			try{
				tempdate = formatter.parse(fdate);
			} catch (ParseException e){
				response.sendRedirect("/csc370p1/passengers?er2=true");
				return;
			}
			java.sql.Date dateval = new java.sql.Date(tempdate.getTime());


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

			if(type.equals("dep")) {
				PreparedStatement insertOutgoing = conn.prepareStatement(
						"INSERT INTO OutgoingFlight(num,departs_at) " +
						"VALUES( ?,?)");
				insertOutgoing.setInt(1,numval);
				insertOutgoing.setDate(2,dateval);
				insertOutgoing.executeUpdate();
				insertOutgoing.close();

				PreparedStatement insertDeparture = conn.prepareStatement(
						"INSERT INTO Departure(gate,departing_date,status,fnum) " +
						"VALUES( ?,?,?,?)");
				insertDeparture.setString(1,gate);
				insertDeparture.setDate(2,dateval);
				insertDeparture.setString(3,status);
				insertDeparture.setInt(4,numval);
				insertDeparture.executeUpdate();
				insertDeparture.close();
			} else {
				PreparedStatement insertIncoming = conn.prepareStatement(
						"INSERT INTO IncomingFlight(num,arrives_at) " +
						"VALUES( ?,?)");
				insertIncoming.setInt(1,numval);
				insertIncoming.setDate(2,dateval);
				insertIncoming.executeUpdate();
				insertIncoming.close();

				PreparedStatement insertArrival = conn.prepareStatement(
						"INSERT INTO Arrival(gate,arrival_date,status,fnum) " +
						"VALUES( ?,?,?,?)");
				insertArrival.setString(1,gate);
				insertArrival.setDate(2,dateval);
				insertArrival.setString(3,status);
				insertArrival.setInt(4,numval);
				insertArrival.executeUpdate();
				insertArrival.close();
			}
			response.sendRedirect("/csc370p1/flights");
			

			
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}