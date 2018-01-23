$(function() {
	/*bootstrap table*/
	$('.table.table-striped:eq(0)').bootstrapTable({
		method : 'post',
	    url:"/backend/proxy/getProxys",//请求数据url
	    queryParams: function (params) {
	        return {
	            offset: params.offset,  //页码
	            limit: params.limit,   //页面大小
	            startDate : $('#startDate').val(), //开始时间
	            endDate : $('#endDate').val(), //结束时间
	            proxyId : $('#proxyId').val()
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
	    columns : [
	     {
			field : 'proxyId',
			title : '代理ID'
		},{
			field : 'wechatNum',
			title : '微信号'
		},{
			field : 'mobilePhone',
			title : '手机号'
		},{
			field : 'playerId',
			title : '游戏ID'
		},{
			field:'createTime',
			title: '代理时间'
        }, {
			field : '',
			title : '操作',
			formatter:function(value,row,index){  
	            var e = "<a href='#' onclick='proxyManagementUtil.edit(" + JSON.stringify(row) + ")'>编辑</a>";  
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
		$('#startDate').val('');
		$('#endDate').val('');
	});
	
	$("#startDate").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
//        startDate:new Date()
    }).on("click",function(){
        $("#startDate").datetimepicker("setEndDate",$("#endDate").val());
    });
    $("#endDate").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
//        startDate:new Date()
    }).on("click",function(){
        $("#endDate").datetimepicker("setStartDate",$("#startDate").val());
    });

    $('#addProxy').click(function(){
    	$("#editMobilePhone").val('');
		$("#editPlayerId").val('');
		$("#editWechatNum").val('');
		$("#hiddenProxyId").val('0');
    	$('#myModal').modal({});
	});
    
    $('#modifyProxy').click(function(){
    	proxyManagementUtil.modifyProxy();
	});
});

var proxyManagementUtil = {
		edit:function(row){
			$("#editMobilePhone").val(row.mobilePhone);
			$("#editPlayerId").val(row.playerId);
			$("#editWechatNum").val(row.wechatNum);
			$("#hiddenProxyId").val(row.proxyId);
			$('#myModal').modal({});
		},
		modifyProxy:function(){
			var data = {
					mobilePhone:$("#editMobilePhone").val(),
					playerId:$("#editPlayerId").val(),
					wechatNum:$("#editWechatNum").val(),
					proxyId:$("#hiddenProxyId").val()
			}
			$.ajax({
		        type: "post",
		        url: '/backend/proxy/modifyProxy',
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
		                    message: '代理操作成功',
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
		}
		
		
}

