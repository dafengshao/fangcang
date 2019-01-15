/**爬取价格 命令如下
 */
window.onerror = handleErr;
var isException = false;

function handleErr(msg, url, l) {
	isException = true;
	console.log("phantomjs", msg, url, l);
	console.log("phantomjs page load exception");
	phantom.exit();
	return true;
}

phantom.outputEncoding = "gb2312";
phantom.loadImages = false;
var page = require('webpage').create();
var system = require('system'); //传递一些需要的参数给js文件  
page.settings.userAgent =
	'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36';
page.customHeaders = {
	Referer: "http://hotels.ctrip.com/",
	Cookie: ""
};
page.settings.resourceTimeout = 60000; // ms
page.onResourceTimeout = function(e) {
	console.log("phantomjs", e.errorCode); // it'll probably be 408
	console.log("phantomjs", e.errorString); // it'll probably be 'Network timeout on resource'
	console.log("phantomjs", e.url); // the url whose request timed out
	console.log("phantomjs page load timeout");
	phantom.exit();
};
var testindex = 0;
var loadInProgress = true;
var fs = require('fs');
var jsonStr = system.args[1]; //jsonStr 
var jsonConfig = JSON.parse(jsonStr);
var list = jsonConfig.list; //id数组

page.onLoadStarted = function() {
	loadInProgress = true;
	console.log("phantomjs load started");
};

page.onLoadFinished = function() {
	loadInProgress = false;
	console.log("phantomjs load finished");
};
phantom.onError = function(msg, trace) {
	var msgStack = ['PHANTOM ERROR: ' + msg];
	if (trace && trace.length) {
		msgStack.push('TRACE:');
		trace.forEach(function(t) {
			msgStack.push(' -> ' + (t.file || t.sourceURL) + ': ' + t.line + (t.function ? ' (in function ' + t.function+')' :
				''));
		});
	}
	console.error('phantomjs error', msgStack.join('\n'));
	console.info('phantomjs error', msgStack.join('\n'));
	phantom.exit(1);
};

var steps = [
	
	function(data) {
		page.open(data[index]);
		step_index = 1;
	},

	function(data) {
		fs.write(data[index]+".html", page.content, 'w');
		step_index = 2;
		console.info('success,'+data[index]);
	}
];
var index = 0;
var step_index = 0;
var interval = setInterval(function() {

	if (!loadInProgress&&step_index<=2&&index<list.length) {
		if(step_index==2){
			index++;
			step_index=0;
		}else{
			steps[step_index](list[index]);
		}
		
	}
},300);
var exit = setInterval(function(){
	console.log("index:"+index);
	console.log(index===(list.length));
	if(index===list.length){
		console.log("exit");
		window.clearInterval(interval)
		window.clearInterval(exit);
	}
},900);
