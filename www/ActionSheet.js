
//  ActionSheet.js
//
// Created by Olivier Louvignes on 2011-11-27.
// Updated by Etienne Adriaenssen on 2013-09-07
//
// Copyright 2011-2012 Olivier Louvignes. All rights reserved.
// MIT Licensed
var exec = require('cordova/exec');

var ActionSheet = function() {}

ActionSheet.prototype.create = function(options, callback) {

	$("#uberwrapper").prepend('<div id="actionsheet" style="display:none;position:absolute;z-index:500000000;width:100%;text-align:center;"></div>');
	document.getElementById("actionsheet").style.marginTop =  (document.documentElement.clientHeight + 44 )+'px';

	if(/android/i.test(navigator.userAgent)){
		options || (options = {});
		var scope = options.scope || null;

		var config = {
			title : options.title || '',
			items : options.items || ['Cancel'],
			style : options.style || 'default',
			destructiveButtonIndex : options.hasOwnProperty('destructiveButtonIndex') ? options.destructiveButtonIndex : undefined,
			cancelButtonIndex : options.hasOwnProperty('cancelButtonIndex') ? options.cancelButtonIndex : undefined
		};

		var _callback = function(result) {
			if(!result)
				return;
			var buttonValue = false, 
			buttonIndex = result.buttonIndex;
			if(!config.cancelButtonIndex || buttonIndex != config.cancelButtonIndex) {
				buttonValue = config.items[buttonIndex];
			}
			console.log(buttonIndex);
			if(typeof callback == 'function') callback.apply(scope, [buttonValue, buttonIndex]);
			return buttonIndex;
		};

		$('#ActionSheet').empty();
		var html = '';
		for (var i = config.items.length - 1; i >= 0; i--) {
			if(i==config.cancelButtonIndex){
				html='<span class="button-inset actionsheetbtn" id="test'+i+'" style="position:relative;width: 100%;z-index:15000;line-height:45px">'
				+'<font color="red">'+config.items[i]+'</font>'+'</span>'+html;
			}else{
				html='<span class="button-inset actionsheetbtn" id="test'+i+'" style="position:relative;width: 100%;z-index:15000;line-height:45px">'
				+config.items[i]+'</span>'+html;
			}
		}

		html = '<div id="alterbarquestion" class="alertbar" style="position:absolute;z-index:14999;width:100%"><font>'+config.title+'</font>'+html+'</div>';
		$('.actionsheet-btn').off('touchstart');
		$('#ActionSheet').html(html);

		setTimeout(function(){
			$('.actionsheetbtn').on('touchstart', function(e){
				e.preventDefault();
				e.stopPropagation();
				var id = $(e.currentTarget).attr('id');
				id = id.replace('test', '');
				var result = {};
				result.buttonIndex = id;
				result.buttonValue = $(e.currentTarget).text();
				if(id == config.cancelButtonIndex)
					$('#ActionSheet').hide();

				return _callback(result);

			});
		},10);

		$('#ActionSheet').show();
		return;


	}else{

		options || (options = {});
		var scope = options.scope || null;

		var config = {
			title : options.title || '',
			items : options.items || ['Cancel'],
			style : options.style || 'default',
			destructiveButtonIndex : options.hasOwnProperty('destructiveButtonIndex') ? options.destructiveButtonIndex : undefined,
			cancelButtonIndex : options.hasOwnProperty('cancelButtonIndex') ? options.cancelButtonIndex : undefined
		};

		var _callback = function(result) {
			var buttonValue = false,
			buttonIndex = result.buttonIndex;
			if(!config.cancelButtonIndex || buttonIndex != config.cancelButtonIndex) {
				buttonValue = config.items[buttonIndex];
			}
			if(typeof callback == 'function') callback.apply(scope, [buttonValue, buttonIndex]);
		};

		return cordova.exec(_callback, _callback, 'ActionSheet', 'create', [config]);
	};
}
module.exports = new ActionSheet();