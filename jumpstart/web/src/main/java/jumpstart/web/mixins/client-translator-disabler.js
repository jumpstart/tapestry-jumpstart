// For a field, disables the field's default translator assigned by Tapestry.

define(["jquery"], function($) {

	return function(params) {
		var field = $("#" + params.fieldId);
		field.removeAttr("data-translation");
	}
	
});