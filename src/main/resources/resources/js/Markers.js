var markers = [];


/*********************
Test Code
*********************/

var myCenter=new google.maps.LatLng(31.549708, -97.114810);
var options = {
	map: map,
	content: '<div class="myLabel">Latitude: ' + 31.549708 +
		'<br>Longitude: ' + -97.114810 +
		'<br> Rating: ' + "AddRating" +
		'<br> Category: ' + "AddCategory" +
		'<br> Photo: ' + "AddPhoto" + '</div>',
	position: new google.maps.LatLng(-32.0, 149.0),
	shadowStyle: 1,
	padding: 0,
	backgroundColor: 'rgb(252, 252, 252)',
	borderRadius: 5, 
	arrowSize: 10,
	borderWidth: 5,
	borderColor: '#0A4F63',
	disableAutoPan: true,
	hideCloseButton: false,
	arrowPosition: 30,
	backgroundClassName: 'transparent',
	arrowStyle: 1
}

var marker=new google.maps.Marker({
	position:myCenter,
	icon: '../images/fun_pin.png'
	//animation:google.maps.Animation.BOUNCE
});

//marker.setMap(map);

//Starting marker is centered at Baylor.
google.maps.event.addListener(marker,'click',function(event) {
	map.setZoom(15);
	map.setCenter(marker.getPosition());
	//placeInfoWindow(marker, event.latLng);
	placeInfoBubble(marker, event.latLng, options);
});

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

/* There is a Locations object that holds a list of Data,
each is a string of the format: "name, lat, lng"
*/

/*String theString = this.theName + C + this.theLat + C + this.theLng + C + this.theAddress + C + theImageURL + C + thePhoneNumber + C + theCategory + C + theYelpURL;*/

function add(data){
	var latlngs = [];
	data = data.replace('[','');
	data = data.replace(']','');	
	
	//Breaks if name of location contains commas
	var tokens = data.split("XYZ@@#");
	for(var i = 0; i < tokens.length;){
		var theName = tokens[i];
		if(theName[0] == (','))
			theName = theName.substring(1);
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
		var theRating = tokens[i];
		i++;
		latlngs.push([theName , new google.maps.LatLng(theLat, theLng), theAddress, theImageURL, thePhoneNumber, theCategory, theYelpURL, theRating ]);
	}
	console.log(latlngs);
	return latlngs;
}

function placeInfoBubble(marker, location, options){
	console.log(location);
	
	options["content"] = '<div class ="mypin"> YelpURL: <a href="' + location[6] +'" target="_blank" >' + location[0] + '</a>' +
		'<br> Rating: ' + location[7] +
		'<br> Phone: ' + location[4] + 
		'<br> Address: ' + location[2] + '</div>';
	infoBubble = new InfoBubble(options);
	infoBubble.open(map, marker);
	
}


//Add a marker anywhere the map is clicked. 
/*
google.maps.event.addListener(map, 'click', function(event) {
	placeMarker(event.latLng);
});
*/

//If a marker is clicked, open the info window.
function placeMarker(location) {
	var marker = new google.maps.Marker({
		position: location[1],
		map: map,
		icon: '../images/fun_pin.png'
		//animation:google.maps.Animation.BOUNCE
	});
	markers.push(marker);
	google.maps.event.addListener(marker,'click',function(event) {
		placeInfoBubble(marker, location, options);
	});
}

function placeBouncyMarker(index, location, BouncyMarkers) {
	var marker = new google.maps.Marker({
		position: location[1],
		map: map,
		icon: '../images/fun_pin.png',
		animation:google.maps.Animation.BOUNCE
	});
	BouncyMarkers[index] = marker;
	google.maps.event.addListener(marker,'click',function(event) {
		placeInfoBubble(marker, location, options);
	});
}

function changeMarker(index, locations, BouncyMarkers){
	placeBouncyMarker(index, locations[index], BouncyMarkers);	
}

function populateMapWithPlaces(locations){
	for(var i = 0; i < locations.length; i++){
		placeMarker(locations[i]);		
	}	
}

// Sets the map on all markers in the array.
function setMapOnAll(markers, map) {
  for (var i = 0; i < markers.length; i++) {
  		if(markers[i] != null){
    		markers[i].setMap(map);
    	}
  }
}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers(markers) {
  setMapOnAll(markers, null);
}

function deleteMarker(index, markers){
	console.log(index);
	console.log(markers[index]);
	markers[index].setMap(null);
}

// Shows any markers currently in the array.
function showMarkers() {
  setMapOnAll(map);
}

// Deletes all markers in the array by removing references to them.
function deleteMarkers() {
  clearMarkers();
  markers = [];
}
