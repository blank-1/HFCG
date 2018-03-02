<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.LoanProduct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal">
    <div class="control-group">
        <label class="control-label">产品名称:</label>

        <div class="controls-text" style="margin-left: 164px">
            ${loanProduct.productName}&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">年利率:</label>

        <div class="controls-text" style="margin-left: 164px">
            <span id="annualRateId">0.000</span>%&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">还款方法:</label>

        <div class="controls-text" style="margin-left: 164px">
            <span id="repaymentMethod"></span>&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">还款周期:</label>

        <div class="controls-text" style="margin-left: 164px">
            <span id="repaymentCycle"></span>&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">还款方式:</label>

        <div class="controls-text" style="margin-left: 164px">
            <span id="repaymentType"></span>&nbsp;
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">期限时长:</label>

        <div class="controls-text" style="margin-left: 164px">
            ${loanProduct.dueTime}<span id="dueTimeType"></span> &nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">创建时间:</label>

        <div class="controls-text" style="margin-left: 164px">
            <span id="createTime"></span>&nbsp;
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">有效日期:</label>

        <div class="controls-text" style="margin-left: 164px">
            <span id="startDate"></span>&nbsp;至&nbsp;<span id="endDate"></span>&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">版本号:</label>

        <div class="controls-text" style="margin-left: 164px">
            ${loanProduct.versionCode}&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">产品状态:</label>

        <div class="controls-text" style="margin-left: 164px">
            <span id="productState"></span>&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">产品描述:</label>

        <div class="controls-text" style="margin-left: 164px">
            ${loanProduct.productDesc}&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">最后修改时间:</label>

        <div class="controls-text" style="margin-left: 164px">
            <span id="lastMdfTime"></span>&nbsp;
        </div>
    </div>
    <c:forEach items="${itemTypeList}" var="itemType">
        <div class="control-group">
            <label class="control-label">${itemType.constantName}:</label><br>
            <c:forEach items="${LoanProductFeesList}" var="loanProductFees">
                <c:if test="${loanProductFees.parentConstant == itemType.constantDefineId}">
                    <div class="controls-text" style="margin-left: 164px">
                            ${loanProductFees.constantName};平台收费比例:${loanProductFees.workflowRatio*100}%
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </c:forEach>
</form>
<script language="JAVASCRIPT">
    //处理时间显示
    $("#startDate").text(getDateStr(new Date('${loanProduct.startDate}')));
    $("#endDate").text(getDateStr(new Date('${loanProduct.endDate}')));
    $("#createTime").text(getDateStr(new Date('${loanProduct.createTime}')));
    $("#lastMdfTime").text(getDateTimeStr(new Date('${loanProduct.lastMdfTime}')));
    $("#productState").text(function () {
        return '${loanProduct.productState}' == <%=LoanProduct.PUBLISHSTATE_INVALID%> ? '无效' : '有效';
    });
    $("#dueTimeType").text(function () {
        return '${loanProduct.dueTimeType}' == <%=LoanProduct.DUETIMETYPE_MONTH%> ? '个月' : '天';
    });

    $("#annualRateId").text(function(){
        var num = new Number('${loanProduct.annualRate}');
        return num.toFixed(3);
    });
    var constantDefineJson = "";
    //初始化常量数据
    $.getJSON("${ctx}/jsp/product/feesitem/initConstantDefine", function (json) {
        constantDefineJson = json;
        for (var i = 0; i < constantDefineJson.length; i++) {
            //处理还款方法
            if (constantDefineJson[i].constantValue == '${loanProduct.repaymentMethod}' && "repaymentMethod" == constantDefineJson[i].constantTypeCode) {
                $("#repaymentMethod").text(constantDefineJson[i].constantName);
            }
            //处理还款周期
            if (constantDefineJson[i].constantValue == '${loanProduct.repaymentCycle}' && "repaymentCycle" == constantDefineJson[i].constantTypeCode) {
               var val = ${loanProduct.cycleValue} == '0'?"":${loanProduct.cycleValue}+"天";
                $("#repaymentCycle").text(constantDefineJson[i].constantName+"        "+val);
            }
            //处理还款方式
            if (constantDefineJson[i].constantValue == '${loanProduct.repaymentType}' && "repaymentMode" == constantDefineJson[i].constantTypeCode) {
                $("#repaymentType").text(constantDefineJson[i].constantName);
            }
        }
    });
</script>
</body>
</html>