<!DOCTYPE html>
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0">
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="no-cache"> 
<meta http-equiv="Expires" content="0"> 
<title>扎金花</title>
<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
<script>
// 是否是安卓
function isAndroid() {
var u = navigator.userAgent;
if (u.indexOf('Android') > -1 || u.indexOf('Linux') > -1) { //安卓手机
return true;
}
return false;
}

function isIOS(){
var u = navigator.userAgent;
var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
return isiOS;
}
function isRunOnWeChat(){
var ua = navigator.userAgent.toLowerCase();  
    if(ua.match(/MicroMessenger/i)=="micromessenger") {  
        return true;  
    } else {  
        return false;  
    }  
}
$(document).ready(function(){
initialize();
function initialize(){
if(true == isIOS()){
$('#download-tip').removeClass("common-hide");
$('.common-ios').removeClass('common-hide');
}else if(true == isAndroid()){
$('.common-android').removeClass('common-hide');
}else{
$('.common-pc').removeClass('common-hide');
}
// 如果在微信中打开，直接弹遮罩
if(true == isRunOnWeChat()){
}
}
$('#download-ios').on('click', function(e){
if(true == isRunOnWeChat()){
e.preventDefault();
$('#mask').removeClass("common-hide");
}
});
$('#download-pc').on('click', function(){
alert("在手机中浏览器中打开点击“立即下载”，将会自动下载安装包。");
});
$('#mask').on('click', function(){
//$('#mask').addClass("common-hide");
});
});
</script>
<style>
*
{
margin:0;
padding:0;
}

html, body
{
background: rgb(255, 240, 189);
font-family: "Microsoft YaHei" ! important;
text-align: center;
}
.hp{

}
.common-center{
margin-left:auto;
margin-right:auto;
text-align:center;
}
.common-hide{
display: none;
}
.hp-head{
position: relative;
text-align: center;
background: #fff;
background:url(/resources/img/sjsj/xixi_topdh.png) no-repeat center top;margin: 0 auto;width:100%px;min-height:60px;_height:60px;
}
.hp-head img{
width: 258px;
height: 60px;
}
.hp-desc{
padding-top: 24px;
padding-bottom: 24px;
text-align: center;
}
.hp-desc-title{
color: #ff0000;
font-size: 20px;
padding-top: 5px;
margin-bottom: 10px;
font-weight:bold;
}

.hp-desc-detail{
font-size: 13px;
color: #5a5a5a;
margin-bottom: 1px;
}

.hp-desc-logo{
margin-top: 1px;
width: 120px;
height: 120px;
}

.hp-desc-logo1{
margin-top: 1px;
margin-left: 38px;
width: 120px;
height: 120px;
}

.hp-download{
text-align: center;
margin-left: 12px;
margin-right: 12px;
}
.hp-download-btn{
width: 46%;
}

.hp-mask{
}
.hp-mask-background{
position: fixed;
top: 0;
left: 0;
right: 0;
bottom: 0;
background: #000;
opacity: 0.6;
}
.hp-mask-instruction{
position: fixed;
top: 6px;
right: 8px;
width: 200px;
height: 137px;
}

.hp-guid{

}

.hp-guid img{
max-width: 300px;
display: block;
margin-top: 16px;
}
.hp-guid-title{
margin-top: 10px;
color: #ffa323;
font-size:15px;
margin-top: 10px;
margin-bottom: 10px;
}

</style>
</head>
<body>
<!-- head -->
<section class="hp-head">
<img class="common-center" src="/resources/img/sjsj/logo1.png">
</section>
<!-- 描述 -->
<section class="hp-desc">
<p><img class="common-center hp-desc-logo" src="/resources/img/sjsj/icon_logo1.png">   <img class="common-center hp-desc-logo1" src="/resources/img/sjsj/icon_logo2.png"></p>
<!--<p class="hp-desc-title">麻城红中痞子杠、麻城牛牛—约局神器</p>
<p class="hp-desc-detail">麻城人自己的手机游戏，目前有【麻城红中痞子杠】</p>
<p class="hp-desc-detail">和【麻城牛牛】玩法，与现实中好友一起组局线上游戏。</p>
<p class="hp-desc-detail">全面支持安卓、IOS客户端，随时随地想玩就玩。</p>-->
</section>
<!-- 下载 -->
<section class="hp-download">
<!-- ios下载链接 -->
<a class="common-ios hp-download-download common-hide" href="http://fir.im/wj4h">
<img id="download-ios" class="common-center hp-download-btn" src="/resources/img/sjsj/ios_down.png">
</a>
<a class="common-ios hp-download-download common-hide" href="http://fir.im/wj4h">
<img id="download-android" class="common-center hp-download-btn" src="/resources/img/sjsj/anzhuo_down1.png">
</a>
<!-- android下载链接 -->
<a class="common-android hp-download-download common-hide" href="http://fir.im/wj4h">
<img id="download-android" class="common-center hp-download-btn" src="/resources/img/sjsj/anzhuo_down.png">
</a>
<a class="common-android hp-download-download common-hide" href="http://fir.im/wj4h">
<img id="download-ios" class="common-center hp-download-btn" src="/resources/img/sjsj/ios_down1.png">
</a>
<!-- pc下载链接  sdfsd -->
<a href="http://fir.im/wj4h">
<img id="download-pc" class="common-center common-pc hp-download-btn common-hide" src="/resources/img/sjsj/ios_down.png"></a>
<a href="http://fir.im/wj4h">
<img id="download-pc" class="common-center common-pc hp-download-btn common-hide" src="/resources/img/sjsj/anzhuo_down.png"></a>
</section>

<section id="mask" class="common-hide hp-mask">
<div class="hp-mask-background"></div>
<img class="hp-mask-instruction" src="/resources/img/sjsj/instruction.png">
</section>
<img width="100%" src="/resources/img/sjsj/jianjie.png">
<div align="center"></div><div id="cli_dialog_div"></div></body></html>