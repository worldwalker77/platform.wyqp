/**
 * qiao.util.js
 * 1.qser
 * 2.qdata
 * 3.qiao.on
 * 4.qiao.is
 * 5.qiao.ajax
 * 6.qiao.totop
 * 7.qiao.qrcode
 * 8.qiao.end
 * 9.qiao.cookie
 * 10.qiao.search
 */
var qiao = {};

/**
 * 将表单转为js对象
 */
$.fn.qser = function(){
	var obj = {};
	
	var objs = $(this).serializeArray();
	if(objs.length != 0){
		for(var i=0; i<objs.length; i++){
			obj[objs[i].name] = objs[i].value;
		}
	}

	return obj;
};

/** 
 * 将data属性转为js对象
 */
$.fn.qdata = function(){
	var res = {};
	
	var data = $(this).attr('data');
	if(data){
		var options = data.split(';');
		for(var i=0; i<options.length; i++){
			if(options[i]){
				var opt = options[i].split(':');
				res[opt[0]] = opt[1];
			}
		}
	}
	
	return res;
};

/**
 * qiao.on
 * 事件绑定
 */
qiao.on = function(obj, event, func){
	$(document).off(event, obj).on(event, obj, func);
};

/**
 * qiao.is
 * 一些常用的判断，例如数字，手机号等
 */
qiao.is = function(str, type){
	if(str && type){
		if(type == 'number') return /^\d+$/g.test(str);
		if(type == 'mobile') return /^1\d{10}$/g.test(str);
	}
};

/**
 * qiao.ajax
 * 对$.ajax的封装
 */
qiao.ajaxoptions = {
	url 		: '',
	data 		: {},
	type 		: 'post',
	dataType	: 'json',
	async 		: true,
	crossDomain	: false
};
qiao.ajaxopt = function(options){
	var opt = $.extend({}, qiao.ajaxoptions);
	if(typeof options == 'string'){
		opt.url = options;
	}else{
		$.extend(opt, options);
	}
	
	return opt;
};
qiao.ajax = function(options, success, fail){
	if(options){
		var opt = qiao.ajaxopt(options);
		if(typeof base != 'undefined') opt.url = base + opt.url;
		
		$.ajax(opt).done(function(obj){
			if(success) success(obj);
		}).fail(function(e){
			if(fail){
				fail(e);
			}else{
				alert('数据传输失败，请重试！');
			}
		});
	}
};

/**
 * qiao.totop
 * 返回顶部的方法
 * 可以参考：plugins/_01_qtotop/qtotop.html
 */
qiao.totop = function(el){
	var $el = $(el);
	$el.hide().click(function(){
		$('body, html').animate({scrollTop : '0px'});
	});
	
	var $window = $(window);
	$window.scroll(function(){
		if ($window.scrollTop()>0){
			$el.fadeIn();
		}else{
			$el.fadeOut();
		}
	});
};

/**
 * qiao.end
 * 到达页面底部后自动加载内容
 * end：到达底部后的回调函数
 * $d：容器，默认是$(window)
 * $c：内容，默认是$(document)
 * 可以参考：plugins/_04_qend/qend.html
 */
qiao.end = function(end, $d, $c){
	if(end){
		var $d = $d || $(window);
		var $c = $c || $(document);
		
		$d.scroll(function(){if($d.scrollTop() + $d.height() >= $c.height()) end();});
	}else{
		$(window).scroll(null);
	}
};

/**
 * qiao.cookie
 * 对jquery.cookie.js稍作封装
 * 注：需要引入jquery.cookie.js，<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
 * qiao.cookie(key)：返回key对应的value
 * qiao.cookie(key, null)： 删除key对应的cookie
 * qiao.cookie(key, value)：设置key-value的cookie
 * 可以参考：plugins/_05_qcookie/qcookie.html
 */
