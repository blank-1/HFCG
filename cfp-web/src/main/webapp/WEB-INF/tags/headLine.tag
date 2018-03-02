<%@tag pageEncoding="utf-8"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="action"  required="false" %><%
    String ctx = request.getContextPath();
    request.setAttribute("ctx", ctx);
%>
<div class="header">
    <div class="container <c:if test="${action ne '1'}">container2</c:if> clearFloat">
        <div class="dlogo">
            <a href="${ctx}/"><img src="${ctx}/images/index_new/logo_03.png" /></a>
            <i>导航</i>
        </div>
        <div class="dNav clearFloat">
		<c:if test="${action ne '999'}">
            <ul class="uNav">
                <li><a <c:if test="${action eq '1'}">class="aaction"</c:if> href="${ctx}/">首页</a></li>
                <li><a <c:if test="${action eq '2'}">class="aaction"</c:if> href="${ctx}/finance/list">我要理财</a></li>
                <%-- <li><a <c:if test="${action eq '5'}">class="aaction"</c:if> href="${ctx }/user/fastLoan">我要借款</a></li> --%>
                <c:if test="${not empty sessionScope.currentUser }">
                    <%--账户中心链接--%>
                    <li> <a  <c:if test="${action eq '3'}">class="aaction"</c:if> href="${ctx}/person/account/overview">账户中心</a></li>
                </c:if>
                <c:if test="${empty sessionScope.currentUser }">
                    <li>  <a href="javascript:" data-mask='mask' data-id="login">账户中心</a></li>
                </c:if>
                <li><a  <c:if test="${action eq '4'}">class="aaction"</c:if> href="${ctx }/about">关于我们</a></li>
            </ul>
           </c:if>
        </div>
        <div class="clear_0"></div>
    </div>
</div>
<div class="clear_0"></div>