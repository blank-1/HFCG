;(function($){
	function getParms(w){
	var url=window.location.search.substring(1),
		arr=url.match(new RegExp('(^|&)'+w+'='+'([^&]*)'));
		return arr[2];	
}
function clickShare(){//点击分享
	$("#share").on("click",function(){
		$(".maskBox").show().on("click",function(){
			$(this).hide()
		})
	})
}
clickShare()
$('#resultNum').html(getParms('num'))
var val=$('#resultNum').html();
if(val<=5){
	$("#octuple").addClass("octuple")
}else if(val<=10){
	$("#octuple").addClass("wukongDui")
}else if(val<=50){
	$("#octuple").addClass("regular")
}
if(val==0){
	$("#w_fen").html("<br>请继续努力！");
	$("#designation").hide()
}
if(val>=1){
	$("#w_fen").html("<br>鬼子落荒而逃,</br>获得称号：");
	$("#designation").show()
}
})(Zepto)