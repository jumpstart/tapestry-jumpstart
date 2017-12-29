// A class that invokes a validator in the component via AJAX.
// Based on http://tinybits.blogspot.com/2010/03/new-and-better-zoneupdater.html
// and http://tinybits.blogspot.com/2009/05/simple-onevent-mixin.html
// and tapestry.js.

define(["jquery", "t5/core/zone", "t5/core/dom", "t5/core/console"], function($, zoneManager, dom, console) {
	
	var AjaxValidator = function() {

		var $field, validatorURI;
		var $form, $focusOutOfField;
	
		var init = function(params) {
			fieldSelector = "#" + params.fieldId;
			$field = $(fieldSelector);
			validatorURI = params.validatorURI;
			
			$form = $field.closest("form");
	
			$form.on("focusout", fieldSelector, function(e) {
				$focusOutOfField = $(this);
				console.info("A >>> $focusOutOfField = " + $focusOutOfField);
			});
		
			$form.on("focusin", fieldSelector, function(e) {
				
				// If changing focus *within the same form* then perform validation.  
	
				if ($focusOutOfField === undefined) {
					console.info("B >>> $focusOutOfField = " + $focusOutOfField);
				}
				else {
					console.info("C >>> $focusOutOfField.attr('id')  = " + $focusOutOfField.attr("id"));
				}
//				console.info("$field.attr('id')            = " + $field.attr("id"));
//				console.info("$form                   = " + $form.attr("id"));
//				console.info("$(this).closest('form') = " + $(this).closest("form"));
				var $focusIntoForm = $(this).closest("form");
	//			console.info("z                = " + z);
//				console.info($focusOutOfField.attr('id') + ", " + $field.attr('id') + ", " + $(this).closest('form').attr('id') + ", " +  $form.attr('id') + ".");
				
				if ($focusOutOfField !== undefined && $focusOutOfField.is($field) && $focusIntoForm.is($form)) {
					console.info("!!!!!!!!!!!!!!!!");
					validateInServerAsync();
				}
				
			});
		};
			
		var validateInServerAsync = function() {
			var value = $field.val();
			var validatorURIWithValue = this.validatorURI;
	
			if (value) {
				validatorURIWithValue = appendQueryStringParameter(validatorURIWithValue, 'param', value);
	
				new Ajax.Request(validatorURIWithValue, {
					method: 'get',
					onFailure: function(t) {
					    alert('Error communication with the server: ' + t.responseText.stripTags());
					},
					onException: function(t, exception) {
					    alert('Error communication with the server: ' + exception.message);
					},
					onSuccess: function(t) {
						if (t.responseJSON.error) {
							this.field.showValidationMessage(t.responseJSON.error);
						}
					}.bind(this)
				});
			}
		};

		return {
			init : init
		}
	};
		
	var appendQueryStringParameter = function(url, name, value) {
		if (url.indexOf('?') < 0) {
			url += '?'
		} else {
			url += '&';
		}
		value = escape(value);
		url += name + '=' + value;
		return url;
	};
	
	var create = function(params) {
		var ajaxValidator = new AjaxValidator();
		ajaxValidator.init(params);
	};

	return {
		create : create
	}
	
});

