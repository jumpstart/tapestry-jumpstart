// A FileUpload provides functions that show/hide the progress and results blocks. 
// Written in Protoype style because Tapestry includes the Protoype library (http://www.prototypejs.org/).

var fileUpload;

FileUpload = Class.create({

	initialize : function(fileId, progressId, resultId) {
		this.fileElem = this.resolve(fileId);
		this.progressElem = this.resolve(progressId);
		this.resultElem = this.resolve(resultId);

		// Can't use Event.observe on the submit button because it conflicts with the form submission.
		// So instead, of this... Event.observe(this.submitElem, 'click', this.showProgress.bindAsEventListener(this));
		// ...add this to the submit button: onclick="fileUpload.showProgress(); return true;".

		this.hideProgress();
	},

	hideProgress: function() {
		this.progressElem.hide();
		this.resultElem.show();
		return true;
	},

	showProgress: function() {
	
		// If a file has been chosen, then hide any previous results and show the progress
	
		if (this.fileElem.value != "") {
			this.progressElem.show();
			this.resultElem.hide();
		}
		else {
			this.resultElem.show();
		}
	
		return true;
	},
	
	hideResults: function() {
		this.resultElem.hide();
		return true;
	},

	resolve: function(elementId) {
		var element = $(elementId);
		if (!element) {
			alert("To the developer: element id \"" + elementId + "\" does not exist.");
		}
		return element;
	}

})

// Extend the Tapestry.Initializer with a function that instantiates a FileUpload object.

Tapestry.Initializer.fileUpload = function(spec) {
	fileUpload = new FileUpload(spec.fileId, spec.progressId, spec.resultId);
}
