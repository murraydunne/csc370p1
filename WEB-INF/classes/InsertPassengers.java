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
 
public class InsertPassengers extends HttpServlet{

        private String getClassName(ResultSet rset) throws SQLException {
            if(rset.getString("months") != null) {
                return "Infant";
            }
            if(rset.getString("ss") != null) {
                return "First Class";
            }
            if(rset.getString("condition") != null) {
                return "Special Needs";
            }
            
            return "Economy Class";
        }
        
        private String getClassInfo(ResultSet rset) throws SQLException {
            if(rset.getString("months") != null) {
                return rset.getString("months") + " months old";
            }
            if(rset.getString("ss") != null) {
                if(rset.getString("ss").equals("Y")) {
                    return "Shaken, not stirred.";
                } else {
                    return "Stirred.";
                }
            }
            if(rset.getString("condition") != null) {
                return "Condition: " + rset.getString("condition");
            }
            
            return "N/A";
        }
    
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
				response.sendRedirect("/csc370p1/passengers");

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

                        out.println("<script type=\"text/javascript\"> function ecosel() {"
                                + "document.getElementById('ss').style.display = 'none';"
                                + "document.getElementById('dis').style.display = 'none';"
                                + "document.getElementById('inf').style.display = 'none';"
                                + "}" 
                                + "function firstsel() {"
                                + "document.getElementById('ss').style.display = 'block';"
                                + "document.getElementById('dis').style.display = 'none';"
                                + "document.getElementById('inf').style.display = 'none';"
                                + "}" 
                                + "function specsel() {"
                                + "document.getElementById('ss').style.display = 'none';"
                                + "document.getElementById('dis').style.display = 'block';"
                                + "document.getElementById('inf').style.display = 'none';"
                                + "}" 
                                + "function infsel() {"
                                + "document.getElementById('ss').style.display = 'none';"
                                + "document.getElementById('dis').style.display = 'none';"
                                + "document.getElementById('inf').style.display = 'block';"
                                + "}"
                                + "function cchange() {"
                                + "var csel = document.getElementById(\"csel\");"
                                + "if(csel.selectedIndex == 0) { ecosel(); }"
                                + "if(csel.selectedIndex == 1) { firstsel(); }"
                                + "if(csel.selectedIndex == 2) { specsel(); }"
                                + "if(csel.selectedIndex == 3) { infsel(); }"
                                + "}</script>");
			out.println("Insert a Passenger:<br>" +
						"<form method=\"POST\" action=\"/csc370p1/passengers\" >" +
						"Passenger ID: <input type=\"text\" name=\"pid\" value=\"\" /> <br>" +
						"Name: <input type=\"text\" name=\"name\" value=\"\" /> <br>" +
						"Birthdate: <input type=\"text\" name=\"birthdate\" value=\"\" /> <br>" +
						"Birthplace: <input type=\"text\" name=\"birthplace\" value=\"\" /> <br>" +
						"Citizenship: <input type=\"text\" name=\"citizenship\" value=\"\" /> <br>" +
						"Select Class:" +
						"<select id=\"csel\" name= \"class\" onchange=\"cchange();\" onclick=\"cchange();\" onkeyup=\"cchange();\" onkeydown=\"cchange();\">" +
                                                    "<option value=\"eco\" selected=\"selected\">Economy Class</option>" +
						    "<option value=\"first\">First Class</option>" +
						    "<option value=\"spec\">Special Needs</option>" +
						    "<option value=\"inf\">Infant</option>" +
						 "</select>" +
                                                "<div id=\"ss\" style=\"display:none;\">Shaken not stirred: <input type=\"checkbox\" name=\"ss\" value=\"ss\" checked/></div>" +
                                                "<div id=\"dis\" style=\"display:none;\">Condition: <input type=\"text\" name=\"condition\" value=\"\" /></div>" + 
                                                "<div id=\"inf\" style=\"display:none;\">Months old: <select name=\"months\">" +
                                                    "<option value=\"1\">1</option>" +
                                                    "<option value=\"2\">2</option>" +
                                                    "<option value=\"3\">3</option>" +
                                                    "<option value=\"4\">4</option>" +
                                                    "<option value=\"5\">5</option>" +
                                                    "<option value=\"6\">6</option>" +
                                                    "<option value=\"7\">7</option>" +
                                                    "<option value=\"8\">8</option>" +
                                                    "<option value=\"9\">9</option>" +
                                                    "<option value=\"10\">10</option>" +
                                                    "<option value=\"11\">11</option>" +
                                                    "<option value=\"12\">12</option>" +
                                                "</select></div>" +
						"<div><input type=\"submit\" value=\"Submit\" ></div> <br>" +
						"</form>" + 
						"<hr>");


			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(
					"SELECT p.*, f.shaken_not_stirred AS ss, s.condition AS condition, i.months_old AS months " +
					"FROM Passenger p " +
                                        "LEFT JOIN FirstClass f ON f.pid = p.pid " + 
                                        "LEFT JOIN SpecialNeeds s ON s.pid = p.pid " + 
                                        "LEFT JOIN Infant i ON i.pid = p.pid");
			out.println("<table border=\"1\"><tr>" +
  						"<th>Pid</th>" +
    					"<th>Name</th>" +
    					"<th>Birthdate</th>" +
    					"<th>Birthplace</th>" +
    					"<th>Citizenship</th>" +
                                        "<th>Class</th>" +
                                        "<th>Class Info</th>" +
                                        "<th>Delete?</th>" +
                                        "</tr>");

