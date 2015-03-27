import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
 
public class InsertPassengers extends HttpServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		PrintWriter out = response.getWriter();

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			
			Connection conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@csc370p1.cii93vwsfsde.us-east-1.rds.amazonaws.com:1521:ORCL",
				"csc370p1",
				"csc370p1");

			String errorcode = request.getParameter("er");

			String deletecode = request.getParameter("delete");
			if(deletecode != null) {
				int deletecodeval = Integer.parseInt(deletecode);
				PreparedStatement deletePassengerStatement = conn.prepareStatement(
						"DELETE FROM Passenger " +
						"WHERE pid = ?");
				deletePlaneStatement.setInt(1,deletecodeval);
				deletePlaneStatement.executeUpdate();
				deletePlaneStatement.close();
				response.sendRedirect("/csc370p1/passengers");

			}
			
			out.println("<html>" +
						"<head>" +
						"<title>Insert Passenger</title>" +
						"</head>" +
						"<body><font size=\"4\">");
			if(errorcode != null) {
				out.println("ERROR PID MUST BE INT");
			}
			out.println("Insert a Passenger:<br>" +
						"<form method=\"POST\" action=\"/csc370p1/passengers\" >" +
						"Passenger ID: <input type=\"text\" name=\"pid\" value=\"\" /> <br>" +
						"Name: <input type=\"text\" name=\"name\" value=\"\" /> <br>" +
						"Birthdate: <input type=\"text\" name=\"birthdate\" value=\"\" /> <br>" +
						"Birthplace: <input type=\"text\" name=\"birtplace\" value=\"\" /> <br>" +
						"Citizenship: <input type=\"text\" name=\"citizenship\" value=\"\" /> <br>" +
						"<input type=\"submit\" value=\"Submit\" > <br>" +
						"</form>" + 
						"<hr>");


			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(
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
					"<td>" + "<a href=\"/csc370p1/passengers?delete="+rset.getString("pid")+"\">X</a>" + "</td>");
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
			String name = request.getParameter("name");
			String birthdate = request.getParameter("birthdate");
			String birthplace = request.getParameter("birthplace");
			String citizenship = request.getParameter("citizenship");

			int pidval = 0;

			try {
   				pidval = Integer.parseInt(pid);
			} catch (NumberFormatException e) {
    			response.sendRedirect("/csc370p1/passengers?er=true");
				return;
			}


			PreparedStatement insertPassengerStatement = conn.prepareStatement(
						"INSERT INTO Passenger(pid, name, birthplace, birthdate, citizenship) " +
						"VALUES( ?,?)");
			insertPassengerStatement.setInt(1,pidval);
			insertPassengerStatement.setString(2,name);
			insertPassengerStatement.setString(3,birthplace);
			insertPassengerStatement.setString(4,birthdate);
			insertPassengerStatement.setString(5,citizenship);
			insertPassengerStatement.executeUpdate();
			insertPassengerStatement.close();
			
			response.sendRedirect("/csc370p1/passengers");
			

			
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}