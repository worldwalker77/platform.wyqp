$(function() {
	$("#give-away").click(function(){
		var toPlayerId = $("#to-player-id").val();
		var roomCardNum = $("#room-card-num").val();
		$.ajax({
//	        type: "post",
	        url: '/backend/proxy/getUserByPlayerId?' + "playerId=" + toPlayerId,
	        dataType: "json",
//	        data:data,
	        beforeSend: function () {
	        	
	        },
	        success: function (res) {
	        	if (res.code == 0) {
	        		if (res.data == null || res.data.length == 0) {
	        			BootstrapDialog.show({
				            title: '错误提示',
				            message: '受赠玩家的游戏id不存在'
				        });
						return;
					}
	        		var list = res.data;
	        		var nickName = list[0].nickName;
	        		BootstrapDialog.confirm('确定给玩家' + nickName + '赠送' + roomCardNum + '张房卡?', function(result){
	    	            if(result) {
	    	            	$.ajax({
//	    	        	        type: "post",
	    	        	        url: '/backend/proxy/doGiveAwayRoomCards?' + "toPlayerId=" + toPlayerId + '&roomCardNum=' + roomCardNum,
	    	        	        dataType: "json",
//	    	        	        data:data,
	    	        	        beforeSend: function () {
	    	        	        	
	    	        	        },
	    	        	        success: function (res) {
	    	        	        	if (res.code == 0) {
	    	        	        		BootstrapDialog.show({
	    	        	                    title: '成功提示',
	    	        	                    message: '成功赠送给' + nickName + roomCardNum + '张房卡',
	    	        	                    buttons: [{
	    	        	                        label: '确定',
	    	        	                        action: function(dialog) {
	    	        	                            dialog.close();
	    	        	                            window.location.replace('/backend/proxy/proxyInfo');
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
		
	});
	
});

