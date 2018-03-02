<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>

<!--连连认证支付有卡-->
<p class="quickPay clearFloat">
        <span class="floatLeft">
             <img src="${ctx}/images/pay_061.jpg" />一键支付
        </span>
        <span class="bank banksy mt-0 floatLeft">
            <img class="ml-20" src="${ctx}/images/banklogo/${customerCard.bankCode}.png"/>
             <input type="hidden" name="cardId" id="cardId" value="${customerCard.customerCardId}"/>
             <input type="hidden" name="cardNo" id="cardNo" value="${customerCard.cardCode}"/>

            <i>尾号：<big>${customerCard.cardCodeShort}</big></i>
            <%--<a href="javascript:;" class="ahui">查看限额</a>--%>
             <a href="javascript:;" data-id="p_bank" data-mask="mask">查看限额</a>
        </span>
       <%-- <div class="banked banked2">
            <i class="payie"> 连连支付提供认证支付流程，了解更多<a href="#" target="_blank">查看详情</a></i>
        </div>--%>
</p>


<!-- wrapper end -->
<div style="text-align:center;">
    <a id="with_falure" style="display: none;" href="javascript:" data-mask='mask' data-id="f_with" >&nbsp</a>
</div>

<!-- masklayer start  -->
<div class="masklayer" id="f_with">
    <h2 class="clearFloat"><span>支付</span><a id="f_close_withdraw" href="javascript:" data-id="close"></a></h2>
    <div class="shenf_yanz_main">
        <div style="height:70px;clear:both;"></div>
        <p><img src="${ctx}/images/img/false.jpg" /><span><i id="errorMsg"></i></span></p>
        <div style="height:50px;clear:both;"></div>
        <div class="input_box_shenf">
            <a href="javascript:" data-id="close" id="f_queren_withdraw" ><button>确认</button></a>
            <div style="height:90px;clear:both;"></div>
        </div>
    </div>
</div>