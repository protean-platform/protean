/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 * @author Austin Miller
 */

(function($){

var baseKey = "protean-";

/*
 * util functions
 */

info = function(msg) {
	if(typeof msg === "string") {
		console.log(msg);
	} else if (typeof msg === "object") {
		console.dir(msg);
	}
};

debug = function(msg) {
	info(msg);
};
	
/**
 * ELEMENT ELEMENT ELEMENT
 */
var Element = function(o) {

	this.id = o.id;
	
	$("body").data(baseKey+this.id,this);
};


Element.prototype.destroy = function() {
	$("body").data(baseKey+this.id,undefined);
};

Element.prototype.event = function(method,args) {
	var event = {
		"event" : {
			"id" : ""+this.id,
			"name" : method,
			"args" : args
		}
	};
	
	event = JSON.stringify(event);
	
	window.socket.send(event);
};

Element.prototype.getHtmlElement = function() {
	return $("body");
};

Element.prototype.getId = function() {
	return this.options.id;
};

Element.prototype.hasMethod = function(name) {
	return $.inArray(name, this.options.metadata.methodNames) != -1;
};

Element.prototype.resize = function(event,ui) {
};

/**
 * FORM FORM FORM
 */

var Form = function(o,c) {
	this.options = $.extend(true, {}, this.defaultOptions, o || {});
	Element.call(this,o);
	
	this.div = $("<div>",this.options.divOptions);
	this.div.appendTo(c);
	this.prefix = "#input-" +this.id+"-";
	
	this.createTabs();
	
	debug(this.options.tabs);
};

Form.prototype = new Element({ id: -1 });
Form.prototype.constructor = Form;

Form.prototype.defaultOptions = {
	"divOptions" : {
		"class" : "protean-form"
	},
	"sectionOptions" : {
		"class" : "protean-form-section"
	},
	"tabOptions" : {
		"class" : "protean-form-tab"
	},
	"tabs" : {
		
	}
	
};

Form.prototype.createSection = function(c,name,section) {
	
	var div = $("<div>",this.options.sectionOptions).appendTo(c);
	$("<h2>",{"text":name}).appendTo(div);
	
	var self = this;
	$.each(section,function(k,v){
		var fs = $("<fieldset>").appendTo(div);
		var id = self.prefix + v.name;
		$("<label>",{ "for": id, "text":v.label }).appendTo(fs);
		var filebt = $("<button>",{ "class" : "protean-icon protean-icon-file"}).appendTo(fs);
		$("<input>",{ "id" : id, "name" : v.name }).appendTo(fs);
		
	});
	
	$("<div>",{ "class" : "clear"}).appendTo(div);
};

Form.prototype.createTab = function(name,tab) {
	var opts = { 
		"id" : "tab-"+this.id+"-"+name
	};
	
	$.extend(true,opts,this.options.tabOptions);
	
	var div = $("<div>",opts).appendTo(this.div);
	
	var self = this;
	$.each(tab.sections,function(k,v){
		self.createSection(div,k,v);
	});
};

Form.prototype.createTabs = function(){
	var self = this;
	$.each(this.options.elements,function(k,v){
		if(self.options.tabs[v.tab] == undefined) {
			self.options.tabs[v.tab] = { sections : {} };
		}
		if(self.options.tabs[v.tab].sections[v.section] == undefined) {
			self.options.tabs[v.tab].sections[v.section] = [];
		}
		
		self.options.tabs[v.tab].sections[v.section].push(v);
	});

	
	$.each(this.options.tabs,function(k,v) {
		self.createTab(k,v);
	});
	
	if(Object.keys(this.options.tabs).length > 1) {
		var ul = $("<ul>").prependTo(this.div);
		$.each(this.options.tabs,function(k,v) {
			var li = $("<li>").appendTo(ul);
			$("<a>",{"href":"#tab-"+self.id+"-"+k,"text":k}).appendTo(li);
		});
		this.div.tabs();
	}
};

Form.prototype.destroy = function() {
	this.div.remove();
};

/**
 * GOBAR GOBAR GOBAR
 */

var GoBar = function(o) {
	this.options = $.extend(true, {}, this.defaultOptions, o || {});
	Element.call(this,o);
	
	$(".gobar").attr("id",this.options.id);
	this.options.acOptions.source = o.commands;
	this.options.acOptions.select = this.select;
	
	$(".gobar").autocomplete(this.options.acOptions);
};

GoBar.prototype = new Element({ id: -1 });
GoBar.prototype.constructor = GoBar;

GoBar.prototype.defaultOptions = {
		"acOptions" : {
			source : [],
			position : {
				my : "left bottom",
				at : "left top",
				collision : "none"
			}
		}
	};

GoBar.prototype.select = function(event,ui){
	el = functions.element(this.id);
	el.event("go",{ command: ui.item.value });
	ui.item.value = "";
};

/**
 * IFRAME IFRAME IFRAME
 */

var IFrame = function(o,c) {
	this.options = $.extend(true, {}, this.defaultOptions, o || {});
	Element.call(this,o);
	
	
	console.log(this.options);
	this.options.divOptions.id = this.options.id;
	
	this.div = $("<div>", this.options.divOptions);
	
	this.iframe = $("<iframe>",this.options.iframeOptions);
	
	this.div.append(this.iframe);
	
	this.div.appendTo($("body"));
	
	this.display();
	
};

IFrame.prototype = new Element({ id: -1 });
IFrame.prototype.constructor = IFrame;


IFrame.prototype.defaultOptions = {
	"id" : -1,
	"divOptions" : {
	},
	"iframeOptions" : {
		"src" : "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d48360.28111281012!2d-73.97746077445272!3d40.75063983411891!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x89c25855c6480299%3A0x55194ec5a1ae072e!2stimes+square!5e0!3m2!1sen!2sus!4v1387666311104",
		"frameborder" : "0"
	},
	
	"dialogOptions" : {
		position: { 
			my: "left"
		},
		"height" : 600,
		"width" : 600,
		"title" : "Browser"
	}
};

IFrame.prototype.addButton = function(title,style) {
	var span = $("<span>", { "class" : "ui-icon" });
	var config = {
		"class" : "ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only ui-dialog-titlebar-"+title,
		"role" : "button",
		"aria-disabled" : "false",
		"title" : title
	};

	var button = $("<button>",config);
	span.appendTo(button);

	button.appendTo(this.iframe.parents(".ui-dialog").find(".ui-dialog-titlebar"));
	
	button.hover(function() {
		button.addClass("ui-state-hover");
		button.addClass("ui-state-active");
	},function() {
		button.removeClass("ui-state-hover");
		button.removeClass("ui-state-active");
	});
	
	return button;
};

IFrame.prototype.addButtons = function() {
	this.max = this.addButton("plus");
	this.min = this.addButton("minus");
	this.link = this.addButton("link");
	
	var self = this;
	this.max.click(function(event) {
		var h = $("#windowContainer").innerHeight();
		var w = $("#windowContainer").innerWidth();
		self.recordPosition();
		self.div.dialog( "option", "height", h);
		self.div.dialog( "option", "width", w);
		self.div.dialog( "option", "position", [0,0]);
		self.max = true;
		self.resize();
	});
	
	this.min.click(function(event) {
		if(self.max == false) {
			return;
		}
		self.max = false;
		self.div.dialog( "option", "height", self.lastHeight);
		self.div.dialog( "option", "width", self.lastWidth);
		self.div.dialog( "option", "position", self.lastPosition);
		self.resize();
	});
};

IFrame.prototype.display = function() {
	this.options.dialogOptions.resizeStop = this.resize;
	this.div.dialog(this.options.dialogOptions);
	
	this.div.parents(".ui-dialog").draggable( "option", "containment",$("#windowContainer"));
	this.div.parents(".ui-dialog").draggable( "option", "opacity",0.50);
	this.div.parents(".ui-dialog").draggable({ snap: true });
	
	
	this.recordPosition();
	this.addButtons();
	this.resize();
};

IFrame.prototype.destroy = function() {
	this.div.dialog("destroy");
	this.div.remove();
	
	Element.prototype.destroy.call(this);
};

IFrame.prototype.dragStart = function(event,ui) {
	el = functions.element(this.id);
	if(el.max == true) {
		el.max = false;
		el.div.dialog( "option", "height", el.lastHeight);
		el.div.dialog( "option", "width", el.lastWidth);
		el.resize();
	}
};

IFrame.prototype.getHtmlElement = function() {
	return this.div;
};


IFrame.prototype.recordPosition = function() {
	this.lastHeight = this.div.dialog( "option", "height");
	this.lastWidth = this.div.dialog( "option", "width");
	this.lastPosition = this.div.dialog( "option", "position" );
	
	if(this.lastHeight == "auto") {
		var ud = this.div.parents(".ui-dialog");
		this.lastHeight = ud.height();
		this.lastWidth = ud.width();
	}
};
 

IFrame.prototype.resize = function(event,ui) {
	
	var el = functions.element(this.id);
	
	el.iframe.css("width",el.div.width() + "px");
	el.iframe.css("height",el.div.height() + "px");
};

/**
 * TABLE TABLE TABLE
 */

var Table = function(o,c) {
	this.options = $.extend(true, {}, this.defaultOptions, o || {});
	Element.call(this,o);
	
	this.options.htmlOptions.id = this.options.id;
	
	this.table = $("<table>",this.options.htmlOptions).appendTo(c);
	this.container = c;
	
	var self = this;
	$.each(self.options.dtOptions.aoColumns,function(k,v){
		var col = k;
		if(v.sType == "date") {
			$.each(self.options.dtOptions.aaData,function(k,v){
				v[col] = new Date(v[col]);
			});
		}
	});
	
	this.datatable = this.table.dataTable(this.options.dtOptions);
};

Table.prototype = new Element({ id: -1 });
Table.prototype.constructor = Table;


Table.prototype.defaultOptions = {
	"htmlOptions" : {
		"cellpadding": 0,
		"cellspacing": 0,
		"border" : 0,
		"class" : "display"
	},

	"dtOptions" : {
		"sDom": 't<"F"ifTrp>',
		"bLengthChange" : false,
		"sScrollY": "200px",
		"bDeferRender" : true,
		"bJQueryUI": true,
		"bPaginate": false,
		"oTableTools": {
			"sSwfPath": "resource/copy_csv_xls_pdf.swf",
			"aButtons": [
				"copy", "csv", "xls", "pdf"
			]
		}

	}
};

Table.prototype.getHtmlElement = function() {
	return this.table;
};

Table.prototype.resize = function(event,ui) {
	var th = this.table.parents(".dataTables_wrapper").height();
	var body = this.table.parents(".dataTables_scrollBody");
	var diff = th - body.height();
	var newh = this.container.innerHeight() - diff;
	body.height(newh);
	this.datatable.fnDraw();
};


/**
 * WINDOW WINDOW WINDOW
 */

var Window = function(o) {
	
	this.options = $.extend(true, {}, this.defaultOptions, o || {});
	
	this.options.htmlOptions.id = this.options.id;
	this.options.htmlOptions.title = this.options.title;
	
	Element.call(this,o);
	
	this.addDialogButton("cancel");
	this.addDialogButton("ok");
	
	this.display();
};

Window.prototype = new Element({ id: -1 });
Window.prototype.constructor = Window;

Window.prototype.defaultOptions = {
	"htmlOptions" : {
		"title" : "Window",
		"class" : "window"
	},
	
	"max" : false,
	
	"dialogOptions" : {
		"width" : "580px"
	},
	
	closable: false,
	body: "lorem ipsum"
};




Window.prototype.addButton = function(title,style) {
	var span = $("<span>", { "class" : "ui-icon" });
	var config = {
		"class" : "ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only ui-dialog-titlebar-"+title,
		"role" : "button",
		"aria-disabled" : "false",
		"title" : title
	};

	var button = $("<button>",config);
	span.appendTo(button);

	button.appendTo(this.div.parents(".ui-dialog").find(".ui-dialog-titlebar"));
	
	button.hover(function() {
		button.addClass("ui-state-hover");
		button.addClass("ui-state-active");
	},function() {
		button.removeClass("ui-state-hover");
		button.removeClass("ui-state-active");
	});
	
	return button;
};

Window.prototype.addButtons = function() {
	this.max = this.addButton("plus");
	this.min = this.addButton("minus");
	this.link = this.addButton("link");
	
	var self = this;
	this.max.click(function(event) {
		if(self.max == true) {
			return;
		}
		
		var h = $("#windowContainer").innerHeight();
		var w = $("#windowContainer").innerWidth();
		self.recordPosition();
		self.div.dialog( "option", "height", h);
		self.div.dialog( "option", "width", w);
		self.div.dialog( "option", "position", [0,0]);
		self.max = true;
		self.resize();
	});
	
	this.min.click(function(event) {
		if(self.max == false) {
			return;
		}
		self.max = false;
		self.div.dialog( "option", "height", self.lastHeight);
		self.div.dialog( "option", "width", self.lastWidth);
		self.div.dialog( "option", "position", self.lastPosition);
		self.resize();
	});
	
	var tb =self.div.parents(".ui-dialog").find(".ui-dialog-titlebar");
	
	if(this.hasMethod("create")) {
		var filebt = $("<button>",{ "class" : "protean-titlebar-icon protean-icon protean-icon-file"}).appendTo(tb);
		filebt.click(function() {
			self.event("create");
		});
	}
	if(this.hasMethod("edit")) {
		var editbt = $("<button>",{ "class" : "protean-titlebar-icon protean-icon protean-icon-edit"}).appendTo(tb);
		editbt.click(function() {
			self.event("edit");
		});
	}
	if(this.hasMethod("delete")) {
		var trashbt = $("<button>",{ "class" : "protean-titlebar-icon protean-icon protean-icon-trash"}).appendTo(tb);
		trashbt.click(function() {
			self.event("delete");
		});
	}
};

Window.prototype.constructBody = function() {
	if(typeof this.options.body === "string") {
		this.div.append(this.options.body);
	} else if (typeof this.options.body === "object") {
		this.body = functions[this.options.body.command](this.options.body,this.div); 
	}
};

Window.prototype.destroy = function() {
	this.div.dialog("destroy");
	this.div.remove();
	
	this.body.destroy();
	
	Element.prototype.destroy.call(this);
};

Window.prototype.addDialogButton = function(name) {
	
	if(this.hasMethod(name) == false) {
		return;
	}
	
	if(this.options.dialogOptions.buttons == undefined) {
		this.options.dialogOptions.buttons = {};
	}
	
	var self = this;
	this.options.dialogOptions.buttons[name] = function() {
		self.event(name);
	};
	
};

Window.prototype.display = function() {

	this.div = $("<div>",this.options.htmlOptions).hide();
	
	this.constructBody();
	
	$("body").append(this.div);

	var self = this;
	this.options.dialogOptions.dragStart = this.dragStart;
	if(this.options.resizable == false) {
		this.options.dialogOptions.resizable = false;
	} else {
		this.options.dialogOptions.resizeStop = this.resize;
	}
	
	this.div.dialog(this.options.dialogOptions);
	
	this.addButtons();
	
	this.div.parents(".ui-dialog").draggable( "option", "containment",$("#windowContainer"));
	this.div.parents(".ui-dialog").draggable( "option", "opacity",0.50);
	this.div.parents(".ui-dialog").draggable({ snap: true });
	
	if(this.hasMethod("close")) {
		this.div.on('dialogbeforeclose', function(event,ui) {
			self.event("close",null);
			return false;
		});
	} else {
		this.div.dialog().parent().find(".ui-dialog-titlebar-close").hide();
	}
	
	this.recordPosition();
	this.resize();
	
};

Window.prototype.dragStart = function(event,ui) {
	el = functions.element(this.id);
	if(el.max == true) {
		el.max = false;
		el.div.dialog( "option", "height", el.lastHeight);
		el.div.dialog( "option", "width", el.lastWidth);
		el.resize();
	}
};

Window.prototype.getHtmlElement = function() {
	return this.div;
};

Window.prototype.recordPosition = function() {
	this.lastHeight = this.div.dialog( "option", "height");
	this.lastWidth = this.div.dialog( "option", "width");
	this.lastPosition = this.div.dialog( "option", "position" );
	
	if(this.lastHeight == "auto") {
		var ud = this.div.parents(".ui-dialog");
		this.lastHeight = ud.height();
		this.lastWidth = ud.width();
	}
};

Window.prototype.resize = function(event,ui) {
	
	var el = functions.element(this.id);
	
	if(el.body != undefined) {
		el.body.resize(event,ui);
	}
};



/*
 * protea protocol implementation functions
 */
var functions = {

	"Background" : function(o) {
		$("body").css("background-position","left top");
		$("body").css("background-image","url("+o.href+")");
		$("body").css("background-repeat","no-repeat");
		$("body").css("background-size","100% 100%");
	},

	"Destroy" : function(o){
		functions.element(id).destroy();
	},

	"element" = function(id) {
		return $("body").data(baseKey + id);
	},

	"Form" : function(o,c) {
		return new Form(o,c);
	},
	
	"GoBar" : function(o) {
		return new GoBar(o);
	},
	
	"IFrame" : function(o) {
		return new IFrame(o);
	},
	
	"Table" : function(o,c) {
		return new Table(o,c);
	},
	
	"Time" : function(o) {
		window.deltaTime = +new Date() - +o.epoch;
	},
	
	"Window" : function(o) {
		return new Window(o);
	}
	
};

window.protean = functions;

$(function() {

	$("#programs").click(function() {
		if ($(".menuPanel").is(":visible")) {
			$(".menuPanel").fadeOut('fast');
			return;
		}
		$(".menuPanel").fadeIn('fast');
	});

	$(".menuPanel").hover(function(event) {
		clearTimeout($(".menuPanel").data("hoverTimout"));
	}, function(event) {
		var onHover = function() {
			$(".menuPanel").fadeOut('fast');
		};
		$(".menuPanel").data("hoverTimout", setTimeout(onHover, 1000));
	});

	$(window).resize(function() {
		$("#windowContainer").width($("body").width());
		$("#windowContainer").height($("body").height() - 50);
		$(".menu").width($("body").width() - 30);
	});

	$(window).resize();
	
	window.socket = new WebSocket('ws://'+document.location.host+':8090', ['protea']);

	window.socket.onopen = function () {
		window.socket.send("version:1");
		info("connection opened");
	};

	window.socket.onerror = function (error) {
	  console.log('error ' + error);
	};
	
	window.socket.onmessage = function(e) {
		debug("incoming message: "+new Date().toString("dddd, MMMM, yyyy"));
		
		o = $.parseJSON(e.data);
		
		debug(o);
		
		$.each(o,function(k,v){
			functions[k](v);
		});
	};
	
	var clock = $("#clock");
	window.deltaTime = 0;
	setInterval(function(){
		var text = moment(+new Date() - window.deltaTime).format("HH:mm:ss");
		clock.html(text);
	},1000);
	
	info("protean.js loaded");
});
})(jQuery);