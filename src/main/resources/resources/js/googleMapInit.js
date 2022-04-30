var map = null;
var boxpolys = null;
var distance = null; // km
var routeBoxer = null;
const MAX_DELAY = 500;


initMap();
	
function initMap() {
  var origin_place_id = null;
  var destination_place_id = null;
  var origin_address = document.getElementById("origin-input").value;
  var destination_address = document.getElementById("destination-input").value;
  
  var origin_input = document.getElementById('origin-input');
  var destination_input = document.getElementById('destination-input');
  var distance_input = document.getElementById('distance-input');
  var submit_input = document.getElementById('boxDataForm');
  var modes = document.getElementById('mode-selector');
  var travel_mode = google.maps.TravelMode.DRIVING;
  var directionsService = new google.maps.DirectionsService;
  var directionsDisplay = new google.maps.DirectionsRenderer;
  var origin_autocomplete = new google.maps.places.Autocomplete(origin_input);
  var destination_autocomplete = new google.maps.places.Autocomplete(destination_input);
  
  map = new google.maps.Map(document.getElementById('map'), {
      mapTypeControl: false,
	  //Uncomment to change map to a satellite view.
	  //mapTypeId: google.maps.MapTypeId.HYBRID,
      center: new google.maps.LatLng(31.5497, -97.1147), //Baylor
      zoom: 15,
	  //Paste map style after "styles:"
	  styles: [{"featureType":"water","elementType":"geometry","stylers":[{"visibility":"on"},{"color":"#aee2e0"}]},{"featureType":"landscape","elementType":"geometry.fill","stylers":[{"color":"#abce83"}]},{"featureType":"poi","elementType":"geometry.fill","stylers":[{"color":"#769E72"}]},{"featureType":"poi","elementType":"labels.text.fill","stylers":[{"color":"#7B8758"}]},{"featureType":"poi","elementType":"labels.text.stroke","stylers":[{"color":"#EBF4A4"}]},{"featureType":"poi.park","elementType":"geometry","stylers":[{"visibility":"simplified"},{"color":"#8dab68"}]},{"featureType":"road","elementType":"geometry.fill","stylers":[{"visibility":"simplified"}]},{"featureType":"road","elementType":"labels.text.fill","stylers":[{"color":"#5B5B3F"}]},{"featureType":"road","elementType":"labels.text.stroke","stylers":[{"color":"#ABCE83"}]},{"featureType":"road","elementType":"labels.icon","stylers":[{"visibility":"off"}]},{"featureType":"road.local","elementType":"geometry","stylers":[{"color":"#A4C67D"}]},{"featureType":"road.arterial","elementType":"geometry","stylers":[{"color":"#9BBF72"}]},{"featureType":"road.highway","elementType":"geometry","stylers":[{"color":"#EBF4A4"}]},{"featureType":"transit","stylers":[{"visibility":"off"}]},{"featureType":"administrative","elementType":"geometry.stroke","stylers":[{"visibility":"on"},{"color":"#87ae79"}]},{"featureType":"administrative","elementType":"geometry.fill","stylers":[{"color":"#7f2200"},{"visibility":"off"}]},{"featureType":"administrative","elementType":"labels.text.stroke","stylers":[{"color":"#ffffff"},{"visibility":"on"},{"weight":4.1}]},{"featureType":"administrative","elementType":"labels.text.fill","stylers":[{"color":"#495421"}]},{"featureType":"administrative.neighborhood","elementType":"labels","stylers":[{"visibility":"off"}]}]
  });
  
  
  
  routeBoxer = new RouteBoxer();
  directionsDisplay.setMap(map);
  directionsDisplay.setPanel(document.getElementById('directionsPanel'));

  map.controls[google.maps.ControlPosition.TOP_LEFT].push(origin_input);
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(destination_input);
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(distance_input);
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(submit_input);
  

  
  origin_autocomplete.bindTo('bounds', map);
  
  destination_autocomplete.bindTo('bounds', map);
  
    $(origin_input).keypress(function(e) {
		if (e.which == 13) {
		google.maps.event.trigger(autocomplete, 'place_changed');
		return false;
		}
    });
	
	$(destination_input).keypress(function(e) {
		if (e.which == 13) {
		google.maps.event.trigger(autocomplete, 'place_changed');
		return false;
		}
    });

  // Sets a listener on a radio button to change the filter type on Places
  // Autocomplete.
  function setupClickListener(id, mode) {
    var radioButton = document.getElementById(id);
    radioButton.addEventListener('click', function() {
      travel_mode = mode;
    });
  }

  setupClickListener('destination-input', google.maps.TravelMode.DRIVING);  

  function expandViewportToFitPlace(map, place) {
    if (place.geometry.viewport) {
      map.fitBounds(place.geometry.viewport);
    } else {
      map.setCenter(place.geometry.location);
      map.setZoom(17);
    }
  }
	

	origin_autocomplete.addListener('place_changed', function() {
		var place = origin_autocomplete.getPlace();
		console.log(place);
		if (!place.geometry) {
			window.alert("Autocomplete's returned place contains no geometry");
			return;
		}
		expandViewportToFitPlace(map, place);

		// If the place has a geometry, store its place ID and route if we have
		// the other place ID
		origin_place_id = place.place_id;
		origin_address = place.formatted_address;
		route(origin_address, destination_address, travel_mode,
		directionsService, directionsDisplay);
	});
	
	
	destination_autocomplete.addListener('place_changed', function() {
		var place = destination_autocomplete.getPlace();
		if (!place.geometry) {
			window.alert("Autocomplete's returned place contains no geometry");
		return;
		}
		console.log(place);
		expandViewportToFitPlace(map, place);

		// If the place has a geometry, store its place ID and route if we have
		// the other place ID
		destination_place_id = place.place_id;
		destination_address = place.formatted_address;
		route(origin_address, destination_address, travel_mode,
		  directionsService, directionsDisplay);
	});


	$(document).ready(function(){
		$("#calculateMyRoadtrip").click(function(){
			if(selectedLocations.length <2){
				alert("not enough locations");
				return false;
			}
			clearMarkers(markers);
			clearMarkers(BouncyMarkers);
			markers = [];
			getAddressList( origin_address, destination_address, travel_mode, directionsService, directionsDisplay);
			console.log("submit clicked");
		});
	});
	route(origin_address, destination_address, travel_mode, directionsService, directionsDisplay);

}

