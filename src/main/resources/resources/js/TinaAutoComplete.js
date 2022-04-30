var origin_input = document.getElementById('splace');
    var destination_input = document.getElementById('eplace');
	var origin_autocomplete = new google.maps.places.Autocomplete(origin_input);
    var destination_autocomplete = new google.maps.places.Autocomplete(destination_input);
    
	origin_autocomplete.addListener('place_changed', function() {
	var place = origin_autocomplete.getPlace();
	
	if (!place.geometry) {
	window.alert("Autocomplete's returned place contains no geometry");
	return;
	}
	

	// If the place has a geometry, store its place ID and route if we have
	// the other place ID
	origin_place_id = place.place_id;
	
	});
	
	destination_autocomplete.addListener('place_changed', function() {
	var place = destination_autocomplete.getPlace();
	if (!place.geometry) {
		window.alert("Autocomplete's returned place contains no geometry");
	return;
	}
	

	// If the place has a geometry, store its place ID and route if we have
	// the other place ID
	destination_place_id = place.place_id;
	
	
	});
	
	$(origin_input).keypress(function(e) {
		if (e.which == 13) {
			e.preventDefault(); 
			google.maps.event.trigger(origin_autocomplete, 'place_changed');
			return false;
		}
    });
	
	$(destination_input).keypress(function(e) {
		if (e.which == 13) {
			e.preventDefault(); 
			google.maps.event.trigger(destination_autocomplete, 'place_changed');
			return false;
		}
    });