<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8"/>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">

    <title>账户中心-资金管理-提现</title>
    <%@include file="../common/common_js.jsp" %>
    <script type="text/javascript" src="${ctx}/js/withdraw.js"></script><!-- public javascript -->
    <script type="text/javascript" src="${ctx}/js/jquery_page.js"></script><!-- public javascript -->
    <script type="text/javascript" src="${ctx}/js/person5.4.js"></script>
</head>

<body class="body-back-gray">
<!-- line2 start -->
<%@include file="../common/headLine1.jsp" %>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
<%@include file="../login/login.jsp" %>
<!-- navindex end -->


<!-- tabp start -->
<%request.setAttribute("tab", "4-3");%>
<input type="hidden" id="titleTab" value="2-3"/>
<!-- tabp end -->

<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">资金管理</a>></li>
        <li><span>提现</span></li>
    </ul>
</div>
<!-- person-link end -->


<!-- container start -->
<div class="container clearFloat">
    <!-- pLeft start -->
    <div class="pLeft clearFloat">
    </div>
    <!-- pLeft end -->

    <!-- pRight start -->
    <div class="pRight clearFloat">
        <div class="p-Right-top">
            <p class="tx-title">提现</p>
            <div class="clear_10"></div>
        </div>
        <form id="withDrawForm" action="${ctx}/person/withDraw" method="post">
            <p class="quickPay">
                <img src="${ctx }/images/hfBank.jpg"/> 一键支付
            </p>
            <div class="p-Right-bot yhkgl-mt0">
                <!-- bankpayd start -->
                <div class="bankpayd bankpayd2 user-border-0">
                    <div class="input-group">
                        <label>
                            <span class="fsp2">可提现金额：</span>
                            <big><fmt:formatNumber value="${cashAccount.availValue2}" pattern="#,##0.00"/></big>
                            <small>元</small>
                            <input id="usemoney" type="hidden" value="${cashAccount.availValue2}"/>
                        </label>
                    </div>
                    <div class="input-group mt-30">
                        <label for="moneyp">
                            <span class="fsp2 fash-3">提现金额：</span>
                            <input type="text" id="moneyp" name="moneyp" class="ipt-input" maxlength="10"/><i>元</i>
                            <input type="hidden" id="sxmon" value="500000"/><!--金额上限-->
                            <input type="hidden" id="barate" value="${fee}"/><!--打卡金额利率-->
                        </label>
                        <c:if test="${times eq doTimes}">
                            <em>*当日提现次数已达上限</em>
                        </c:if>
                        <c:if test="${times ne doTimes}">
                            <em class="hui">*每日提现次数上限 <font color="#fe2a4d">${times}</font> 次，提现金额上限 <font color="#fe2a4d">500,000.00</font> 元（伍拾万元）</em>
                            <span id="tip" style="display:none;">*每日提现次数上限 <font color="#fe2a4d">${times}</font> 次，提现金额上限 <font color="#fe2a4d">500,000.00</font> 元（伍拾万元）</span>
                        </c:if>
                    </div>
                    <div class="input-group mt-15">
                        <label>
                            <span class="fsp2">提现手续费：</span>
                            <i id="shouyufei" style="float:left;">&nbsp;&nbsp;3元/笔&nbsp;&nbsp;</i> <label style="float:left;" class="labelwidth  <c:if test="${voucherCount eq 0}">detaildesabed</c:if> "><input id="check"
                                                                                                                                                                                                                 <c:if test="${voucherCount eq 0}">disabled</c:if> class="check" type="checkbox">我要使用3元财富券(提现专用)：剩余<font id="onde" color="red">${voucherCount}</font>张</label><a class="whycfy ahui" style="font-size:14px!important;line-height:21px;text-decoration:underline!important;" target="_blank" href="http://help.caifupad.com/guide/caifuquan/">如何获取？</a>
                        </label>
                    </div>
                    <div class="clear_0"></div>
                    <div class="input-group mt-20">
                        <label>
                            <span class="fsp2">打卡金额：</span>
                            <big id="bamoney">0.00</big>
                            <small>元</small>
                        </label>
                    </div>
                    <div class="input-group mt-30 clearFloat">
                        <label for="bankid">
                            <span class="fsp2 floatLeft">银行卡：</span>

                            <c:if test="${not empty customerCard}">
                                <div class="bankblock bankuse use1">
                                    <div class="titlet clearFloat">
                                        <span class="img floatLeft" style="background-image: url('${ctx}/images/banklogo/${customerCard.bankCode}.png')">&nbsp;</span>
                                        <span class="floatRight"></span>
                                    </div>
                                    <p class="tal ml-20">尾号：<big>${customerCard.encryptCardNo}</big></p>
                                    <div class="clearFloat lastl">
                                    </div>
                                </div>
                            </c:if>

                            <c:if test="${empty customerCard}">
                                <div class="bankblock" style="float:left;">
                                    <h5 class="personh2">＊您未进行绑卡，请先进行<a href="${ctx}/bankcard/to_bankcard_list">绑卡</a>再进行提现操作</h5>
                                </div>
                            </c:if>

                        </label>
                        <div class="huanka_tip">
                            <p class="tip_title"><img src="${ctx }/images/news_icon/text_icon.png"/>换卡流程</p>
                            <p>如遇银行卡丢失或需更换银行卡，请出示以下信息：</p>
                            <p>1、手持“身份证以及已绑定银行卡”的清晰照片</p>
                            <p>2、身份证清晰照片</p>
                            <p>3、已绑定银行卡的清晰照片</p>
                            <p>4、如银行卡丢失，需提供银行出示的挂失凭证照</p>
                            <p>请将以上信息发送至财富派客服邮箱：myservice@mayitz.com，</p>
                            <p>我们将在1-3个工作日进行信息审核及解绑操作，解绑前我们会与您</p>
                            <p>电话沟通，请保持手机畅通，感谢您的配合。 详情可拨打客服电话</p>
                            <p>进行咨询：400-061-8080</p>
                        </div>
                    </div>
                    <div class="input-group mt-30">
                        <label for="phone">
                            <span class="fsp2">平台绑定手机号：</span>
                            ${userInfo.encryptMobileNo}
                        </label>
                        <em></em>
                    </div>
                    <div class="input-group">
                        <label for="valid" class="floatLeft">
                            <span class="fsp2 fash-3">验证码：</span>
                            <input type="text" value="" id="valid" name="valid" maxlength="6" class="ipt-input widthvalid"/>
                        </label>
                        <button type="button" id="getvalid" class="btn btn-red mt-0 floatLeft" style="width:90px;">获取验证码</button>
                        <a href="http://help.caifupad.com/guide/common/reg/" target="_black" class="tx-a">收不到验证码？</a>
                        <div class="clearFloat"></div>
                        <em></em>
                    </div>
                    <div class="input-group">
                        <label for="rankm">
                            <span class="fsp2 fash-3">交易密码：</span>
                            <input id="rankm" type="password" onfocus="if(this.value==defaultValue) {this.value='';this.type='password'}" onblur="if(!value) {value=defaultValue; this.type='text';}" name="rankm" class="ipt-input"/>
                        </label>
                        <a href="javascript:;" id="ed_ex_psdad" class="tx-a2">找回交易密码</a>
                        <c:if test="${isBidEqualLoginPass eq 'true'}">
                            <em class="hui" d="bidpass">*交易密码默认与登录密码相同，为保证您的帐号安全，请尽快修改！</em>
                        </c:if>
                        <c:if test="${isBidEqualLoginPass ne 'true'}">
                            <em></em>
                        </c:if>
                    </div>

                    <div class="user-btn-group">
                        <button type="button" id="wdbtn" class="btn btn-error">确认提现</button>
                    </div>
                </div><!-- bankpayd end -->
                <div class="clear_50"></div>
            </div>
        </form>
        <input type="hidden" id="regitxt" data-val="<c:if test="${empty customerCard||times eq doTimes}">1</c:if><c:if test="${not empty customerCard&& times ne doTimes}">2</c:if>"/><!-- 2:不可禁用　1:禁用-->
        <div class="tx_list">
            <div class="p-Right-top">
                <p class="txjl-title">提现记录</p>
            </div>
            <div class="txjl-list">
                <ul class="txjl-ul-big">
                    <li>
                        <ul class="tx-ul">
                            <li>提现时间</li>
                            <li>提现金额(元)</li>
                            <li>提现卡</li>
                            <li>状态</li>
                        </ul>
                    </li>
                </ul>
                <div class="clear_0"></div>
            </div>
            <div class="tcdPageCode mt-20"></div>
        </div>
    </div>

    <div style="text-align:center;"><a id="with_success" style="display: none;" href="javascript:" data-mask='mask' data-id="with">&nbsp</a></div>

    <!-- masklayer start  -->
    <div class="masklayer" id="with">
        <h2 class="clearFloat"><span>提现</span><a id="close_withdraw" href="javascript:" data-id="close"></a></h2>
        <div class="shenf_yanz_main">
            <div style="height:70px;clear:both;"></div>
            <p><img src="${ctx}/images/img/true.jpg"/><span>提现申请已提交！</span></p>
            <div style="height:50px;clear:both;"></div>
            <div class="input_box_shenf">
                <a href="javascript:" data-id="close" id="queren_withdraw">
                    <button>确认</button>
                </a>
                <div style="height:90px;clear:both;"></div>
            </div>
        </div>
    </div>
    <div style="text-align:center;"><a id="with_falure" style="display: none;" href="javascript:" data-mask='mask' data-id="f_with">&nbsp</a></div>

    <!-- masklayer start  -->
    <div class="masklayer" id="f_with">
        <h2 class="clearFloat"><span>提现</span><a id="f_close_withdraw" href="javascript:" data-id="close"></a></h2>
        <div class="shenf_yanz_main">
            <div style="height:70px;clear:both;"></div>
            <p><img src="${ctx}/images/img/false.jpg"/><span>提现失败！<i id="errorMsg"></i></span></p>
            <div style="height:50px;clear:both;"></div>
            <div class="input_box_shenf">
                <a href="javascript:" data-id="close" id="f_queren_withdraw">
                    <button>确认</button>
                </a>
                <div style="height:90px;clear:both;"></div>
            </div>
        </div>
    </div>

