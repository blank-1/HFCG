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
%>
<body>
<div id="addRateProduct" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <form class="form-horizontal" id="addRateProduct_form" method="post" action="">

        <div class="control-group">
            <label class="control-label">加息券名称：</label>

           	<div class="controls">
            	${rateProduct.rateProductName }
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">加息利率：</label>
            
            <div class="controls">
            	${rateProduct.rateValue }%
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
	            <c:if test="${rateProductConditionVO.condition0_nothing eq false}">无条件</c:if>
	            <c:if test="${rateProductConditionVO.condition0_nothing eq true}">有条件</c:if>
            </div>
            
        </div>
        
        <c:if test="${rateProductConditionVO.condition0_nothing eq true}">
			<div class="control-group">
	            <label class="control-label"></label>
	
	            <div class="controls">
	            	<c:if test="${rateProductConditionVO.condition1_term eq true}">
	            		标的期限:&nbsp; 限投 ${rateProductConditionVO.con1_min }-${rateProductConditionVO.con1_max } 月标；
	            	</c:if>
	            </div>
	        </div>
	        <div class="control-group">
	            <label class="control-label"></label>
	
	            <div class="controls">
	                <c:if test="${rateProductConditionVO.condition2_type eq true}">
	            		标的类型:&nbsp;
	            		<c:if test="${rateProductConditionVO.con2_0 eq true}">信贷；&nbsp;</c:if>
	            		<c:if test="${rateProductConditionVO.con2_1 eq true}">房贷；&nbsp;</c:if>
	            		<c:if test="${rateProductConditionVO.con2_2 eq true}">企业车贷；&nbsp;</c:if>
	            		<c:if test="${rateProductConditionVO.con2_3 eq true}">企业信贷；&nbsp;</c:if>
	            		<c:if test="${rateProductConditionVO.con2_4 eq true}">企业保理；&nbsp;</c:if>
	            		<c:if test="${rateProductConditionVO.con2_5 eq true}">基金；&nbsp;</c:if>
	            		<c:if test="${rateProductConditionVO.con2_6 eq true}">企业标；&nbsp;</c:if>
	            	</c:if>
	            </div>
	        </div>
	        <div class="control-group">
	            <label class="control-label"></label>
	
	            <div class="controls">
	                <c:if test="${rateProductConditionVO.condition3_amount eq true}">
	            		起投金额:&nbsp; 单笔投资达 ${rateProductConditionVO.con3_start_amount } 元时可用；
	            	</c:if>
	            </div>
	        </div>
		</c:if>
        
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
            <label class="control-label">生效时间：</label>

            <div class="controls">
                <fmt:formatDate value="${rateProduct.startDate }" pattern="yyyy-MM-dd"/> 至 
                <fmt:formatDate value="${rateProduct.endDate }" pattern="yyyy-MM-dd"/>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">叠加使用：</label>

            <div class="controls">
                <c:forEach items="${rateProductIsOverlay}" var="v">
                	<c:if test="${rateProduct.isOverlay eq v.value}">${v.desc}</c:if>
                </c:forEach>
            </div>
        </div>
        
        <div class="control-group" id="voucherRemark">
            <label class="control-label">使用说明：</label>

            <div class="controls">
                ${rateProduct.usageRemark }
            </div>
        </div>
        
    </form>
</div>
</body>
</html>