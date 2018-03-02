// JavaScript Document
//require.js调用的主框架
require.config({
    paths: {
        "jquery": ["lib/jquery-1.11.0.min"],
        "swal": ["lib/sweetalert2"],
        "bankCityAddress": ["newjs/bankCityAddress"]
    },
    shim: {
        'bankCityAddress': {
            exports: 'bankCityAddress'
        }
    }
})
require(['jquery', 'swal', 'bankCityAddress'], function ($, swal, bankCityAddress) {
    $(function () {
        if(!!$('[name=cust_nm]').val()){
            $('[name=cust_nm]').attr('readOnly',true);
        }
        if(!!$('[name=certif_id]').val()){
            $('[name=certif_id]').attr('readOnly',true);
        }


        let overscroll = function (el) {//阻止浏览量默认滚动
            el.addEventListener('touchstart', function () {
                let top = el.scrollTop
                    , totalScroll = el.scrollHeight
                    , currentScroll = top + el.offsetHeight
                //If we're at the top or the bottom of the containers
                //scroll, push up or down one pixel.
                //
                //this prevents the scroll from "passing through" to
                //the body.
                if (top === 0) {
                    el.scrollTop = 1
                } else if (currentScroll === totalScroll) {
                    el.scrollTop = top - 1
                }
            })
            el.addEventListener('touchmove', function (evt) {
                //if the content is actually scrollable, i.e. the content is long enough
                //that scrolling can occur
                if (el.offsetHeight < el.scrollHeight)
                    evt._isScroller = true
            })
        }
        overscroll(document.querySelector('.l_NewScroll'));
        document.body.addEventListener('touchmove', function (evt) {
            //In this case, the default behavior is scrolling the body, which
            //would result in an overflow.  Since we don't want that, we preventDefault.
            if (!evt._isScroller) {
                evt.preventDefault()
            }
        })

        var nameState = false,
            idState = false,
            bankNameState = false,
            carState = false,
            addressState = false,
            BranchState = false,
            BranchNameState = false;

        let Verification = {
            _init() {//自定义函数
                //支行搜索
                // $("#Branch").on("focus", function () {
                //     $("#Branch-list").slideDown(300);
                // })
                $("#Branch").on("blur", function () {
                    $("#Branch-list").slideUp(300);
                })
                //选择省市
                $("#addressBox").on("click", function (evet) {
                    evet.stopPropagation();
                    $("#select-mask").slideDown(300);
                    $(".city").addClass("cur").siblings().removeClass("cur");
                    $("#cityBox").animate({
                        "marginLeft": 0
                    });
                })
                /*  $(document).on("click", function () {
                      $("#select-mask").slideUp(300);
                  })*/
                $("#citylisting").on("click", "li", function () {//选择省
                    flushCityData($(this).attr('_code'));
                    let cityVal = $(this).html(),
                        cityBoxW = $("#cityBox ul:first-child").width();
                    $("#cityBox").animate({
                        "marginLeft": -(cityBoxW) + 'px'
                    });
                    $(".area").addClass("cur").siblings().removeClass("cur");
                })
                $("#areaListing").on("click", "li", function () {//选择市区
                    let areaVal = $(this).html();
                    $("#address").html(areaVal).css("color","#000");
                    $('#city_id').val($(this).attr('_code'));
                    $("#select-mask").slideUp(300);
                })
                $("#Branch-list").on("click", "li", function (evet) {//获取支行信息
                    evet.stopPropagation();
                    let thisVal = $(this).html();
                    $("#Branch").val(thisVal);
                })
                $("#Selected").on("click", "span", function (evet) {//城市tab切换
                    evet.stopPropagation();
                    $(this).addClass("cur").siblings().removeClass('cur');
                    let thisInd = $(this).index(),
                        cityBoxW = $("#cityBox ul:first-child").width();
                    $("#cityBox").animate({
                        "margin-left": -(thisInd) * (cityBoxW) + 'px'
                    }, 300)
                })


                $("#nameInput").on("input propertychange", Verification.checkoutName)//校验名字
                $("#idInput").on("input propertychange", Verification.checkoutId);//校验身份证号
                $("#bankInput").on("input propertychange", Verification.checkoutBank);//校验银行名称
                $("#bankCar").on("input propertychange", Verification.checkoutCar);//校验银行卡
                $("#address").on("input propertychange", Verification.checkoutAddress);//校验地址
                $("#Branch").on("input propertychange", Verification.checkoutBranch);//校验支行
                $("#BranchName").on("input propertychange", Verification.checkoutBranchName);//校验支行名称
                $("#NextStep").on("click", Verification.checkout);//检测是不是全部通过
                $("#nameInput,#idInput,#bankInput,#bankCar,#address,#Branch,#BranchName,#NextStep").trigger("propertychange");
            },
            checkoutName() {//校验名字
                if($(this).val()){
                    nameState = true;
                }
            },
            checkoutId() {//校验身份证号
                if($(this).val()) {
                    idState = true;
                }
            },
            checkoutBank() {//校验银行名称
                if($(this).val()) {
                    bankNameState = true;
                }
            },
            checkoutCar() {//校验银行卡
                if($(this).val()) {
                    carState = true;
                }
            },
            checkoutAddress() {//校验地址
                if($(this).html() !== "") {
                    addressState = true;
                }
            },
            checkoutBranch() {//校验支行
                if($(this).val()) {
                    BranchState = true;
                }
            },
            checkoutBranchName() {//校验支行名称
                if($(this).val()) {
                    BranchNameState = true;
                }
            },
            checkout() {//校验以上全部释放通过
                //  ||!BranchNameState
                if($('#bankInput').val() && $('#bankInput_code').val()){
                    bankNameState = true;
                }
                if($('#address').val() && $('#city_id').val()){
                    addressState = true;
                }
                if(!nameState ){
                    swal(
                        '请输入用户名称',
                        '',
                        'warning'
                    );
                    return ;
                }
                if (!idState ) {
                    swal(
                        '请输入身份证号码',
                        '',
                        'warning'
                    )
                    return ;
                }
                if(!carState){
                    swal(
                        '请输入银行卡信息',
                        '',
                        'warning'
                    );
                    return ;
                }
                if(!bankNameState){
                    swal(
                        '暂不支持此银行卡',
                        '',
                        'warning'
                    );
                    return ;
                }
                if(!BranchState){
                    swal(
                        '请完善支行信息',
                        '',
                        'warning'
                    );
                    return ;
                }
                if(!addressState){
                    swal(
                        '请完善开户地区信息',
                        '',
                        'warning'
                    );
                    return ;
                }

                if(!$('#password').val() ){
                    swal(
                        '请输入支付密码',
                        '',
                        'warning'
                    );
                    return ;
                }
                if($('#password').val() != $('#password2').val()){
                    swal(
                        '支付密码不一至',
                        '',
                        'warning'
                    );
                    return ;
                }

                if(!$('#lpassword').val() ){
                    swal(
                        '请输入登陆密码',
                        '',
                        'warning'
                    );
                    return ;
                }
                if($('#lpassword').val() != $('#lpassword2').val()){
                    swal(
                        '登陆密码不一至',
                        '',
                        'warning'
                    );
                    return ;
                }
                {//全部通过跳转
                    $.ajax({
                        url:'/person/identityAuthentication'
                        ,data:$('#FM_save').serialize()
                        ,dataType:'json'
                        ,type:'post'
                    }).done(function (o){
                        if(o.success){
                            var fromUrl = $('meta[name="from-url"]').attr('content');
                            if(fromUrl){
                                window.location.href='/apiCombine/openSuccess?fromUrl='+fromUrl;
                            }else{
                                window.location.href = 'person/account/overview?' + Math.random();
                            }
                        }else{
                            swal(
                                o.error,
                                '',
                                'warning'
                            )
                        }
                    });
                }
            }
        }
        Verification._init()

    });

    $('#bankCar').on("keyup", function () {
        // 开户行
        var baneNo = $(this).val();
        if (baneNo.length < 6) {
            $("#bankInput").val("");
            $("#bankInput_code").val("");
        } else if (baneNo.length >= 6) {
            var sixStr = baneNo.substr(0, 6);
            var bankFlag, bankCode;
            for (var i = 0; i < bankCodeArr.length; i++) {
                if (bankCodeArr[i].card_bin.indexOf(sixStr) != -1) {
                    bankFlag = true;
                    bankCode = bankCodeArr[i].bank_code;
                    $("#bankInput").val(bankCodeArr[i].bank_name);
                    $("#bankInput_code").val(bankCode);
                    return;
                }
            }
        }
    });

    $('#BranchBox .search').on('click', function () {
        // Branch-list
        var keyWord = $("#Branch").val();
        if (!keyWord || keyWord.length <= 1) {
            return;
        }
        $("#Branch-list").html('');
        $.ajax({
            url: '/person/queryBranch'
            , type: 'post'
            , data: {key: keyWord,userToken:$("meta[name='userToken']").attr('content'),source:$("meta[name='source']").attr('content')}
            , dataType: 'json'
        }).done(function (o) {
            if (o.success) {
                o.data.forEach(function (item, index, all) {
                    $("#Branch-list").append("<li _code='" + item.code + "'>" + item.branchName + "</li>");
                });
                $("#Branch-list").slideDown(300);
            }
        });
    });

    /**
     * 获取市区数据
     */
    function flushCityData(provinceCode) {
        if (!provinceCode) {
            swal('无法获取市区数据', '', 'warning');
            return;
        }
        var cityData = localStorage.getItem(provinceCode);
        if (cityData ) {
            var cityArrays = JSON.parse(cityData);
            $('#areaListing').html('');
            for (var index in cityArrays) {
                var item = cityArrays[index];
                $('#areaListing').append("<li _code='" + item.code + "'>" + item.name + "</li>");
            }

        }
    }

    /**
     * 加载省
     */
    (function () {
        console.log("加载省");
        $('#citylisting').html('');
        cityAddressArr.forEach(function (item, index, all) {
            $('#citylisting').append("<li _code='" + item.code + "'>" + item.name + "</li>");
            localStorage.setItem(item.code, JSON.stringify(item.area));
        });
    })();
    $('#Branch').trigger('change');
    $('#bankCar').trigger('keyup');

});