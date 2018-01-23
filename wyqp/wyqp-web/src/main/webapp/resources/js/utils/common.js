//常量
//图片大小
var MAX_IMG_SIZE = 122880;
var MAX_IMG_NAME_LEN = 50;
var ZG = '找钢网';
var ZY = '自营';
var JF = '积分抽奖';
//商品描述最大长度
var MAX_REMARK_LEN = 1000;
var currentPageSize = 10;
//库存预警
var STOCK_ALERT_NUM = 10;

/**
 * 绑定下拉菜单
 * @param elId			select元素id
 * @param url			请求url
 * @param valueField	需要传递给后台的数据，通常是id
 * @param displayField	需要显示的选项名称，通常是name
 * @param checkedValue	可选项，选中值，通常用在编辑页面中
 * @param callback		可选项，回调函数
*/
function bindSelect(elId, url, valueField, displayField, checkedValue, callback) {
	var allString = '<option value="">全部</option> ';	
	$.ajax({
		url : contextPath + url,
		type : 'GET',
		dataType : 'JSON',
		contentType : 'application/json',
		success : function(data) {
			var optionString = '';
			
			for ( var i in data) {
				var jsonObj = data[i];
				
				optionString += '<option value="' 
						+ jsonObj[valueField]
						+ '">'
						+ jsonObj[displayField] 
						+ '</option> ';
			}
			
			$('#' + elId).html(
					allString + optionString);
			
			if(checkedValue) {
				$('#' + elId).val(checkedValue);
			}
			
			if(callback) {
				callback();
			}
		},
		error : function(msg) {
			$('#' + elId).html(allString);
		}
	});
}

/**
 * 数字超过3位逗号分隔
 * @param number
 * @returns
 */
function numberComma(number)  {
	if(number && number.toString().length > 3) {
		var prefix = '';	
		if(number < 0) {
			prefix = '-';
		}	
		return prefix + Math.abs(number).toString().split('').reverse().join('').replace(/(\d{3})/g,
		'$1,').replace(/\,$/, '').split('').reverse().join('');
	}
	
	return number;
}

/**
 * 校验金额
 * @param value
 * @returns {Boolean}
 */
function validateMoney(value){
    var reg = /^(([0-9]|([1-9][0-9]{0,9}))((\.[0-9]{1,2})?))$/;
    
    if(reg.test(value) && value > 0){
        return true;
    }
    
    return false;
}

/**
 * 验证正整数
 * @param value
 * @returns {Boolean}
 */
function validateNumber(value){
    var reg = /^[1-9][0-9]*$/;
    
    if(reg.test(value) && value > 0) {
        return true;
    }
    
    return false;
}

/**
 * 验证图片格式及大小
 * @param file
 * @returns {Boolean}
 */
function validateImg(file) {
	var filename = file.name;
	
	if(!filename) {
		qiao.bs.msg({
			msg  : '请上传图片!',
			type : 'danger'
		});
		
		return false;
	}
	
	if(filename.length > MAX_IMG_NAME_LEN) {
		qiao.bs.msg({
			msg  : '图片名过长!',
			type : 'danger'
		});
		
		return false;
	}
	
	var extStart = filename.lastIndexOf('.');
	var ext = filename.substring(extStart, filename.length).toLowerCase();
	
	if (ext != '.png' && ext != '.jpg' && ext != '.jpeg' && ext != '.gif'
			&& ext != '.bmp') {
		qiao.bs.msg({
			msg  : '图片格式错误，仅支持png|jpg|jpeg|gif|bmp格式!',
			type : 'danger'
		});
		
		return false;
	}
	
	if(file.size >= MAX_IMG_SIZE) {
		qiao.bs.msg({
			msg  : '图片大小不能超过120KB!',
			type : 'danger'
		});
		
		return false;
	}
	
	return true;
}

/**
 * 获取文件本地路径
 * @param file
 * @returns
 */
function getFileURL(file) {
	var url = null;
	if (window.createObjectURL != undefined) { // basic
		url = window.createObjectURL(file);
	} else if (window.URL != undefined) { // mozilla(firefox)
		url = window.URL.createObjectURL(file);
	} else if (window.webkitURL != undefined) { // webkit or chrome
		url = window.webkitURL.createObjectURL(file);
	}
	
	return url;
}

/**
 * 输入框只能输入数字
 * 调用方式onkeyup="checkNumber(this);" onblur="checkNumber(this);"
 * 注意：必须加上onblur事件，防止搜狗拼音输入法、文本粘贴等方式使输入框输入非数字的形式
 */
