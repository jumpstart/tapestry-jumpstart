// A module that ignores clicks after the first one.
// For each clickable element that you want to participate, call init. 
// Once any of the elements is clicked, this module will cancel subsequent clicks from any of the elements.

define([ "jquery" ], function($) {
	
	var alreadyClickedOnce = false;

	var init = function(spec) {
		$element = $("#" + spec.elementId);

		var doClickOnce = function() {
			
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