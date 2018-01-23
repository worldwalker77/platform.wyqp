$(function() {
	FastClick.attach(document.body);
	
	$('#confirmLogin').click(function() {
		var mobilePhone = $('#mobilePhone').val();
		var password = $('#password').val();
		if (mobilePhone == '' || password == '') {
			alert("手机号或密码不能为空");
			return;
		}
//		var data = {};
//		data["mobilePhone"] = mobilePhone;
//		data["password"] = password;
		$.ajax({
//	        type: "post",
	        url: '/login/doLogin?' + "mobilePhone=" + mobilePhone + '&password=' + password,
	        dataType: "json",
//	        data:data,
	        beforeSend: function () {
	        	
	        },
	        success: function (data) {
	        	if (data.code != 0) {
	        		alert(data.desc);
				}else{
					window.location.replace($('#redirectUrl').val());
				}
	        },
	        complete: function () {
	            
	        },
	        error: function (data) {
	        	alert("异常");
	        }
	    });
	});
});

