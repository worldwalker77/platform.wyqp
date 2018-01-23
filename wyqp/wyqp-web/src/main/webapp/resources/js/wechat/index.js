    
$(function() {
	$("#entry-6-flower").click(function(){
		window.location.replace("http://" + $("#hd-domain").val() + "/game/zjh?playerNumLimit=6&token=" + $('#hd-token').val());
	});
	$("#entry-9-flower").click(function(){
		window.location.replace("http://" + $("#hd-domain").val() + "/game/zjh?playerNumLimit=9&token=" + $('#hd-token').val());
	});
	$("#entry-12-flower").click(function(){
		window.location.replace("http://" + $("#hd-domain").val() + "/game/zjh?playerNumLimit=12&token=" + $('#hd-token').val());
	});
});


