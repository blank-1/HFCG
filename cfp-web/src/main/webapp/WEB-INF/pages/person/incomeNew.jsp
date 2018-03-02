<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8"/>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">

    <title>账户中心-资金管理-充值</title>
    <%@include file="../common/common_js.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctx}/css/hDate.css"/><!-- index css -->
    <script type="text/javascript" src="${ctx}/js/jquery_page.js"></script><!-- public javascript -->
    <script type="text/javascript" src="${ctx}/js/recharge.js"></script>
</head>
<body class="body-back-gray">
<!-- line2 start -->
<%@include file="../common/headLine1.jsp" %>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->


<!-- tabp start -->
<%request.setAttribute("tab", "4-2");%>
<input type="hidden" id="titleTab" value="2-2"/>
<!-- tabp end -->

<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">资金管理</a>></li>
        <li><span>充值</span></li>
    </ul>
</div>
<!-- person-link end -->
<form id="hf_cz_form" class="form" method="post" target="_blank">
    <input type="hidden" id="mchnt_cd2" name="mchnt_cd" value=""/>
    <input type="hidden" id="mchnt_txn_ssn2" name="mchnt_txn_ssn" value=""/>
    <input type="hidden" id="login_id" name="login_id" value="${userExt.mobileNo}"/>
    <input type="hidden" id="amt" name="amt" value=""/>
    <input type="hidden" id="page_notify_url2" name="page_notify_url" value=""/>
    <input type="hidden" id="back_notify_url2" name="back_notify_url" value=""/>
    <input type="hidden" id="signature2" name="signature" value=""/>

</form>

<!-- container start -->
<div class="container clearFloat">
    <!-- pLeft start -->
    <div class="pLeft clearFloat">
    </div>
    <!-- pLeft end -->

    <!-- pRight start -->
    <div class="pRight clearFloat">
        <div class="p-Right-top">
            <p class="cz-title">充值</p>
        </div>
        <div class="p-Right-bot yhkgl-mt0">
            <!-- rechan_grc start -->
            <div class="rechan_gro">
                <!-- bankpayd start -->
                <div class="bankpayd2">
                    <!-- 网银支付 -->
                    <h2 class="botyibao clearFloat s_hide" onclick="chooseType(this)">
                        <img class="renzheng" src="${ctx }/images/pay/internatePay.jpg"/>
                        <i>跳转至银行页面，支付限额高</i>
                    </h2>
                </div>
                <!-- bankpayd start -->

                <!-- bankpayd start -->
                <div class="bankpayd2">
                    <h2 class="botyibao clearFloat s_show" onclick="chooseType(this)">
                        <input id="rdi-quick" class="label2_1" checked type="radio" name="bankPayChannel"/>
                        <img class="renzheng" src="${ctx }/images/pay/quickPay.jpg"/>
                        <i>一键支付，快捷方便，付款过程安全流畅</i>
                    </h2>
                    <!-- quick-group start -->
                    <div class="inter-group clearFloat inter-show" style="display: block;">

                        <div class="cz-list-le">
                            <div class="input-group">
                                <label>
                                    <c:if test="${userExt.isVerified ne '0'}">
                                        <span class="cz-span">开户名：</span>${userExt.realName}
                                    </c:if>
                                    <c:if test="${userExt.isVerified eq '0'}">
                                        <span class="cz-span">开户名：</span>未开通
                                        <small>＊您未开通存管帐号，请先进行
                                            <a data-mask='mask' data-id="hengfengCard" href="javascript:;">开通恒丰存管</a>再进行充值操作
                                        </small>
                                    </c:if>

                                </label>
                            </div>
                            <div class="input-group mt-30">
                                <label>
                                    <span class="cz-span">银行卡号：</span>
                                    <div class="bankblock bankuse">
                                        <div class="titlet clearFloat">
                                            <c:if test="${not empty customerCard}">
                                                <img style="float: left" src="${ctx}/images/banklogo/${customerCard.bankCode}.png" alt="">
                                                <span class="floatRight"><small>尾号：</small>${customerCard.encryptCardNo}</span>
                                            </c:if>
                                        </div>
                                    </div>
                                </label>
                            </div>
                            <div class="clear_0"></div>
                            <%--<div class="input-group mt-20">
                                <label for="password">
                                    <span class="cz-span">付款限额：</span>
                                    <a href="javascript:;" class="cz-list-a">点击查看</a>
                                </label>
                            </div>--%>
                            <div class="input-group mt-30">
                                <label>
                                    <span class="cz-span">充值金额：</span>
                                    <input <c:if test="${userExt.isVerified ne '2'}">readonly</c:if> type="text"  oninput="inputToValue('amt',this.value)" name="moneyp" id="moneyp" class="ipt-input user-cz-input">
                                </label>
                                <em class="hui cz-em">*充值最小金额不得小于<font color="#fe2a4d">50</font>元</em>
                            </div>
                        </div>
                        <div class="clear_0"></div>
                        <div class="bankphonebtyn cz-btn mb-30">
                            <button type="button" <c:if test="${userExt.isVerified ne '2'}">disabled style="color: #ccc" </c:if> id="b_lianlian_WBD" class="btn btn-error">确认充值</button>
                        </div>
                        <div class="clear_30"></div>
                    </div>

                </div><!-- bankpayd end -->
            </div>
            <!-- rechan_grc end -->

        </div>
        <div class="tx_list">
            <div class="p-Right-top" style="margin-top:0px;">
                <p class="czlist-title">充值记录</p>
            </div>
            <div class="txjl-list">
                <ul class="txjl-ul-big" id="rechargeRecords">
                    <li>
                        <ul class="tx-ul">
                            <li>充值时间</li>
                            <li>充值金额(元)</li>
                            <li>充值银行</li>
                            <li>状态</li>
                        </ul>
                    </li>
                </ul>
                <div class="clear_0"></div>
            </div>
            <div class="tcdPageCode mt-20"></div>
        </div>

    </div>
    <!-- pRight end -->
