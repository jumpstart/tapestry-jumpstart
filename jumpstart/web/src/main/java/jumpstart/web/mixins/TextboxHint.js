// A TextboxHint observes a field, giving it a "textbox hint" when it is empty and does not have focus.
// Beware: it is flawed because it won't allow the user to submit text that is the same as the hint!
// Written in Protoype style because Tapestry includes the Protoype library (http://www.prototypejs.org/).

TextboxHint = Class.create( {

	initialize : function(textboxId, hintText, hintColor) {
		this.textbox = $(textboxId);
		this.hintText = hintText;
		this.hintColor = hintColor;
		this.normalColor = this.textbox.getStyle('color');

		Event.observe(this.textbox, 'focus', this.doClearHint.bindAsEventListener(this));
		Event.observe(this.textbox, 'blur', this.doCheckHint.bindAsEventListener(this));
		Event.observe(this.textbox, 'change', this.doCheckHint.bindAsEventListener(this));
		Event.observe(this.textbox.form, 'submit', this.doClearHint.bindAsEventListener(this));
		
		this.doCheckHint();
	},
	
	doClearHint : function(e) {
		if (this.textbox.value == this.hintText) {
			this.textbox.value = "";
		}
		this.textbox.setStyle({color: this.normalColor});
	},

	doCheckHint : function(e) {
		
		// If field is empty, put the hintText in it and set its color to hintColor
		
		if (this.textbox.value.length == 0) {
			this.textbox.value = this.hintText;
			this.textbox.setStyle({color: this.hintColor});
		}
		
		// Else if field contains hintText, set its color to hintColor
		
		else if (this.textbox.value == this.hintText) {
			this.textbox.setStyle({color: this.hintColor});
		}
		
		// Else, set the field's color to its normal color
		
		else {
			this.textbox.setStyle({color: this.normalColor});
		}

	}
	
} )

// Extend the Tapestry.Initializer with a static method that instantiates a TextboxHint.

Tapestry.Initializer.textboxHint = function(spec) {
    new TextboxHint(spec.textboxId, spec.hintText, spec.hintColor);
}