// A ClientTranslatorDisabler replaces Tapestry's default translator for a field with one that does nothing.
// Written in Protoype style because Tapestry includes the Protoype library (http://www.prototypejs.org/).

ClientTranslatorDisabler = Class.create( {

	initialize : function(fieldId) {
		this.field = $(fieldId).getStorage();
		
		this.field.fieldEventManager.translator = function(input) {
			return input; 
		}
	}
	
} )

// Extend the Tapestry.Initializer with a static method that instantiates a ClientTranslatorDisabler.

Tapestry.Initializer.clientTranslatorDisabler = function(spec) {
    new ClientTranslatorDisabler(spec.fieldId);
}