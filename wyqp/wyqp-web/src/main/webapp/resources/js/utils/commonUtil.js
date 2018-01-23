/**
 * 网络连接层的回调函数,方法由具体实例去实现
 */
function ConnectLayCallBack() {
}
ConnectLayCallBack.prototype.success = function(data) {
};
ConnectLayCallBack.prototype.error = function(data) {
};
ConnectLayCallBack.prototype.complete = function(data) {
};
var urlCollection = {
		historyMsgsUrl :contextPath + '/backend/msg/list',
		getDistinctCsrsUrl :contextPath + '/backend/msg/getDistinctCsrs',
		getDistinctCustomersUrl :contextPath + '/backend/msg/getDistinctCustomers',
		sendSysMsgUrl :contextPath + '/backend/system/sendSysMsg',
		friendCsrListUrl :contextPath + '/backend/friends/csrlist',
		groupListUrl :contextPath + '/backend/friends/grouplist',
		buddyListUrl :contextPath + '/backend/friends/buddylist',
		versionUploadUrl :contextPath + '/backend/versioncontroll/uploadfile',
		enOrDisableUrl :contextPath + '/backend/versioncontroll/enordisable'
};
var commonUtil = {
		
		ajax : function(url, params, success, error, timeout, async){
			try {
				timeout = timeout ? timeout : 10000;
				var async = typeof (async) == 'undefined' ? true : async;
				return $.ajax({
					url : url,
					type : 'POST',
					dataType : "json",
					async : async,
					cache : false,
					timeout : timeout,
					data : params,
					success : success ? success : function(data, textStatus) {
					},
					error : error ? error : function(XMLHttpRequest,
							textStatus, errorThrown) {
						if (textStatus == "timeout") {
						}
					}
				});
			} catch (e) {
				commonUtil.Log.error(e);
			}
		},

		Log : {
			info : function(e){
				try {
					if(console && console.log){
						/*console.log(e);*/
					}
				} catch (e) {}
			},
			error : function(e){
				try {
					if(console && console.error){
						/*console.error(e);*/
					}
				} catch (e) {}
			}
		},
		
};