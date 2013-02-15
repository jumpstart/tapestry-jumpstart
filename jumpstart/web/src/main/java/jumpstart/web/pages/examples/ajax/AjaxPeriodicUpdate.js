// A class that periodically issues an AJAX request to update the zone nominated by the "refreshTimeZone" EventLink.
// Based on a solution by Josh Canfield at 
// http://tapestry.1045711.n5.nabble.com/T5-Complexity-for-simple-things-where-is-the-documentation-tt3214893.html#a3215535 -->

PeriodicTimeZoneUpdater = Class.create( {
	
	initialize: function() {
		var eventLink = $('refreshTimeZone');

		// Use Prototype's PeriodicalExecuter to update eventLink's zone every 3 seconds, 4 times.

		var updatesCount = 0;

		new PeriodicalExecuter(function(pe) {
			if (updatesCount++ < 4) {
				// Do what the EventLink would do if we clicked on it.
				Tapestry.findZoneManager(eventLink).updateFromURL(eventLink.href);
			}
			else {
				pe.stop();
			}
		}, 3);
	}

})
	
// Extend the Tapestry.Initializer with a function that instantiates a PeriodicTimeZoneUpdater.

Tapestry.Initializer.periodicTimeZoneUpdater = function(spec) {
	new PeriodicTimeZoneUpdater();
}

