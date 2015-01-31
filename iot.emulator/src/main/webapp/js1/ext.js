var requestId=0;
var loadingTimeout;
var loadingActive=-1;


function getBrowserWidth(){
	var myWidth=0;
	if(typeof (window.innerWidth)=="number"){myWidth=window.innerWidth
}else{if(document.documentElement&&(document.documentElement.clientWidth||document.documentElement.clientHeight)){myWidth=document.documentElement.clientWidth
}else{if(document.body&&(document.body.clientWidth||document.body.clientHeight)){myWidth=document.body.clientWidth
}}}return myWidth
}function getBrowserHeight(){var myHeight=0;
if(typeof (window.innerWidth)=="number"){myHeight=window.innerHeight
}else{if(document.documentElement&&(document.documentElement.clientWidth||document.documentElement.clientHeight)){myHeight=document.documentElement.clientHeight-37
}else{if(document.body&&(document.body.clientWidth||document.body.clientHeight)){myHeight=document.body.clientHeight
}}}return myHeight
}	




 (function($) {
    $.fn.forceNumeric = function(options) {
        var settings = $.extend({
            'allowNegative' : false,
            'allowDecimal' : false,
            'decimalPlaces' : 0
        }, options);

        return this.each(function() {
            $(this).keypress(function(e) {
                var key;
                var isCtrl = false;
                var keychar;
                var reg;

                if (window.event) {
                    key = e.keyCode;
                    isCtrl = window.event.ctrlKey;
                }
                else if (e.which) {
                    key = e.which;
                    isCtrl = e.ctrlKey;
                }

                if (isNaN(key)) return true;

                keychar = String.fromCharCode(key);

                // check for backspace or delete, or if Ctrl was pressed
                if (key == 8 || isCtrl) {
                    return true;
                }

                reg = /\d/;
                var isFirstNegative = settings.allowNegative ? keychar == '-' && this.value.indexOf('-') == -1 : false;
                var isFirstDecimal = settings.allowDecimal ? keychar == '.' && this.value.indexOf('.') == -1 : false;

                var cursorPosition = $(this).getCursorPosition();
                var currentValue = $(this).val();
                var currentValueLength = currentValue.length;

                //only allow negative character in the beginning
                if (settings.allowNegative && isFirstNegative) {
                    return cursorPosition == 0;
                }

                //if first decimal, make sure it is in the right decimal position, else move it automatically to the right decimal place
                if (settings.allowDecimal && isFirstDecimal && settings.decimalPlaces > 0) {
                    //first decimal and at the end of the value, it's ok
                    if (cursorPosition == currentValueLength || currentValueLength - cursorPosition <= settings.decimalPlaces) {
                        return true;
                    }

                    //else we have to move it automatically to the right decimal place
                    if (currentValueLength - cursorPosition != settings.decimalPlaces) {
                        var integerPart = currentValue.substring(0, currentValueLength - settings.decimalPlaces);
                        var fractionalPart = currentValue.substring(currentValueLength - settings.decimalPlaces, currentValueLength);
                        $(this).val(integerPart + "." + fractionalPart);
                        //return false to avoid system to print the . at the end
                        return false;
                    }
                }

                //if there's a decimal in place, make sure no more numeric allowed after specified decimal place
                if (settings.allowDecimal && currentValue.indexOf('.') > 0) {
                    //if decimal places was reached, cancel the input
                    if (cursorPosition > currentValue.indexOf('.') && currentValue.length - currentValue.indexOf('.') > settings.decimalPlaces) {
                        return false;
                    }
                }

                return isFirstNegative || isFirstDecimal || reg.test(keychar);
            });
        });
    };

    $.fn.getCursorPosition = function() {
        var pos = 0;
        var input = $(this).get(0);
        // IE Support
        if (document.selection) {
            input.focus();
            var sel = document.selection.createRange();
            var selLen = document.selection.createRange().text.length;
            sel.moveStart('character', -input.value.length);
            pos = sel.text.length - selLen;
        }
        // Firefox support
        else if (input.selectionStart || input.selectionStart == '0')
            pos = input.selectionStart;

        return pos;
    };
})(jQuery);




$.fn.scrollable = function(options){
		var el = $(this);
		var h = el.attr('scrolheight');
		if(el.scrollTop < el.scrollHeight - h)
		    return;
		var params = {'casta_applicationid' : el.attr('appid'), 'casta_eventid': el.attr('eventid'), 'casta_componentid':el.attr('id')};
		sCall(params);
};


jQuery.getCSS = function( url ) {
    jQuery( document.createElement('link') ).attr({
        href: url,
        media:'screen',
        type: 'text/css',
        rel: 'stylesheet'
    }).appendTo('head');
};
	



