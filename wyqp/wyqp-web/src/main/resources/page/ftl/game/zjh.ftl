<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <title>微侣-炸金花</title>
    <meta name="viewport" content="width=device-width,initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="full-screen" content="true"/>
    <meta name="screen-orientation" content="landscape"/>
    <meta name="x5-fullscreen" content="true"/>
    <meta name="360-fullscreen" content="true"/>
    <style>
        html, body {
            -ms-touch-action: none;
            background: #000000;
            padding: 0;
            border: 0;
            margin: 0;
            height: 100%;
        }
    </style>

    <!--这个标签为通过egret提供的第三方库的方式生成的 javascript 文件。删除 modules_files 标签后，库文件加载列表将不会变化，请谨慎操作！-->
    <!--modules_files_start-->
	<script egret="lib" src="${h5GameStaticUrl}libs/modules/egret/egret.min.js"></script>
	<script egret="lib" src="${h5GameStaticUrl}libs/modules/egret/egret.web.min.js"></script>
	<script egret="lib" src="${h5GameStaticUrl}libs/modules/game/game.min.js"></script>
	<script egret="lib" src="${h5GameStaticUrl}libs/modules/res/res.min.js"></script>
	<script egret="lib" src="${h5GameStaticUrl}libs/modules/eui/eui.min.js"></script>
	<script egret="lib" src="${h5GameStaticUrl}libs/modules/tween/tween.min.js"></script>
	<script egret="lib" src="${h5GameStaticUrl}libs/modules/socket/socket.min.js"></script>
	<script egret="lib" src="${h5GameStaticUrl}weixinapi/bin/weixinapi/weixinapi.min.js"></script>
	<script egret="lib" src="${h5GameStaticUrl}promise/promise.min.js"></script>
	<!--modules_files_end-->

    <!--这个标签为不通过egret提供的第三方库的方式使用的 javascript 文件，请将这些文件放在libs下，但不要放在modules下面。-->
    

    <!--这个标签会被替换为项目中所有的 javascript 文件。删除 game_files 标签后，项目文件加载列表将不会变化，请谨慎操作！-->
    <!--game_files_start-->
	<script src="${h5GameStaticUrl}main.min.js?version=${version}"></script>
	<script src="/resources/js/jquery/jquery.min.js"></script>
	<!--game_files_end-->
</head>
<body>
	<input type="hidden" id="hd-token" value="${token}">
	<input type="hidden" id="hd-room-id" value="${roomId}">
	<input type="hidden" id="hd-player-num-limit" value="${playerNumLimit}">
	
	<input type="hidden" id="hd-domain" value="${domain}">
	<input type="hidden" id="hd-h5-game-static-url" value="${h5GameStaticUrl}">
	
    <div style="margin: auto;width: 100%;height: 100%;" class="egret-player" id="gameDiv"
         data-entry-class="Main"
         data-orientation="landscape"
         data-scale-mode="fixedWidth"
         data-frame-rate="30"
         data-content-width="1136"
         data-content-height="640"
         data-show-paint-rect="false"
         data-multi-fingered="2"
         data-show-fps="false" data-show-log="false"
         data-show-fps-style="x:0,y:0,size:12,textColor:0xffffff,bgAlpha:0.9">
    </div>
    <script>
		//是否对接微信
		function isUseWeixin()
		{
			return 0;
		}
		//
		function getCdnUrl()
		{
			return $("#hd-h5-game-static-url").val();
		}
		//httpapi
		function getHttpApi()
		{
			return "http://" + $("#hd-domain").val() + "/";
		}
		function getToken()
		{
			return $("#hd-token").val();
		}
		
		function getRoomId()
		{
			return $("#hd-room-id").val();
		}
		function getPlayerNumLimit()
		{
			return $("#hd-player-num-limit").val();
		}
        /**
         * {
         * "renderMode":, //引擎渲染模式，"canvas" 或者 "webgl"
         * "audioType": "" //使用的音频类型，0:默认，1:qq audio，2:web audio，3:audio
         * "antialias": //WebGL模式下是否开启抗锯齿，true:开启，false:关闭，默认为false
         * }
         **/
        egret.runEgret({renderMode:"canvas", audioType:0});
    </script>
</body>
</html>
