<%--
  User: Ren yulin
  Date: 15-7-25 下午2:31
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
%>
<form class="form-horizontal" id="repayment_form" method="post">
    <input type="hidden" id="loanApplicationId" name="loanApplicationId" value="${loanApplicationId}">
    <input type="hidden" id="repaymentPlanId" name="repaymentPlanId" value="${repaymentPlanId}">
    <input type="hidden" id="isAheadStr" name="isAheadStr" value="false">
    <div class="control-group">
        <label class="control-label"><span style="color: red;">*</span>支付方式：</label>

        <div class="controls">
            <select id="channelCode" class="easyui-combobox" name="channelCode" style="width:200px;">
                <%--<option value="0">划扣</option>--%>
                <option value="1">转账</option>
                <option value="2">现金</option>
            </select>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red;">*</span>流水号：</label>

        <div class="controls">
            <input type="text" name="serialNumber" id="serialNumber"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red;">*</span>还款日期：</label>

        <div class="controls">
            <input type="text" name="repaymentDate" id="repaymentDate" editable="false"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red;">*</span>罚息：</label>

        <div class="controls">
            <input type="hidden" id="ignoreDelay" name="ignoreDelay" value="0">

            <div class="btn-group" data-toggle="buttons-radio">
                <input type="button" class="btn" id="ignore" onclick="$('#ignoreDelay').val(1)" value="免">
                <input type="button" class="btn" id="notIgnore" onclick="$('#ignoreDelay').val(0)" value="收">
            </div>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">应还金额：</label>

        <div class="controls">
            <span id="balance"/><br>
            <span id="repaymentInfo"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">本次还款：</label>

        <div class="controls">
            <input type="text" required="true" class="easyui-numberbox"  data-options="min:0,precision:2"
                   name="repaymentBalance" id="repaymentBalance"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">账户余额：</label>

        <div class="controls">
            <input type="hidden" id="isUserBalance" name="isUserBalance" value="0">

            <div class="btn-group" data-toggle="buttons-radio">
                <input type="button" class="btn active" id="notUseBalance" onclick="$('#isUserBalance').val(0)" value="不使用">
                <input type="button" class="btn" id="useBalance" onclick="$('#isUserBalance').val(1)" value="使用">
            </div>
            <span id="showUserBalance"/>
            <input type="hidden" id="userBalance" name="userBalance" value="0">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">本次实收：</label>

        <div class="controls">
            <span id="factPaid"></span>
            <input type="hidden" name="paidBalance" id="paidBalance"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">备注：</label>

        <div class="controls">
            <textarea rows="6" id="desc" name="desc" cols="60" style="width: 400px;height: 150px"></textarea>
        </div>
    </div>

</form>

<script language="JavaScript">
    var loanApplicationId = '${loanApplicationId}';

    if ('${balanceInfo}') {
        var balanceInfo = '${balanceInfo}';
        var balance = '${balanceInfo.balance}';
        if (!balance) {
            balance = 0;
        }
        var capital = '${balanceInfo.capital}';
        if (!capital) {
            capital = 0;
        }
        var interest= '${balanceInfo.interest}';
        if (!interest) {
            interest = 0;
        }
        var feesBalance = '${balanceInfo.feesBalance}';
        if (!feesBalance) {
            feesBalance = 0;
        }
        var defaultInterest = '${balanceInfo.defaultInterest}';
        if (!defaultInterest) {
            defaultInterest = 0;
        }
        $("#balance").html(formatNum(balance, 2) + "元");
        $("#repaymentBalance").val(balance);
        $("#factPaid").html(formatNum(balance,2) + "元");
        $("#repaymentInfo").html("本金:" + formatNum(capital, 2) + "元,利息:" + formatNum(interest, 2) + "元,费用:" + formatNum(feesBalance, 2) + "元,罚息:" + formatNum(defaultInterest, 2) + "元");
        $("#paidBalance").val(($("#repaymentBalance").val() - $("#userBalance").val()));
    }
    $('#repaymentDate').datebox({
        onSelect: function (date) {
            <%--var url = "${ctx}/jsp/financial/calculateBalance";--%>
            <%--$.post(url, {loanApplicationId: loanApplicationId, repaymentDate: $("#repaymentDate").datebox("getValue")}, function (data) {--%>
                <%--console.info(data);--%>
                <%--if (data) {--%>
                    <%--$("#balance").html(formatNum(data.data.balance, 2) + "元");--%>
                    <%--$("#repaymentBalance").numberbox("setValue",data.data.balance);--%>
                    <%--$("#factPaid").html(formatNum(data.data.balance,2) + "元");--%>
                    <%--$("#repaymentInfo").html("本金:" + formatNum(data.data.capital, 2) + "元,利息:" + formatNum(data.data.interest, 2) + "元,费用:" + formatNum(data.data.feesBalance, 2)--%>
                            <%--+ "元,罚息:" + formatNum(data.data.defaultInterest, 2) + "元");--%>
                    <%--$("#paidBalance").val(($("#repaymentBalance").val() - $("#userBalance").val()));--%>
                <%--}--%>

            <%--})--%>
        }
    });

    $("#useBalance").click(function () {
        var url = '${ctx}/jsp/financial/getUserBalance';
        $.post(url, {loanApplicationId: loanApplicationId}, function (data) {
            if (data != null) {
                $("#showUserBalance").show();
                $("#userBalance").val(data);
                $("#showUserBalance").html(formatNum(data, 2) + "元");
                $("#factPaid").html(formatNum(($("#repaymentBalance").val() - data),2) + "元");
                $("#paidBalance").val(($("#repaymentBalance").val() - data));
            }
        })
    })
    $("#notUseBalance").click(function () {
        $("#showUserBalance").hide();
        $("#showUserBalance").html("");
        $("#userBalance").val("0");
        $("#factPaid").html(formatNum(($("#repaymentBalance").val() - $("#userBalance").val()),2) + "元");
        $("#paidBalance").val(($("#repaymentBalance").val() - $("#userBalance").val()));
    })

    $("#repaymentBalance").blur(function () {
        $("#factPaid").html(formatNum(($("#repaymentBalance").val() - $("#userBalance").val()),2) + "元");
        $("#paidBalance").val(($("#repaymentBalance").val() - $("#userBalance").val()));
         if(parseFloat($("#paidBalance").val())>parseFloat(balance)){
            $.messager.alert("系统提示", "本次还款不能大于应还金额！", "info", function () {
            });
            $("#factPaid").html(formatNum(balance,2) + "元");
            $("#paidBalance").val(balance);
            $("#repaymentBalance").val(balance);
        }

    })
    $("#repayment_form").form({
        url: '${ctx}/jsp/financial/repayment',
        onSubmit:function(data) {
            console.info($("#repayment_form").serialize());
            var result = $(this).form('validate');

            if(result){
                var date = $("#repaymentDate").datebox("getValue");
                if(date == ''){
                    $.messager.alert("验证提示", "请选择还款日期！", "info");
                    result = false;
                }
            }
            Utils.loading();
            if(!result){
                Utils.loaded();
            }

            return result;
        },
        success: function (data) {
            Utils.loaded();
            console.info("data:" + data);
            if (data == 'success') {
                $.messager.alert("系统提示", "借款申请还款成功！", "info", function () {
                    parent.$("#repayment_loan_list").datagrid("reload");
                    parent.$("#repaymentDialog").dialog("close");
                });
            } else {
                $.messager.alert("系统提示", "借款申请还款失败！", "info");
            }
        }
    })
</script>
