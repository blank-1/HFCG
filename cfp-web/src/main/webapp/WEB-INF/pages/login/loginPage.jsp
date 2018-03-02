<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
  <title>财富派-登录</title>
  <%@include file="../common/common_js.jsp"%>
  <script type="text/javascript" src="${ctx}/js/login.js"></script><!-- public javascript -->
</head>

<body>
<header>

<!-- navindex start -->
<customUI:headLine action="999" />
<!-- navindex end -->

</header>
<div class="clear"></div>


<!-- article start -->
<div class="plogind clearFloat">
	<!-- wrapper start -->
    <div class="container plogin" >
    	<!-- logind start  -->
        <div class="logind">
            <h2><span>登录财富派</span></h2>
                <!-- equity start -->
                <div class="login">
                   <form action="" method="get" name="form" class="">
         				 <input id="pastUrl" name="pastUrl" type="hidden"  value="${pastUrl}"/>
                        <div class="input-group">
                            <label for="username">
                               <input type="text" id="unlogin1" maxlength="20" name="username1" value="用户名/手机号" placeholder="用户名/手机号"  class="ipt-input unlogin" style="line-height:42px;"/>
           				 	   <input type="text" id="unlogin" maxlength="20" name="loginName" value=""  class="ipt-input unlogin" placeholder="用户名/手机号"   style="display:none;line-height:42px"  />
                            </label>
                            <em class="hui"></em>
                        </div>
                        <div class="input-group clearFloat">
                            <label for="password">
                                <input type="text" id="pwlogin_txt" maxlength="16" name="loginPass1" placeholder="请您输入密码" value="请您输入密码" autocomplete="off" class="ipt-input pwlogin_txt"	style="line-height:42px;"/>
                                <input type="password" id="pwlogin" maxlength="16" name="loginPass" value=""   placeholder="请您输入密码"  autocomplete="off" class="ipt-input pwlogin"	 style="display:none;line-height:42px;"   />
                            </label>
                            <em class="passwordem floatLeft"></em>
						<a class="write floatRight passworda mr-10" href="${ctx}/user/rePasswordOne">忘记密码？</a>
                        </div>
                        <div class="btn-group">
                            <button type="button" id="submit-login" class="btn btn-error mt-0">登录</button>
                            <a class="floatLeft passworda new-login mr-10" href="${ctx}/user/regist/home">注册账号</a>
                        </div>
                    </form>
                    
                </div><!-- equity start -->
        </div><!-- logind end  -->
    </div><!-- wrapper end-->

</div><!-- article end -->




<!-- footer start -->
<%@include file="../common/footLine3.jsp"%>
<!-- footer end -->
  <script type='text/javascript'>//(function(){document.getElementById('___szfw_logo___').oncontextmenu = function(){return false;}})();
  </script>
<script>
$(function(){
	//用户名
var unlogin1 = $("#unlogin1"), unlogin = $("#unlogin");

	unlogin1.bind("focus",function(){

		if($(this).val() != "用户名/手机号") return;

		$(this).css("display","none");
		unlogin.css("display","block");
		unlogin.val("");
		unlogin.focus();
	});

	unlogin.bind("blur",function(){

		if($(this).val() != "") return;
		$(this).css("display","none");
		unlogin1.css("display","block");
		unlogin1.val("用户名/手机号");
	});
	//密码
var pwlogin_txt = $(".pwlogin_txt"), pwlogin = $(".pwlogin");

	pwlogin_txt.bind("focus",function(){
		
		if($(this).val() != "请您输入密码") return;

		$(this).css("display","none");
		pwlogin.css("display","block");
		pwlogin.val("");
		pwlogin.focus();
	});
	
	pwlogin.bind("blur",function(){
		
		if($(this).val() != "") return;
		$(this).css("display","none");
		pwlogin_txt.css("display","block");
		pwlogin_txt.val("请您输入密码");
	});
})//function end
</script>
<style>.footer_small{margin-top:0px;}</style>
</body>
</html>
