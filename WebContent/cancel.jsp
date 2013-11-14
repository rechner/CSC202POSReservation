<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.almworks.sqlite4java.*" %>
<%@ page import="java.util.List, reservation.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cancel Reservation</title>
</head>
<body>

<%
String id = request.getParameter("confirm");
if ((request.getMethod() == "POST") ||
		((request.getMethod() == "GET") && (id != null))) {
	out.println("<!-- post -->");
	
	try {
		
		Integer.parseInt(id);
		DBI db = new DBI();
		ReservationObject pop = db.GetReservation(id);
		if (pop != null) {
			db.CancelReservation(id);
			out.print("<blockquote>Reservation cancelled for ");
			out.print(pop.getPartyName() + ", party of ");
			out.print(pop.getPartySize() + " for ");
			out.print(pop.getReservationTime());
		} else {
			out.println("<blockquote>Invalid confirmation number.</blockquote>");
		}
		
	} catch (java.lang.NumberFormatException e) {
		out.println("<blockquote>Please enter a valid confirmation number</blockquote>");
	}
}  
%>

<p>Please enter your confirmation number to cancel your reservation:</p>
<form action="cancel.jsp" method=post>
	<input type="number" name="confirm" id="confirm">
	<input type="submit">
</form>
</body>
</html>