</div>
<!-- pRight end -->
</div>
<!-- container end -->

<script type="text/javascript">
    <c:if test="${empty customerCard||times eq doTimes}">
    $("input.ipt-input[type=text]:visible,button.btn").each(function () {
        $(this).attr("disabled", true);
    });
    </c:if>

</script>


<!-- footerindex start -->
<%@include file="../common/footLine3.jsp" %>
<!-- fbottom end -->

<!-- 找回交易密码-开始 -->
<script src="${ctx}/js/ed_ex_psd.js" type="text/javascript"></script>
<div class="zhezhao6"></div>
<div class="masklayer masklback" id="ed_ex_psd">
    <h2 class="clearFloat"><span>找回交易密码</span><a href="javascript:;" data-id="close"></a></h2>
    <div class="xiugai_phone_main" id="xigai1">
        <img src="${ctx}/images/ed_ex_psd1.jpg" class="mt-30 mb-30"/>
        <form action="" class="form" method="post">
            <div class="input_box_phone">
                <label>
                    <span>绑定的手机号码</span>
                    <i class="tal ex-plone">${sessionScope.currentUser.encryptMobileNo}</i>
                </label>
                <em></em>
            </div>
            <div class="input_box_phone">
                <label>
                    <span>手机验证码</span>
                    <input type="text" id="ex_valid" value="" autocomplete="off" maxlength="6" placeholder="请输入验证码" style=""/>
                    <button type="button" id="ex_get_valid" class="huoqu_yanzm">获取验证码</button>
                </label>
                <em></em>
            </div>
            <div class="input_box_phone ipt_box_phone">
                <button type="button" id="next_sub1">下一步</button>
            </div>
        </form>
    </div>
    <div class="xiugai_phone_main display-none" id="phone_d2">
        <img src="${ctx}/images/ed_ex_psd2.jpg" class="mt-30 mb-30"/>
        <form action="" class="form" method="post">
            <div class="input_box_phone input_box ipt-box-ex">
                <label>
                    <span>输入新交易密码</span>
                    <input type="password" class="width200" id="ed_ex_psd1" value="" maxlength="16" style="width:160px;" autocomplete="off" value="" onFocus="if(value==defaultValue){value=''}" onBlur="if(!value){value=defaultValue}" onKeyUp="ed_ex_Check(this.value)"/>

                    <div type="button" id="rejc_ex_psd" class="Tcolor floatLeft">无</div>
                </label>
                <em class="hui fontsize12">交易密码为6 -16 位字符，支持字母及数字,字母区分大小写</em>
            </div>
            <div class="input_box_phone">
                <label>
                    <span>再次输入新交易密码</span>
                    <input type="password" class="width200" value="" onFocus="if(value==defaultValue){value=''}" onBlur="if(!value){value=defaultValue}" id="ed_ex_psd2" value="" maxlength="16" autocomplete="off"/>
                </label>
                <em></em>
            </div>
            <div class="input_box_phone ipt_box_phone">
                <button type="button" id="next_sub2">下一步</button>
            </div>
        </form>
    </div>
    <div class="xiugai_phone_main display-none" id="phone_d3">

        <img src="${ctx}/images/ed_ex_psd3.jpg" class="mt-30 mb-30"/>
        <p class="mt-50"><img src="${ctx}/images/img/true.jpg"/><span>交易密码重置成功！</span></p>

        <div class="input_box_phone ipt_box_phone" style="margin-top:85px;">
            <a href="javascript:;" id="ed_ex_psda">
                <button>确认</button>
            </a>
        </div>
    </div>
</div>

</body>
</html>