function route(origin_place_id, destination_place_id, travel_mode, directionsService, directionsDisplay) {
	console.log(origin_place_id);
	console.log(destination_place_id);
	if (!origin_place_id || !destination_place_id) {
      return;
    }
    directionsService.route({
      origin: origin_place_id,
      destination: destination_place_id,
      travelMode: travel_mode
    }, function(response, status) {
      if (status === google.maps.DirectionsStatus.OK) {
      	
      	console.log("Logging Directions Response");
        directionsDisplay.setDirections(response);
        console.log(response);
		
		
		console.log("Calling getRouteForBoxes()");
		// Clear any previous route boxes from the map
		clearBoxes();
		// Convert the distance to box around the route from miles to km
		distance = parseFloat(document.getElementById("distance-input").value) * 1.609344;
		console.log("distance = " + distance);
		
		// Make the directions request
		//Path overview
		var path = response.routes[0].overview_path;
		var pathToString = [];
		
		for (var i = 0; i < path.length; i++) {
		  pathToString.push(path[i].lat());
		  pathToString.push(path[i].lng());
        }
        
        document.getElementById('tripInfo').value = pathToString.toString();
        console.log(document.getElementById('tripInfo').value);
        
        
		console.log("path is ");
		console.log(path);
		var boxes = routeBoxer.box(path, distance);
		
		//send data to controller
		document.getElementById('boxData').value = boxes;
		console.log(document.getElementById('boxData').value);
		//drawBoxes(boxes);
		console.log("Routing Done");
	  }
	  else {
        window.alert('Directions request failed due to ' + status);
      }
    });
  }


