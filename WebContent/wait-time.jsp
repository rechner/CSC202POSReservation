<%@page import="java.util.Random"%>
<%@ page import="com.almworks.sqlite4java.*" %>
<%@ page import="java.util.List, reservation.*" %>
<%@ page language="java" contentType="text/plain; charset=UTF-8"  pageEncoding="UTF-8"%>
<% 

	String time = request.getParameter("time");	
	DBI db = new DBI();

	//request.getParameter("time");
	out.print("{\"wait_time\": ");
	int wait = 5 + (int) (Math.random() * 15);
	out.print(wait);
	out.print(", \"window\": ");
	out.print(db.GetFreeWindowSeats(time));
	//out.print( 1 + (int) (Math.random() * 100));
	out.print(", \"booth\": ");
	out.print(db.GetFreeBoothSeats(time));
	//out.print( 1 + (int) (Math.random() * 100));
	out.print(", \"bar\": ");
	out.print(db.GetFreeBarSeats(time));
	//out.print( 1 + (int) (Math.random() * 100));
	out.print("}");
%>