qiao.cookie = function(key, value){
	if(typeof value == 'undefined'){
		return $.cookie(key);
	}else if(value == null){
		$.cookie(key, value, {path:'/', expires: -1});
	}else{
		$.cookie(key, value, {path:'/', expires:1});
	}
};

/**
 * qiao.search
 * 获取url后参数中的value
 * qiao.search(key)：返回参数中key对应的value
 * 可以参考：plugins/_06_qsearch/qsearch.html
 */
qiao.search = function(key){
	var res;
	
	var s = location.search;
	if(s){
		s = s.substr(1);
		if(s){
			var ss = s.split('&');
			for(var i=0; i<ss.length; i++){
				var sss = ss[i].split('=');
				if(sss && sss[0] == key) res = sss[1]; 
			}
		}
	}
	
	return res;
};

/**
 * qiao.bs.js
 * 1.alert
 * 2.confirm
 * 3.dialog
 * 4.msg
 * 5.tooltip
 * 6.popover
 * 7.tree
 * 8.scrollspy
 * 9.initimg
 * 10.bsdate
 * 11.bstro
 */
qiao.bs 	= {};
qiao.bs.modaloptions = {
	id		: 'bsmodal',
	url 	: '',
	fade	: 'fade',
	close	: true,
	title	: '<i class="glyphicon glyphicon-exclamation-sign"></i> 操作提示',
	head	: true,
	foot	: true,
	btn		: false,
	okbtn	: '<i class="glyphicon glyphicon-ok"></i> 确定',
	qubtn	: '<i class="glyphicon glyphicon-remove"></i> 取消',
	msg		: '<div class="progress"><div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%"></div></div>',
	size	: '', //大：lg 小:sm
	show	: false,
	remote	: false,
	backdrop: 'static',
	keyboard: true,
	style	: '',
	mstyle	: '',
	callback: null
};
qiao.bs.modalstr = function(opt){
	var start = '<div class="modal '+opt.fade+'" id="' + opt.id + '" tabindex="-1" role="dialog" aria-labelledby="bsmodaltitle" aria-hidden="true" style="position:fixed;top:20px;'+opt.style+'">';
	if(opt.size == 'lg'){
		start += '<div class="modal-dialog modal-lg" style="'+opt.mstyle+'"><div class="modal-content">';
	} else if (opt.size == 'sm'){
		start += '<div class="modal-dialog modal-sm" style="'+opt.mstyle+'"><div class="modal-content">';
	} else {
		start += '<div class="modal-dialog" style="'+opt.mstyle+'"><div class="modal-content">';
	}
	
	var end = '</div></div></div>';
	
	var head = '';
	if(opt.head){
		head += '<div class="modal-header">';
		if(opt.close){
			head += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
		}
		head += '<h5 class="modal-title" id="bsmodaltitle">'+opt.title+'</h5></div>';
	}
	
	var body = '<div class="modal-body"><p>'+opt.msg+'</p></div>';
	
	var foot = '';
	if(opt.foot){
		foot += '<div class="modal-footer" style="text-align: center"><button type="button" class="btn btn-primary bsok">'+opt.okbtn+'</button>';
		if(opt.btn){
			foot += '<button type="button" class="btn btn-default bscancel">'+opt.qubtn+'</button>';
		}
		foot += '</div>';
	}
	
	return start + head + body + foot + end;
};
qiao.bs.alert = function(options, func){
	// options
	var opt = $.extend({}, qiao.bs.modaloptions);
	
	if(typeof options == 'string'){
		opt.msg = options;
	}else{
		$.extend(opt, options);
	}
	
	// add
	$('body').append(qiao.bs.modalstr(opt));
	
	// init
	var $modal = $('#' + opt.id); 
	$modal.modal(opt);
	
	// bind
	qiao.on('#' + opt.id +' button.bsok', 'click', function(){
		if(func) func();
		$modal.modal('hide');
	});
	qiao.on('#' + opt.id, 'hidden.bs.modal', function(){
		$modal.remove();
	});
	
	// show
	$modal.modal('show');
};

