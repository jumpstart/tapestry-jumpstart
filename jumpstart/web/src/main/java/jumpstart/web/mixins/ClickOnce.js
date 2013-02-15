// A class that ignores clicks after the first one.

var alreadyClickedOnce = false;

ClickOnce = Class.create( {

	initialize: function(elementId) {
		Event.observe($(elementId), 'click', this.doClickOnce.bindAsEventListener(this));
	},
        
	doClickOnce: function(e) {
		if (alreadyClickedOnce) {
			e.stop();
		}
		alreadyClickedOnce = true;
	}

} )


// Extend the Tapestry.Initializer with a static method that instantiates a ClickOnce.

Tapestry.Initializer.clickOnce = function(spec) {
    new ClickOnce(spec.elementId);
}
