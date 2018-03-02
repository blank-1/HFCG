<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />    
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<title>输入手机号</title>
<link rel="stylesheet" href="${ctx }/gamecss/reset.css">
<link href="${ctx }/gamecss/PublicStyle.css" rel="stylesheet" type="text/css">
</head>
<body>
<!-- 代码 开始 -->
	<div class="w_phoneBg">
		<div class="w_btnBox">
			<div class="w_phoneTitleBox">
				<p class="w_inputBxo">
					<input type="tel" class="w_tel" id="w_tel" name="w_tel" placeholder="请输入手机号" maxlength="11">
				</p>
				<span id="w_submit" class="w_submit">
					确定
				</span>
				<div class="w_pattern" id="w_pattern">
					手机格式错误
				</div>
			</div>
		</div>
	</div>
<!-- 代码 结束 -->
<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
<script src="${ctx }/gamejs/wechat_cashCoupon.js" type="text/javascript"></script>
<script>
	var rootPath = '<%=ctx%>';	
	//判断手机号是否正确
	$("#w_submit").on("click",function(){
	  var phoneVal=$("#w_tel").val();
	      if(phoneVal==""){
	        $("#w_pattern").fadeIn(500,function(){
	        	$(this).fadeOut(1800)
	        }).html("输入内容不能为空"); 
	      }else if(!(/^1[3|4|5|8|7][0-9]\d{4,8}$/.test(phoneVal))){
	          $("#w_pattern").fadeIn(500,function(){
	          	 $("#w_pattern").fadeOut(1800)
	          }).html("请输入正确的手机号")
	      }else if(phoneVal.length<11){
	        $("#w_pattern").fadeIn(500,function(){
	        	 $("#w_pattern").fadeOut(1800)
	        }).html("不是完整的11位手机号")
	      }else{
	           $.ajax({
					url:rootPath+"/game/entryCellPhoneNumber",
					type:"post",
					data:{"phone":$("#w_tel").val()},
					error : function(data) {
						
					},
					success:function(data){
						var _data =  eval("("+data+")");
						if(_data.isSuccess){
							location.href=rootPath+"/game/toReceiveSuccess";
						}else{
							if(_data.id == "noRegister"){
								location.href=rootPath+"/game/toNotRegistered";
							}else if(_data.id == "repeat"){
								location.href=rootPath+"/game/toReceiveRepeat";
							}else{
								$("#w_pattern").fadeIn(500,function(){
	        	 			$("#w_pattern").fadeOut(1800)
	        				}).html(_data.info)
							}
						}
	     	 		}
	     	 })
	     }
	})
</script>
</body>
</html>