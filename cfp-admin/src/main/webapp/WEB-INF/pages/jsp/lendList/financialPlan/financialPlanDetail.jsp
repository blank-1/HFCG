<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>

</head>
<body>
<div id="financialPlan" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div style="padding:3px;">
    		<span style="font-size: 14px;font-weight: bold;">${lendOrderExtProduct.publishName }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		<span>购买人姓名:${lendOrderExtProduct.realName }</span>
    </div>
    
    <fieldset style="height:65px;width: 1200px;">
    	
        <legend>省心计划信息</legend>
        <table align="center" border="0" width="100%" >

            <tr>
                <td align="left" style="font-size: 12px;">预期年利率：${lendOrderExtProduct.profitRate }-${lendOrderExtProduct.profitRateMax }%</td>
                <td align="left" style="font-size: 12px;" >时长(月)：${lendOrderExtProduct.timeLimit }</td>
                <td align="left" style="font-size: 12px;">封闭期(月)：${lendOrderExtProduct.closingDate }</td>
                <td align="left" style="font-size: 12px;">起投金额：<fmt:formatNumber value="${lendOrderExtProduct.startsAt }" pattern="#,#00.00"/></td></td>               
                <td align="left" style="font-size: 12px;" >本期发行：${lendOrderExtProduct.publishCode}</td>
                <td align="left" style="font-size: 12px;">付息方式：
                <c:choose><c:when test="${lendOrderExtProduct.toInterestPoint ==6}">周期付息到期还本</c:when><c:otherwise>到期还本付息</c:otherwise> </c:choose></td>
                <td align="left" style="font-size: 12px;">保障说明：
                	<c:if test="${lendOrderExtProduct.guaranteeType ==1 }">本息保障</c:if><c:if test="${lendOrderExtProduct.guaranteeType ==2 }">本金保障</c:if><c:if test="${lendOrderExtProduct.guaranteeType ==3 }">无</c:if>
                </td>
            </tr>
             <tr>
                <td align="left" style="font-size: 12px;">收款账户：<!-- 到期还款方式：0返还至账户，1打到银行卡 -->
                	<c:if test="${lendOrderExtProduct.theReturnMethod ==0}">平台账户</c:if>
                	<c:if test="${lendOrderExtProduct.theReturnMethod ==1}">线下划扣账户</c:if>
                </td>
                <td align="left" style="font-size: 12px;" >购买金额：<fmt:formatNumber value="${lendOrderExtProduct.buyBalance }" pattern="#,##0.00"/></td>
                <td align="left" style="font-size: 12px;">在投资金预期收益：<fmt:formatNumber value="${lendOrderExtProduct.expectProfit }" pattern="#,##0.00"/></td>
                <td align="left" style="font-size: 12px;">购买日期：<fmt:formatDate value="${lendOrderExtProduct.buyTime }" pattern="yyyy-MM-dd" type="date"/></td>               
                <td align="left" style="font-size: 12px;" >起息日期：<fmt:formatDate value="${lendOrderExtProduct.agreementStartDate }" pattern="yyyy-MM-dd" type="date"/></td>
                <td align="left" style="font-size: 12px;">到期日期：<fmt:formatDate value="${lendOrderExtProduct.agreementEndDate }" pattern="yyyy-MM-dd" type="date"/></td>
                <td align="left" style="font-size: 12px;">理财金额：<fmt:formatNumber value="${lendOrderExtProduct.forLendBalance}" pattern="#,#00.00"/></td>
            </tr>

        </table>
   </fieldset>

	
		<table id="lendList_list"></table>
	</div>
	<script language="javascript">


	/**
	 * 执行：列表加载。
	 */
    function loadList(){
        $("#lendList_list").datagrid({
            idField: 'lendOrderId',
            title: '匹配的债权列表',
            url: '${ctx}/jsp/lendList/findCreditorRightsByDetailList?lendOrderId='+${lendOrderExtProduct.lendOrderId },
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.71,
            height: document.body.clientHeight * 0.8,
            singleSelect: true,
            rownumbers: true,
           
            columns:[[
                      {field:'loanApplicationCode', width:150,title:'借款ID'},
                      {field:'loanApplicationName', width:300,title:'借款标题'},
                      {field:'dueTime', width:80,title:'借款时长(月)'},
                      {field:'annualRate', width:100,title:'借款年化利率'},
                      {field:'buyBalance', width:100,title:'出借金额'},
                      {field:'expectProfit', width:100,title:'预期收益'},
                      {field:'factBalance', width:100,title:'已回款总额'},
                      {field:'newWaitTotalpayMent', width:100,title:'待回款总额'},
                      {field:'rightsState', width:100,title:'状态'},
       
            ]],
           	onBeforeLoad: function (value, rec) {
           		var buyBalance = $(this).datagrid("getColumnOption", "buyBalance");
                if (buyBalance) {
                	buyBalance.formatter = function (value, rowData, rowIndex) {
             			return formatNum(value,2);
                    }
                }
                var expectProfit = $(this).datagrid("getColumnOption", "expectProfit");
                if (expectProfit) {
                	expectProfit.formatter = function (value, rowData, rowIndex) {
             			return formatNum(value,2);
                    }
                }
                var factBalance = $(this).datagrid("getColumnOption", "factBalance");
                if (factBalance) {
                	factBalance.formatter = function (value, rowData, rowIndex) {
             			return formatNum(value,2);
                    }
                }
                var newWaitTotalpayMent = $(this).datagrid("getColumnOption", "newWaitTotalpayMent");
                if (newWaitTotalpayMent) {
                	newWaitTotalpayMent.formatter = function (value, rowData, rowIndex) {
             			return formatNum(value,2);
                    }
                }
               var annualRate = $(this).datagrid("getColumnOption", "annualRate");
               if (annualRate) {
            	   annualRate.formatter = function (value, rowData, rowIndex) {
            			
                       	return value+"%";
                   }
               }
               var rightsState = $(this).datagrid("getColumnOption", "rightsState");
               if (rightsState) {
            	   rightsState.formatter = function (value, rowData, rowIndex) {
            			if(value == 0){return '已生效';}
            			else if(value == 1){return '还款中';}
            			else if(value == 2){return '已转出';}
            			else if(value == 3){return '已结清';}
            			else if(value == 5){return '申请转出';}
            			else if(value == 6){return '已转出(平台垫付)';}
            			else if(value == 7){return '提前结清';} 
            			else if(value == 8){return '转让中';}
            			else if(value == 8){return '已锁定';} 
            			else return value;
                   }
               }
           }
       });
    }
	 
    $(function(){
    	loadList();	
    });
</script>
</body>
</html>