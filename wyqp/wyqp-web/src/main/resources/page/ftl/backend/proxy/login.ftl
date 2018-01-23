<!DOCTYPE html>
<html>
  <head>
    <title>代理登录</title>
    <meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
	
	<meta name="description" content="Write an awesome description for your new site here. You can edit this line in _config.yml. It will appear in your document head meta (for Google search results) and in your feed.xml site description.
	">
	
	<link rel="stylesheet" href="/resources/jquery-weui-build/lib/weui.min.css">
	<link rel="stylesheet" href="/resources/jquery-weui-build/css/jquery-weui.css">
	<link rel="stylesheet" href="/resources/jquery-weui-build/demos/css/demos.css">
    </head>
    
    <body>
		<input id="redirectUrl" type="hidden" value="${redirectUrl}" />
		
		<div class="weui-cells weui-cells_form">
		  <div class="weui-btn weui-btn_disabled weui-btn_warn">代理后台登录</div>
		  <div class="weui-cell">
		    <div class="weui-cell__hd">
		      <label class="weui-label">手机号</label>
		    </div>
		    <div class="weui-cell__bd">
		      <input id="mobilePhone" class="weui-input" type="tel" placeholder="请输入手机号">
		    </div>
		  </div>
		  <div class="weui-cell">
		    <div class="weui-cell__hd"><label class="weui-label">密码</label></div>
		    <div class="weui-cell__bd">
		      <input id="password" class="weui-input" type="number" pattern="[0-9]*" placeholder="请输入密码">
		    </div>
		  </div>
		  <div id="confirmLogin" class="weui-btn weui-btn_warn">确认</div>
		</div>
		<script src="/resources/jquery-weui-build/lib/jquery-2.1.4.js"></script>
		<script src="/resources/jquery-weui-build/lib/fastclick.js"></script>
		<script src="/resources/jquery-weui-build/js/jquery-weui.js"></script>
		<script type="text/javascript" src="/resources/js/wechat/login.js?version=${version}"></script>
    </body>
</html>

