// A TextboxHint observes a field, giving it a "textbox hint" when it is empty and does not have focus.
// Beware: it is flawed because it won't allow the user to submit text that is the same as the hint!
// Written in jQuery style because we have included the tapestry5-jquery library (http://tapestry5-jquery.com/).

(function($) {
	$.TextboxHint = function(textboxId, hintText, hintColor) {

		var textbox = $("#" + textboxId);
		var hintText = hintText;
		var hintColor = hintColor;
		var normalColor = textbox.css('color');
	
		textbox.on('focus', function(event) { doClearHint() } );
		textbox.on('blur', function(event) { doCheckHint() } );
		textbox.on('change', function(event) { doCheckHint() } );
		textbox.on('submit', function(event) { doClearHint() } );
		
		var doClearHint = function() {
			if (textbox.val() == hintText) {
				textbox.val('');
			}
			textbox.css('color', normalColor);
		}.bind(this);
	
		var doCheckHint = function() {
			
			// If field is empty, put the hintText in it and set its color to hintColor
			
			if (textbox.val().length == 0) {
				textbox.val(hintText);
				textbox.css('color', hintColor);
			}
			
			// Else if field contains hintText, set its color to hintColor
			
			else if (textbox.val() == hintText) {
				textbox.css('color', hintColor);
			}
			
			// Else, set the field's color to its normal color
			
			else {
				textbox.css('color', normalColor);
			}
	
		}.bind(this);
		
		doCheckHint();
	};
	
	// Extend the Tapestry.Initializer with a function that instantiates a TextboxHint.

	$.extend(Tapestry.Initializer, {
		textboxHint : function(params) {
			$.TextboxHint(params.textboxId, params.hintText, params.hintColor);
		}
	});

})(jQuery)
