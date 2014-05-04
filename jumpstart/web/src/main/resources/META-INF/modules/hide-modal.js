define(["jquery", "bootstrap/modal"], function($) {

	// Hides a modal.

	return function(modalId) {
		var $modal = $('#' + modalId);

		if ($modal.length > 0) {
			$modal.modal('hide');
		}
		else {
			// The modal's already gone, but the backdrop may still be there.
			$('body').removeClass('modal-open');
			$('.modal-backdrop').remove();
		}
	}

});
