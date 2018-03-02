$(function() {



    // 银行卡号
    $("#cardCode").blur(function () {

        var userVal = $(this).val();
        userVal = $.trim(userVal);
        if (!userVal) {
            $("#j_bank_no").html("请输入银行卡号");
            return;
        }
        if (userVal.length < 16) {
            $("#j_bank_no").html("请输入正确的银行卡号");
            return;
        }
        var checkCard = checkCardInfo(userVal);
        if (!checkCard) {
            return;
        }
        $("#j_bank_no").html("");
        // $("#bank_no").keyup();
        return userVal;
    });

    // 开户行
    $("#cardCode").keyup(function () {
        console.log("--->开始判断绑卡")
        var baneNo = $(this).val();
        if (baneNo.length < 6) {
            $("#j_bank_no").html("");
            $("#bank_name").val("");
        } else if (baneNo.length >= 6) {
            var sixStr = baneNo.substr(0, 6);
            var bankFlag, bankCode;
            for (var i = 0; i < bankCodeArr.length; i++) {
                if (bankCodeArr[i].card_bin.indexOf(sixStr) != -1) {
                    bankFlag = true;
                    bankCode = bankCodeArr[i].bank_code;
                    if(null==bankCodeArr[i].bank_name||""==bankCodeArr[i].bank_name){
                        return;
                    }
                    $("#bankCode1").val(bankCodeArr[i].bank_name);
                    $("#bankCode1").attr("readonly", "readonly");
                    $("#bankCode").val(bankCode);

                    return;
                }else {
                        $("#bankCode1").attr("disabled", "disabled");
                        $("#bankCode").val("");
                    $("#bankCode1").val("");


                }
            }
            // if(!bankFlag){
            // 	$("#j_bank_no").html("对不起，暂不支持您的卡种，请更换银行卡");
            // }
        }
    });

    // 开户城市
    $("#province").click(function () {
        console.log("timber")
        $(".adr_box").toggle();
    });
    for (var i = 0; i < cityAddressArr.length; i++) {
        var newProLi = '<li proCode="' + cityAddressArr[i].code + '">' + cityAddressArr[i].name + '</li>';
        $(".province_list").append(newProLi);
    }
    $(".province_list").find("li").eq(0).addClass("province_current");
    for (var i = 0; i < cityAddressArr[0].area.length; i++) {
        var newCityLi = '<li citycode="' + cityAddressArr[0].area[i].code + '">' + cityAddressArr[0].area[i].name + '</li>';
        $(".city_list").append(newCityLi);
    }
    // $(".city_list").find("li").eq(0).addClass("city_current");
    $(".province_list li").click(function () {
        $(this).addClass("province_current").siblings().removeClass("province_current");
        var proCode = $(this).attr("proCode");
        $(".city_list").empty();
        for (var i = 0; i < cityAddressArr.length; i++) {
            var proCodeJson = cityAddressArr[i].code;
            if (proCode == proCodeJson) {
                for (var j = 0; j < cityAddressArr[i].area.length; j++) {
                    var newCityLi = '<li citycode="' + cityAddressArr[i].area[j].code + '">' + cityAddressArr[i].area[j].name + '</li>';
                    $(".city_list").append(newCityLi);
                }
                break;
            }
        }
    });
    $(".city_list").on("click", "li", function () {
        $(this).addClass("city_current").siblings().removeClass("city_current");
        var cityCode = $(this).attr("citycode");
        var cityVal = $(this).html();
        var proVal = $(".province_list li[class='province_current']").html();
        console.log("you better move");
        $("#province").val(proVal + ' ' + cityVal);
        $("#cityCodeHF").val(cityCode);
        $("#registeredBank").val(proVal + ' ' + cityVal);

        $("#j_bank_adr").html("");
        $(".adr_box").hide();
    });


//卡bin识别
    function checkCardInfo(bankCardNo) {
        var cardFlag;
        var param = {
            bankCardNo: bankCardNo
        }
        $.ajax({
            url: '/webp2p_interface_mysql/account/cardinfo',
            type: 'post',
            data: JSON.stringify(param),
            dataType: 'json',
            async: false,
            cache: false,
            contentType: "application/json;charset=UTF-8",
            success: function (data) {
                console.log(data);
                if (data.result === "0000") {
                    // 成功
                    var businessSupport = data.data.businessSupport;
                    var failReason = data.data.failReason;
                    var bankName = data.data.bankName;
                    var bankCode = data.data.bankCode;
                    if (businessSupport == "0") {
                        cardFlag = true;
                        $("#bank_name").val(bankName);
                        $("#bank_name").attr("bankcode", bankCode);
                    } else {
                        $("#j_bank_no").html(failReason);
                    }
                } else {
                    var errorMsg = data.message;
                }
            },
            error: function (xhr) {
                // console.log(xhr);
                var errorCode = JSON.parse(xhr.responseText).code;
                var errorMsg = JSON.parse(xhr.responseText).message;
                var errorCodeArr = ["FUS_2000", "FUS_2001", "FUS_2002", "FUS_2004", "FUS_2005", "FUS_2006", "FUS_2007", "FUS_2008", "FUS_2009", "FAPP_9999", "FAPP_9997"];
                for (var i = 0; i < errorCodeArr.length; i++) {
                    if (errorCodeArr[i].indexOf(errorCode) != -1) {
                        var timenow = new Date().getTime();
                        window.location.href = "/?_=" + timenow;
                    }
                }
            }
        });
        return cardFlag;
    }


// 身份证基本验证
    function checkIdNo(card) {
        // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
        var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X)$)/;
        if (reg.test(card) === false) {
            // alert("身份证输入不合法");
            return false;
        }
        return true;
    }

});


