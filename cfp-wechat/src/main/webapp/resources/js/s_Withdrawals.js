// JavaScript Document
//require.js调用的主框架
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"main" : ["main"],
		"swal":["lib/sweetalert2"]
	}
})
require(['jquery',"main","swal"],function($,main,swal){

    $(function() {
        var unRealBroadP = false;
        var overscroll = function(el) {//阻止浏览量默认滚动
            el.addEventListener('touchstart', function() {
                var top = el.scrollTop
                    , totalScroll = el.scrollHeight
                    , currentScroll = top + el.offsetHeight
                if(top === 0) {
                    el.scrollTop = 1
                } else if(currentScroll === totalScroll) {
                    el.scrollTop = top - 1
                }
            })
            el.addEventListener('touchmove', function(evt) {
                if(el.offsetHeight < el.scrollHeight)
                    evt._isScroller = true
            })
        }
        overscroll(document.querySelector('.l_NewScroll'));
        document.body.addEventListener('touchmove', function(evt) {
            if(!evt._isScroller) {
                evt.preventDefault()
            }
        })


        /**
         * 提现
         * @param callback
         */
        var timer_flag=false;
        var exceed_val = $("#usemoney").val(),
            flag=$("#voucherCount").val()=="0"?false:true,
            selfHasClass = false;
        var Withdrawals = {
            _init: function (){
                $("#moeny").on('input propertychange',function(){//判断输入框条件
                    var money_val = $(this).val();
                    Withdrawals._JudgmentLen(money_val, exceed_val);
                });
                $("#allOut").on("click",Withdrawals._CounterFee);//使用提现券
                $("#sure_Button").on("click",Withdrawals._verifyPwd);

                $("#pwd_frame").on('input propertychange',function(){//判断输入框条件
                    var html=$("#errorPrompt").html(),html2=$("#errorPrompt").next().html(),moneyVal=$("#Arrival").html();
                    var money_flag = moneyCheck($("#moeny").val(), exceed_val);
                    $("#errorPrompt").html(html);
                    $("#errorPrompt").next().html(html2);
                    $("#Arrival").html(moneyVal);
                    var pwd_flag=pwdCheck($("#pwd_frame").val());
                    if(money_flag&&pwd_flag){
                        $(".sure_Button").addClass("adopt").attr("disabled",false);
                    }else{
                        $(".sure_Button").removeClass("adopt").attr("disabled",true);
                        $("#sure_Button").html("确认提现");
                    }

                });
            },
            _JudgmentLen: function(money_val, exceed_val){
                $("#allOut").removeClass("colose");
                var money_flag = moneyCheck(money_val, exceed_val);
                var pwd_flag=pwdCheck($("#pwd_frame").val());
                if(money_flag&&pwd_flag){
                   $(".sure_Button").addClass("adopt").attr("disabled",false);
                }else{
                   $(".sure_Button").removeClass("adopt").attr("disabled",true);
                   $("#sure_Button").html("确认提现");
                }

            },
            _CounterFee: function(){
                var self = $(this),
                    Arrival = $("#Arrival"),
                    surplusAmount = $("#surplusAmount").val(),
                    money_val = $("#moeny").val(),
                    errorPrompt = $("#errorPrompt");
                surplusAmount=parseFloat(surplusAmount);
                if(flag){
                    if($("#allOut").hasClass("maskUse")){
                        if(selfHasClass){
                            $("#CounteFee").removeClass("CounteFee");
                            self.html("使用提现券").removeClass("colose");
                            Arrival.html(""+(money_val-3)+"");
                            selfHasClass = false;
                        }else{
                            $("#CounteFee").addClass("CounteFee");
                            self.html("取消").addClass("colose");
                            Arrival.html(""+(money_val)+"");
                            selfHasClass = true;
                        }
                    }else{
                        $("#moeny").val(surplusAmount);
                        self.html("使用提现券").addClass("maskUse").removeClass("colose");
                        Arrival.html(surplusAmount-3);
                        errorPrompt.html("需扣除￥<span id='CounteFee'>3.00</span>手续费").removeClass("prompt");
                    }
                }else{
                    $("#moeny").val(surplusAmount);
                    self.html("").addClass("maskUse").removeClass("colose");
                    Arrival.html(surplusAmount-3);
                }
            }
            ,_verifyPwd: function(){
                var pwd_val = $("#pwd_frame").val();

                if (timer_flag){
                    $(".l_topInfo").animate({top:0},10);
                    $(".l_btmInfo").animate({bottom:0},10);
                    $(".mask").show();
                    $(".l_broadBG").animate({bottom:0},600);
                    unRealBroad.touchMe();
                    return false;
                }


                if(pwd_val == ""){
                    swal('交易密码不能为空','','warning');
                }else{
                    var money=$("#moeny").val();
                    var ktXmoney = $("#usemoney").val();
                    if(money != ""&&ktXmoney != ""){
                        if(!/^[0-9]+\.?[0-9]*$/.test(money)){
                            swal('请输入正确的提现金额','','warning');
                            return;
                        }
                    }
                    if(money == ""){
                        swal('提现金额不能为空','','warning');
                        return;
                    }
                    money=parseFloat(money);
                    ktXmoney=parseFloat(ktXmoney);
                    if(money == ""){
                        swal('提现金额不能为空','','warning');
                        return;
                    }else if(money < 100){
                        swal('提现金额最少100元','','warning');
                        return;
                    }else if(money > ktXmoney){
                        swal('提现金额不能大于可提现金额','','warning');
                        return;
                    }
                    $("#sure_Button").html("校验中");
                    setTimeout(function () {
                        $.ajax({
                            url:rootPath+"/person/withDraw/validate",
                            type:"post",
                            data:{"amount":money,"validCode":"111111","bidPass":pwd_val,"flag":true},
                            success:function(data){
                                var bidpass =  eval("("+data.bidpass+")");
                                if(!bidpass.isSuccess){
                                    $("#sure_Button").html(bidpass.info);
                                    return;
                                }
                                var amount =  eval("("+data.amount+")");
                                if(!amount.isSuccess){
                                    $("#sure_Button").html(amount.info);
                                    return;
                                }
                                var times =  eval("("+data.times+")");
                                if(!times.isSuccess){
                                    $("#sure_Button").html(times.info);
                                    return;
                                }

                                $("#sure_Button").html("确认提现");
                                window.setTimeout(unRealBroad.showHide(),300);
                                $(this).addClass("adopt");
                                $.ajax({
                                    url:rootPath+"/person/withDraw/getMsg",
                                    type:"post",
                                    success:function(data){
                                        timer_flag=true;
                                    }
                                });

                            },
                            error:function(XMLHttpRequest){
                                if(XMLHttpRequest.readyState==0){
                                    $("#sure_Button").html("网络异常");
                                }else{
                                    $("#sure_Button").html("未知错误");
                                }
                            }
                        });
                    },2000);
                }
            }
        }
        Withdrawals._init();

        function pwdCheck(pwd){
            if(pwd =="" || pwd.length<6 || pwd.length>18){
                if(pwd ==""){
                    $("#pwd_frame").val("");
                }
                return false;
            }else{
                return true;
            }

        }

        function moneyCheck(money_val, exceed_val){
            var	errorPrompt = $("#errorPrompt"),
                allOut = $("#allOut"),
                surplusAmount = $("#surplusAmount").val(),
                Arrival = $("#Arrival");
            surplusAmount=parseFloat(surplusAmount);
            if(money_val != ""&&exceed_val != ""){
                if(!/^[0-9]+(\.[0-9]*)?$/.test(money_val.trim())){
                    errorPrompt.html("请输入正确的提现金额").removeClass("prompt");
                    $(".sure_Button").removeClass("adopt").attr("disabled",true);
                    return false;
                }
            }
            if(money_val == ""){
                errorPrompt.html("单笔最大可提￥"+ surplusAmount +"元").removeClass("prompt");
                allOut.show();
                Arrival.html("0");
                $("#allOut").html("全部提现").removeClass("maskUse");
                $(".sure_Button").removeClass("adopt").attr("disabled",true);
                $("#moeny").val("");
                return false;
            }
            money_val=parseFloat(money_val);
            exceed_val=parseFloat(exceed_val);
            if(money_val < 100){
                errorPrompt.html("提现金额不得小于100元").addClass("prompt");
                allOut.hide();
                Arrival.html("0");
                $(".sure_Button").removeClass("adopt").attr("disabled",true);
                return false;
            }
            if(money_val>500000){
                errorPrompt.html("提现金额不得大于50万元").addClass("prompt");
                allOut.hide();
                Arrival.html("0");
                $(".sure_Button").removeClass("adopt").attr("disabled",true);
                return false;
            }
            if(money_val>surplusAmount){
                errorPrompt.html("提现金额不得大于单笔可提金额").addClass("prompt");
                allOut.hide();
                Arrival.html("0");
                $(".sure_Button").removeClass("adopt").attr("disabled",true);
                return false;
            }
            if(money_val > exceed_val){
                errorPrompt.html("提现金额不得大于当前账户余额(￥"+ exceed_val +"元)").addClass("prompt");
                allOut.hide();
                Arrival.html("0");
                $(".sure_Button").removeClass("adopt").attr("disabled",true);
                return false;
            }
            if(money_val >= 100 && money_val <= exceed_val){
                errorPrompt.html("需扣除￥<span id='CounteFee'>3.00</span>手续费").removeClass("prompt");
                allOut.show();
                if(flag){
                    allOut.html("使用提现券").addClass("maskUse");
                }else{
                    allOut.html("").addClass("maskUse");
                }
                Arrival.html(""+(money_val-3)+"");
                return true;
            }
        }

        /*
         模拟键盘
         */
        // 提现虚拟键盘功能
        var UnRealBroad = function(telNum) {
            // 插入DOM结构
            this.addBroad = function (telNum) {
                var mask = $("<div>").addClass("mask").appendTo($("body")),
                    broadBG = $("<section>").addClass("l_broadBG").appendTo(mask),
                    topInfo = $("<ul>").addClass("l_topInfo").html("<li class='l_close'></li><li>请输入验证码</li><li>已发送至："+telNum+"</li><li class='l_checkCode'><input type='hidden' id='checkCode' value=''/><span></span><span></span><span></span><span></span><span></span><span></span><p class='pjred' id='errTip'>验证码错误，请重新输入</p><button class='l_checkCodeBtn'>重新发送</button></li>").appendTo(broadBG),
                    btmInfo = $("<ul>").addClass("l_btmInfo").html("<li data-val='1'>1</li><li data-val='2'>2</li><li data-val='3'>3</li><li data-val='4'>4</li><li data-val='5'>5</li><li data-val='6'>6</li><li data-val='7'>7</li><li data-val='8'>8</li><li data-val='9'>9</li><li data-val=''></li><li data-val='0'>0</li><li data-val='del'></li>").appendTo(broadBG);
            };
            // 绑定touch事件
            this.touchMe = function(){
                touchBind();
            };
            this.countDown_fun = function(){
                $(".l_checkCodeBtn").on("click",function (e) {
                    e.stopPropagation();
                    e.preventDefault();
                    countDown(60,$(this));
                    $.ajax({
                        url:rootPath+"/person/withDraw/getMsg",
                        type:"post",
                        success:function(data){

                        }
                    });
                })
            };
            // 显示隐藏键盘方法
            this.showHide = function () {
                $(".mask").show();
                $(".l_broadBG").animate({bottom:0},600);
                countDown(60,$(".l_checkCodeBtn"));
                $(".l_close").on("touchend",function(e) {
                    e.stopPropagation();
                    e.preventDefault();
                    clearBroad();
                    $(".mask").hide();
                    $(".l_broadBG").animate({bottom:-32+"rem"},600);
                    $(".l_btmInfo li").off("touchend").off("touchstart");
                    $(".l_btmInfo li").removeClass("l_touchShadow");

                })
            };
            // 结果返回方法
            function checkResult(result) {
                if (result) {
                    $(".mask").hide();
                    $(".l_broadBG").animate({bottom:-32+"rem"},600);
                }else{
                    $("#errTip").show();
                    $(".l_broadBG").animate({bottom:0},600);
                    $(".l_topInfo").animate({top:0},1000);
                    $(".l_btmInfo").animate({bottom:0},1000);
                }
            }
            // 初始化时调用
            this.addBroad(telNum);
            this.touchMe();
            this.countDown_fun();
            // 清除键盘方法
            function clearBroad() {
                $(".mask").hide();
                $(".l_broadBG").animate({bottom:-32+"rem"},600);
                $("#checkCode").val("");
                $(".l_topInfo").animate({top:0},1000);
                $(".l_btmInfo").animate({bottom:0},1000);
                $(".l_checkCode span").text("");

            }

            // 绑定touch事件私有方法
            function touchBind() {
                $(".l_btmInfo li").on("touchstart",function(e){
                    e.stopPropagation();
                    e.preventDefault();
                    $("#errTip").hide();
                    if ($(this).attr("data-val")!='') {
                        $(this).toggleClass("l_touchShadow");
                    }
                }).on("touchend",function (e) {
                    e.stopPropagation();
                    e.preventDefault();

                    if ($(this).attr("data-val")!='') {
                        $(this).toggleClass("l_touchShadow");
                        if ($(this).attr("data-val")!='del'&&$("#checkCode").val().length<=6) {
                            $("#checkCode").val($("#checkCode").val().toString()+$(this).attr("data-val").toString());
                        }else if($(this).attr("data-val")=='del') {
                            $("#checkCode").val($("#checkCode").val().substring(0,$("#checkCode").val().length-1));
                        }
                        var iptarr =$("#checkCode").val().split('');
                        $(".l_checkCode span").each(function (index) {
                            $(this).text("");
                            $(this).text(iptarr[index]);
                            if ($(".l_checkCode span").last().text() != "") {
                                $(".l_btmInfo li").off("touchend").off("touchstart");
                                $(".l_topInfo").animate({top:-50+"%"},1000);
                                $(".l_btmInfo").animate({bottom:-50+"%"},1000);

                                var	tXmoney = $("#moeny").val(),
                                    tXcode = $("#checkCode").val(),
                                    tXpwd = $("#pwd_frame").val();

                                $.ajax({
                                    url:rootPath+"/person/withDraw",
                                    type:"post",
                                    data:{"moneyp":tXmoney,"valid":tXcode,"rankm":tXpwd,"voucher":$("#allOut").hasClass("colose")},
                                    success:function(data){
                                        if(typeof(data)=="string"){
                                            if(data.indexOf('success')>=0){
                                                //提现成功
                                                var withdrawId=data.split(",")[1];
                                                window.location.href = rootPath+"/person/withDrawResult?withdrawId="+withdrawId;
                                            }else if(data=='error'){
                                                swal('提现失败','','warning');
                                            }
                                        }else{
                                            var valid =  eval("("+data.valid+")");
                                            if(!valid.isSuccess){
                                                checkResult(false);
                                            }
                                            var bidpass =  eval("("+data.bidpass+")");
                                            if(!bidpass.isSuccess){
                                                clearBroad();
                                                swal(bidpass.info,'','warning');
                                            }
                                            var amount =  eval("("+data.amount+")");
                                            if(!amount.isSuccess){
                                                clearBroad();
                                                swal(amount.info,'','warning');
                                            }
                                            var times =  eval("("+data.times+")");
                                            if(!times.isSuccess){
                                                clearBroad();
                                                swal(times.info,'','warning');
                                            }
                                        }
                                    }
                                });
                            }
                        })
                    }
                });

            };
            // 倒计时私有方法
            function countDown(num,el) {
                num-=1;
                if (num == 0) {
                    el.text("重新发送");
                    el.removeAttr('disabled');
                    el.removeClass("l_disable");
                }else{
                    el.attr("disabled","");
                    el.addClass("l_disable");
                    el.text(num + "s后重新发送");
                    setTimeout(function(){countDown(num--,el)},1000);
                }
            }

        }
        var unRealBroad = new UnRealBroad($("#mobileNo_star").val());
    });
})
