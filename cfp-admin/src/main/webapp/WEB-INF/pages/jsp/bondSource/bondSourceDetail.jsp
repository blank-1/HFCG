<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div class="cf" style="float:left; width:99%;margin:0 10px 5px 10px;height: inherit;">
    <fieldset style="height:65px">
        <legend>渠道信息</legend>
        <table align="center" border="0" width="100%" >

            <tr>
                <td align="left" style="font-size: 12px;">名称：${bondSource.sourceName}</td>
                <td align="left" style="font-size: 12px;" >公司所在地：${bondSource.locatioin}</td>
                <td align="left" style="font-size: 12px;">联系人：${bondSource.contactPersion}</td>
                <td align="left" style="font-size: 12px;">联系电话：${bondSource.contactPhone}</td>
            </tr>
            <tr>
                <td align="left"  style="font-size: 12px;">账户余额：${bondSource.value}</td>
                <td align="left" style="font-size: 12px;">可用金额：${bondSource.avilableValue}</td>
                <td align="left" style="font-size: 12px;">冻结金额：${bondSource.freezeValue}</td>
                <td align="left" style="font-size: 12px;">状态：
                    <c:if test="${bondSource.status eq '0'}" >正常</c:if>
                    <c:if test="${bondSource.status ne '0'}" >禁用</c:if>
                </td>
            </tr>
        </table>
    </fieldset>
    <div id="detailTabs" style="height:100%;display: block;">
        <div title="借款"
             style="padding:5px;"></div>
        <div title="原始债权人"
             style="padding:20px;"></div>
        <div title="账户流水"
             style="padding:20px;"></div>
        <div title="充值记录"
             style="padding:20px;"></div>
        <div title="提现记录"
             style="padding:20px;"></div>
    </div>

    <script language="javascript">

        function showBondUser(id,title,index) {
            var tabPanel = $('#detailTabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/bondSource/showBondUser?bondSourceId=' + id,
            });
        }

        function showSimplifyHtmlContent(id,title,index) {
            var tabPanel = $('#detailTabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/jsp/loanManage/loan/showBorrow?bondSourceId=' + id,
            });
        }

        function showCrashFlow(id,title,index){
            var tabPanel = $('#detailTabs').tabs('getTab', index);

            tabPanel.panel({
                href : '${ctx}/cashFlow/showCashFlow?userId=' + id,
                cache:false
            });
        }

        function showIncomeList(id,title,index){
            var tabPanel = $('#detailTabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/rechargeOrder/showIncome?userId=' + id,
            });
        }

        function showWithDrawList(id,title,index){
            var tabPanel = $('#detailTabs').tabs('getTab', index);
            tabPanel.panel({
                href : '${ctx}/withdraw/showWithDraw?userId=' + id,
            });
        }

        function init(){
            $('#detailTabs').tabs({
                fit : true,
                onSelect : function(title,index) {
                    if (index == 0) {
                        showSimplifyHtmlContent(${bondSource.bondSourceId},title,index);
                    } else if (index == 1) {
                        //原始债权人
                        showBondUser(${bondSource.bondSourceId},title,index);
                    } else if (index == 2) {
                        //账户流水
                        showCrashFlow(${bondSource.userId},title,index);
                    } else if (index == 3) {
                        //充值记录
                        showIncomeList(${bondSource.userId},title,index);
                    } else if (index == 4) {
                        //提现记录
                        showWithDrawList(${bondSource.userId},title,index);
                    }
                }
            });

            $('#detailTabs').tabs('select',0);
        }


        $(document).ready(function(){
            init();
        });
    </script>
</div>
</body>
</html>