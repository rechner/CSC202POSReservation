<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.almworks.sqlite4java.*" %>
<%@ page import="java.util.List, reservation.*" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="refresh" content="2;URL=admin.jsp">
<title>Reservation Admin</title>
</head>
<body>

<%
String id = request.getParameter("id");
if (!id.equals("")) {
	DBI db = new DBI();
	db.MarkPaid(id);
	db.close();
}
%>

</body>
</html>