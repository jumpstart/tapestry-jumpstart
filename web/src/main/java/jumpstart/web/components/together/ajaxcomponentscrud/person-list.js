define(["jquery"], function($) {

	var $submit;

	var init = function(submitId) {
		$submit = $("#" + submitId);
	};
	
	var submit = function(updatedFilterFormAction) {
		$form = $submit.closest("form");
		$form.attr("action", updatedFilterFormAction);
		$submit.click();
	}
	
	return {
		init : init,
		submit : submit
	}

});