// Draw the array of boxes as polylines on the map
function drawBoxes(boxes) {
    boxpolys = new Array(boxes.length);
    for (var i = 0; i < boxes.length; i++) {
		boxpolys[i] = new google.maps.Rectangle({
			bounds: boxes[i],
			fillOpacity: 0,
			strokeOpacity: 1.0,
			strokeColor: '#000000',
			strokeWeight: 1,
			map: map
	    });
    }
}

// Clear boxes currently on the map
function clearBoxes() {
  if (boxpolys != null) {
	for (var i = 0; i < boxpolys.length; i++) {
	  boxpolys[i].setMap(null);
	}
  }
  boxpolys = null;
}


/* Open a pop up map
var mapWindow = null;
if(mapWindow == null){
		mapWindow = window.open("../views/fullscreenMap.html", "", "width=200,height=100");
		console.log("mapWindow " + mapWindow);
}
*/


//Use geocoder to get all adresses. Once All addresses are acquired it is safe to call this function.
function calculateAndDisplayRoute(addressList, origin_address, destination_address, travel_mode, directionsService, directionsDisplay) {
	//Add list of locations to waypoints array.
  var waypts = addressList;  
	//Call directions service to create route through all waypoints
	console.log(origin_address);
	console.log(destination_address);
	console.log(google.maps.TravelMode.DRIVING);
	console.log(waypts);
  directionsService.route({
    origin: origin_address,
    destination: destination_address,
    travelMode: google.maps.TravelMode.DRIVING,
    waypoints: waypts,
    optimizeWaypoints: true,
  }, function(response, status) {
	  //Edit this code to diplay route on map.
    if (status === google.maps.DirectionsStatus.OK) {
      directionsDisplay.setDirections(response);
    } else {
      window.alert('Directions request failed due to ' + status);
    }
  });
  
}

//This function will acquire one address per call.
function geocodeLatLng(addressList, geocoder, map, location) {
  var input = location.toString();
  input =  input.replace("(", "");
  input = input.replace(")", "");
  var latlngStr = input.split(',', 2);
  var latlng = {lat: parseFloat(latlngStr[0]), lng: parseFloat(latlngStr[1])};
  geocoder.geocode({'location': latlng}, function(results, status) {
    if (status === google.maps.GeocoderStatus.OK) {
      if (results[1]) {
		addressList.push({
        location: results[1].formatted_address,
        stopover: true
		});
      } else {
        window.alert('No results found');
      }
    } else {
      window.alert('Geocoder failed due to: ' + status);
	  console.log("Could not locate the following");
	  console.log(location);
    }
  });
  return 1;
}


//This function will call the geocodeLatLng function repeatedly until all addresses are acquired.
//Then it will call calculateAndDisplayRoute.
function getAddressList( origin_address, destination_address, travel_mode, directionsService, directionsDisplay, giveMe){
	var addressList = [];
	for (var i = 0; i < selectedLocations.length; i++) {
	    console.log(Locations["locations"][selectedLocations[i]][1].toString());
	    sleep(500);
	    geocodeLatLng(addressList, geocoder, map, Locations["locations"][selectedLocations[i]][1].toString());
	    placeMarker(Locations["locations"][selectedLocations[i]]);
    }

	function sleep(milliseconds) {
	  var start = new Date().getTime();
	  for (var i = 0; i < 1e7; i++) {
		if ((new Date().getTime() - start) > milliseconds){
		  break;
		}
	  }
	}
	
	function giveMe(){
		console.log("giveMe");
		console.log(addressList);
		calculateAndDisplayRoute(addressList, origin_address, destination_address, travel_mode, directionsService, directionsDisplay)
	}
	
	setTimeout(giveMe, 2000);
}



















