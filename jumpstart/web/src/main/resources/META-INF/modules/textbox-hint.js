// This module observes a field, giving it a "textbox hint" when it is empty and does not have focus.
// Beware: it is flawed because it won't allow the user to submit text that is the same as the hint!

define(["jquery"], function($) {

	return function(params) {
		var textbox = $("#" + params.textboxId);
		var hintText = params.hintText;
		var hintColor = params.hintColor;

		var normalColor = textbox.css("color");

		function doClearHint() {
			var o = $(this);
			
			if (o.val() == hintText) {
				o.val("");
			}

			o.css("color", normalColor);
		};

		function doCheckHint() {
			var o = $(this);

			// If field is empty, put the hintText in it and set its color to
			// hintColor

			if (o.val() == "") {
				o.val(hintText);
				o.css("color", hintColor);
			}

			// Else if field contains hintText, set its color to hintColor

			else if (o.val() == hintText) {
				o.css("color", hintColor);
			}

			// Else, set the field's color to its normal color

			else {
				o.css("color", normalColor);
			}
		};

		textbox.on("focus", doClearHint);
		textbox.on("blur", doCheckHint);
		textbox.on("change", doCheckHint);
		textbox.on("submit", doClearHint);

		textbox.blur();
	}

})