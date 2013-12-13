// This module observes a field, giving it a "textbox hint" when it is empty and does not have focus.
// Beware: it is flawed because it won't allow the user to submit text that is the same as the hint!

define(["jquery"], function($) {

	return function(textboxId, hintText, hintColor) {
		var textbox = $("#" + textboxId);

		var normalColor = textbox.css("color");

		textbox.on("focus", doClearHint);
		textbox.on("blur", doCheckHint);
		textbox.on("change", doCheckHint);
		textbox.on("submit", doClearHint);

		textbox.blur();

		function doClearHint() {
			var field = $(this);
			
			if (field.val() == hintText) {
				field.val("");
			}

			field.css("color", normalColor);
		}

		function doCheckHint() {
			var field = $(this);

			// If field is empty, put the hintText in it and set its color to
			// hintColor

			if (field.val() == "") {
				field.val(hintText);
				field.css("color", hintColor);
			}

			// Else if field contains hintText, set its color to hintColor

			else if (field.val() == hintText) {
				field.css("color", hintColor);
			}

			// Else, set the field's color to its normal color

			else {
				field.css("color", normalColor);
			}
		}

	}

})