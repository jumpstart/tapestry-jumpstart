// A class that ignores clicks after the first one.

define([ "jquery" ], function($) {
	
	alreadyClickedOnce = false;

	init = function(spec) {
		$element = $("#" + spec.elementId);

		doClickOnce = function() {
			
			if (alreadyClickedOnce) {
				// Cancel the original click event.
				return false;
			}

			alreadyClickedOnce = true;
			return true;
		}
		
		$element.on("click", doClickOnce);
	};
	
	return {
		init : init
	};
	
});