


/*********************
Test Code
*********************/
var myCenter=new google.maps.LatLng(31.549708, -97.114810);

var marker=new google.maps.Marker({
	position:myCenter,
	animation:google.maps.Animation.BOUNCE
});

marker.setMap(map);

//Starting marker is centered at Baylor.
google.maps.event.addListener(marker,'click',function(event) {
	map.setZoom(15);
	map.setCenter(marker.getPosition());
	placeInfoWindow(marker, event.latLng);
});
var Locations = {

}



/*********************************
TO DO
ADD WAYPOINTS TO DIRECTIONS SERVICE
ADD ALL LOCATIONS TO MAP
CREATE LIST OF ATTRACTIONS TO DISPLAY
CREATE LIST OF PLANNED VISITS TO DISPLAY
SAVE DIRECTIONS TO A FILE.
*******************************/




/************************
Function Definitions
************************/
/*String theString = this.theName + C + this.theLat + C + this.theLng + C + this.theAddress + C + theImageURL + C + thePhoneNumber + C + theCategory + C + theYelpURL;*/


function add(locations){
	var latlngs = [];
	locations.replace('[','');
	locations.replace(']','');	
	var tokens = locations.split(",");
	for(var i = 0; i < tokens.length;){
		var theName = tokens[i];
		i++;
		var theLat = tokens[i];
		i++;
		var theLng = tokens[i]; 
		i++;
		var theAddress = tokens[i];
		i++;
		var theImageURL = tokens[i];
		i++;
		var thePhoneNumber = tokens[i];
		i++;
		var theCategory = tokens[i];
		i++;
		var theYelpURL = tokens[i];
		i++;
		latlngs.push(new google.maps.LatLng(lat, lng));
	}
	return latlngs;
}



function placeInfoWindow(marker, location){
	console.log(location);
	var infowindow = new google.maps.InfoWindow({
		content: 'Latitude: ' + location.lat() +
		'<br>Longitude: ' + location.lng()
	});
	infowindow.open(map, marker);
}


//Add a marker anywhere the map is clicked. 
google.maps.event.addListener(map, 'click', function(event) {
	placeMarker(event.latLng);
});

//If a marker is clicked, open the info window.
function placeMarker(location) {
	var marker = new google.maps.Marker({
		position: location,
		map: map,
		animation:google.maps.Animation.BOUNCE
	});
	google.maps.event.addListener(marker,'click',function(event) {
		placeInfoWindow(marker, event.latLng);
	});
}

function populateMapWithPlaces(locations){
	for(var i = 0; i < locations.length; i++){
		placeMarker(locations[i]);		
	}	
}

