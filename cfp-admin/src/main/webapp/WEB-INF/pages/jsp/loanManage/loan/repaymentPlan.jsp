<%@ page import="com.xt.cfp.core.constants.LoanApplicationStateEnum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<body>
<div>初始借款费用及还款计划</div>
<hr/>
<div style="float:left;width:50%;">
    <fieldset style="height:90px;">
        <legend>费用信息</legend>
        <table style=" font-size: 12px;font-weight: normal;">

            <c:forEach items="${loanApplicationFeesItems}" var="item">
                <c:if test="${item.itemType eq '1'||item.itemType eq '2'||item.itemType eq '3'||item.itemType eq '7'}">
                  <tr>
                      <td><span style="margin-right: 5px;">${item.itemName}(<customUI:dictionaryTable constantTypeCode="radicesType" desc="true" key="${item.radicesType}"/>*${item.feesRate}%)</span></td>
                      <td><span style="margin-right: 5px;"><fmt:formatNumber value="${item.amount}" pattern="#,##0.##"/></span></td>
                      <td><span style="margin-right: 5px;">
                          <c:forEach items="${feePoint}" var="point">
                             <c:if test='${point.value eq item.chargeCycleStr}'>${point.desc}</c:if>
                          </c:forEach></span>
                      </td>
                  </tr>
                </c:if>
            </c:forEach>
        </table>
    </fieldset>
</div>
<div style="float:left;width:50%;">
    <fieldset  style="height:90px;">
        <legend>违约扣费</legend>
        <table style=" font-size: 12px;font-weight: normal;">

            <c:forEach items="${loanApplicationFeesItems}" var="item">
                <c:if test="${item.itemType eq '4'||item.itemType eq '5'||item.itemType eq '6'}">
                    <tr>
                        <td><span style="margin-right: 5px;">${item.itemName}(<customUI:dictionaryTable constantTypeCode="radicesType" desc="true" key="${item.radicesType}"/>*${item.feesRate}%)</span></td>
                        <%--<td><span style="margin-right: 5px;"><fmt:formatNumber value="${item.amount}" pattern="#,##0.##"/></span></td>--%>
                        <td><span style="margin-right: 5px;">
                          <c:forEach items="${feePoint}" var="point">
                              <c:if test='${point.value eq item.chargeCycleStr}'>${point.desc}</c:if>
                          </c:forEach></span>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </fieldset>
</div>
<div style="clear:both;"></div>
<c:if test="${loanApplication.applicationState eq '0'||loanApplication.applicationState eq '1'||loanApplication.applicationState eq '2'||loanApplication.applicationState eq '3'||loanApplication.applicationState eq '4'||loanApplication.applicationState eq '5'}">
    <div id="repaymentPlanList"></div>
    <script type="text/javascript">
        <c:if test="${not empty loanApplication.loanBalance}">
             $("#repaymentPlanList").load("${ctx}/jsp/loanManage/loan/toShowRepaymentPlan?loanProductId=${loanApplication.loanProductId}&balance=${loanApplication.loanBalance}");
        </c:if>
        <c:if test="${not empty loanApplication.confirmBalance}">
            $("#repaymentPlanList").load("${ctx}/jsp/loanManage/loan/toShowRepaymentPlan?loanProductId=${loanApplication.loanProductId}&balance=${loanApplication.confirmBalance}");
        </c:if>
    </script>
</c:if>
<c:if test="${loanApplication.applicationState eq '6'||loanApplication.applicationState eq '7'||loanApplication.applicationState eq '8'||loanApplication.applicationState eq '9'||loanApplication.applicationState eq 'A'}">
    <div>
        <table id="repaymentPlan_list" border="1" style=" font-size: 12px;width: 100%; font-weight: normal;">
            <thead>
            <th>期号</th>
            <th>还款日</th>
            <th>本金</th>
            <th>利息</th>
            <th>费用</th>
            <th>罚息</th>
            <th>总计</th>
            <th>当期欠款</th>
            <th>当期状态</th>
            </thead>
            <tbody>
            <c:forEach items="${repaymentPlanList}" var="plan">
                <tr>
                    <td>第${plan.sectionCode}期</td>
                    <td><fmt:formatDate value="${plan.repaymentDay}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                    <td><span name="_shouldCapital">${plan.shouldCapital2}</span></td>
                    <td><span name="_shouldInterest">${plan.shouldInterest2}</span></td>
                    <td>
                        <c:forEach items="${repaymentVos}" var="rvo">
                            <c:if test="${rvo.repaymentPlanId eq plan.repaymentPlanId}"><span name="_shouldFee">${rvo.shouldFee}</span></c:if>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach items="${repaymentVos}" var="rvo">
                            <c:if test="${rvo.repaymentPlanId eq plan.repaymentPlanId}"><font color="green" name="factfaxi">${rvo.factfaxi}</font>/<span name="shouldFaxi">${rvo.shouldFaxi}</span></c:if>
                        </c:forEach>
                    </td>
                    <td><span name="_shouldBalance2">${plan.shouldBalance2}</span></td>
                    <td><span name="_shouldBalance">${plan.shouldBalance2-plan.factBalance}</span></td>
                    <td>
                        <c:forEach items="${repaymentPlanState}" var="status">
                            <c:if test="${plan.planState eq status.valueChar}">${status.desc}</c:if>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td>合计</td>
                <td></td>
                <td><span id="_shouldCapital"></span></td>
                <td><span id="_shouldInterest"></span></td>
                <td>
                    <span id="_shouldFee"></span>
                </td>
                <td>
                    <span id="_shouldFaxi"></span>
                </td>
                <td><span id="_shouldBalance2"></span></td>
                <td><span id="_shouldBalance"></span></td>
                <td></td>
            </tr>
            </tbody>
        </table>

    </div>
</c:if>

<script type="text/javascript">

    //格式化金额
    function fmoney(s, n) {
        n = n > 0 && n <= 20 ? n : 2;
        s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
        var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
        t = "";
        for (i = 0; i < l.length; i++) {
            t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
        }
        return t.split("").reverse().join("") + "." + r;
    }

    //还原小数
    function rmoney(s) {
        return parseFloat(s.replace(/[^\d\.-]/g, ""));
    }


    //应还
    function shouldPay(id){
        var sum = 0;
        var shouldPay = $("span[name="+id+"]");
        for(var i=0;i<shouldPay.length;i++){
            sum += parseFloat(shouldPay.eq(i).html());
        }
        $("#"+id+"").html(fmoney(sum,2));
    }

    shouldPay('_shouldCapital');

    shouldPay('_shouldInterest');

    shouldPay('_shouldFee');

    shouldPay('_shouldFaxi');

    shouldPay('_shouldBalance');

    shouldPay('_shouldBalance2');
</script>

</body>