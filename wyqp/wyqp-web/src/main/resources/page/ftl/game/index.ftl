<html ng-app="app" class="ng-scope"><head><style type="text/css">@charset "UTF-8";[ng\:cloak],[ng-cloak],[data-ng-cloak],[x-ng-cloak],.ng-cloak,.x-ng-cloak,.ng-hide:not(.ng-hide-animate){display:none !important;}ng\:form{display:block;}.ng-animate-shim{visibility:hidden;}.ng-anchor{position:absolute;}</style><style type="text/css">@charset "UTF-8";[ng\:cloak],[ng-cloak],[data-ng-cloak],[x-ng-cloak],.ng-cloak,.x-ng-cloak,.ng-hide:not(.ng-hide-animate){display:none !important;}ng\:form{display:block;}.ng-animate-shim{visibility:hidden;}.ng-anchor{position:absolute;}</style>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="format-detection" content="telephone=no">
<meta name="msapplication-tap-highlight" content="no">
<title>微吕游戏</title>
<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>

</head>
	<body ng-controller="myCtrl" style="background: #000;" class="ng-scope">
		<input id="hd-player-id" type="hidden" value="${playerId!''}" />
		<input id="hd-nick-name" type="hidden" value="${nickName!''}" />
		<input id="hd-token" type="hidden" value="${token!''}" />
		<input type="hidden" id="hd-domain" value="${domain}">
		<div class="main" style="">

			<img src="/resources/images/body1-1.jpg" style="width: 100%;height: 100%;position: fixed;top:0;left:0;">
			<img src="/resources/images/top-1.png" style="width:60%;position: fixed;height:17vw;top:0;left:0;z-index:99 ">
			<div class="user" style="height: 72.39999999999999;position: fixed;z-index:99;">
				<img ng-src="http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKfhjTxZu9Abv1eO5dwkgl4eDkuticTHAE9EcicaRYyxmHJe4OQo3MMecDQL5fd7c4T3bw8Iz0huxVw/0" class="avatar" style="width:13.2vw;height:13.2vw;margin-left:2.8vw;margin-top:1vw; " src="${headImgUrl!''}">
				<a class="name ng-binding" style="margin-top:5.3vw;color:#fff;position:fixed;top:2vw;left:18.5vw">${nickName!''}</a>		
			</div>
			<div class="roomCard" style="position:fixed;z-index:99;right:0;top0;width:26% ">
				<img src="/resources/images/roomCard.png" class="img1" style="height:8vw;margin-top:5vw;float:left;position:relative;z-index:50 ">
				<a class="num ng-binding" style="margin-top:5.3vw; background: rgba(0,0,0,0.5);border-radius:4vw;color:#fff;float:left;font-size:3.5vw;margin-left:-3vw;padding:1vw 4vw;z-index:49;position:relative">${roomCardNum!''}张</a>	
			</div>

			
			<img id="entry-12-flower" src="/resources/images/12-flower.jpg" style="width: 43vw;position: absolute;height:34vw;top:50vw;left:5vw;border-radius: 2vw;">
			<img id="entry-12-bull" src="/resources/images/12-bull.jpg" style="width: 43vw;position: absolute;height:34vw;top:50vw;right:5vw;border-radius: 2vw;">
			<img id="entry-6-flower" src="/resources/images/6-flower.jpg" style="width: 43vw;position: absolute;height:34vw;top:124vw;left:5vw;border-radius: 2vw;" >
			<img id="entry-6-bull" src="/resources/images/6-bull.png" style="width: 43vw;position: absolute;height:34vw;top:124vw;right:5vw;border-radius: 2vw;">
			<img src="/resources/images/26bullfight-1.png" style="width: 43vw;position: absolute;height:34vw;top:198vw;left:5vw;border-radius: 2vw;">
			<img src="/resources/images/26landlord-1.png" style="width: 43vw;position: absolute;height:34vw;top:161vw;right:5vw;border-radius: 2vw;">
			<img src="/resources/images/26majiang-1.png" style="width: 43vw;position: absolute;height:34vw;top:161vw;left:5vw;border-radius: 2vw;">
			<img id="entry-9-flower" src="/resources/images/9-flower.jpg" style="width: 43vw;position: absolute;height:34vw;top:87vw;left:5vw;border-radius: 2vw;">
			<img id="entry-9-bull" src="/resources/images/9-bull.png" style="width: 43vw;position: absolute;height:34vw;top:87vw;right:5vw;border-radius: 2vw;">
			<img src="/resources/images/home_new1.png" style="width: 16vw;position: absolute;height:16vw;top:86vw;left:51vw;border-radius: 2vw;"> 
			
			<div style="height: 237vw"></div>
		</div>
		
		<script src="/resources/js/jquery/jquery.min.js"></script>
		<script type="text/javascript" src="/resources/js/wechat/index.js"></script>
	</body>
</html>