// This module observes the "firstName" element, switching its color on every keystroke.

define(["jquery"], function($) {

	return function() {
		var firstName = $("#firstName");
		var red = "rgb(255, 0, 0)";
		var green = "rgb(0, 128, 0)";

		firstName.css("color", red);

		function doSwitchColor() {
			var o = $(this);

			if (o.css("color") == red) {
				o.css("color", green);
			}
			else {
				o.css("color", red);
			}
		};

		firstName.on("keyup", doSwitchColor);
	}

})
