<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="linktop">
  <div class="container container2 clearFloat">
    <ul class="uWelcome clearFloat">
      <li class="lFirst linknone">欢迎来到财富派</li>
      <c:if test="${not empty sessionScope.currentUser }">
        <%--账户中心链接--%>
        <li><a href="${ctx}/person/account/overview">${sessionScope.currentUser.loginName}</a></li>
        <li><a href="${ctx}/user/logout">退出</a></li>
      </c:if>
      <c:if test="${empty sessionScope.currentUser }">
        <li><a href="javascript:" data-mask='mask' data-id="login">登录</a></li>
        <li><a href="${ctx}/user/regist/home">注册</a></li>
      </c:if>
      <li class="linknone"><a href="http://help.caifupad.com/" class="a_333">帮助中心</a></li>
      <li class="linknone"><a href="https://www.caifupad.com/zt/yindao/yindao.html" class="a_333">新手引导</a></li>
    </ul>
    <ul class="uSevers">
      <li>客服热线：400-061-8080</li>
      <li class="linknone">工作日：09:00-18:00 &nbsp;&nbsp;&nbsp; 周 六：09:00-17:00</li>
    </ul>
  </div>
</div>


<%@include file="sidebar.jsp"%>

