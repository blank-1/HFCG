<%--
  User: Ren yulin
  Date: 15-7-23 下午4:10
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
%>
<head>
    <title></title>
</head>
<body>

    <table id="plan_list"></table>
<script language="javascript">
    $("#plan_list").datagrid({
        title: '',
        url: '${ctx}/jsp/loanManage/loan/showRepaymentPlan?loanProductId=${loanProductId}&balance=${balance}',
        pagination: false,
        singleSelect: true,
        rownumbers: true,
        columns:[[
            {field:'secCode',align:'center', width:60,title:'期号' },
            {field:'calital',align:'center', width:120,title:'本金' ,formatter:decimalFormatter},
            {field:'interest',align:'center', width:100,title:'利息',formatter:decimalFormatter },
            {field:'fees',align:'center', width:100,title:'费用',formatter:decimalFormatter },
            {field:'balance',align:'center', width:120,title:'总额',formatter:decimalFormatter }
                ]]
    });
    function decimalFormatter(val){
        return formatNum(val,2);
    }

</script>
</body>
</html>
