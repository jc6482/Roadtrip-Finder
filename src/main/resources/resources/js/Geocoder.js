geocoder = new google.maps.Geocoder();

function codeAddress( ReturnVariable, address){
    geocoder.geocode( { 'address': address}, function(results, status) {
    	if (status == google.maps.GeocoderStatus.OK) {
		  ReturnVariable = [results[0].geometry.location.lat(), results[0].geometry.location.lng()];
		}
        else {
          alert("Geocode was not successful for the following reason: " + status);
        }
    });
    //console.log(address);
  }