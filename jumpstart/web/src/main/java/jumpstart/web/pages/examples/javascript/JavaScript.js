// ColorSwitcher observes the 'firstName' element, switching its color on every keystroke.
// Written in Protoype style because Tapestry includes the Protoype library (http://www.prototypejs.org/).

ColorSwitcher = Class.create( {

	initialize : function() {
		this.element = $('firstName');
		Event.observe(this.element, 'keyup', this.doSwitchColor.bindAsEventListener(this));
		this.element.setStyle({color: 'red'});
	},
	
	doSwitchColor : function(e) {
		if (this.element.getStyle('color') == 'red') {
			this.element.setStyle({color: 'green'});
		}
		else {
			this.element.setStyle({color: 'red'});
		}
	}

} )