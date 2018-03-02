<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="navindex">
  <div class="wrapper">
    <div class="navleft">
      <a href="https://www.caifupad.com"><img src="${ctx}/images/finance_03.jpg" class="borderright" /></a>
      <img src="${ctx}/images/finance_06.jpg" />
    </div>
    <ul class="navright">
      <li><a href="${ctx}/">首页</a></li>
      <li><a href="${ctx}/finance/list">我要理财</a></li>
      <%--<li><a href="#">我要借款</a></li>--%>
      	<c:if test="${not empty sessionScope.currentUser }">
            <%--账户中心链接--%>
           <li> <a href="${ctx}/person/account/overview">账户中心</a></li>
        </c:if>
        <c:if test="${empty sessionScope.currentUser }">
          <li>  <a href="javascript:" data-mask='mask' data-id="login">账户中心</a></li>
        </c:if>
      <li><a href="${ctx }/about">关于我们</a></li>
      <!-- <li><a href="${ctx }/storesJoin">门店加盟</a></li> -->
    </ul>
  </div>
</div>