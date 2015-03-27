import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
 
public class InsertBags extends HttpServlet{

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
				PreparedStatement deletebagstatement = conn.prepareStatement(
						"DELETE FROM Baggage " +
						"WHERE bid = ?");
				deletebagstatement.setInt(1,deletecodeval);
				deletebagstatement.executeUpdate();
				deletebagstatement.close();
				response.sendRedirect("/csc370p1/bags");

			}
			
			out.println("<html>" +
						"<head>" +
						"<title>Insert Bag</title>" +
						"</head>" +
						"<body><font size=\"4\">");
			if(errorcode != null) {
				out.println("ERROR BID, PID, AND FNUM MUST BE INTS");
			}
			out.println("Insert a Bag:<br>" +
						"<form method=\"POST\" action=\"/csc370p1/bags\" >" +
						"Bag ID: <input type=\"text\" name=\"bid\" value=\"\" /> <br>"
						);

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


			out.println("</select><br>" +
						"Select a Flight:" +
						"<select name=\"num\">");
						stmt = conn.createStatement();
						rset = stmt.executeQuery("SELECT * FROM Flight");
						while (rset.next()) {
							out.print (
							"<option value=\"" + rset.getString("num") + "\">" + rset.getString("num") + "</option>");
						}
						stmt.close();

			out.println("</select>" +
						"<input type=\"submit\" value=\"Submit\" > <br>" +
						"</form>" + 
						"<hr>");


			stmt = conn.createStatement();
			rset = stmt.executeQuery(
					"SELECT * " +
					"FROM Baggage");
			out.println("<table border=\"1\"><tr>" +
  						"<th>Bag ID</th>" +
    					"<th>Passenger ID</th>" +
    					"<th>Flight Number</th>" +
   	 					"<th>Delete?</th>" +
  						"</tr>");

			while (rset.next()) {
				out.println("<tr>");
				out.print (
					"<td>"+rset.getString("bid")+"</td>" +
					"<td>"+rset.getString("pid")+"</td>" +
					"<td>"+rset.getString("fnum")+"</td>" +
					"<td>" + "<a href=\"/csc370p1/bags?delete="+rset.getString("bid")+"\">X</a>" + "</td>");
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
			String bid = request.getParameter("bid");
			String pid = request.getParameter("pid");
			String fnum = request.getParameter("fnum");
			int bidval = 0;
			int pidval = 0;
			int fnumval = 0;

			try {
   				bidval = Integer.parseInt(bid);
   				pidval = Integer.parseInt(pid);
   				fnumval = Integer.parseInt(fnum);
			} catch (NumberFormatException e) {
    			response.sendRedirect("/csc370p1/bags?er=true");
				return;
			}



			PreparedStatement insertbagstatement = conn.prepareStatement(
						"INSERT INTO Baggage(bid,pid,fnum) " +
						"VALUES(?,?,?)");
			insertbagstatement.setInt(1,bidval);
			insertbagstatement.setInt(2,pidval);
			insertbagstatement.setInt(3,fnumval);
			insertbagstatement.executeUpdate();
			insertbagstatement.close();

			
			response.sendRedirect("/csc370p1/bags");
			

			
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}