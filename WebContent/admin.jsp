<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.almworks.sqlite4java.*" %>
<%@ page import="java.util.List, reservation.*" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Reservation Admin</title>
</head>
<body>

<!-- dump the waiting queue -->
<%
DBI db = new DBI();
List<ReservationObject> queue = db.GetWaitingReservations();
List<ReservationObject> seated = db.GetSeated();
db.close();
%>
<h1>Reservation Admin</h1>
<form action="do-reserve.jsp" method=post class=add-entry>
    <dl>
      <dt>Name:</dt>
      <dd><input type=text size=30 name=name></dd>
      <dt>Reservation Time:</dt>
      <dd><input id="time" name="time" type=time name=time maxlength="5" style="width: 70px"> 
      <dt>Seat Preference:</dt>
      <dd>
      	<select name="type">
      	  <option value="window">Window Seat</option>
      	  <option value="booth">Booth Seat</option>
      	  <option value="bar">Bar Stool</option>
      	</select>
      </dd>
      <dt>Party of:</dt>
      <dd><input name="size" type="number"></dd>
    </dl>
    <input type="submit">
  </form>

<hr>
<h1>Reserved Tables</h1>
<table border=1>
	<tr>
		<th>Party Name</th>
		<th>Party of</th>
		<th>Reservation Time</th>
		<th>Status</th>
		<th>Arrived</th>
		<th>Cancel</th>
	</tr>
<%
for (ReservationObject row : queue ) {
	%>
	<tr>
		<td><%= row.getPartyName() %></td>
		<td><%= row.getPartySize() %></td>
		<td><%= row.getReservationTime() %></td>
		<td><%= row.getStatus() %></td>
		<td><a href="arrive.jsp?id=<%= row.getId() %>">Mark Arrived</a></td>
		<td><a href="cancel.jsp?confirm=<%= row.getId() %>">Cancel</a></td>
	</tr>
	<%
}
%>
</table>

<h1>Seated</h1>
<table border=1>
	<tr>
		<th>Party Name</th>
		<th>Party of</th>
		<th>Reservation Time</th>
		<th>Status</th>
		<th>Arrival Time</th>
		<th>Paid Bill</th>
	</tr>
<%
for (ReservationObject row : seated ) {
	%>
	<tr>
		<td><%= row.getPartyName() %></td>
		<td><%= row.getPartySize() %></td>
		<td><%= row.getReservationTime() %></td>
		<td><%= row.getStatus() %></td>
		<td><%= row.getSeatedTime() %></td>
		<td><a href="paybill.jsp?id=<%= row.getId() %>">Pay Bill</a></td>
	</tr>
	<%
}
%>
</table>

</body>
</html>