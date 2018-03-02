<%@page import="com.xt.cfp.core.constants.RateEnum"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<head>
</head>
<%
    //使用场景
    RateEnum.RateProductScenarioEnum[] rateProductScenario = RateEnum.RateProductScenarioEnum.values();
    request.setAttribute("rateProductScenario", rateProductScenario);
    
    //是否可以叠加
    RateEnum.RateProductIsOverlayEnum[] rateProductIsOverlay = RateEnum.RateProductIsOverlayEnum.values();
    request.setAttribute("rateProductIsOverlay", rateProductIsOverlay);
    
    //加息券发放状态
    RateEnum.RateUserStatusEnum[] rateUserStatus = RateEnum.RateUserStatusEnum.values();
    request.setAttribute("rateUserStatus", rateUserStatus);
    
    //加息券来源
    RateEnum.RateUserSourceEnum[] rateUserSource = RateEnum.RateUserSourceEnum.values();
    request.setAttribute("rateUserSource", rateUserSource);
%>
<body>

<div id="addRateProduct" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <form class="form-horizontal" id="addRateProduct_form" method="post" action="">
    	
    	<div class="control-group">
            <label class="control-label">用户名：</label>
           	<div class="controls">
            	${userInfoVO.loginName}
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">姓名：</label>
           	<div class="controls">
            	${userInfoVO.realName}
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">手机号：</label>
           	<div class="controls">
            	${userInfoVO.mobileNo}
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">加息券名称：</label>
           	<div class="controls">
            	${rateProduct.rateProductName}
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">加息利率：</label>
           	<div class="controls">
            	${rateProduct.rateValue}%
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">当前状态：</label>
           	<div class="controls">
            	<c:forEach items="${rateUserStatus}" var="v">
                	<c:if test="${rateUser.status eq v.value}">${v.desc}</c:if>
                </c:forEach>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">来源：</label>
           	<div class="controls">
            	<c:forEach items="${rateUserSource}" var="v">
                	<c:if test="${rateUser.source eq v.value}">${v.desc}</c:if>
                </c:forEach>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">使用场景：</label>
           	<div class="controls">
            	<c:forEach items="${rateProductScenario}" var="v">
                	<c:if test="${rateProduct.usageScenario eq v.value}">${v.desc}</c:if>
                </c:forEach>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">使用条件：</label>
           	<div class="controls">
            	${conditionDesc }
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">使用次数：</label>
           	<div class="controls">
            	${rateProduct.usageTimes }次
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">有效时长：</label>
           	<div class="controls">
            	<c:if test="${rateProduct.usageDuration eq 0}">长期有效</c:if>
                <c:if test="${rateProduct.usageDuration ne 0}">${rateProduct.usageDuration }天</c:if>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">发放时间：</label>
           	<div class="controls">
            	<fmt:formatDate value="${rateUser.startDate }" pattern="yyyy-MM-dd"/>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">失效时间：</label>
           	<div class="controls">
            	<fmt:formatDate value="${rateUser.endDate }" pattern="yyyy-MM-dd"/>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">使用说明：</label>
           	<div class="controls">
            	${rateProduct.usageRemark }
            </div>
        </div>

		<!-- 历史列表（开始） -->
		<table width="1000px;" border="1" style="font-size: 12px;">
			<tbody>
				<tr style="background-color: #6699CC;text-align: center;">
					<td>标的名称</td>
					<td style="width: 60px;">期限(月)</td>
					<td style="width: 100px;">投资金额(元)</td>
					<td style="width: 140px;">使用时间</td>
				</tr>
				
				<c:if test="${not empty rateUsageHistoryVOList}">
					<c:forEach items="${rateUsageHistoryVOList}" var="v">
                		<tr style="text-align: center;">
							<td>${v.lendOrderName }</td>
							<td>${v.timeLimit }</td>
							<td><fmt:formatNumber value="${v.buyBalance }" pattern="#,###.00"/></td>
							<td><fmt:formatDate value="${v.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
                	</c:forEach>
				</c:if>
				
				<c:if test="${empty rateUsageHistoryVOList}">
					<tr style="text-align: center;">
						<td colspan="4">无使用记录</td>
					</tr>
				</c:if>
				
			</tbody>
		</table>
		<!-- 历史列表（结束） -->

	</form>
</div>
</body>
</html>