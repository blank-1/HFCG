<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div class="cf" style="float:left; width:99%;margin:0 10px 5px 10px;height: inherit;">
    <fieldset style="height:65px">
        <legend>担保公司信息</legend>
        <table align="center" border="0" width="100%" >

            <tr>
                <td align="left" style="font-size: 12px;">担保公司名称：${guaranteeCompany.companyName}</td>
                <td align="left" style="font-size: 12px;" >公司所在地：${guaranteeCompany.companyLocation}</td>
                <td align="left" style="font-size: 12px;">联系人：${guaranteeCompany.contactPersion}</td>
                <td align="left" style="font-size: 12px;">联系电话：${guaranteeCompany.contactPhone}</td>
            </tr>
            <tr>
                <td align="left"  style="font-size: 12px;">账户余额：${guaranteeCompany.value}</td>
                <td align="left" style="font-size: 12px;">可用金额：${guaranteeCompany.avilableValue}</td>
                <td align="left" style="font-size: 12px;">冻结金额：${guaranteeCompany.freezeValue}</td>
                <td align="left" style="font-size: 12px;">状态：
                    <c:if test="${guaranteeCompany.status eq '0'}" >正常</c:if>
                    <c:if test="${guaranteeCompany.status ne '0'}" >禁用</c:if>
                </td>
            </tr>
        </table>
    </fieldset>
    <div id="g_detailTabs" style="height:100%;">
        <div title="借款"
             style="overflow:auto;padding:5px;"></div>
        <div title="账户流水"
             style="padding:20px;"></div>
        <div title="充值记录"
             style="padding:20px;"></div>
        <div title="提现记录"
             style="padding:20px;"></div>
    </div>

    <script language="javascript">

        function showCrashFlow(id,title,index){
            var tabPanel = $('#g_detailTabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/cashFlow/showCashFlow?userId=' + id,
            });
        }

        function showIncomeList(id,title,index){
            var tabPanel = $('#g_detailTabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/rechargeOrder/showIncome?userId=' + id,
            });
        }

        function showWithDrawList(id,title,index){
            var tabPanel = $('#g_detailTabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/withdraw/showWithDraw?userId=' + id,
            });
        }

        function init(){
            $('#g_detailTabs').tabs({
                fit : true,
                onSelect : function(title,index) {
                    if (index == 0) {
//                                showSimplifyHtmlContent(title,index);
                    } else if (index == 1) {
                        //账户流水
                        showCrashFlow(${guaranteeCompany.userId},title,index);
                    } else if (index == 2) {
                        //充值记录
                        showIncomeList(${guaranteeCompany.userId},title,index);
                    } else if (index == 3) {
                        //提现记录
                        showWithDrawList(${guaranteeCompany.userId},title,index);
                    }
                }
            });

            $('#g_detailTabs').tabs('select',1);
        }


        $(document).ready(function(){
            init();

        });
    </script>
</div>
</body>
</html>