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
                <label class="control-label" style="padding-top: 0;">财富券ID：</label>
                <div class="controls" >${voucher.voucherId}
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">持有人用户名：</label>
                <div class="controls" >${voucher.userName}
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">持有人真实姓名：</label>
                <div class="controls" >${voucher.realName}
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">财富券名称：</label>
                <div class="controls" >${voucher.voucherName}
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">财富券金额：</label>
                <div class="controls">${voucher.amountStr}
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">有效期：</label>
                <div class="controls">${voucher.effictiveDate}
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">使用场景：</label>
                <div class="controls">${voucher.usageScenarioStr}
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">使用条件：</label>
                <div class="controls">${voucher.conditionAmountStr}
                </div>
            </div>
            <div class="control-group" >
                <label class="control-label" style="padding-top: 0;">叠加使用：  </label>
                <div class="controls"><span id="fee">${voucher.isOverlyStr}</span></div>
            </div>

            <div class="control-group" >
                <label class="control-label" style="padding-top: 0;">财富券来源：  </label>
                <div class="controls"><span id="">${voucher.sourceStr}</span></div>
            </div>

            <div class="control-group" >
                <label class="control-label" style="padding-top: 0;">财富券获取时间：  </label>
                <div class="controls"><span id=""><fmt:formatDate value="${voucher.createDate}" pattern="yyyy-MM-dd hh:mm:ss"/> </span></div>
            </div>

            <div class="control-group" >
                <label class="control-label" style="padding-top: 0;">财富券使用时间：  </label>
                <div class="controls"><span id="fee"><fmt:formatDate value="${voucher.usageDate}" pattern="yyyy-MM-dd hh:mm:ss"/> </span></div>
            </div>

            <div class="control-group" >
                <label class="control-label" style="padding-top: 0;">财富券使用记录：  </label>
                <div class="controls"><span id="fee">${voucher.orderName}</span></div>
            </div>

            <div class="control-group">
                <label class="control-label" style="padding-top: 0;">体验型财富券：</label>
                <div class="controls"><span id="amountWithoutFee">${voucher.isExperienceStr}</span>
                </div>
            </div>
        </form>
</div>
</body>
</html>