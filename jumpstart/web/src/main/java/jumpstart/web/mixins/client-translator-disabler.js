// For a field, replaces Tapestry's default translator with one that does nothing.

define(["jquery"], function($) {

	return function(params) {
		var field = $("#" + params.fieldId);
		field.removeAttr("data-translation");
	}
	
});