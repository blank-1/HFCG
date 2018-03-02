<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<!--连连认证支付无卡-->
<div class="pay-left">
	<div class="ipt-img">
		<img src="${ctx}/images/pay/order_pay_10.jpg" alt="">
	</div>
    <div class="input-group">
        <label>
            <span class="fsp1">开户名：</span>${userExtInfo.realName}
        </label>
    </div>
    <div class="input-group mt-30">
        <label for="bankid">
            <span class="fsp1">银行卡号：</span>
            <input type="text" id="bankid"  size="25" onKeyUp="formatBankNo(this)" onKeyDown="formatBankNo(this)" flag="true" <c:if test="${not empty hisCustomerCard && isSupport eq 'true' }">value="${hisCustomerCard.cardCode}"</c:if>
                   name="cardNo" class="ipt-input" />
        </label><i id="bankshow"></i>
        <em id="userBankName" style="display:block;">*仅支持<font color="#fe2a4d">${userExtInfo.realName}</font>的储蓄卡进行支付</em>
    </div>

    <div class="input-group mt-20">
        <label for="password">
            <span class="fsp1">付款限额：</span>
            <a href="javascript:;" data-id="p_bank" data-mask="mask">点击查看</a>
        </label>
    </div>

    <div class="btn-group mt-30 mb-30 ml-15">
        <label for="">
            <a href="javascript:;" data-mask="mask" data-id="lianlian" style="color:#666!important">《连连支付认证支付开通协议》</a>
        </label>
    </div>
</div>
<div class="pay-right">
	<div class="bindcard" id="bindcard">
		<h2>绑卡提醒</h2>
		<p>为使您的投资更加方便快捷，我司的第三方支付平台已全面升级为连连支付，您需要在连连支付平台重新绑定银行卡。如有问题，请咨询客服400-061-8080</p>
	</div>
</div>
