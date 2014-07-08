// A class that invokes a validator in the component via AJAX.
// Based on http://tinybits.blogspot.com/2010/03/new-and-better-zoneupdater.html
// and http://tinybits.blogspot.com/2009/05/simple-onevent-mixin.html
// and tapestry.js.

define(["jquery", "t5/core/zone", "t5/core/dom", "t5/core/console"], function($, zoneManager, dom, console) {
	
	var AjaxValidator = function() {

		var $field, validatorURI;
		var $form, $focusOutOfField;
	
		var init = function(params) {
			$field = $("#" + params.fieldId);
			validatorURI = params.validatorURI;
			
			$form = $field.closest("form");
	
			$form.on("focusout", $field, function(e) {
				$focusOutOfField = $(this);
			});
		
			$form.on("focusin", $field, function(e) {
				
				// If changing focus *within the same form* then perform validation.  
	
				if ($focusOutOfField === undefined) {
					console.info("$focusOutOfField = " + $focusOutOfField);
				}
				else {
					console.info("$focusOutOfField.attr('id')  = " + $focusOutOfField.attr("id"));
				}
				console.info("$field.attr('id')            = " + $field.attr("id"));
				console.info("$form                   = " + $form).attr("id");
				console.info("$(this).closest('form') = " + $(this).closest("form"));
				var z = $(this).closest("form") == $form;
	//			console.info("z                = " + z);
				console.info(" ");
				
				if ($focusOutOfField !== undefined && $focusOutOfField == $field && $(this).closest("form") == $form) {
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

