<!DOCTYPE html>
<html>
  <head>
    <title>微吕炸金花</title>
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
		
		<div class="weui-tab">
	      <div class="weui-tab__bd">
	      	<#if isAdmin == true>
		        <div id="tab1" class="weui-tab__bd-item" style="-webkit-overflow-scrolling:touch; overflow: scroll;">
		          <iframe style='width:100%;height:90%' scrolling="yes" frameborder="0" src="/backend/proxy/proxyManagement"></iframe>
		        </div>
	        </#if>
	        <div id="tab2" class="weui-tab__bd-item" style="-webkit-overflow-scrolling:touch; overflow: scroll;">
	          <iframe style='width:100%;height:90%' scrolling="yes" frameborder="0" src="/backend/proxy/myMembers"></iframe>
	        </div>
	        <#if isAdmin == true>
		        <div id="tab3" class="weui-tab__bd-item" style="-webkit-overflow-scrolling:touch; overflow: scroll;">
		          <iframe style='width:100%;height:90%' scrolling="yes" frameborder="0" src="/backend/proxy/winProbability"></iframe>
		        </div>
	        </#if> 
	        <div id="tab4" class="weui-tab__bd-item weui-tab__bd-item--active" style="-webkit-overflow-scrolling:touch; overflow: scroll;">
	          <iframe style='width:100%;height:90%' scrolling="yes" frameborder="0" src="/backend/proxy/proxyInfo"></iframe>
	        </div>
	      </div>
	
	      <div class="weui-tabbar">
	      	<#if isAdmin == true>
		        <a href="#tab1" class="weui-tabbar__item">
		          <div class="weui-tabbar__icon">
		            <img src="/resources/jquery-weui-build/demos/images/icon_nav_button.png" alt="">
		          </div>
		          <p class="weui-tabbar__label">代理管理</p>
		        </a>
	        </#if>  
	        <a href="#tab2" class="weui-tabbar__item">
	          <div class="weui-tabbar__icon">
	            <img src="/resources/jquery-weui-build/demos/images/icon_nav_msg.png" alt="">
	          </div>
	          <p class="weui-tabbar__label">我的会员</p>
	        </a>
	        <#if isAdmin == true>
	        	<a href="#tab3" class="weui-tabbar__item">
		          <div class="weui-tabbar__icon">
		            <img src="/resources/jquery-weui-build/demos/images/icon_nav_article.png" alt="">
		          </div>
		          <p class="weui-tabbar__label">概率控制</p>
		        </a>
			</#if>  
	        
	        <a href="#tab4" class="weui-tabbar__item weui-bar__item--on">
	          <div class="weui-tabbar__icon">
	            <img src="/resources/jquery-weui-build/demos/images/icon_nav_cell.png" alt="">
	          </div>
	          <p class="weui-tabbar__label">基本信息</p>
	        </a>
	      </div>
	    </div>
		<script src="/resources/jquery-weui-build/lib/jquery-2.1.4.js"></script>
		<script src="/resources/jquery-weui-build/lib/fastclick.js"></script>
		<script>
		  $(function() {
		    FastClick.attach(document.body);
		  });
		</script>
		<script src="/resources/jquery-weui-build/js/jquery-weui.js"></script>
    </body>
</html>

