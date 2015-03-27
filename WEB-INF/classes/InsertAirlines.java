import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
 
public class InsertAirlines extends HttpServlet{

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
				PreparedStatement deletePlaneStatement = conn.prepareStatement(
						"DELETE FROM Airline " +
						"WHERE code = ?");
				deletePlaneStatement.setInt(1,deletecodeval);
				deletePlaneStatement.executeUpdate();
				deletePlaneStatement.close();
				response.sendRedirect("/csc370p1/airlines");

			}
			
			out.println("<html>" +
						"<head>" +
						"<title>Insert Airline</title>" +
						"</head>" +
						"<body><font size=\"4\">");
			if(errorcode != null) {
				out.println("ERROR CODE MUST BE INT");
			}
			out.println("Insert an Airline:<br>" +
						"<form method=\"POST\" action=\"/csc370p1/airlines\" >" +
						"Airline Code: <input type=\"text\" name=\"code\" value=\"\" /> <br>" +
						"Name: <input type=\"text\" name=\"name\" value=\"\" /> <br>" +
						"Website: <input type=\"text\" name=\"website\" value=\"\" /> <br>" +
						"<input type=\"submit\" value=\"Submit\" > <br>" +
						"</form>" + 
						"<hr>");


			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(
					"SELECT * " +
					"FROM Airline");
			out.println("<table border=\"1\"><tr>" +
  						"<th>Code</th>" +
    					"<th>Name</th>" +
    					"<th>Website</th>" +
   	 					"<th>Delete?</th>" +
  						"</tr>");

			while (rset.next()) {
				out.println("<tr>");
				out.print (
					"<td>"+rset.getString("code")+"</td>" +
					"<td>"+rset.getString("name")+"</td>" +
					"<td>"+rset.getString("website")+"</td>" +
					"<td>" + "<a href=\"/csc370p1/airlines?delete="+rset.getString("code")+"\">X</a>" + "</td>");
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
			String code = request.getParameter("code");
			String name = request.getParameter("name");
			String website = request.getParameter("website");
			int codeval = 0;

			try {
   				codeval = Integer.parseInt(code);
			} catch (NumberFormatException e) {
    			response.sendRedirect("/csc370p1/airlines?er=true");
				return;
			}


			PreparedStatement insertAirlineStatement = conn.prepareStatement(
						"INSERT INTO Airline(code,name,website) " +
						"VALUES( ?,?,?)");
			insertAirlineStatement.setInt(1,codeval);
			insertAirlineStatement.setString(2,name);
			insertAirlineStatement.setString(3,website);
			insertAirlineStatement.executeUpdate();
			insertAirlineStatement.close();


			response.sendRedirect("/csc370p1/airlines");
			

			
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}