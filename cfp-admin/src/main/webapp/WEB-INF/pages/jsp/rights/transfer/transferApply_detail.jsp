<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div class="cf" style="float:left; width:99%;margin:0 10px 5px 10px;height: inherit;">
    <div>
       <table width="80%"><tr>
           <td align="right" style="font-size: 12px;"><B>借款标题：</B></td>
           <td align="center" style="font-size: 12px;">${loanApplication.loanApplicationName}</td>
           <td align="right" style="font-size: 12px;"><B>借款ID：</B></td>
           <td align="left" style="font-size: 12px;">${loanApplication.loanApplicationCode}</td>
           <td align="right" style="font-size: 12px;"><B>状态：</B></td>
           <td align="left" style="font-size: 12px;"><c:forEach items="${loanApplicationState}" var="state">
               <c:if test="${state.value eq loanApplication.applicationState}">
                   ${state.desc}
               </c:if>
           </c:forEach> </td>
           <td align="right" style="font-size: 12px;"><B>标的类型：</B></td>
           <td align="center" style="font-size: 12px;">
               <c:if test="${loanApplication.subjectType eq '1'}">借款标</c:if>
               <c:if test="${loanApplication.subjectType eq '0'}">债权标</c:if>
           </td>

       </tr></table>

    </div>
    <div style="clear: both;"></div>
    <hr/>

    <script language="javascript">

        //标的信息(个人)
        function showConcreteDetails(id,title,index) {
            var tabPanel = $('#detail_Tabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/jsp/loanManage/loan/showConcreteDetails?loanApplicationId=' + id,
            });
        }
        //标的信息（企业）
        function showConcreteDetailsForEnterprise(id,title,index) {
            var tabPanel = $('#detail_Tabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/jsp/enterprise/loan/showConcreteDetails?loanApplicationId=' + id,
            });
        }
        //抵押信息（个人）
        function showChargeInfo(id,title,index) {
            var tabPanel = $('#detail_Tabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/jsp/loanManage/loan/showChargeInfo?loanApplicationId=' + id,
            });
        }
        //抵押信息(企业)
        function showChargeInfoForEnterprise(id,title,index) {
            var tabPanel = $('#detail_Tabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/jsp/enterprise/loan/showChargeInfo?loanApplicationId=' + id,
            });
        }
        //出借人列表
        function showLendList(id,title,index) {
            var tabPanel = $('#detail_Tabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/jsp/loanManage/loan/showLendList?loanApplicationId=' + id,
            });
        }
        //还款情况列表
        function showRepaymentList(id,title,index) {
            var tabPanel = $('#detail_Tabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/jsp/loanManage/loan/showRepaymentList?loanApplicationId=' + id,
            });
        }
        //还款计划
        function showRepaymentPlan(id,title,index) {
            var tabPanel = $('#detail_Tabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/jsp/loanManage/loan/repaymentPlan?loanApplicationId=' + id,
            });
        }
        //相关附件
        function showAttachment(id,title,index) {
            var tabPanel = $('#detail_Tabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/jsp/loanManage/loan/showAttachment?loanApplicationId=' + id,
            });
        }
		//财务流水
        function showCrashFlow(id,title,index){
            var tabPanel = $('#detail_Tabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/cashFlow/showCashFlowLoan?loanApplicationId=' + id,
            });
        }
		//定向信息
        function showOrient(id,title,index){
            var tabPanel = $('#detail_Tabs').tabs('getTab', index);
            tabPanel.panel({
           		href : '${ctx}/jsp/loanManage/loan/showOrientByLoanApplication?loanApplicationId=' + id,
            });
        }
        
        function init(loanType){
            $('#detail_Tabs').tabs({
                fit : true,
                onSelect : function(title,index) {
                    if (index == 0) {
                    	if(loanType == '0' || loanType == '1' || loanType == '7'){
                    		//标的信息(个人)
	                        showConcreteDetails(${loanApplication.loanApplicationId},title,index);
                    	}else{
                    		//标的信息(企业)
	                        showConcreteDetailsForEnterprise(${loanApplication.loanApplicationId},title,index);
                    	}
                    } else if (index == 1) {
                    	if(loanType == '0' || loanType == '1' || loanType == '7'){
                    		//抵押信息（个人）
	                        showChargeInfo(${loanApplication.loanApplicationId},title,index);
                    	}else{
                    		//抵押信息（企业）
	                        showChargeInfoForEnterprise(${loanApplication.loanApplicationId},title,index);
                    	}
                    } else if (index == 2) {
                        //还款计划
                        showRepaymentPlan(${loanApplication.loanApplicationId},title,index);
                    } else if (index == 3) {
                        //相关附件
                        showAttachment(${loanApplication.loanApplicationId},title,index);
                    } else if (index == 4) {
                        //出借人列表
                        showLendList(${loanApplication.loanApplicationId},title,index);
                    }  else if (index == 5) {
                        //还款情况
                        showRepaymentList(${loanApplication.loanApplicationId},title,index);
                    }  else if (index == 6) {
                        //财务流水
                        showCrashFlow(${loanApplication.loanApplicationId},title,index);
                    }  else if(index == 7){
                    	//定向设置
                    	  showOrient(${loanApplication.loanApplicationId},title,index);
                    }
                }
            });

            $('#detail_Tabs').tabs('select',0);
        }

        function init1(loanType){
            $('#detail_Tabs').tabs({
                fit : true,
                onSelect : function(title,index) {
                    if (index == 0) {
                    	if(loanType == '0' || loanType == '1' || loanType == '7'){
                    		//标的信息(个人)
	                        showConcreteDetails(${loanApplication.loanApplicationId},title,index);
                    	}else{
                    		//标的信息(企业)
	                        showConcreteDetailsForEnterprise(${loanApplication.loanApplicationId},title,index);
                    	}
                    } else if (index == 1) {
                        //还款计划
                        showRepaymentPlan(${loanApplication.loanApplicationId},title,index);
                    } else if (index == 2) {
                        //相关附件
                        showAttachment(${loanApplication.loanApplicationId},title,index);
                    } else if (index == 3) {
                        //出借人列表
                        showLendList(${loanApplication.loanApplicationId},title,index);
                    } else if (index == 4) {
                        //还款情况
                        showRepaymentList(${loanApplication.loanApplicationId},title,index);
                    }  else if (index == 5) {
                        //财务流水
                        showCrashFlow(${loanApplication.loanApplicationId},title,index);
                    }else if(index == 6){
                    	//定向设置
                  	  showOrient(${loanApplication.loanApplicationId},title,index);
                  }
                }
            });

            $('#detail_Tabs').tabs('select',0);
        }

        $(document).ready(function(){
        	var loanType = '${loanApplication.loanType}';
        	if(loanType != '0' && loanType != '5'){
        		init(loanType);
        	}else{
        		init1(loanType);
        	}
        });
    </script>
    <div id="detail_Tabs">
        <div title="标的信息" style="overflow:auto;height: 105%;"></div>
        <c:if test="${loanApplication.loanType ne '0' || loanApplication.loanType ne '5'}"><!-- 个人信贷和企业基金无抵押信息 -->
            <div title="抵押信息" style="padding:20px;"></div>
        </c:if>
        <div title="还款计划" style="padding:20px;"></div>
        <div title="相关附件" style="padding:20px;"></div>
        <div title="出借人列表" style="padding:20px;"></div>
        <div title="还款情况" style="padding:20px;"></div>
        <div title="财务流水" style="padding:20px;"></div>
        <div title="定向信息" style="padding:20px;"></div>
    </div>
</div>
</body>
</html>