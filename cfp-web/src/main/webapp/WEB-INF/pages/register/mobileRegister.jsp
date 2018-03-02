<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%@include file="../common/taglibs.jsp"%>
<%
  String ctx = request.getContextPath();
  pageContext.setAttribute("ctx", ctx);
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
  pageContext.setAttribute("basePath", basePath);
  pageContext.setAttribute("picPath", Constants.picPath);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" ></meta>
  <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0" ></meta>
  <!-- <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, minimal-ui" /> -->
  <meta name="keywords" content="" ></meta>
  <meta name="description" content="" ></meta>
  <title>手机用户注册 - 财富派</title>
  <link href="${ctx}/mobile/m_css/index.css" rel="stylesheet" type="text/css" ></link>
  <script type="text/javascript" src="${ctx}/mobile/m_js/jquery-1.8.2.min.js"></script>
  <script type="text/javascript" src="${ctx}/js/register.js"></script>
  <script type="text/javascript">
    var rootPath = '<%=ctx%>';
  </script>
</head>
<body>
<!-- nav start -->
<div class="nav">
  <img src="${ctx}/mobile/m_image/web1_01.jpg" />
</div>
<!-- nav start -->

<!-- index start -->
<div class="index">
  <h2 class="clearFloat"><span class="floatLeft">注册</span><a style="color:#475174;" class="floatRight" href="${ctx }/user/to_login">登录</a></h2>
  <form id="registForm" action="${ctx}/user/regist/register" class="form mt-40" method="post">
    <input type="hidden" name="token" value="${token}" />
    <input type="hidden" name="inviteUserId" value="${inviteUserId}" />
    <div class="ipt-group">
      <label><input type="text" id="username" name="loginName" class="ipt" maxlength="20" placeholder="用户名" /></label>
      <em class="hui">请输入4~20位字符，支持汉字、字母、数字及"-"、"_组合"</em>
    </div>
    <div class="ipt-group">
      <label><input type="text" id="phone" name="mobileNo" class="ipt" maxlength="11" placeholder="手机号" /></label>
      <em class="hui">该手机号将用于手机号登录和找回密码</em>
    </div>
    <div class="ipt-group short-info clearFloat">
      <label><input type="text" id="valid" class="ipt" maxlength="6" name="validCode" placeholder="输入短信验证码"/></label>
      <label class="floatRight"><button type="button" id="getvalid" class="btn btn-blue">获取验证码</button></label>
      <em></em>
    </div>
    <div class="ipt-group login-pwd">
      <label><input type="password" class="ipt" id="password" name="loginPass" autocomplete="off" maxlength="16" placeholder="设置登录密码" onKeyUp="CheckIntensity(this.value)" /></label>
      <em class="hui">请输入6~16位字符，支持字母及数字,字母区分大小写</em><span id="rejc">无</span>
    </div>
    <div class="ipt-group visite">
      <label><span>邀请码</span><input type="text"  readonly="readonly" id="visate" name="inviteCode"  maxlength="6" class="ipt" value="${invite_code}" /></label>
    </div>
    <div class="btn-group">
      <input type="button" id="submit-register" class="btn btn-error" value="立即注册" />
    </div>
  </form>
</div>
<!-- index end -->
<div class="bottom">
  <p class="text-p">北京汇聚融达网络科技有限公司</p>

  <p class="text-p">财富派京ICP备14051030号</p>

  <p class="text-p">客服电话<font color="#f1f1f1">400-061-8080</font></p>
</div>
</body>
</html>