qiao.bs.confirm = function(options, ok, cancel, hide){
	// options
	var opt = $.extend({}, qiao.bs.modaloptions);
	
	if(typeof options == 'string'){
		opt.msg = options;
	}else{
		$.extend(opt, options);
	}
	opt.btn = true;
	
	// append
	$('body').append(qiao.bs.modalstr(opt));
	
	// init
	var $modal = $('#' + opt.id); 
	$modal.modal(opt);
	
	// bind
	qiao.on('#' + opt.id +' button.bsok', 'click', function(){
		$modal.modal('hide');
		if(ok) ok();
	});
	qiao.on('#' + opt.id +' button.bscancel', 'click', function(){
		$modal.modal('hide');
		if(cancel) cancel();
	});
	qiao.on('#' + opt.id, 'hidden.bs.modal', function(e){
		$modal.remove();
		if(hide) hide();
	});
	
	// show
	$modal.modal('show');
};
qiao.bs.dialog = function(options, func,cancel){
	// options
	var opt = $.extend({}, qiao.bs.modaloptions, options);
	opt.size = 'lg';
	
	// append
	$('body').append(qiao.bs.modalstr(opt));
	
	// ajax page
	qiao.ajax({
		url:options.url,
		data : options.data,
		type : 'POST',
		dataType:'html'
	}, function(html){
		$('#' + opt.id + ' div.modal-body').empty().append(html);
		if(options.callback) options.callback();
	});
		
	// init
	var $modal = $('#' + opt.id); 
	$modal.modal(opt);
	
	// bind
	$('#' + opt.id +' button.bsok').on('click', function(){
		var flag = true;
		if(func){
			flag = func();
		}
		if(flag){
			$modal.modal('hide');
		}
	});
/*	$('#' + opt.id +' button.bscancel').on('click', function(){
		$modal.modal('hide');
	});*/
	qiao.on('#' + opt.id +' button.bscancel', 'click', function(){
		$modal.modal('hide');
		if(cancel) cancel();
	});
	qiao.on('#' + opt.id, 'hidden.bs.modal', function(){
		$modal.remove();
	});
	
	// show
	$modal.modal('show');
};
qiao.bs.msgoptions = {
	msg  : 'msg',
	type : 'success',
	time : 2000,
	position : 'top'
};
qiao.bs.msgstr = function(msg, type, position){
	if($('#bsalert')) {
		$('#bsalert').remove();
	}
	
	return '<div class="alert alert-'+type+' alert-dismissible" role="alert" style="display:none;position:fixed;' + position + ':0;left:0;width:100%;z-index:2001;margin:0;text-align:center;" id="bsalert"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'+msg+'</div>';
};
qiao.bs.msg = function(options){
	var opt = $.extend({},qiao.bs.msgoptions);
	
	if(typeof options == 'string'){
		opt.msg = options;
	}else{
		$.extend(opt, options);
	}
	
	$('body').prepend(qiao.bs.msgstr(opt.msg, opt.type , opt.position));
	$('#bsalert').slideDown();
	setTimeout(function(){
		$('#bsalert').slideUp(function(){
			$('#bsalert').remove();
		});
	},opt.time);
};
qiao.bs.popoptions = {
	animation 	: true,
	container 	: 'body',
	content		: 'content',
	html		: true,
	placement	: 'bottom',
	title		: '',
	trigger		: 'hover'//click | hover | focus | manual.
};
$.fn.bstip = function(options){
	var opt = $.extend({}, qiao.bs.popoptions);
	if(typeof options == 'string'){
		opt.title = options;
	}else{
		$.extend(opt, options);
	}
	
	$(this).data(opt).tooltip();
};
$.fn.bspop = function(options){
	var opt = $.extend({}, qiao.bs.popoptions);
	if(typeof options == 'string'){
		opt.content = options;
	}else{
		$.extend(opt, options);
	}
	
	$(this).popover(opt);
};