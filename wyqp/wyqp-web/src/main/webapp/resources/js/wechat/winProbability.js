$(function() {
	/*bootstrap table*/
	$('.table.table-striped:eq(0)').bootstrapTable({
		method : 'post',
	    url:"/backend/proxy/getWinProbability",//请求数据url
	    queryParams: function (params) {
	        return {
	            offset: params.offset,  //页码
	            limit: params.limit,   //页面大小
//	            order : params.order, //排序
//	            ordername : params.sort, //排序
	            playerId : $('#playerId').val(), //开始时间
	            nickName : $('#nickName').val() //结束时间
	        };
	    },
	    responseHandler : function(res){
	    	if (res.code == 0) {
	    		return res.data;
			}else{
				return [];
			}
	    	
	    },
	    showHeader : true,
//	    showColumns : true,
//	    showRefresh : true,
	    pagination: true,//分页
	    sidePagination : 'server',//服务器端分页
	    pageNumber : 1,
	    pageList: [5, 10, 20, 50],//分页步进值
	    //表格的列
	    columns : [{
			field : 'playerId',
			title : '玩家游戏ID'
		}, {
			field : 'nickName',
			title : '玩家昵称'
		}, {
			field : 'winProbability',
			title : '赢牌概率'
		}, {
			field : '',
			title : '操作',
			formatter:function(value,row,index){  
	            var e = "<a href='#' onclick='probabilityUtil.edit(" + JSON.stringify(row) + ")'>编辑</a>";  
                return e;  
             } 
		}]
	});
	
	// 查询按钮点击事件,refresh方法存在bug，无法从第一页开始
	$('#searchBtn').click(function() {
		var res = $('.table.table-striped:eq(0)').bootstrapTable('getData').length;
		if (res > 0) {
			$('.table.table-striped:eq(0)').bootstrapTable('selectPage', 1);
		} else {
			$('.table.table-striped:eq(0)').bootstrapTable('refresh');
		}
	});
	
	/*清空按钮事件*/
	$('#resetBtn').click(function(){
		$('#playerId').val('');
		$('#nickName').val('');
	});
	
	/*清空按钮事件*/
	$('#modifyProbability').click(function(){
		var data = {
				playerId:$("#editPlayerId").val(),
				winProbability:$("#editWinProbability").val()
		}
		$.ajax({
	        type: "post",
	        url: '/backend/proxy/modifyWinProbability',
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
	                    message: '玩家' + $("#editNickName").val() + "的赢牌概率成功修改为：" + $("#editWinProbability").val(),
	                    buttons: [{
	                        label: '确定',
	                        action: function(dialog) {
	                            dialog.close();
	                            $('.table.table-striped:eq(0)').bootstrapTable('refresh');
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
	});
	
});

var probabilityUtil = {
		edit:function(row){
			$("#editPlayerId").val(row.playerId);
			$("#editNickName").val(row.nickName);
			$("#editWinProbability").val(row.winProbability);
			$('#myModal').modal({});
		}
}

