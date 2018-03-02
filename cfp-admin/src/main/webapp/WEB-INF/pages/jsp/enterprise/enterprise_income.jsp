<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="addIncome" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <fieldset style="height:65px">
        <legend>企业信息</legend>
        <table align="center" border="0" width="100%" >

            <tr>
                <td align="left" style="font-size: 12px;">企业名称：${enterpriseInfoExt.enterpriseName}</td>
                <td align="left" style="font-size: 12px;">企业类型：
                	<c:if test="${enterpriseInfoExt.enterpriseType eq '0'}" >保理</c:if>
                    <c:if test="${enterpriseInfoExt.enterpriseType ne '0'}" >其它</c:if>
                </td>
                <td align="left" style="font-size: 12px;">法人姓名：${enterpriseInfoExt.legalPersonName}</td>
                <td align="left" style="font-size: 12px;">开户许可证：${enterpriseInfoExt.accountNumber}</td>
            </tr>
            <tr>
                <td align="left"  style="font-size: 12px;">账户余额：${enterpriseInfoExt.value}</td>
                <td align="left" style="font-size: 12px;">可用金额：${enterpriseInfoExt.avilableValue}</td>
                <td align="left" style="font-size: 12px;">冻结金额：${enterpriseInfoExt.freezeValue}</td>
                <td align="left" style="font-size: 12px;">状态：
                    <c:if test="${enterpriseInfoExt.state eq '0'}" >正常</c:if>
                    <c:if test="${enterpriseInfoExt.state ne '0'}" >禁用</c:if>
                </td>
            </tr>
        </table>
   </fieldset>
    <fieldset style="height:220px">
        <legend>充值</legend>
        <form class="form-horizontal" id="income_form" method="post" action="${ctx}/jsp/enterprise/income">
            <input type="hidden" name="enterpriseId" id="enterpriseId" value="${enterpriseInfoExt.enterpriseId}"/>
            <input type="hidden" name="userId" id="userId" value="${enterpriseInfoExt.userId}"/>

            <div class="control-group">

                <label class="control-label">充值金额：</label>
                <div class="controls">
                    <input type="text" style="width: 200px"
                     class="easyui-validatebox" required="true" missingMessage="充值金额不能为空"
                           validtype="amount"
                           name="amount" id="amount" value=""><span style="color: red">*</span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" >支付方式：</label>
                <div class="controls">
                    <select id="channelCode" class="easyui-combobox" name="channelCode" style="width:200px;">
                            <option value="0">划扣</option>
                            <option value="1">转账</option>
                            <option value="2" >现金</option>
                    </select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">转账流水号：</label>
                <div class="controls">
                    <input type="text" style="width: 200px"
                    class="easyui-validatebox" required="true" missingMessage="转账流水号不能为空"
                           name="externalNo" id="externalNo" value=""><span style="color: red">*</span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">备注信息：</label>
                <div class="controls">
                    <textarea id="desc" name="desc" autocomplete="off" class="textbox-text" placeholder="" style="margin-left: 0px; margin-right: 0px; height: 100px; width: 200px;"></textarea>
                </div>
            </div>
        </form>
    </fieldset>
</div>
</body>
</html>