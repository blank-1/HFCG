<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>用户注册 - 财富派</title>
	<%@include file="../common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx}/js/register.js"></script><!-- action javascript -->
</head>
<body>
<header>
  <!-- navtopbg start-->
  <%@include file="../common/headLine1.jsp"%>
</header>

<!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->


<div class="clear"></div>

<%@include file="../login/login.jsp"%>
<!-- article start -->
<div class="wrapper clearFloat">

  <!-- register-left start -->
  <div class="register-left">
    <h2>财富派账号<small class="floatRight">已有账号
      <%--<a href="login.html">登录财富派</a>--%>
      <a href="javascript:" data-mask='mask' data-id="login">登录财富派</a>
    </small></h2>

    <form id="registForm" action="${ctx}/user/regist/register" class="form mt-40" method="post">
      <input type="hidden" name="token" value="${token}" />
      <input type="hidden" name="inviteUserId" value="${inviteUserId}" />
      <div class="input-group">
        <label for="username">
          <span>用户名：</span>
          <input type="text" value="" id="username" maxlength="20" name="loginName" placeholder="请输入用户名" class="ipt-input" />
        </label>
        <em class="hui">请输入4~20位字符，支持汉字、字母、数字及"-"、"_组合"</em>
      </div>

      <div class="input-group">
        <label for="phone">
          <span>手机号：</span>
          <input type="text" value="" id="phone" maxlength="11" name="mobileNo" placeholder="请输入手机号" class="ipt-input" />
        </label>
        <em class="hui">该手机号将用于手机号登录和找回密码</em>
      </div>

      <div class="input-group clearFloat">
        <label for="valid" class="floatLeft">
          <span class="floatLeft linheight">验证码：</span>
          <input type="text" value="" autocomplete="off"  id="valid" name="validCode" maxlength="6" autocomplete="off"  placeholder="请输入验证码" class="ipt-input widthvalid" />
        </label>
        <button type="button" id="getvalid" class="btn btn-blue mt-0 floatLeft">获取验证码</button>
        <div class="clear"></div>
        <em></em>
      </div>

      <div class="input-group clearFloat">
        <label for="password" class="floatLeft">
          <span class="floatLeft linheight">密&nbsp;&nbsp; &nbsp;码：</span>

          <input type="text" id="pwlogin_txt1" maxlength="16" name="loginPass1" style="color:#999;" value="密码" autocomplete="off" class="ipt-input widpass pwlogin_txt2" />
         <input  maxlength="16" type="password" autocomplete="off" name="loginPass" id="password" value=""  class="ipt-input widpass pwlogin2"  style="display:none;" onKeyUp="CheckIntensity(this.value)" />
        </label>
        <div type="button" id="rejc" class="Tcolor floatLeft mt-0">无</div>
        <div class="clear"></div>
        <em class="hui">请输入6~16位字符，支持字母及数字,字母区分大小写</em>
      </div>

      <div class="input-group">
        <label for="visate">
          <span>邀请码：</span>
          <c:if test="${not empty invite_code}">
            <input type="text" value="${invite_code}" readonly="readonly" id="visate" maxlength="6" name="inviteCode" placeholder="邀请码" class="ipt-display-input ipt-disabled" />
            <i id="viste_i_zc" class="viste_zc_top"></i>
          </c:if>
          <c:if test="${ empty invite_code}">
            <input type="text" value="${invite_code}"  id="visate" maxlength="6" name="inviteCode" placeholder="邀请码" class="ipt-display-input display-none" />
            <i id="viste_i_zc" class="viste_zc_bottom"></i>
          </c:if>
<style>

  .ipt-display-input{  border: 1px solid #dddddd;
    color: #333333;
    height: 35px;
    line-height: 35px;
    padding-left: 9px;}

</style>

        </label>
        <em></em>
      </div>
<style>input.ipt-disabled{ background:#f1f1f1; cursor: no-drop;}

</style>
      <div class="btn-group">
        <label for="">
          <span class="shu"></span><a data-mask="mask" data-id="payshowstate" href="javascript:;">《财富派注册协议》</a>
        </label>
        <button type="button" id="submit-register" class="btn btn-error widbtn">确认协议并注册</button>
      </div>
    </form>
  </div>
  <input type="hidden" id="regitxt" data-val="2"/><!-- 2:不可禁用　1:禁用-->

  <script>
    $(function(){
      //密码
      var pwlogin_txt = $(".pwlogin_txt2"), pwlogin = $(".pwlogin2");

      pwlogin_txt.bind("focus",function(){

        if($(this).val() != "密码") return;

        $(this).css("display","none");
        pwlogin.css("display","inline-block");
        pwlogin.val("");
        pwlogin.focus();
      });

      pwlogin.bind("blur",function(){

        if($(this).val() != "") return;
        $(this).css("display","none");
        pwlogin_txt.css("display","inline-block");
        pwlogin_txt.val("密码");
      });
    })//function end
  </script>
  <!-- register-left end -->
<!--registerer protocol-->
  <%@include file="../common/registerProtocol.jsp"%>
  <div class="clear"></div>
</div><!-- article end -->

<!-- footer start -->
<%@include file="../common/footLine3.jsp"%>
<!-- footer end -->
</body>
</html>