function checkNumber(obj) {
	obj.value=obj.value.replace(/[^0-9]+/,'');
}
/**
 * 输入框只能输入非中文字符
 * 调用方式onkeyup="checkCn(this);" onblur="checkCn(this);"
 * 注意：必须加上onblur事件，防止搜狗拼音输入法、文本粘贴等方式使输入框输入非数字的形式
 */
function checkCn(obj) {
	obj.value=obj.value.replace(/[\u4e00-\u9fa5]+/,'');
}
function checkNumChar(obj) {
	obj.value=obj.value.replace(/[\u4e00-\u9fa5_\-.]+/,'');
}


/**
 * 生成省市区联动下拉框
 * @param eleId		存放省市区下拉框的DIV的ID
 * @param classname	下拉框<select>的class名
 * @param selectedPara	省市区选中参数，固定格式 {'provId': provId , 'cityId': cityId, 'areaId': areaId}
 */
function provinceAreaSelect(eleId, classname, selectedPara,flag) {
	//console.log('@provId: ' + provId + ' @cityId: ' + cityId + ' @areaId: ' + areaId);
	var headoption = '<div class="col-xs-4">';
		headoption += '<div class="input-group">';
		headoption += '<span class="input-group-addon"><font color="red">*</font>省份</span>';
		if(flag == 0){
			headoption += "<select class=\""+classname+"\" id=\"commonProvinceId\" disabled=\"disabled\">";		
		}else{			
			headoption += "<select class=\""+classname+"\" id=\"commonProvinceId\" >";
		}
	var blankoption = "<option value=\"\">请选择</option>";
	var footoption = '</select>';
		footoption += '</div>';
		footoption += '</div>';
		footoption += '<div class="col-xs-4">';
		footoption += '<div class="input-group">';
		footoption += '<span class="input-group-addon"><font color="red">*</font>市区</span>';
		if(flag == 0){
			footoption += '<select disabled=\"disabled\" class=\"'+classname+'\" id=\"commonCityId\">'+ blankoption +'</select>';						
		}else{
			footoption += '<select class=\"'+classname+'\" id=\"commonCityId\">'+ blankoption +'</select>';			
		}
		footoption += '</div>';
		footoption += '</div>';
		footoption += '<div class="col-xs-4">';
		footoption += '<div class="input-group">';
		footoption += '<span class="input-group-addon"><font color="red">*</font>区县</span>';
		if(flag == 0){			
			footoption += '<select disabled=\"disabled\" class=\"'+classname+'\" id=\"commonAreaId\">"+ blankoption +"</select>';		
		}else{
			footoption += '<select class=\"'+classname+'\" id=\"commonAreaId\">"+ blankoption +"</select>';		
		}
		footoption += '</div>';
		footoption += '</div>';
	$.ajax({
		url : contextPath + '/order/getProvList',
		type : 'GET',
		dataType : 'JSON',
		contentType : 'application/json',
		success : function(data) {
			var optionString = '';
			var selProvCode = '';
			for ( var i in data) {
				var jsonObj = data[i];
				var checkstr = '';
				if(jsonObj.pkid == selectedPara.provId) {
					checkstr = 'selected';
					selProvCode = jsonObj.code;
				}
				
				optionString += "<option value=\"" + jsonObj.pkid
						+ "\" code=\"" + jsonObj.code + "\" " + checkstr + ">"
						+ jsonObj.name + "</option> ";
			}
			
			$('#' + eleId).html(headoption + blankoption + optionString + footoption);
			
			if(selProvCode != null && selProvCode != ''){
				changeProvince({'provCode' : selProvCode},selectedPara);
			}
			
			$('#commonProvinceId').bind("change", this, function(){
				var scode = $(this).children('option:selected').attr("code");
				changeProvince({'provCode':scode});
			});
		},
		error : function(msg) {
			$('#' + eleId).html(headoption + blankoption + footoption);
		}
	});	
}
/**
 * 省份下拉框变动事件
 * @param para	{'provCode' : provCode}	必须包含provCode属性
 * @param selectedPara	省市区选中参数，固定格式 {'provId': provId , 'cityId': cityId, 'areaId': areaId}
 */
