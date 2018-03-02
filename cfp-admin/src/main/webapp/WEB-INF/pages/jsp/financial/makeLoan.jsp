<%--
  User: Ren yulin
  Date: 15-7-20 下午4:03
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
借款标题：${loanApplication.loanApplicationName}
借款ID:${loanApplication.loanApplicationCode}
标的类型：${subjectTypeName}
<span style="float: right;margin-right: 100px">
    状态：${applicationStateName}
</span>

<div class="cf">
    <fieldset style="height: auto; margin: 10px auto;">
        <table  align="center" border="0" width="90%" style="font-size: 12px" >
            <tr>
                <td style="width: 15%;text-align: right">借款人:</td>
                <td style="text-align: left">${user.realName}</td>
                <td  style="width: 15%;text-align: right">手机号:</td>
                <td style="text-align: left">${user.mobileNo}</td>
                <td  style="width: 15%;text-align: right">身份证号:</td>
                <td style="text-align: left">${user.idCard}</td>
            </tr>
            <tr>
                <td style="width: 15%;text-align: right">借款金额:</td>
                <td style="text-align: left">${loanApplication.loanBalance}</td>
                <td style="width: 15%;text-align: right">创建时间:</td>
                <td style="text-align: left"><fmt:formatDate value="${loanApplication.createTime}" type="both"/></td>
                <td style="width: 15%;text-align: right">满标时间:</td>
                <td style="text-align: left"><fmt:formatDate value="${loanApplication.fullTime}" type="both"/></td>
            </tr>

        </table>
    </fieldset>


</div>
<div style="width: 60%">
    <form class="form-horizontal" id="loan_make_form" method="post">
        <input type="hidden" name="loanApplicationId" id="loanApplicationId"
               value="${loanApplication.loanApplicationId}">

        <div class="control-group">
            <label class="control-label">借款批复金额：</label>

            <div class="controls">
                ${loanApplication.confirmBalance}
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">放款扣费：</label>

            <div class="controls">
                ${fees}
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">放款金额：</label>

            <div class="controls">
                ${makeLoanBalance}
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">打款卡银行：</label>

            <div class="controls">
                ${customerCard.bankName}
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">打款卡开户行：</label>

            <div class="controls">
                ${customerCard.registeredBank}
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">银行卡号：</label>

            <div class="controls">
                ${customerCard.cardCode}
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">开户名：</label>

            <div class="controls">
                ${customerCard.cardCustomerName}
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">备注：</label>

            <div class="controls">
                <textarea rows="20" cols="30" id="desc" name="desc" style="width: 300px;height: 100px"></textarea>
            </div>
        </div>


    </form>
    <input style="float: right;width: 100px" type="button" class="btn btn-primary" value="确认放款"
           onclick="submitButton();">
</div>
<script language="JavaScript">
    $("#loan_make_form").form({
        url: '${ctx}/jsp/financial/makeLoan',
        data: {loanApplicationId: $("#loanApplicationId").val()},
        success: function (data) {
            if (data == 'success') {
                $.messager.progress('close');
                $.messager.alert("系统提示", "放款成功!", "info",function(){
                    opener.$("#wait_loan_list").datagrid("reload");
                    window.close();
                });

            } else {
                $.messager.progress('close');
                $.messager.alert("系统提示", "放款失败!", "info");
            }
        }
    })

    function submitButton() {
        $.messager.progress({
            title:'请稍后',
            msg:'页面加载中...'
        });

        $("#loan_make_form").submit();
    }
</script>
</body>
</html>
