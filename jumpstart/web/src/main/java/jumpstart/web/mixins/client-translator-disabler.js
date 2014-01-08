// For a field, disables the field's default translator assigned by Tapestry.

define(["jquery"], function($) {

	return function(fieldId) {
		var $field = $("#" + fieldId);
		$field.removeAttr("data-translation");
	}
	
});