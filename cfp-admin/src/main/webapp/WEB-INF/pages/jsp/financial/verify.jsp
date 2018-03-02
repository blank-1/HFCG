<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>

</head>
<body>
<div id="addBondSource" class="container-fluid" style="padding: 5px 0px 0px 10px">

        <form class="form-horizontal" id="doVerify_form" method="post" >
            <input type="hidden" name="withdrawId" value="${withdrawId}"/>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">提现人用户名：</label>
                <div class="controls" >${withDraw.loginName}
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">累计充值金额：</label>
                <div class="controls">${withDraw.allIncomeAmount}元
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">累计提现金额：</label>
                <div class="controls">${withDraw.allWithDrawAmount}元
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">持有债权金额：</label>
                <div class="controls">${withDraw.allBondondAmount}元
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">账户现金总额：</label>
                <div class="controls">${withDraw.value}元
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">提现金额：</label>
                <div class="controls">${withDraw.withdrawAmount}元
                </div>
            </div>
            <div class="control-group" >
                <label class="control-label" style="padding-top: 0;">手续费：  </label>
                <div class="controls"><span id="fee">${withDraw.commissionFee}</span>元</div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">到账金额：</label>
                <div class="controls"><span id="amountWithoutFee">${withDraw.amountWithoutFee}</span>元
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">开户银行：</label>
                <div class="controls">
                    <customUI:dictionaryTable constantTypeCode="bank" desc="true" key="${withDraw.bankName}" keyType="CONSTANT_DEFINE_ID"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">卡号：</label>
                <div class="controls">${withDraw.cardNo}
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">开户名：</label>
                <div class="controls">${withDraw.realName}
                </div>
            </div>

<hr>
            <div class="control-group">
                <label class="control-label">审核结果：</label>
                <div class="controls">
                    <input type="radio" checked name="verifyStatus" value="1"/>批准
                    <input type="radio" name="verifyStatus" value="2"/>驳回
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">审核意见：</label>
                <div class="controls">
                    <textarea name="verifyOpinion" autocomplete="off" class="textbox-text validatebox-text" placeholder="" style="margin-left: 0px; margin-right: 0px; height: 75px; width: 250px;"></textarea>
                </div>
            </div>
        </form>
</div>
</body>
</html>