<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<head>
</head>
<body>

<div class="easyui-layout" style="width:100%;height:90%;">
	<div id="content" region="center" title="查询结果" >
		<div style="padding: 10px 20px 20px 20px;">
		    
		</div>
		<div id="loan" class="container-fluid" style="width: 1100px;">
		    <table id="customer_repayment_detail"></table>
		</div>
	</div>
</div>


<script language="javascript">
function loadRepaymentDetailList(){
    $("#customer_repayment_detail").datagrid({
        title: '详情',
        url: '${ctx}/jsp/custom/customer/getRepaymentDetailList',
        pagination: true,
        pageSize: 10,
        queryParams: {loanApplicationId:'${loanApplicationId }'},
        singleSelect: true,
        rownumbers: true,
        columns:[[
            {field:'loanApplicationId', width:150,title:'借款ID'},
            {field:'sectionCode', width:150,title:'期数'},
            {field:'factCalital', width:100,title:'本金'},
            {field:'factInterest', width:100,title:'利息'},
            {field:'repaymentFees', width:100,title:'费用' },
            {field:'depalFine', width:100,title:'罚息' },
            {field:'noRepaymentPercent', width:100,title:'待还款比例',formatter:noRepaymentPercentFormatter}
        ]]
    });
}
function noRepaymentPercentFormatter(val){
    if (val == undefined || val == ""){        	
        return "";
    }else{        	
        return val + '%';
    }
}
$(function(){
	loadRepaymentDetailList();
});
</script>

</body>