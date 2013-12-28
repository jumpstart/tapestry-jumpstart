/*
 * A client-side bean validator that requires a string to contain letters only. Used with Letters.java.
 */
define(["t5/core/dom", "t5/core/events", "t5/beanvalidator/beanvalidator-validation"], function(dom, events) {

	dom.onDocument(events.field.validate, "input[data-validate-letters], textarea[data-validate-letters]", function(event, memo) {
		var value = memo.translated;

		if (value != null) {
			if (value.match('[A-Za-z]+') != value) {
				memo.error = (this.attr("data-letters-message")) || "LETTERS ONLY";
				return false;
			}
		}

		return true;
	});

})
