
//点击显示或隐藏图层
$(".w_sureBtn").on("click",function(){
	$(".w_uiMask").show().on("click",function(){
		$(this).hide()
	})
})