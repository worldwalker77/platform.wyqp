<!DOCTYPE html>
<html>
  <head>
    <title>万友游戏</title>
    <meta charset="utf-8">
	<#include "ftl/common/common.ftl"/> 
  </head>
    
  <body>
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">
	            	赠送房卡
	        </h3>
	    </div>
	    <div class="panel-body" style="padding-left:0;padding-right:0">
	        	<form class="bs-example bs-example-form" role="form">
			        <div class="input-group">
			            <span class="input-group-addon">赠送给</span>
			            <input type="text" id="to-player-id" class="form-control" placeholder="游戏id">
			        </div>
			        <br>
			        <div class="input-group">
			            <span class="input-group-addon">房卡数</span>
			            <input type="text" id="room-card-num" class="form-control" placeholder="房卡数">
			        </div>
			    </form>
			    <br>
			    <button type="button" id="give-away" class="btn btn-primary btn-block">赠送</button>
	    </div>
	</div>
	<script type="text/javascript" src="/resources/js/wechat/giveAwayRoomCards.js?version=${version}"></script>
  </body>
</html>

