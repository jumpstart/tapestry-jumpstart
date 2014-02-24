define(["jquery", "t5/core/console"], function($, console) {

	return {
		$submit : "",

		init : function(submitId) {
			$submit = $("#" + submitId);
		},
		
		submit : function(updatedFilterFormAction) {
			$form = $submit.closest("form");
			$form.attr("action", updatedFilterFormAction);
			$submit.click();
		}
	}

});