function changeProvince(para, selectedPara){
	var blankoption = "<option value=\"\">请选择</option>";
	$.ajax({
		url : contextPath + '/order/getCityList',
		type : 'GET',
		dataType : 'JSON',
		data : para,
		contentType : 'application/json',
		success : function(data) {
			var optionString = '';
			var selCityCode = '';
			for ( var i in data) {
				var jsonObj = data[i];
				var selstr = '';
				if(selectedPara != null && selectedPara.cityId != null && jsonObj.pkid == selectedPara.cityId){
					selstr = 'selected';
					selCityCode = jsonObj.code;
				}
				optionString += "<option value=\"" + jsonObj.pkid
						+ "\" code=\"" + jsonObj.code + "\" " + selstr + ">"
						+ jsonObj.name + "</option> ";
			}
			
			$('#commonCityId').html(blankoption + optionString);
			$('#commonAreaId').html(blankoption);
			if(selCityCode != null && selCityCode != ''){
				changeCity({"cityCode":selCityCode},selectedPara);
			}
			$('#commonCityId').bind("change", this, function(){
				var scode = $(this).children('option:selected').attr("code");
				changeCity({"cityCode":scode});
			});
		}
	});	
}
/**
 * 地市下拉框变动事件
 * @param para	{'cityCode' : cityCode}
 * @param selectedPara	省市区选中参数，固定格式 {'provId': provId , 'cityId': cityId, 'areaId': areaId}
 */
function changeCity(para,selectedPara){
	var blankoption = "<option value=\"\">请选择</option>";
	$.ajax({
		url : contextPath + '/order/getAreaList',
		type : 'GET',
		dataType : 'JSON',
		data : para,
		contentType : 'application/json',
		success : function(data) {
			var optionString = '';
			for ( var i in data) {
				var jsonObj = data[i];
				
				var selstr = '';
				if(selectedPara != null && selectedPara.areaId != null){
					selstr = (jsonObj.pkid == selectedPara.areaId ? 'selected' : '');
				}
				optionString += "<option value=\"" + jsonObj.pkid
						+ "\" code=\"" + jsonObj.code + "\" " + selstr + ">"
						+ jsonObj.name + "</option> ";
			}
			
			$('#commonAreaId').html(blankoption + optionString);
		}
	});	
}

/**
 * 输入框只能输入数字、小数点
 * @param obj
 */
function checkMoney(obj) {
	obj.value = obj.value.replace(/[^\d.]/g, ''); // 清除“数字”和“.”以外的字符
	
	obj.value = obj.value.replace(/^0\d*/g, '0'); // 0开头，后面紧跟着数字

	obj.value = obj.value.replace(/^\./g, ''); // 验证第一个字符是数字而不是“.”

	obj.value = obj.value.replace(/\.{2,}/g, '.'); // 只保留第一个“.”,清除多余的
	
	obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); // 禁止录入小数点后两位以上
	
	obj.value = obj.value.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.');
}

/**
 * 编号校验，只允许字母/数字/下划线/-/.
 * @param obj
 */
function checkCode(obj) {
	obj.value = obj.value.replace(/[^\w-.]/g, '');
}
function trimNumber(obj) {
	obj.value = obj.value.replace(/[^\d-]/g, '');
}
function trimScript(obj) {
	obj.value = obj.value.replace(/[<>]/g, '');
}

/**
 * 获取URL中查询参数信息
 * 
 * @param name 参数名
 * @returns
 */
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return (r[2]);
	
	return null;
}

/**
 * 输入框只输入0和非0开头的正整数
 * @param obj
 */
function checkPositiveNum(obj) {
	var reg = /^(0|[1-9][0-9]*)$/;   //0和非0开头的整数
	var regNum = /^\d+$/
	if(!reg.test(obj.value))
			obj.value='';
}

/**
 * 字符串格式化
 * 调用例	$.format('确定要删除商品:{0}吗？', productName);
 * 		{0}对应productName，支持个多参数
 */
