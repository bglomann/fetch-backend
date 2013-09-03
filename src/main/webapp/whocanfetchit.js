var getRequest = function(){
 	var xhr = new XMLHttpRequest();
	xhr.open("GET","https://www.sandbox.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize" );

	console.log(xhr.status);
	console.log(xhr.statusText);
 	}

 	getRequest();

var greeting = function()
{
	console.log("This is a greeting.");
}

greeting();

$(document).ready(function(){
    $('p').animate(easing);
    
})
 

	