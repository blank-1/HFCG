<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>

</head>
<body>
<div id="addBondSource" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <fieldset style="height:65px">
        <legend>渠道信息</legend>
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
    <fieldset style="height:220px">
        <legend>充值</legend>
        <form class="form-horizontal" id="guaranteeCompany_income_form" method="post" action="">
            <input type="hidden" name="companyId" id="companyId" value="${guaranteeCompany.companyId}"/>
            <input type="hidden" name="userId" id="userId" value="${guaranteeCompany.userId}"/>

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
                    <select id="adminState" class="easyui-combobox" name="channelCode" style="width:200px;">
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
                    <textarea name="desc" autocomplete="off" class="textbox-text" placeholder="" style="margin-left: 0px; margin-right: 0px; height: 100px; width: 200px;"></textarea>
                </div>
            </div>
        </form>
    </fieldset>
</div>
</body>
</html>