$.format = function(source, params) {
	if (arguments.length == 1)
		return function() {
			var args = $.makeArray(arguments);
			args.unshift(source);
			return $.format.apply(this, args);
		};
		
	if (arguments.length > 2 && params.constructor != Array) {
		params = $.makeArray(arguments).slice(1);
	}
	
	if (params.constructor != Array) {
		params = [ params ];
	}
	
	$.each(params, function(i, n) {
		source = source.replace(new RegExp("\\{" + i + "\\}", "g"), n);
	});
	
	return source;
}; 
// 校验手机 1开头
function validPhone(str){
	var reg = /^1[0-9]{10}$/;
	return reg.test(str);
}
// 校验快递单号 5位数字字母
function validExpressNumber(str){
	var reg = /^[0-9a-zA-Z]{5,}$/;
	return reg.test(str);
}
// 校验客服电话
function validServerPhone(str){
	var reg = /^[0-9\-]{5,20}$/;
	return reg.test(str);
}
//公司代码
function validCode(str){
	var reg = /^[0-9\w_\-.]+$/;
	return reg.test(str);
}
// 校验6位纯数字验证码
function validCheckcode(str){
	var reg = /^[0-9]{6}$/;
	return reg.test(str);
}
//校验网址
function validHttp(str){
	var reg = /^(http|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?$/;
	return reg.test(str);
}
// 检验字符 英文中文下划线
function validChars(str){
	var reg = /^[\u4e00-\u9fa5a-zA-Z0-9\-]{1,}$/;
	return reg.test(str);
}
// 检验名字
function validName(str){
	var reg = /^[\u4e00-\u9fa5a-zA-Z]{1,10}$/;
	return reg.test(str);
}

//校验im账号
function validImAccount(str){
	var reg = /^[0-9]{6,16}$/;
	return reg.test(str);
}
//校验im密码
function validImPwd(str){
	var reg = /^[0-9a-zA-Z]{6,16}$/;
	return reg.test(str);
}

//校验im密码，可以是特殊字符
function validImPwd1(str){
//	var reg = /[，。、！？：“”［］——（）…！＠＃￥＆＊＋＞＜；：‘\u4e00-\u9fa5\s\n\r\t]{6,16}/i;
	var reg =  /[\u4E00-\u9FA5\uF900-\uFA2D]|[\uFF00-\uFFEF]/i;
	return reg.test(str);
}

//-------table列数据格式化start---------//
function webFormatter(value, row, index){
	return [ '<a href="', value, '" target="_blank">', value, '</a>' ].join('');
}

function codeFormatter(value, row, index){
	return ['<strong class="text-success">',value,'</strong>'].join('');
}

function statusFormatter(value, row, index){
	var text = value == 1 ? '启用' : '禁用';
	var color = value == 1 ? 'label-success' : 'label-danger';
	return [ '<span class="label ', color, '">', text, '</span>' ].join('');
}

function operationFormatter(value, row, index){
	var text = row.status == 0 ? "启用" : "禁用";
	var color = row.status == 0 ? 'btn-success' : 'btn-danger';
	return [ '<div class="btn-group btn-group-xs">',
			 '<button type="button" class="btn btn-primary btn-xs editBtn">',
			 '<i class="glyphicon glyphicon-pencil"></i> 编辑</button>',
			 '<button type="button" class="btn ', color, ' btn-xs statusBtn">',
			 '<i class="glyphicon glyphicon-cog"></i> ', text, '</button>',
			 '</div>' ].join('');
}
//-------成功失败提示---------//
function failTip(tip){
	qiao.bs.alert({
		backdrop : true,
		msg : '<strong class="text-danger">'+ tip + '</strong>',
		mstyle : 'width:300px;'
	});
}

function successTip(tip){
	qiao.bs.alert({
		backdrop : true,
		msg : '<strong class="text-success">'+ tip +'</strong>',
		mstyle : 'width:300px;'
	});
}

/**       
 * 对Date的扩展，将 Date 转化为指定格式的String       
 * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q) 可以用 1-2 个占位符       
 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)       
 * eg:       
 * (new Date()).pattern("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423       
 * (new Date()).pattern("yyyy-MM-dd E HH:mm:ss") ==> 2009-03-10 二 20:09:04       
 * (new Date()).pattern("yyyy-MM-dd EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04       
 * (new Date()).pattern("yyyy-MM-dd EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04       
 * (new Date()).pattern("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18       
 */          
Date.prototype.pattern=function(fmt) {           
    var o = {           
    "M+" : this.getMonth()+1, //月份           
    "d+" : this.getDate(), //日           
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时           
    "H+" : this.getHours(), //小时           
    "m+" : this.getMinutes(), //分           
    "s+" : this.getSeconds(), //秒           
    "q+" : Math.floor((this.getMonth()+3)/3), //季度           
    "S" : this.getMilliseconds() //毫秒           
    };           
    var week = {           
    "0" : "/u65e5",           
    "1" : "/u4e00",           
    "2" : "/u4e8c",           
    "3" : "/u4e09",           
    "4" : "/u56db",           
    "5" : "/u4e94",           
    "6" : "/u516d"          
    };           
    if(/(y+)/.test(fmt)){           
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));           
    }           
    if(/(E+)/.test(fmt)){           
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);           
    }           
    for(var k in o){           
        if(new RegExp("("+ k +")").test(fmt)){           
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));           
        }           
    }           
    return fmt;           
} 