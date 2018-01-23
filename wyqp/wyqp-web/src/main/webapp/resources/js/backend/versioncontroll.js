var versionTable = $('.table.table-striped:eq(0)');
var currentPageSize = 5;
$(function() {  
	VersionViewer.init();
	VersionViewer.initEvents();  
});  

var VersionViewer = {

		/**
		 * 初始化
		 */
		init : function(){
			VersionViewer.loadTable();
		},
			
		// 加载表格
		loadTable : function() {
			versionTable.bootstrapTable({
				method : 'post',
				url : contextPath + "/backend/getVersionList",
				contentType : 'application/x-www-form-urlencoded',
				showColumns : false,
				showRefresh : false,
				idField : 'id',
				queryParams : VersionViewer.queryParams,
				responseHandler : VersionViewer.responseHandler,
				columns : [  {
					//field : 'id',
					title : '序号',
					formatter : function(value, row, index) {
						return [index + 1].join('');
					}
				}, {
					field : 'clientVersion',
					title : '版本号'
				},{
					field : 'codeUrl',
					title : '代码下载地址'
				}, {
					field : 'updateUrl',
					title : '资源包下载地址'
				}, {
					field : 'newFeature',
					title : '新特性'
				}, {
					field : 'createTime',
					title : '创建时间',
				}, {
					field : 'updateTime',
					title : '更新时间'
				}]
			});
		},
		
		// 查询请求参数
		queryParams : function(params) {
	
			var dto = {
					
			};
	
			// pageSize改变，从第一页开始
			if (currentPageSize != params.limit) {
				// 设置从起始页开始
				versionTable.bootstrapTable('getOptions').pageNumber = 1;
				// 必须设置
				params.offset = 0;
			}
			currentPageSize = params.limit;
			dto.start = params.offset;
			dto.limit = params.limit;
			return dto;
		},
		// 表格回调函数
		responseHandler : function(res) {
			if (res.code == 0) {
				return res.data;
			} else {
				return {
					"rows" : [],
					"total" : 0
				};
			}
		},
	

	initEvents:function() {
		$("#submit-button").click(function(){
			var clientVersion = $("#client-version").val();
			var changeLog = $("#change-log").val();
			var file=$("#multipart-files").val();
			if (clientVersion == '' || changeLog == '' || file == null || file == '') {
				failTip("请在输入框中输入正确的值");
				return;
			}
			$("#submit-button").attr('disabled',"true");
			var date = new FormData($("form[name='versionUploadForm']")[0]);
			var url = contextPath + "/backend/uploadClientfile";
			var callback = function(result){
				if (result.code == 0) {
					failTip("上传版本成功");
					$("#submit-button").removeAttr("disabled"); 
					versionTable.bootstrapTable('refresh');
				}else{
					$("#submit-button").removeAttr("disabled"); 
					successTip("上传版本失败");
				}
				
			};
			var xmlhttp;
		      if (window.ActiveXObject) {
			        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			   }else if (window.XMLHttpRequest) {
			        xmlhttp = new XMLHttpRequest();
			  }
		      xmlhttp.onreadystatechange = function() {
		        if (xmlhttp.readyState == 4) {
		          if (xmlhttp.status == 200) {
		        	var result = $.parseJSON(xmlhttp.responseText);  
		            callback(result);
		            
		          }
		        }
		      };
		      xmlhttp.open("POST", url, true);
		      xmlhttp.send(date);
		});
	}
}


