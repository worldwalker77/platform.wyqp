$(function() {

    $('#modifyPassword').click(function(){
    	$('#myModal').modal({});
	});
    
    $('#doModifyPassword').click(function(){
    	proxyInfoUtil.doModifyPassword();
	});
});

var proxyInfoUtil = {
		doModifyPassword:function(){
			var data = {
					mobilePhone:$("#mobilePhone").val(),
					oldPassword:$("#oldPassword").val(),
					newPassword:$("#newPassword").val()
			}
			$.ajax({
		        type: "post",
		        url: '/backend/proxy/doModifyPassword',
		        dataType: "json",
		        contentType: "application/json",
		        data:JSON.stringify(data),
		        beforeSend: function () {
		        	
		        },
		        success: function (res) {
		        	if (res.code == 0) {
		        		$('#myModal').modal('hide')
		        		BootstrapDialog.show({
		                    title: '成功提示',
		                    message: '修改密码成功',
		                    buttons: [{
		                        label: '确定',
		                        action: function(dialog) {
		                            dialog.close();
		                        }
		                    }]
		                });
					}else{
						BootstrapDialog.show({
				            title: '错误提示',
				            message: res.desc
				        });
					}
		        },
		        complete: function () {
		            
		        },
		        error: function (data) {
		        	alert("异常");
		        }
		    });
		}
		
		
}

