<!DOCTYPE html>
<html>
  <head>
    <title>嘻哈纸牌</title>
    <meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
	
	<meta name="description" content="Write an awesome description for your new site here. You can edit this line in _config.yml. It will appear in your document head meta (for Google search results) and in your feed.xml site description.
	">
	<#include "ftl/common/common.ftl"/> 
	<link rel="stylesheet" href="/resources/jquery-weui-build/lib/weui.min.css">
	<link rel="stylesheet" href="/resources/jquery-weui-build/css/jquery-weui.css">
	<link rel="stylesheet" href="/resources/jquery-weui-build/demos/css/demos.css">
    </head>
    
    <body>
		
		<div class="weui-cells">
		  <div class="weui-cell">
		    <div class="weui-cell__hd"><img src=""></div>
		    <div class="weui-cell__bd">
		      <p>推广ID</p>
		    </div>
		    <div class="weui-cell__ft">${proxyId}</div>
		  </div>
		  <div class="weui-cell">
		    <div class="weui-cell__hd"><img src=""></div>
		    <div class="weui-cell__bd">
		      <p>游戏ID</p>
		    </div>
		    <div class="weui-cell__ft">${playerId}</div>
		  </div>
		  <div class="weui-cell">
		    <div class="weui-cell__hd"><img src=""></div>
		    <div class="weui-cell__bd">
		      <p>手机号</p>
		    </div>
		    <div class="weui-cell__ft">${mobilePhone}</div>
		  </div>
		  <div class="weui-cell">
		    <div class="weui-cell__hd"><img src=""></div>
		    <div class="weui-cell__bd">
		      <p>微信号</p>
		    </div>
		    <div class="weui-cell__ft">${wechatNum}</div>
		  </div>
		</div>
		
		
		<div class="weui-cells">
		  <div class="weui-cell">
		    <div class="weui-cell__hd"><img src=""></div>
		    <div class="weui-cell__bd">
		      <p>累计收益(元)</p>
		    </div>
		    <div class="weui-cell__ft">${totalIncome}</div>
		  </div>
		  <div class="weui-cell">
		    <div class="weui-cell__hd"><img src=""></div>
		    <div class="weui-cell__bd">
		      <a href='/backend/proxy/giveAwayRoomCards'>赠送房卡</a>
		    </div>
		  </div>
		  
		  <div class="weui-cell">
		    <div class="weui-cell__hd"><img src=""></div>
		    <div class="weui-cell__bd">
		      <a id='modifyPassword' href='#'>修改密码</a>
		    </div>
		  </div>
		</div>
		
		<!-- 模态框（Modal） -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		    <div class="modal-dialog">
		        <div class="modal-content">
		            <div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title" id="myModalLabel">修改密码</h4>
		            </div>
		            <div class="modal-body">
		            		<div class="row"> 
			            		<div class="col-xs-3">
									<span>手机号</span>
								</div>
								<div class="col-xs-4">
									<input size="16" type="text" id="mobilePhone">
								</div>
							</div>
							<div class="row"> 
			            		<div class="col-xs-3">
									<span>老密码</span>
								</div>
								<div class="col-xs-4">
									<input size="16" type="text" id="oldPassword">
								</div>
							</div>
							<div class="row"> 
			            		<div class="col-xs-3">
									<span>新密码</span>
								</div>
								<div class="col-xs-4">
									<input size="16" type="text" id="newPassword">
								</div>
							</div>
		            </div>
		            <div class="modal-footer">
		                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		                <button type="button" class="btn btn-primary" id="doModifyPassword">确定</button>
		            </div>
		        </div><!-- /.modal-content -->
		    </div><!-- /.modal -->
		
		<script src="/resources/jquery-weui-build/lib/fastclick.js"></script>
		<script>
		  $(function() {
		    FastClick.attach(document.body);
		  });
		</script>
		<script src="/resources/jquery-weui-build/js/jquery-weui.js"></script>
		<script type="text/javascript" src="/resources/js/wechat/proxyInfo.js?version=${version}"></script>
    </body>
</html>

