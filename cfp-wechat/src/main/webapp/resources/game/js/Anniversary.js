/*滚动事件*/
var myScroll;//iscroll滚动
myScroll=new iScroll('wrapper',{
	  onBeforeScrollStart:function(e){
	  	var target=e.target;
	  	while(target.nodeType!=1){
	  		target=target.parentNode;
	  	}
	  	 var ele=e.target.tagName.toLowerCase();
	  	 if(ele!='input' && ele!='textarea' && ele!='select'){
            e.preventDefault();
	  	 }
    },
    checkDOMChanges:true,
    fadeScrollbar:true
});

//页面全部加载完成消失
$(function(){
	setTimeout(function(){
		$("#loading").hide();
	},1500)
})

//字体加载
 var deNum=0;
 function autoTsq(){
	$(".mvSq").css("color","#F5FAFD");
	setTimeout(function(){$(".mvSq").eq(0).css("color","#29B6FF")},0);
	setTimeout(function(){$(".mvSq").eq(1).css("color","#29B6FF")},500);
	setTimeout(function(){$(".mvSq").eq(2).css("color","#29B6FF")},1000);
 }
 setInterval(autoTsq,1500);
	//判断手机号是否正确
$("#ReceiveBtn").on("click",function(){
	if($("#Receive").is(":visible")){
		var phoneVal=$("#Receive").val();
	      if(phoneVal==""){
	        $("#w_pattern").fadeIn(500,function(){
	        	$(this).fadeOut(1200)
	        }).html("输入内容不能为空"); 
	      }else if(!(/^1[3|4|5|8|7][0-9]\d{4,8}$/.test(phoneVal))){
	          $("#w_pattern").fadeIn(500,function(){
	          	$(this).fadeOut(1200)
	          }).html("请输入正确的手机号")
	      }else if(phoneVal.length<11){
	        $("#w_pattern").fadeIn(500,function(){
	        	$(this).fadeOut(1200)
	        }).html("不是完整的11位手机号")
	      }else{
	    	  $("#phone").val(phoneVal);
	    	  $("#frm").submit();
	      }
	}else{
		$("#frm").submit();
	}
})
//显示与隐藏蒙层
$("#w_share").on("click",function(){
  $("#markBox").show().on("click",function(){
    $(this).hide()
  })
})