</div>
<!-- container end -->
<%@include file="../common/hengfengCard.jsp" %>
<script type="text/javascript">
    var payFlag=false;
    function chooseType(obj){
        var h2=$(".rechan_gro").find("h2").filter(".botyibao");
        $(h2).each(function(i,v){
            if(v==obj){
                $(v).removeClass("s_hide");
                $(v).addClass("s_show");
                payFlag=!payFlag;
            }else{
                $(v).removeClass("s_show");
                $(v).addClass("s_hide");
            }
        })
    }
    $("#b_lianlian_WBD").on("click",function(){
        var mobile=$("#login_id").val(),
            amt=$("#amt").val(),
            url="/api/recharge/signRecharge";
        if(amt==undefined||amt==""){
            alert("请输入正确的金额");
            return false;
        }
        if(!/^[1-9]([0-9]+)?(\.[0-9]{1,2})?$/.test(amt)&&amt>=100) {
            alert("请输入正确的金额");
            return false;
        }
        $.ajax({
            url:url,
            type:"post",
            data:{"mobile":mobile,"amount": amt,"flag":payFlag?"0":"1"},
            success:function(data){
                $("#mchnt_cd2").val(data.mchnt_cd);
                $("#mchnt_txn_ssn2").val(data.mchnt_txn_ssn);
                var moneyp=$("#moneyp").val();
                //moneyp=parseInt(moneyp)*100;
                $("#amt").val(data.amt);
                $("#page_notify_url2").val(data.page_notify_url);
                $("#back_notify_url2").val(data.back_notify_url);
                $("#signature2").val(data.signature);
                $("#hf_cz_form").attr("action",data.actionUrl);
                $("#hf_cz_form").submit();
                setTimeout(function(){
                    location.href=location.href;
                },1500);

            }

        });
    })
</script>
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp" %>
<!-- fbottom end -->


</body>
</html>
