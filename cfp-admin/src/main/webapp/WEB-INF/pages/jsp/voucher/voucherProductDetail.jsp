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
                <label class="control-label" style="padding-top: 0;">财富券名称：</label>
                <div class="controls" >${voucherProduct.voucherName}
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">财富券类型：</label>
                <div class="controls">${voucherProduct.voucherTypeStr}
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">有效期：</label>
                <div class="controls">${voucherProduct.effictiveDate}
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">有效时长(天)：</label>
                <div class="controls">${voucherProduct.effectiveCountStr}
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">使用场景：</label>
                <div class="controls">${voucherProduct.usageScenarioStr}
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">使用条件：</label>
                <div class="controls">${voucherProduct.conditionAmountStr}
                </div>
            </div>
            <div class="control-group" >
                <label class="control-label" style="padding-top: 0;">叠加使用：  </label>
                <div class="controls"><span id="fee">${voucherProduct.isOverlyStr}</span></div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">体验型财富券：</label>
                <div class="controls"><span id="amountWithoutFee">${voucherProduct.isExperienceStr}</span>
                </div>
            </div>
        </form>
</div>
</body>
</html>