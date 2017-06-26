var origin_input = document.getElementById('splace');
var destination_input = document.getElementById('eplace');
 
 
var origin_autocomplete = new google.maps.places.Autocomplete(splace);
var destination_autocomplete = new google.maps.places.Autocomplete(eplace);

 
 origin_autocomplete.addListener('place_changed', function() {
	var place = origin_autocomplete.getPlace();
	if (!place.geometry) {
	window.alert("Autocomplete's returned place contains no geometry");
	return;
	}
	var apiKey="AIzaSyA1yn9ChLO32EGrefSQLu3ha_OJ_tLpxcg";
	var maxSize =400;
	var photoReference = place.photoreference;
	var DetailsRequest = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place + "&key=" key;
	var PhotoRequest = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + maxSize + "&photoreference="+ photoReference +"&key="+ apiKey;
	
	
	var val = document.getElementById('myimg').value,
	src = request,
	img = document.createElement('img');
	img.src = src;
	document.body.appendChild(img);
	
	
});


destination_autocomplete.addListener('place_changed', function() {
	var place = destination_autocomplete.getPlace();
	if (!place.geometry) {
		window.alert("Autocomplete's returned place contains no geometry");
	return;
	}
});

var apiKey="AIzaSyA1yn9ChLO32EGrefSQLu3ha_OJ_tLpxcg";
var maxSize =400;
var photoReference;
var request = "https://maps.googleapis.com/maps/api/place/photo?maxWidth=" + maxSize + "&photoreference="+ photoReference +"&key="+ apiKey;
 