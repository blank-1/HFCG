// JavaScript Document
//require.js调用的主框架
require.config({
    paths: {
        "jquery": ["lib/jquery-1.11.0.min"],
        "main": ["main"],
        "swal": ["lib/sweetalert2"]
    }
})
require(['jquery', 'main', 'swal'], function ($, main, swal) {
    $(function () {
        var overscroll = function (el) {//阻止浏览量默认滚动
            el.addEventListener('touchstart', function () {
                var top = el.scrollTop
                    , totalScroll = el.scrollHeight
                    , currentScroll = top + el.offsetHeight
                if (top === 0) {
                    el.scrollTop = 1
                } else if (currentScroll === totalScroll) {
                    el.scrollTop = top - 1
                }
            })
            el.addEventListener('touchmove', function (evt) {
                if (el.offsetHeight < el.scrollHeight)
                    evt._isScroller = true
            })
        }
        overscroll(document.querySelector('.l_NewScroll'));
        document.body.addEventListener('touchmove', function (evt) {
            if (!evt._isScroller) {
                evt.preventDefault()
            }
        })

        $(".couponList").on("touchmove", function (e) {
            e.stopPropagation();
        })
        var realPay = parseFloat($("#pay").html().replace(/,/g, ""));
        var accountBalance = parseFloat($("#AccountBalance").html().replace(/,/g, ""));
        if (realPay <= accountBalance) {
            $("#surplusMoney").text(0);
            $("#accountPayValue").val(realPay);
            $(".l_Show").hide();
            $(".Agreement").hide();
        } else {
            $("#surplusMoney").text((realPay - accountBalance).toFixed(2));
            $("#rechargePayValue").val((realPay - accountBalance).toFixed(2));
            $("#accountPayValue").val(accountBalance);
            $(".l_Show").show();
            $(".Agreement").show();
        }

    })

    //银行卡校验
    $("#cardNo").on("blur", function () {
        $("#loading").show();
        setTimeout(function () {
            var cardNo = $("#cardNo").val();
            if (cardNo.trim() == "") {
                swal('请您输入银行卡号！', '', 'warning');
                $("#loading").hide();
                return;
            }
            if (cardNo.replace(/\s/g, "").length <= 19 && cardNo.replace(/\s/g, "").length >= 15) {
                $.ajax({
                    url: rootPath + '/bankcard/check_card',
                    type: "post",
                    data: {
                        cardNo: cardNo,
                    },
                    success: function (data) {
                        $("#loading").hide();
                        if (data.result == 'success') {
                            $("#cardNoVal").val(cardNo);
                        } else if (data.result == 'error') {
                            swal(data.errMsg, '', 'warning');
                        } else {
                            swal("网络异常，请稍后操作！", '', 'warning');
                        }

                    }
                });
            } else {
                swal('银行卡号格式错误！', '', 'warning');
                $("#loading").hide();
            }
        }, 2000);

    })

    //隐藏显示密码切换
    var flag = true;
    $("#eye").on("click", function () {
        if (flag) {
            $("#psw").prop("type", "text");
            $(this).removeClass("eye-close");
            flag = false;
        } else {
            $("#psw").prop("type", "password");
            $(this).addClass("eye-close");
            flag = true;
        }
    })
    //判断密码是否正确
    $("#confirm").on("click", function () {
        var pwdVal = $("#psw").val();
        if (pwdVal == "") {
            swal('密码不能为空', '', 'warning');
        }
    })

    //限制密码长度
    $("#psw").on("input", function () {
        main.limitNUM(16);
    });
    // .on("focus",function(){
    //     var agent = navigator.userAgent.toLowerCase();
    //     if (/(android)/i.test(agent)){
    //         $("body").addClass("l_topForAndroid");
    //     }
    // }).on("blur",function(){
    //         $("body").removeClass("l_topForAndroid");
    // })

    $("#btn2").on("click", function (e) {
        e.stopPropagation();
        e.preventDefault();
        var b1 = passwordf($("#psw"), "1");
        if (b1 != "") {
            return false;
        }
        document.getElementById("btn2").disabled = true;
        var _true = true;
        var flag = $("#sxFlag").val();
        if (flag == null || flag == undefined || flag == "") {
            payForNormal(b1);
        } else {
            payForSX(b1);
        }

    });

    //判断交易密码
    function passwordf(passval1, pa) {
        if (passval1.val() == "") {
            if (pa == "0") {
                massage = "";
            } else {
                massage = "请您输入交易密码！";
                swal('请您输入交易密码！', '', 'warning');
            }
        } else {
            if (passval1.val().length >= 6 && passval1.val().length <= 16 && /^[0-9a-zA-Z]+$/.test(passval1.val())) {
                massage = "";
            } else {
                massage = "交易密码格式错误";
                swal('交易密码格式错误', '', 'warning');
            }

        }
        return massage;
    }

    function payForNormal(b1) {
        //开始判断
        if (accipt_ched()) {
            //有银行卡 支付
            if (b1 == "") {
                $("#loading").show();
                $.ajax({
                    url: rootPath + "/recharge/pay",
                    type: "post",
                    data: $("#f1").serialize(),
                    dataType: 'json',
                    error: function (data) {
                        var data = data.responseText;
                        var _data = eval('(' + data + ')');
                        $("#loading").hide();
                        swal(_data.errorMsg, '', 'warning');
                    },
                    success: function (data) {
                        $("#loading").hide();
                        if (data.isSuccess) {
                            var dataSource = data.dataSource;
                            $('#recharge_params').attr('action', dataSource.actionUrl);
                            $("[name='mchnt_cd']", '#recharge_params').val(dataSource.mchnt_cd);
                            $("[name='mchnt_txn_ssn']", '#recharge_params').val(dataSource.mchnt_txn_ssn);
                            $("[name='login_id']", '#recharge_params').val(dataSource.login_id);
                            $("[name='amt']", '#recharge_params').val(dataSource.amt);
                            $("[name='page_notify_url']", '#recharge_params').val(dataSource.page_notify_url);
                            $("[name='back_notify_url']", '#recharge_params').val(dataSource.back_notify_url);
                            $("[name='signature']", '#recharge_params').val(dataSource.signature);
                            $('#recharge_params').submit();
                        } else {
                            swal(data.info, '', 'warning');
                        }
                    }
                });

            }
        } else {
            $("#loading").show();
            //无银行卡，账户余额
            if (b1 == "") {
                $.ajax({//校验交易密码
                    url: rootPath + "/finance/checkBidLoanByAccountBalance",
                    type: "post",
                    data: {"bidPass": $("#psw").val()},
                    error: function (data) {
                        $("#loading").hide();
                        var data = data.responseText;
                        var _data = eval('(' + data + ')');
                        swal(_data.errorMsg, '', 'warning');
                    },
                    success: function (data) {
                        $("#loading").hide();
                        var _data = eval("(" + data + ")");
                        if (!_data.isSuccess) {
                            result = false;
                            swal(_data.info, '', 'warning');
                        } else {
                            //余额支付
                            if ($("#productType").val() == '1') {
                                $.ajax({
                                    url: rootPath + "/finance/bidLoanByAccountBalance",
                                    type: "post",
                                    data: $("#f1").serialize(),
                                    error: function (data) {
                                        $("#loading").hide();
                                        var data = data.responseText;
                                        var _data = eval('(' + data + ')');
                                        swal(_data.errorMsg, '', 'warning');
                                    },
                                    success: function (data) {
                                        $("#loading").hide();
                                        var _data = eval("(" + data + ")");
                                        if (_data.isSuccess) {
                                            location.href = rootPath + "/lendOrder/paySuccess?orderId=" + _data.info;
                                        } else {
                                            swal(_data.info, '', 'warning');
                                        }
                                    }
                                });
                            }
                            if ($("#productType").val() == '2') {
                                $.ajax({
                                    url: rootPath + "/finance/buyRightsByAccountBalance",
                                    type: "post",
                                    data: $("#f1").serialize(),
                                    error: function (data) {
                                        $("#loading").hide();
                                        var data = data.responseText;
                                        var _data = eval('(' + data + ')');
                                        swal(_data.errorMsg, '', 'warning');
                                    },
                                    success: function (data) {
                                        $("#loading").hide();
                                        var _data = eval("(" + data + ")");
                                        if (_data.isSuccess) {
                                            location.href = rootPath + "/lendOrder/paySuccess?orderId=" + _data.info;
                                        } else {
                                            swal(_data.info, '', 'warning');
                                        }
                                    }
                                });
                            } else {
                                $.ajax({
                                    url: rootPath + "/finance/buyFinanceByAccountAmount",
                                    type: "post",
                                    data: $("#f1").serialize(),
                                    error: function (data) {
                                        $("#loading").hide();
                                        var data = data.responseText;
                                        var _data = eval('(' + data + ')');
                                        swal(_data.errorMsg, '', 'warning');
                                    },
                                    success: function (data) {
                                        $("#loading").hide();
                                        var _data = eval("(" + data + ")");
                                        if (_data.isSuccess) {
                                            location.href = rootPath + "/lendOrder/paySuccess?orderId=" + _data.info;
                                        } else {
                                            swal(_data.info, '', 'warning');
                                        }
                                    }
                                });
                            }

                        }
                    }
                });
            }
        }
    }

    function payForSX(b1) {
        var lendOrderId = $("#lendOrderId").val();

        //省心计划收益分配方式(开始)
        $.ajax({
            url: rootPath + "/finance/profitReturnConfig",
            type: "post",
            data: {
                "lendOrderId": lendOrderId,
                "profitReturnConfig": $("#syfp").val()
            },
            error: function (data, e, a) {
                swal("交易失败", '', 'warning');
            },
            success: function (data) {
                var _data = eval("(" + data + ")");
                if (!_data.isSuccess) {
                    swal(_data.info, '', 'warning');
                } else {
                    if (accipt_ched()) {
                        $("#loading").show();
                        $.ajax({
                            url: rootPath + "/recharge/llpay",
                            type: "post",
                            data: $("#f1").serialize(),
                            error: function (data) {
                                var data = data.responseText;
                                var _data = eval('(' + data + ')');
                                swal(_data.errorMsg, '', 'warning');
                            },
                            success: function (data) {
                                var _data = eval("(" + data + ")");
                                if (!_data.isSuccess) {
                                    if (_data.id == 'redirect') {
                                        //跳转值错误页面
                                        $("#loading").hide();
                                        swal(_data.info, '', 'warning');
                                    } else {
                                        //校验错误
                                        if (typeof(_data.tocken) != "undefined") {
                                            $("#token").val(_data.tocken);
                                        }
                                        $("#loading").hide();
                                        swal(_data.info, '', 'warning');
                                    }
                                } else {
                                    $("#req_data").val(_data.info);
                                    /*var cardFlag=$("#cardNo").val();
                                    if(cardFlag==null||cardFlag==undefined){
                                        $("#llHasCardForm").submit();
                                    }else{
                                        $("#llNoCardForm").submit();
                                    }*/
                                    $("#llHasCardForm").submit();
                                }
                            }
                        });
                    } else {
                        if (b1 == "") {
                            $.ajax({
                                url: rootPath + "/finance/checkBidLoanByAccountBalance",
                                type: "post",
                                data: {"bidPass": $("#psw").val()},
                                error: function (data) {
                                    $("#loading").hide();
                                    var data = data.responseText;
                                    var _data = eval('(' + data + ')');
                                    swal(_data.errorMsg, '', 'warning');
                                },
                                success: function (data) {
                                    $("#loading").hide();
                                    var _data = eval("(" + data + ")");
                                    if (!_data.isSuccess) {
                                        result = false;
                                        swal(_data.info, '', 'warning');
                                    } else {
                                        $("#loading").show();
                                        $("#productType").val(0);
                                        $("#f1").attr("action", rootPath + "/finance/buySxByAccountAmount").submit();
                                    }
                                }
                            })
                        }
                    }
                }
            }
        });
        //省心计划收益分配方式(结束)
    }

    //判断是否需要判断验证码手机号银行卡
    function accipt_ched() {
        if (rmoney($("#surplusMoney").html()) == 0) {
            return false;
        } else {
            return true;
        }

    }

    function showVouch() {
        var spans1 = $(".couponList").find("div").filter(".cfqyxBj");
        var spans2 = $(".couponList").find("div").filter(".jxqyxBj");
        if (spans1.length + spans2.length > 0) {
            var sum = 0;
            var rate = 0;
            var vids = "";
            var rids = "";
            for (var i = 0; i < spans1.length; i++) {
                var s = spans1[i];
                var sv = $(s).attr("data-val");
                var id = $(s).attr("data-id");
                sum += parseInt(sv);
                if (i == spans1.length - 1) {
                    vids += id;
                } else {
                    vids += (id + ",");
                }
            }
            for (var i = 0; i < spans2.length; i++) {
                var s = spans2[i];
                var sv = $(s).attr("data-val");
                var id = $(s).attr("data-id");
                rate = sv;
                rids = id;
            }
            if (rate != 0) {
                rate = "加息券" + rate + "%";
            } else {
                rate = "";
            }
            var account = $("#amount").val();
            if (spans1.length > 0) {
                if (sum > account) {
                    for (var i = 0; i < spans1.length; i++) {
                        $(spans1[i]).removeClass("cfqyxBj");
                    }
                    var num = $("#voucherNum").val();
                    $("#voucherInfo").html(num + "张");
                    return false;
                }
            }
            var sumstr = "";
            if (sum > 0) {
                sumstr = "财富券" + sum + "元";
            }
            var content = sumstr + rate;
            $("#voucherInfo").html(content);
            $("#userVoucherId").val(vids);
            $("#rateUserIds").val(rids);
            account = parseFloat(account.replace(/,/g, ""));
            var accountBalance = $("#AccountBalance").html();
            accountBalance = parseFloat(accountBalance.replace(/,/g, ""));
            if (rmoney($("#surplusMoney").html()) != 0) {
                var surplusMoney = (account - sum - accountBalance).toFixed(2);
                if (surplusMoney >= 0) {
                    $("#surplusMoney").html(surplusMoney);
                    $("#accountPayValue").val(accountBalance.toFixed(2));
                    $("#rechargePayValue").val((account - sum - accountBalance).toFixed(2));
                } else {
                    $("#surplusMoney").html("0");
                    $("#accountPayValue").val((account - sum).toFixed(2));
                    $("#rechargePayValue").val("0");
                }
            } else {
                $("#accountPayValue").val((account - sum).toFixed(2));
                $("#rechargePayValue").val((account - sum - accountBalance).toFixed(2));
            }
        } else {
            var num = $("#voucherNum").val();
            $("#voucherInfo").html(num + "张");
            var account = $("#amount").val();
            account = parseFloat(account.replace(/,/g, ""));
            var accountBalance = $("#AccountBalance").html();
            accountBalance = parseFloat(accountBalance.replace(/,/g, ""));
            if (rmoney($("#surplusMoney").html()) != 0) {
                $("#surplusMoney").html((account - accountBalance).toFixed(2));
                $("#rechargePayValue").val((account - accountBalance).toFixed(2));
            }
            $("#userVoucherId").val("");
            $("#rateUserIds").val("");
        }
    }

    $(".couponBox").on("click", function () {
        var type = $(this).attr("data-type");
        var dil = $(this).attr("data-il");
        var did = $(this).attr("data-id");
        var vouchers = $(".couponList").find("div").filter(".couponBox");
        for (var i = 0; i < vouchers.length; i++) {
            var s = vouchers[i];
            var st = $(s).attr("data-type");
            var si = $(s).attr("data-il");
            var sd = $(s).attr("data-id");
            if (type == 1) {//财富券
                if (sd != did) {
                    if (st == 2) {
                        if (si == 0) {//当前的券不能叠加
                            $(s).removeClass("jxqyxBj");
                        }
                    } else {
                        if (dil == 0) {//选中的券不能叠加
                            $(s).removeClass("cfqyxBj");
                        } else {
                            if (si == 0) {//当前的券不能叠加
                                $(s).removeClass("cfqyxBj");
                            }
                        }
                    }
                }
            } else {//加息券
                if (sd != did) {
                    if (st == 2) {
                        $(s).removeClass("jxqyxBj");
                    } else {
                        if (dil == 0) {
                            $(s).removeClass("cfqyxBj");
                        }
                    }
                }
            }
        }
        if (type == 1) {
            $(this).toggleClass("cfqyxBj");
        } else {
            $(this).toggleClass("jxqyxBj");
        }
    })

    $("#voucherInfo").on("click", function (e) {
        $(".couponList").show();
        $(".w_close").show();
    })

    $(".w_close").on("click", function (e) {
        $(".couponList").hide();
        $(".w_close").hide();
        showVouch();
    })

    $(".couponList").on("touchstart", function (e) {
        e.stopPropagation();
    })
})