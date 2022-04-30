var map = null;
var boxpolys = null;
var distance = null; // km
var routeBoxer = null;
const MAX_DELAY = 500;
	
function initMap() {
  var origin_place_id = null;
  var destination_place_id = null;
  var travel_mode = google.maps.TravelMode.DRIVING;
  map = new google.maps.Map(document.getElementById('map'), {
    mapTypeControl: false,
	mapTypeId: google.maps.MapTypeId.HYBRID,
    center: {lat: 31.549708, lng: -97.114810}, //Baylor
    zoom: 15
  });
  
  
  var directionsService = new google.maps.DirectionsService;
  var directionsDisplay = new google.maps.DirectionsRenderer;
  routeBoxer = new RouteBoxer();
  directionsDisplay.setMap(map);
  directionsDisplay.setPanel(document.getElementById('right-panel'));
  
  
  var origin_input = document.getElementById('origin-input');
  var destination_input = document.getElementById('destination-input');
  var distance_input = document.getElementById('distance-input');
  var submit_input = document.getElementById('boxDataForm');
  
  
  var modes = document.getElementById('mode-selector');

  map.controls[google.maps.ControlPosition.TOP_LEFT].push(origin_input);
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(destination_input);
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(distance_input);
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(submit_input);
  

  var origin_autocomplete = new google.maps.places.Autocomplete(origin_input);
  origin_autocomplete.bindTo('bounds', map);
  var destination_autocomplete = new google.maps.places.Autocomplete(destination_input);
  destination_autocomplete.bindTo('bounds', map);

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
    if (!place.geometry) {
      window.alert("Autocomplete's returned place contains no geometry");
      return;
    }
    expandViewportToFitPlace(map, place);

    // If the place has a geometry, store its place ID and route if we have
    // the other place ID
    origin_place_id = place.place_id;
    route(origin_place_id, destination_place_id, travel_mode,
          directionsService, directionsDisplay);
  });

  destination_autocomplete.addListener('place_changed', function() {
    var place = destination_autocomplete.getPlace();
    if (!place.geometry) {
      window.alert("Autocomplete's returned place contains no geometry");
      return;
    }
    expandViewportToFitPlace(map, place);

    // If the place has a geometry, store its place ID and route if we have
    // the other place ID
    destination_place_id = place.place_id;
    route(origin_place_id, destination_place_id, travel_mode,
          directionsService, directionsDisplay);
  });


  function route(origin_place_id, destination_place_id, travel_mode,
                 directionsService, directionsDisplay) {
    if (!origin_place_id || !destination_place_id) {
      return;
    }
    directionsService.route({
      origin: {'placeId': origin_place_id},
      destination: {'placeId': destination_place_id},
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

initMap();