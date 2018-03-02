<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 15-7-16
  Time: 下午1:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp"%>
<script type="text/javascript" src="${ctx}/js/bank_card.js"></script>
<html>
<head>
    <title></title>
</head>
<body>
<a href="${ctx}/timer/crAutoMatch.do" target="_blank">债权自动匹配</a><br/>
<a href="${ctx}/timer/bidFail.do" target="_blank">流标</a><br/>
<%--<a href="${ctx}/timer/createAgreement.do" target="_blank">生成出借合同</a><br/>--%>
<%--<a href="${ctx}/timer/refreshOrderReceive.do" target="_blank">重新生成订单回款计划</a><br/>--%>
<input type="text" id="card" style="width: 200px"><input type="button" value="验证卡号" id="checkCard" onclick="checkCard()">


<script language="JavaScript">

    function checkCard(){
        var check = luhmCheck($("#card").val());
        if (check) {
            alert("卡号有效");
        } else {
            alert("卡号无效");
        }
    }
</script>
</body>
</html>
