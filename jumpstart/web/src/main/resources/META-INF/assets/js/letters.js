/*
 * A client-side validator that requires a string to contain letters only. Used with Letters.java.
 */
Tapestry.Validator.letters = function(field, message) {
	
	field.addValidator(function(value) {
    	if (value != null) {
    		if (value.match('[A-Za-z]+') != value) {
           	    throw message;
            }
    	}
    });

};
