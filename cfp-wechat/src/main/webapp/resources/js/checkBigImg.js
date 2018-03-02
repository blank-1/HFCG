// JavaScript Document
function checkBig(){//点击查看大图
	var index=0,
		picList=$("#w_picList"),
		len=$("#w_picList li").size(),
		w=picList.find('li:first').outerWidth(),
		dotBtn=$(".dotBtn li");
		picList.width(w*(len+1))
	$("#w_picList").on("swipeleft","li",function(){
		index+=1;
		if(index>=len-1){
			index=len-1;
		}
		picList.css({
			'marginLeft':-w*index+"px",
			'-webkit-transition':'all .5s ease'
		},500)
		titFont=$(this).next().attr("data-type");
		$("#titFont").html(titFont);
		dotBtn.eq(index).addClass('now').siblings().removeClass("now");
	})
	$("#w_picList").on("swiperight","li",function(){
		index-=1;
		if(index<0){
			index=0
		}
		picList.css({
			'marginLeft':index*-w+"px",
			'-webkit-transition':'all .5s ease'
		},500)
		titFont=$(this).prev().attr("data-type");
		$("#titFont").html(titFont);
		dotBtn.eq(index).addClass('now').siblings().removeClass("now");
	})
	$("#w_mask").on("click",function(){
		location.href="FinancingProductinfo.html"
	})
}
checkBig()