(function( $ ) {
	
	$.fn.select = function(options){
		var me = $(this);
		var selected =options.selectedindex;

		$.ajax({
			url:options.url,
			type:'GET',
			dataType:'json',
			success:function(data){
				//alert(data);
				var buff ="";
				for(d in data){
					var obj = data[d];
	//				alert(data[obj]);
					var sel = "";
					if(obj.key == selected)
						sel = "selected"
					buff = buff + "<option value='"+obj.key+"' "+sel+">" + obj.value + "</option>" 
				}
				//alert(me.attr('id'));

				me.html(buff);
				//me.selectpicker();
				me.val(selected);
			}
		});


	};
	
	
	$.fn.dataTable = function(option){

	var cols = option.columns;	
	var table = $('<table></table>').addClass('table table-hover table-condensed table-bordered').css('margin', '0px');
	var thead = $('<thead></thead>');
	var tbody = $('<tbody></tbody>');
	
	var tr = $('<tr></tr>').addClass('danger');
	for(var i=0;i < cols.length;i++){
		tr.append($('<th></th>').text(cols[i]));
	}
	thead.append(tr);
	table.append(thead);
	var data = option.table;
	
	
	//alert(data.length);
	if(data.length>0){
		for(var row=0;row<data.length;row++){
			var jrow = $('<tr></tr>').attr('rowid', row + '').css('cursor', 'pointer').click(option.rowclick);
			for(var col = 0; col < cols.length;col++){
				//alert(data[row][col]);
				jrow.append($('<td></td>').text(data[row][col]));
			}
			tbody.append(jrow);
		}
	}else{
		var jrow = $('<tr></tr>').attr('rowid', row + '').css('cursor', 'pointer').append($('<td colspan=1000></td>').css('text-align', 'center').text("No records found"));
		tbody.append(jrow);
	}
	
	table.append(tbody);
	if(option.pages > 1){
		var tfoot = $('<tfoot></tfoot>');
		var tfoottr =$('<tr></tr>');
		tfoot.append(tfoottr);
		var pag = $('<td colspan=\'1000\'></td>');
		tfoottr.append(pag);
	
		pag.bootpag({'total':option.pages});
		table.append(tfoot);
	}
	
	
	$(this).append(table);
	
};
	
	
}( jQuery ));




$.fn.bigIntegerInput = function(){

    $(this).keydown(function (e) {
        // Allow: backspace, delete, tab, escape, enter and .
        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
             // Allow: Ctrl+A
            (e.keyCode == 65 && e.ctrlKey === true) || 
             // Allow: home, end, left, right
            (e.keyCode >= 35 && e.keyCode <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        // Ensure that it is a number and stop the keypress
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
        }
    });
};
	
$.fn.castafiore = function(app, params){

		

		var me = $(this);
		me.append("<div id='root_"+app+"'>");
		me.append("<div id='script_"+app+"'>");
		//$("#script_" + app).mask('Please wait....');
		var url = "castafiore/?casta_applicationid=" + app;
		var curUrl = window.location.href;
		var p = curUrl.split('?')[1];
		if(p){
			url = url +'&' + p;
		}

		if(!params){
			params = {};
		}

		$.ajax({url: url,type: 'POST',dataType: 'text',data:params,success: function(data){$("#script_" + app).html(data);}});		
};
	
function loading(){
	if(loadingActive==-1){
		//loadingTimeout = setTimeout('$.blockUI({message:"<h3 class=\'ui-widget-header\'>Please wait...</h3>"})', 1000); 
		loadingTimeout = setTimeout('$(".loader").fadeIn(100)', 10);
		loadingActive = 1;
	}
}

function hideloading(){
	$(".loader").fadeOut(100);
	if(loadingActive==1){
		loadingActive = -1;
		if (loadingTimeout) clearTimeout(loadingTimeout);
	}
}


function openPage(page){
	var app = $('#root_erp').children().first();
	var params ={'casta_applicationid':'erp', 'casta_componentid' :app.attr('id'), 'casta_eventid':app.attr('id'), 'action' :'openpage', 'page': page};
	sCall(params);
	
}

function sCall(params)
{	
	loading();
	
	$(':checkbox').each(function(i){
		if($(this).is(':checked')){
			params["casta_value_"+$(this).attr("id")]= "checked";
		}else{
			params["casta_value_"+$(this).attr("id")]= "";
		}

	});
	$(" *[stf]").each(function(i){

		if(params["casta_value_"+$(this).attr("id")] == undefined){
			
			if($(this).val() == undefined){
				params["casta_value_"+$(this).attr("id")]=$(this).attr("value");
			}else{
				params["casta_value_"+$(this).attr("id")]=$(this).val();
			}
		}
	});
	params.requestId=requestId++;

	$.post("castafiore/"+requestId+".jsp",params,function(data){
		$("#script_"+params['casta_applicationid']).append(data);
	}, "text");
}


$(document).ready(function(){

  $(window).resize(function()
  {
	 
    if($(window).width() >= 765){
      $(".sidebar #nav, .sidebar .sidenav").slideDown(350);
    }
    else{
      $(".sidebar #nav, .sidebar .sidenav").slideUp(350); 
    }
  });


  $("#nav > li > a, .sidenav > li > a").on('click',function(e){
	   alert("ssdf");
      if($(this).parent().hasClass("has_sub")) {
        e.preventDefault();
      }   

      if(!$(this).hasClass("subdrop")) {
        // hide any open menus and remove all other classes
        $("#nav li ul, .sidenav li ul").slideUp(350);
        $("#nav li a, .sidenav li a").removeClass("subdrop");
        
        // open our new menu and add the open class
        $(this).next("ul").slideDown(350);
        $(this).addClass("subdrop");
      }
      
      else if($(this).hasClass("subdrop")) {
        $(this).removeClass("subdrop");
        $(this).next("ul").slideUp(350);
      } 
      
  });
});

$(document).ready(function(){
  $(".sidebar-dropdown a").on('click',function(e){
      e.preventDefault();

      if(!$(this).hasClass("open")) {
        // hide any open menus and remove all other classes
        $(".sidebar #nav, .sidebar .sidenav").slideUp(350);
        $(".sidebar-dropdown a").removeClass("open");
        
        // open our new menu and add the open class
        $(".sidebar #nav, .sidebar .sidenav").slideDown(350);
        $(this).addClass("open");
      }
      
      else if($(this).hasClass("open")) {
        $(this).removeClass("open");
        $(".sidebar #nav, .sidebar .sidenav").slideUp(350);
      }
  });

});