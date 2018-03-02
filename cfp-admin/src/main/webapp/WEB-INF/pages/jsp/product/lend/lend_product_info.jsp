<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.LendProduct" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" method="post">
    <div class="control-group">
        <label class="control-label">产品名称:</label>

        <div class="controls-text" style="margin-left: 195px">
            <span id="productType"></span>
            &nbsp;${lendProduct.productName}
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">收益利率:</label>

        <div class="controls-text" style="margin-left: 195px">
            <span style="width: 200px;display: inline-block">
                ${lendProduct.profitRate}%
                <c:if test="${lendProduct.productType ne '1'}">
	                ~${lendProduct.profitRateMax}%
            	</c:if>
            </span>
			
            <c:if test="${lendProduct.productType eq '1'}">
	            <span style="width: 60px;display: inline-block"> 封闭期:</span>
	            ${lendProduct.closingDate}(<span id="spanClosingType"/>)
			</c:if>

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">起售金额:</label>

        <div class="controls-text" style="margin-left: 195px">
            <span style="width: 200px;display: inline-block">
                ${lendProduct.startsAt}元
            </span>
            <span style="width: 60px;display: inline-block">递增金额:</span>
            ${lendProduct.upAt}元
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">期限:</label>

        <div class="controls-text" style="margin-left: 195px">
            <span style="width: 200px;display: inline-block">
            ${lendProduct.timeLimit}(<span id="spanTimeLimitType"/>)

            </span>
            <span style="width: 60px;display: inline-block"> 返息周期:</span>
            <span id="spanToInterestPoint"/>

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">续费:</label>

        <div class="controls-text" style="margin-left: 195px">&nbsp;
        <span style="width: 200px;display: inline-block">
            <span id="spanRenewalCycleType"/>

            <span id="spanRenewalType">
            <span style="display: inline-block">&nbsp;&nbsp;类型:<span id="renewalType"/></span>

            ${lendProduct.renewal}

        </div>
        &nbsp;
    </div>
    <div class="control-group">
        <label class="control-label">版本号:</label>

        <div class="controls-text" style="margin-left: 195px">&nbsp;
        <span style="width: 200px;display: inline-block">
            ${lendProduct.versionCode}
        </div>
        &nbsp;
    </div>

    <div class="control-group">
        <label class="control-label">产品描述:</label>

        <div class="controls-text" style="margin-left: 195px">
            ${lendProduct.productDesc}&nbsp;

        </div>
    </div>

    <fieldset>
        <legend>费用</legend>
        <c:forEach items="${itemTypeList}" var="itemType">
            <div class="control-group">
                <label class="control-label">${itemType.constantName}:</label><br>
                <c:forEach items="${lendProductAndFeesList}" var="lendProductAndFees">
                    <c:if test="${lendProductAndFees.parentConstant == itemType.constantDefineId}">
                        <div class="controls-text" style="margin-left: 195px">
                                ${lendProductAndFees.constantName};平台收费比例:${lendProductAndFees.workflowRatio*100}%
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </c:forEach>

    </fieldset>
    <%-- 取消阶梯优惠
    <fieldset>
        <legend>阶梯优惠</legend>
        <div class="control-group">
            <label class="control-label">优惠:</label><br>
            <c:forEach items="${lendProductLadderDiscountFees}" var="LadderDiscountFees">
                <div class="controls-text" style="margin-left: 195px">
                    <span id="into"
                          minAmount="${LadderDiscountFees.lendProductLadderDiscount.minAmount}"
                          lpldfId="LadderDiscountFees_${LadderDiscountFees.lpldfId}"
                          maxAmount="${LadderDiscountFees.lendProductLadderDiscount.maxAmount}"/>
                    金额范围:<span
                        id="LadderDiscountFees_${LadderDiscountFees.lpldfId}"></span>;&nbsp;管理费:${LadderDiscountFees.itemName};&nbsp;优惠:${LadderDiscountFees.discountRate == '0'?'100':100-LadderDiscountFees.discountRate*100}%
                </div>
            </c:forEach>
        </div>
    </fieldset> --%>

    <fieldset>
        <legend>适用债权</legend>
        <div class="control-group">
            <label class="control-label">债权列表:</label><br>
            <c:forEach items="${lendLoanBindings}" var="lendLoanBinding">
                <div class="controls-text" style="margin-left: 195px">

                        ${lendLoanBinding.loanProduct.productName};&nbsp;占比:${lendLoanBinding.loanRatio*100}%;&nbsp;${lendLoanBinding.feesItem.itemName}
                </div>
            </c:forEach>
        </div>
    </fieldset>

</form>

<script language="JavaScript">
    var checkedVal = $("span#into");
    for (var j = 0; j < checkedVal.length; j++) {
        var minAmount = "";
        var lpldfId = "";
        var maxAmount = "";
        for (var i = 0; i < checkedVal[j].attributes.length; i++) {
            if (checkedVal[j].attributes[i].name == "lpldfid") {
                lpldfId = checkedVal[j].attributes[i].nodeValue;
                continue;
            }
            if (checkedVal[j].attributes[i].name == "minamount") {
                minAmount = checkedVal[j].attributes[i].nodeValue;
                continue;
            }
            if (checkedVal[j].attributes[i].name == "maxamount") {
                maxAmount = checkedVal[j].attributes[i].nodeValue;
                continue;
            }
            $("#" + lpldfId).text(formatNum(minAmount, 2) + "~" + formatNum(maxAmount, 2));
        }
    }

    $("#productType").text(function () {
        return '${lendProduct.productType}' == <%=LendProduct.PRODUCTTYPE_FINANCING%> ? '省心计划产品' : '债权类产品';
    });

    var url = "${ctx}/jsp/product/loan/getConstantDefines";
    var cDefines = {};
    $.ajax({
        async: false,
        url: url,
        success: function (data) {
            cDefines = data;
        }
    })
    $.each(cDefines, function (key, theValue) {
        if (theValue.constantTypeCode == 'closingType') {

            if ('${lendProduct.closingType}' == theValue.constantValue) {
                $("#spanClosingType").html(theValue.constantName);
            }
        }
        if (theValue.constantTypeCode == 'lendProductTimeLimitType') {
            if ('${lendProduct.timeLimitType}' == theValue.constantValue) {
                $("#spanTimeLimitType").html(theValue.constantName);
            }
        }
        if (theValue.constantTypeCode == 'toInterestPoint') {
            if ('${lendProduct.toInterestPoint}' == theValue.constantValue) {
                $("#spanToInterestPoint").html(theValue.constantName);
            }
        }
        if (theValue.constantTypeCode == 'renewalCycleType') {
            if ('${lendProduct.renewalCycleType}' == theValue.constantValue) {
                $("#spanRenewalCycleType").html(theValue.constantName);
            }
        }
        if (theValue.constantTypeCode == 'renewalBalanceType') {
            if ('${lendProduct.renewalType}' == theValue.constantValue) {
                $("#renewalType").html(theValue.constantName);
            }
        }

    })
    if ('${lendProduct.renewalCycleType}' == 1) {
        $("#spanRenewalType").hide();
    } else {
        $("#spanRenewalType").show();
    }
</script>
</body>
</html>
