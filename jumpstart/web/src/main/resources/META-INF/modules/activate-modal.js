define(["jquery", "bootstrap/modal"], function($) {

	// Activates a modal.

	return function(modalId, options) {
		$('#' + modalId).modal(options);
	};
	
});
