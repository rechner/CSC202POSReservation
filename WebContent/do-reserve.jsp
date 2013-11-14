<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.almworks.sqlite4java.*" %>
<%@ page import="java.util.List, reservation.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Register Confirmation</title>
</head>
<body>
<%
if (request.getMethod() == "POST") {
	
	String partyName = request.getParameter("name");
	String reservationTime = request.getParameter("time");
	String tableType = request.getParameter("type");
	int partySize = Integer.parseInt(request.getParameter("size"));
	
	DBI db = new DBI();
	
	if (tableType.equals("window")) {
		int freeTables = db.GetFreeWindowSeats(reservationTime);
		if ((freeTables - partySize) < 0) { %>
			<blockquote>Sorry,  there are only <%= freeTables %> tables available for this time slot.</blockquote>
	  <%} else {
		  	// add them to the queue:
			int a = db.MakeReservation(partyName, reservationTime, tableType, partySize);
			%><blockquote>Thank you for making an online reservation.
			Your confirmation code is <strong><%= a %></strong>.  Please
			write this code down in case you need to cancel or change your
			reservation.</blockquote><%
	  }
		
	}
	
	if (tableType.equals("booth")) {
		int freeTables = db.GetFreeBoothSeats(reservationTime);
		if ((freeTables - partySize) < 0) { %>
			<blockquote>Sorry,  there are only <%= freeTables %> tables available for this time slot.</blockquote>
	  <%} else {
		  	// add them to the queue:
			int a = db.MakeReservation(partyName, reservationTime, tableType, partySize);
			%><blockquote>Thank you for making an online reservation.
			Your confirmation code is <strong><%= a %></strong>.  Please
			write this code down in case you need to cancel or change your
			reservation.</blockquote><%
	  }
		
	}
	
	if (tableType.equals("bar")) {
		int freeTables = db.GetFreeBarSeats(reservationTime);
		out.println(tableType);
		out.println(freeTables);
		out.println(partySize);
		if ((freeTables - partySize) < 0) { %>
			<blockquote>Sorry,  there are only <%= freeTables %> tables available for this time slot.</blockquote>
	  <%} else {
		  	// add them to the queue:
			int a = db.MakeReservation(partyName, reservationTime, tableType, partySize);
			%><blockquote>Thank you for making an online reservation.
			Your confirmation code is <strong><%= a %></strong>.  Please
			write this code down in case you need to cancel or change your
			reservation.</blockquote><%
	  }
		
	}


	db.close();
} else {
	response.sendError(418, "I'm a teapot");
}
%>
</body>
</html>