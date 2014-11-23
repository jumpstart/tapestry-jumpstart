// For an element (elementId), listens to it for a given event (clientEvent), and responds by issuing 
// an AJAX request (listenerURI) with the element value appended as a query string, 
// to update a zone (zoneId).
//
// Based on http://tinybits.blogspot.com/2010/03/new-and-better-zoneupdater.html
// and some help from Inge Solvoll.

define(["jquery", "t5/core/zone"], function($, zoneManager) {

	return function(elementId, clientEvent, listenerURI, zoneElementId) {
		var $element = $("#" + elementId);

		if (clientEvent) {
			$element.on(clientEvent, updateZone);
		}
	
		function updateZone() {
			var listenerURIWithValue = listenerURI;
	
			if ($element.val()) {
				listenerURIWithValue = appendQueryStringParameter(listenerURIWithValue, 'param', $element.val());
			}
	
			zoneManager.deferredZoneUpdate(zoneElementId, listenerURIWithValue);
		}
	}

	function appendQueryStringParameter(url, name, value) {
		if (url.indexOf('?') < 0) {
			url += '?'
		}
		else {
			url += '&';
		}
		value = escape(value);
		url += name + '=' + value;
		return url;
	}

});