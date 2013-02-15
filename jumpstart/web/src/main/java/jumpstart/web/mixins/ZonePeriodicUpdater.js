// A class that periodically issues an AJAX request (spec.eventURL) to update a zone (spec.zoneElementId).
// Based on a solution by Taha Hafeez at
// http://tapestry.1045711.n5.nabble.com/T5-Complexity-for-simple-things-where-is-the-documentation-tt3214893.html

ZonePeriodicUpdater = Class.create( {

	initialize : function(spec) {
		this.zoneElement = $(spec.zoneElementId).getStorage();
		this.eventURL = spec.eventURL;
		this.frequencySecs = spec.frequencySecs;
		this.maxUpdates = spec.maxUpdates;

		// Use Prototype's PeriodicalExecuter to run updateZone() every 3 seconds, this.maxUpdates times.

		var updatesCount = 0;

		new PeriodicalExecuter(function(pe) {
			if (updatesCount++ < this.maxUpdates) {
				this.updateZone();
			}
			else {
				pe.stop();
			}
		}.bind(this), this.frequencySecs);

	},

	updateZone : function() {
		var zoneManager = this.zoneElement.zoneManager;
		if(!zoneManager){
			return;
		}

		zoneManager.updateFromURL(this.eventURL);
	}
	
} )

// Extend the Tapestry.Initializer with a static method that instantiates a ZonePeriodicUpdater.

Tapestry.Initializer.zonePeriodicUpdater = function(spec) {
    new ZonePeriodicUpdater(spec);
}