			while (rset.next()) {
				out.println("<tr>");
				out.print (
					"<td>"+rset.getString("pid")+"</td>" +
					"<td>"+rset.getString("name")+"</td>" +
					"<td>"+rset.getString("birthdate").substring(0, 10)+"</td>" +
					"<td>"+rset.getString("birthplace")+"</td>" +
					"<td>"+rset.getString("citizenship")+"</td>" +
                                        "<td>"+getClassName(rset)+"</td>" +
                                        "<td>"+getClassInfo(rset)+"</td>" +
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

			//Format Date
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date tempdate = null;

			try{
				tempdate = formatter.parse(birthdate);
			} catch (ParseException e){
				response.sendRedirect("/csc370p1/passengers?er2=true");
				return;
			}
	
			java.sql.Date birthdateval = new java.sql.Date(tempdate.getTime());


			int pidval = 0;

			try {
   				pidval = Integer.parseInt(pid);
			} catch (NumberFormatException e) {
    			response.sendRedirect("/csc370p1/passengers?er1=true");
				return;
			}


			PreparedStatement insertPassengerStatement = conn.prepareStatement(
						"INSERT INTO Passenger(pid,name,birthdate,birthplace,citizenship) " +
						"VALUES( ?,?,?,?,?)");
			insertPassengerStatement.setInt(1,pidval);
			insertPassengerStatement.setString(2,name);
			insertPassengerStatement.setDate(3,birthdateval);
			insertPassengerStatement.setString(4,birthplace);
			insertPassengerStatement.setString(5,citizenship);
			insertPassengerStatement.executeUpdate();
			insertPassengerStatement.close();
                        
                        
                        PreparedStatement insertClassStatement;
                        String className = request.getParameter("class");
                        
                        if(className.equals("first")) {
                            insertClassStatement = conn.prepareStatement(
                                "INSERT INTO FirstClass (pid, shaken_not_stirred) VALUES (?, ?)");
                            insertClassStatement.setInt(1, pidval);
                            insertClassStatement.setString(2, request.getParameter("ss") == null ? "N" : "Y");
                            
                        } else if(className.equals("spec")) {
                            insertClassStatement = conn.prepareStatement(
                                "INSERT INTO SpecialNeeds (pid, condition) VALUES (?, ?)");
                            insertClassStatement.setInt(1, pidval);
                            insertClassStatement.setString(2, request.getParameter("condition"));
                            
                        } else if(className.equals("inf")) {
                            insertClassStatement = conn.prepareStatement(
                                "INSERT INTO Infant (pid, months_old) VALUES (?, ?)");
                            insertClassStatement.setInt(1, pidval);
                            
                            int monthsval = 1;
                            try {
   				monthsval = Integer.parseInt(request.getParameter("months"));
                            } catch (NumberFormatException e) {
                                monthsval = 1;
                            }
                            
                            insertClassStatement.setInt(2, monthsval);
                            
                        } else {
                            insertClassStatement = conn.prepareStatement(
                                "INSERT INTO EconomyClass (pid) VALUES (?)");
                            insertClassStatement.setInt(1, pidval);
                        }
                        
                        insertClassStatement.executeUpdate();
			insertClassStatement.close();
			
			response.sendRedirect("/csc370p1/passengers");
			

			
		} catch(SQLException ex) {
			out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
		}
	}
}