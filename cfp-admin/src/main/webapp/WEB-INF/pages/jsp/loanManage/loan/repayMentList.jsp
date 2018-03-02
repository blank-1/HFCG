<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<head>
</head>
<body>

<div class="cf" style="width:900px;margin:0 10px 5px 10px;">
    未还总金额：${allShouldPaid}
    未还本金：${repaymentCapital}
    未还利息：${replaymentInterest}
    未还费用：${loanFeeNopaied}
    未还罚息：${interestPaid}
</div>

<div id="repayment" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <table id="repayment_list" border="1" style=" font-size: 12px;font-weight: normal;">
        <thead>
            <th>期号</th>
            <th>已还/应还本金</th>
            <th>已还/应还利息</th>
            <th>已还/应还费用</th>
            <th>已还/应还罚息</th>
            <th>已还/应还总额</th>
            <th>应还日期</th>
            <th>本期最后还款</th>
            <th>当期状态</th>
        </thead>
        <tbody>
            <c:forEach items="${repaymentPlanList}" var="plan">
                <tr>
                    <td>第${plan.sectionCode}期</td>
                    <td><font color="green" name="factCalital">${plan.factCalital}</font>/<span name="shouldCapital">${plan.shouldCapital2}</span></td>
                    <td><font color="green" name="factInterest">${plan.factInterest}</font>/<span name="shouldInterest">${plan.shouldInterest2}</span></td>
                    <td>
                        <c:forEach items="${repaymentVos}" var="rvo">
                            <c:if test="${rvo.repaymentPlanId eq plan.repaymentPlanId}"><font color="green" name="factFee">${rvo.factFee}</font>/<span name="shouldFee">${rvo.shouldFee}</span></c:if>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach items="${repaymentVos}" var="rvo">
                            <c:if test="${rvo.repaymentPlanId eq plan.repaymentPlanId}"><font color="green" name="factfaxi">${rvo.factfaxi}</font>/<span name="shouldFaxi">${rvo.shouldFaxi}</span></c:if>
                        </c:forEach>
                    </td>
                    <td><font color="green" name="factBalance">${plan.factBalance}</font>/<span name="shouldBalance2">${plan.shouldBalance2}</span></td>
                    <td><fmt:formatDate value="${plan.repaymentDay}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                    <td>
                        <c:forEach items="${repaymentVos}" var="rvo">
                            <c:if test="${rvo.repaymentPlanId eq plan.repaymentPlanId}">
                                <c:if test="${not empty rvo.recentRepaymetDate}">
                                    <fmt:formatDate value="${rvo.recentRepaymetDate}" pattern="yyyy-MM-dd hh:mm:ss"/>
                                </c:if>
                                <c:if test="${ empty rvo.recentRepaymetDate}">
                                    本期尚未还款
                                </c:if>
                            </c:if>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach items="${repaymentPlanState}" var="status">
                            <c:if test="${plan.planState eq status.valueChar}">${status.desc}</c:if>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td>合计</td>
                <td><font color="green" id="factCalital"></font>/<span id="shouldCapital"></span></td>
                <td><font color="green" id="factInterest"></font>/<span id="shouldInterest"></span></td>
                <td>
                    <font color="green" id="factFee"></font>/<span id="shouldFee"></span>
                </td>
                <td>
                    <font color="green" id="factfaxi"></font>/<span id="shouldFaxi"></span>
                </td>
                <td><font color="green" id="factBalance"></font>/<span id="shouldBalance2"></span></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </tbody>
    </table>
</div>
<div style="margin-bottom: 70px;"></div>
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
    function shouldPayAll(id){
        var sum = 0;
        var shouldPay = $("span[name="+id+"]");
        for(var i=0;i<shouldPay.length;i++){
           sum += parseFloat(shouldPay.eq(i).html());
        }
        $("#"+id+"").html(fmoney(sum,2));
    }
    //已还
    function factPayAll(id){
        var sum = 0;
        var factCalitals = $("font[name="+id+"]");
        for(var i=0;i<factCalitals.length;i++){
            sum += parseFloat(factCalitals.eq(i).html());
        }
        $("#"+id+"").html(fmoney(sum,2));
    }

    factPayAll('factCalital');
    shouldPayAll('shouldCapital');

    factPayAll('factInterest');
    shouldPayAll('shouldInterest');

    factPayAll('factFee');
    shouldPayAll('shouldFee');

    factPayAll('factfaxi');
    shouldPayAll('shouldFaxi');

    factPayAll('factBalance');
    shouldPayAll('shouldBalance2');
</script>

</body>