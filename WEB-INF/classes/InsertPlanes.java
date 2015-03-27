import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
 
public class InsertPlanes extends HttpServlet{

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
						"DELETE FROM PlaneModel " +
						"WHERE code = ?");
				deletePlaneStatement.setInt(1,deletecodeval);
				deletePlaneStatement.executeUpdate();
				deletePlaneStatement.close();
				response.sendRedirect("/csc370p1/planes");

			}
			
			out.println("<html>" +
						"<head>" +
						"<title>Insert Plane</title>" +
						"</head>" +
						"<body><font size=\"4\">");
			if(errorcode != null) {
				out.println("ERROR CODE AND CAP MUST BE INTS");
			}
			out.println("Insert a Plane:<br>" +
						"<form method=\"POST\" action=\"/csc370p1/planes\" >" +
						"Plane Code: <input type=\"text\" name=\"code\" value=\"\" /> <br>" +
						"Capacity: <input type=\"text\" name=\"cap\" value=\"\" /> <br>" +
						"<input type=\"submit\" value=\"Submit\" > <br>" +
						"</form>" + 
						"<hr>");


			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(
					"SELECT * " +
					"FROM PlaneModel");
			out.println("<table border=\"1\"><tr>" +
  						"<th>Code</th>" +
    					"<th>Capacity</th>" +
   	 					"<th>Delete?</th>" +
  						"</tr>");

			while (rset.next()) {
				out.println("<tr>");
				out.print (
					"<td>"+rset.getString("code")+"</td>" +
					"<td>"+rset.getString("capacity")+"</td>" +
					"<td>" + "<a href=\"/csc370p1/planes?delete="+rset.getString("code")+"\">X</a>" + "</td>");
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
			String cap = request.getParameter("cap");
			int codeval = 0;
			int capval = 0;

			try {
   				codeval = Integer.parseInt(code);
   				capval = Integer.parseInt(cap);
			} catch (NumberFormatException e) {
    			response.sendRedirect("/csc370p1/planes?er=true");
				return;
			}


				


			PreparedStatement insertPlaneStatement = conn.prepareStatement(
						"INSERT INTO PlaneModel(code,capacity) " +
						"VALUES( ?,?)");
			insertPlaneStatement.setInt(1,codeval);
			insertPlaneStatement.setInt(2,capval);
			insertPlaneStatement.executeUpdate();
			insertPlaneStatement.close();




			
			response.sendRedirect("/csc370p1/planes");
			

			
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}
