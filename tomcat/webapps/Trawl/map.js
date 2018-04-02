//*This code references the google heat maps API
//https://developers.google.com/maps/documentation/javascript/examples/layer-heatmap

// This example requires the Visualization library. Include the libraries=visualization
// parameter when you first load the API. For example:
// <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=visualization">

var map, heatmap;

// Generate map
function initMap(longi,lati) {
  map = new google.maps.Map(document.getElementById('map'), {
    zoom: 13,
    center: {lat: 37.775, lng: -122.434},
    mapTypeId: 'satellite'
  });

  heatmap = new google.maps.visualization.HeatmapLayer({
    data: getPoints(longi,lati),
    map: map
  });
}

// Check if a heatmap already exists
function toggleHeatmap() {
  heatmap.setMap(heatmap.getMap() ? null : map);
}

// Initialize colour scheme for heatmap gradient
function changeGradient() {
	var gradient = [
		‘rgba(0, 255, 255, 0)’,
		‘rgba(0, 255, 255, 1)’,
		‘rgba(0, 191, 255, 1)’,
		‘rgba(0, 127, 255, 1)’,
		‘rgba(0, 63, 255, 1)’,
		‘rgba(0, 0, 255, 1)’,
		‘rgba(0, 0, 223, 1)’,
		‘rgba(0, 0, 191, 1)’,
		‘rgba(0, 0, 159, 1)’,
		‘rgba(0, 0, 127, 1)’,
		‘rgba(0, 63, 91, 1)’,
		‘rgba(0, 127, 63, 1)’,
		‘rgba(0, 191, 10, 1)’,
		‘rgba(0, 255, 0, 1)’
		]
	heatmap.set('gradient', heatmap.get('gradient') ? null : gradient);
}

// Set heatmap radius about each point
function changeRadius() {
  heatmap.set('radius', heatmap.get('radius') ? null : 20);
}

// Set opacity of colours
function changeOpacity() {
  heatmap.set('opacity', heatmap.get('opacity') ? null : 0.2);
}


// Input latitude and longitude into map points
function getPoints(latitude, longitude) {
	var result = [];
	for (int i = 0 ; i < latitude.length ; i++) {
		result.push( new google.maps.LatLng(latitude[i], longitude[i]));
	}
	
  return result;
}