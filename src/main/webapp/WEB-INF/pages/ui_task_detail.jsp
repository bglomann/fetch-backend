<%@ include file="taglibs.jsp" %>
<!DOCTYPE html>
<html xmlns = "http://www.w3.org/1999/xhtml">
<head>
	<title>Create a Task</title>
  <script src="${ctx}/resources/scripts/jquery.js"></script>
  <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDgZm26Ek5AZpET-mkX6ennYUTaY9lz72M&sensor=false"></script>
  <script language="JavaScript" src="${ctx}/resources/scripts/task_detail.js" type="text/javascript"></script>
  <link href="${ctx}/resources/styles/task_detail.css" rel="stylesheet" />
</head>
<body>
  
  <div> 
    <center><h1>Create a task</h1></center>
  <center><table border="1"><form id="mainForm" action="${ctx}/service/tasks" method="post">
  <tr>
    <td>Description: <input type="text" name="description" id="description" required><br></td>
  </tr>
  
  <tr>
  <td>Expense: <input type="text" name="expenses" id="expenses" required><br></td>
  </tr>

  <tr>
  <td>Pick-up address: <input type="text" name="pickup" id="pickup" required><br></td>
  </tr>

  <tr>
  <td>Drop off address: <input style ="placeholder: Hello" type="text" name="dropoff" id="dropoff" required><br></td>
  </tr>

  <tr>
  <td>Due by: <input type="text" name="dueDate" id="dueDate" readonly onClick="GetDate(this);" /></td>
  </tr>
  <p></p>
  
  <tr>
  <td><center><input type="submit" value="Submit"></center></td>
</tr>
</form></table>
</center>
	</div>
  <!-- the result will be rendered inside this div -->
  <center>
  <div id="result" style="font-size: 16pt"></div>
  </center>
<script>
function callREST(url, method, data, callback) {
	$.ajax({
		url: url,
        type: method,
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify(data)
	}).done(callback);
}

geocoder = new google.maps.Geocoder();

var addresses;
var task;

function codeAddress() {
	var address = addresses.shift();
	console.log("Owner: " + task.owner);
	console.log("Description: " + task.description);
	console.log("Expenses: " + task.expenses);
	console.log("Geocoding address: " + address);
    geocoder.geocode( { 'address': address }, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
    	  console.log("address=" + results[0].formatted_address);
    	  console.log("lat=" + results[0].geometry.location.lat());
    	  console.log("lng=" + results[0].geometry.location.lng());
    	  task.steps.push({
    		  address: results[0].formatted_address,
    		  latitude: results[0].geometry.location.lat(),
    		  longitude: results[0].geometry.location.lng()
    	  });
    	  
    	  if (addresses.length > 0) {
    		  codeAddress();
    	  } else {
    		  var url = $("#mainForm").attr( 'action' );
    			callREST(url, "POST", task,
    					function(result) {
    						if (result.error === undefined) {
        						$( "#result" ).empty().append("Task submitted successfully");
    						} else {
        						$( "#result" ).empty().append("Error: " + result.error);
    						}
    					});
    	  }
      } else {
        alert("Geocode was not successful for the following reason: " + status);
      }
    });
  }

/* attach a submit handler to the form */
$("#mainForm").submit(function(event) {
 
  /* stop form from submitting normally */
  event.preventDefault();
 
  /* get some values from elements on the page: */
  var $form = $( this ),
      url = $form.attr( 'action' );

  task = { owner:"anna@glomann.com",
			description: $("#description").val(),
			expenses: $("#expenses").val(),
			steps:[],
					dueDate: $("#dueDate").val() };
  addresses = [$("#pickup").val(),
               $("#dropoff").val()
               ];
  
  codeAddress();
});
</script>
</body>
</html>