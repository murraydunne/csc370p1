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
 
public class InsertArrivals extends HttpServlet{

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
				PreparedStatement deleteArrivalStatement = conn.prepareStatement(
						"DELETE FROM Arrival " +
						"WHERE id = ?");
				deleteArrivalStatement.setInt(1,deletecodeval);
				deleteArrivalStatement.executeUpdate();
				deleteArrivalStatement.close();
				response.sendRedirect("/csc370p1/arrivals");

			}
			
			out.println("<html>" +
						"<head>" +
						"<title>Insert Arrival</title>" +
						"</head>" +
						"<body><font size=\"4\">");
			if(errorcode1 != null) {
				out.println("ERROR ID AND FNUM MUST BE INTS");
			}

			if(errorcode2 != null){
				out.println("ERROR INVALID DATE FORMAT");
			}

			out.println("Insert an Arrival:<br>" +
						"<form method=\"POST\" action=\"/csc370p1/arrivals\" >" +
						"Arrival ID: <input type=\"text\" name=\"id\" value=\"\" /> <br>" +
						"Gate: <input type=\"text\" name=\"gate\" value=\"\" /> <br>" +
						"Arrival Date: <input type=\"text\" name=\"arrival_date\" value=\"\" /> <br>" +
						"Status: <input type=\"text\" name=\"status\" value=\"\" /> <br>" +
						"Flight Number: <input type=\"text\" name=\"fnum\" value=\"\" /> <br>" +
						"<input type=\"submit\" value=\"Submit\" > <br>" +
						"</form>" + 
						"<hr>");


			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(
					"SELECT * " +
					"FROM Arrival");
			out.println("<table border=\"1\"><tr>" +
  						"<th>ID</th>" +
    					"<th>Gate</th>" +
    					"<th>Arrival_Date</th>" +
    					"<th>Status</th>" +
    					"<th>Flight Number</th>" +
   	 					"<th>Delete?</th>" +
  						"</tr>");

			while (rset.next()) {
				out.println("<tr>");
				out.print (
					"<td>"+rset.getString("id")+"</td>" +
					"<td>"+rset.getString("gate")+"</td>" +
					"<td>"+rset.getString("arrival_date")+"</td>" +
					"<td>"+rset.getString("status")+"</td>" +
					"<td>"+rset.getString("fnum")+"</td>" +
					"<td>" + "<a href=\"/csc370p1/arrivals?delete="+rset.getString("id")+"\">X</a>" + "</td>");
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
			String arrival_date = request.getParameter("arrival_date");
			String status = request.getParameter("status");
			String fnum = request.getParameter("fnum");

			//Format Date
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date tempdate = null;

			try{
				tempdate = formatter.parse(arrival_date);
			} catch (ParseException e){
				response.sendRedirect("/csc370p1/arrivals?er2=true");
				return;
			}
	
			java.sql.Date arrival_dateval = new java.sql.Date(tempdate.getTime());


			int idval = 0;
			int fnumval = 0;

			try {
   				idval = Integer.parseInt(id);
   				fnumval = Integer.parseInt(fnum);
			} catch (NumberFormatException e) {
    			response.sendRedirect("/csc370p1/arrivals?er1=true");
				return;
			}


			PreparedStatement insertArrivalStatement = conn.prepareStatement(
						"INSERT INTO Arrival(id,gate,arrival_date,status,fnum) " +
						"VALUES( ?,?,?,?,?)");
			insertArrivalStatement.setInt(1,idval);
			insertArrivalStatement.setString(2,gate);
			insertArrivalStatement.setDate(3,arrival_dateval);
			insertArrivalStatement.setString(4,status);
			insertArrivalStatement.setInt(5,fnumval);
			insertArrivalStatement.executeUpdate();
			insertArrivalStatement.close();
			
			response.sendRedirect("/csc370p1/arrivals");
			

			
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}