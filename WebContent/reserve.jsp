<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.almworks.sqlite4java.*" %>
<%@ page import="java.util.List, reservation.*" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Reservation</title>
</head>
<body>
<div>The current wait time is <strong><span id="wait">?</span> minutes</strong></div>
  <form action="do-reserve.jsp" method=post class=add-entry>
    <dl>
      <dt>Name:</dt>
      <dd><input type=text size=30 name=name></dd>
      <dt>Reservation Time:</dt>
      <dd><input id="time" name="time" type=time name=time maxlength="5" style="width: 70px"> 
      	<button type="button" onClick="get_time();">Check Availability</button></dd>
      <dt>Seat Preference:</dt>
      <dd>
      	<select name="type">
      	  <option value="window">Window Seat</option>
      	  <option value="booth">Booth Seat</option>
      	  <option value="bar">Bar Stool</option>
      	</select>
      </dd>
      <dt>Party of:</dt>
      <!-- <dd><input type=number name=number ></dd>-->
      <dd><input name="size" type="range" min="1" max="20" value="2" onchange="rangevalue.value=value"></dd>
      <dd> <output id="rangevalue">2</output> </dd>
    </dl>
    <input type="submit">
    <a href="cancel.jsp">Need to cancel your reservation?</a>
  </form>
  
  <% DBI db = new DBI();  %>
  
  <div>
  	<p>There are <strong><span id="window">?</span> / <%= db.GetConfigEntry("windows_max") %></strong> window seats remaining<br>
  	There are <strong><span id="booth">?</span> / <%= db.GetConfigEntry("booths_max") %></strong> booth seats remaining<br>
  	There are <strong><span id="bar">?</span> / <%= db.GetConfigEntry("bar_max") %></strong> bar stool seats remaining<br>
  </div>

<script>
function get_time() {
	var http = new XMLHttpRequest();
	var time = document.getElementById("time").value;
	if ( time != "" ) {
		var params = "time=" + time;
		http.open("POST", "wait-time.jsp", true);

		//Send the proper header information along with the request
		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

		http.onreadystatechange = function() {//Call a function when the state changes.
		    if(http.readyState == 4 && http.status == 200) {
		        var json = http.responseText;
		        console.log(json);
		        var obj = JSON.parse(json);
		        document.getElementById("wait").innerHTML = obj.wait_time;
		        document.getElementById("window").innerHTML = obj.window;
		        document.getElementById("booth").innerHTML = obj.booth;
		        document.getElementById("bar").innerHTML = obj.bar;
		        
		    }
		}
		http.send(params);
	} else {
		alert("Please enter a time");
	}
}
</script>

</body>
</html>