// A script that detects when any Form is being submitted, or any component issues a request, involving a zone. 
// It reacts by overlaying the zone with a div of class "zone-loading-overlay". 
// The idea is that you should define that class, in css, to display an animated GIF.
//
// Based on a solution by Howard Lewis Ship at http://tapestryjava.blogspot.co.uk/2011/12/adding-ajax-throbbers-to-zone-updates.html .

define(["t5/core/dom", "t5/core/events", "t5/core/zone"], function(dom, events, zoneManager) {

	return function(params) {

		function addZoneOverlay() {
			var $zone = this.$;
			this.prepend("<div class='zone-loading-overlay'/>");
			var overlay = $zone.find("div:first");

			overlay.css({
				width : $zone.width() + "px",
				height : $zone.height() + "px"
			});
		}

		// Tell document to call addZoneOverlay whenever a zone-related refresh is requested.

		dom.onDocument(events.zone.refresh, addZoneOverlay);

	}

});
