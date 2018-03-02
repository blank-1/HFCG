// JavaScript Document
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"publicJS":["main"],
		"swal":["lib/sweetalert2"],
	}
})
require(['jquery','publicJS','swal'],function($,publicJS,swal) {
	var flag=false;
    var rule=$("#publishRule").val();
    var second=$("#sxTimer").val();
	$(function() {
			var overscroll = function(el) {
			el.addEventListener('touchstart', function() {
				var top = el.scrollTop
					, totalScroll = el.scrollHeight
					, currentScroll = top + el.offsetHeight
				if(top === 0) {
					el.scrollTop = 1
				} else if(currentScroll === totalScroll) {
					el.scrollTop = top - 1
				}
			});
			el.addEventListener('touchmove', function(evt) {
				if(el.offsetHeight < el.scrollHeight)
					evt._isScroller = true
			})
		};
		overscroll(document.querySelector('.l_NewScroll'));
		document.body.addEventListener('touchmove', function(evt) {
			if(!evt._isScroller) {
				evt.preventDefault()
			}
		});
		$("#loading").show();
		setTimeout(function(){
            $("#loading").hide();
        },1000);
        timerAccount();
        zqAccount();

        if (second > 0 && rule == "1") {// 省心优先倒计时处理中
            var sx = window.setInterval(function(){
                if(flag){
                    sxTimer();
                    clearInterval(sx);
                }
            }, 1000);
        }else{
            perLineAnimate();
		}


	})

    function zqAccount(){
        var type=$("#creditor_type").val();
        if(type=="zqzr"){
            countDown($("#residualTime").val(), function(msg)
            {
                $("#yure").html(msg);
            });
        }
    }

    function countDown(maxtime, fn) {
        var timer = setInterval(function() {
            if (maxtime >= 0) {
                d = parseInt(maxtime / 3600 / 24);
                h = parseInt((maxtime / 3600) % 24);
                minutes = parseInt((maxtime / 60) % 60);
                seconds = parseInt(maxtime % 60);
                // minutes = Math.floor(maxtime/60);
                // seconds = Math.floor(maxtime%60);
                msg = "转让倒计时:    "+d+"天"+ h + "时" + minutes + "分" + seconds+ "秒" ;
                fn(msg);
                // if(maxtime == 5*60) //alert('注意，还有5分钟!');
                --maxtime;
            } else {
                clearInterval(timer);
                fn("时间到，结束!");
            }
        }, 1000);
    }

	function sxTimer(){
		var timer = setInterval(function() {
			$("#yure").html("预计结束时间："+second+"秒");
			second--;
			if (second == 0) {
				setTimeout(function() {
					clearInterval(timer);
					perLineAnimate();
					$(".l_incomein").removeClass("l_incomein");
				},200);
			}
		}, 1000);
	}

    function timerAccount(){
        var timerFlag=$("#timerFlag").val();
        if(timerFlag=="1"){
			var intDiff=$("#secondBetwween").val();
			intDiff=parseInt(intDiff);
			var interval = window.setInterval(function(){
				var day=0,
					hour=0,
					minute=0,
					second=0;//时间默认值
				if(intDiff > 0){
					day = Math.floor(intDiff / (60 * 60 * 24));
					hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
					minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
					second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
				}
				if(intDiff <= 0){
                    flag=true;
					clearInterval(interval);
					return;
				}

				if (minute <= 9) minute = '0' + minute;
				if (second <= 9) second = '0' + second;
				$("#yure").html('预热中,剩余时间'+day+"天"+hour+":"+minute+':'+second);
				intDiff--;
			}, 1000);
        }else{
            flag=true;
		}
    }

	//进度条动画
	function  perLineAnimate() {
		var money = $("#loanAmount").attr("data-money"),
				hasmoney = $("#hasmoney").attr("data-hasmoney"),
				per = Math.round(hasmoney/money*100),
				perbgin = 0;
		$(".l_perLine>i").css("width",per+"%");
		(function perAnimate(){
			if (perbgin == per) {
					$(".l_perLine>i>b").text(per+"%");
			}else{
					$(".l_perLine>i>b").text(perbgin+"%");
					setTimeout(function(){perAnimate(perbgin++)},2000/per);
			}
		})();
	}

    $(".l_CheckInfo").on("click",function() {
        var url=rootPath + "/finance/detail?goType="+$("#creditor_type").val();
        if($("#loanApplicationNo").val()!=""){
            url+="&loanApplicationNo="+$("#loanApplicationNo").val();
		}
		if($("#creditorRightsApplyId").val()!=""){
            url+="&creditorRightsId="+$("#creditorRightsApplyId").val();
		}
		location.href=url;
	});

    $("#verified").click(function(){
        location.href=rootPath + "/finance/toRealName?url="+location.href;
    });

    var passFlag=false;
    $("#inputPassword").on("blur",function() {
    	if(passFlag){
    		return;
		}
        $("#loading").show();
        var psw=$("#inputPassword").val();
        var placeholder =$("#inputPassword").attr("placeholder");
        setTimeout(function(){
            if(psw==""){
                $("#loading").hide();
                swal('请填写定向密码','','warning');
                $("#inputPassword").val("").attr("placeholder",placeholder).css('border','solid 1px #ff5e61');
                return;
            }
            $.ajax({
                url:rootPath+"/finance/getPass",
                type:"post",
                data: {
                    "pass":psw,
                    "loanApplicationId": $("#loanApplicationId").val()
                },
                success: function(data) {
                    $("#loading").hide();
                    if(data=="success"){
                        passFlag=true;
                        $("#targetPass").attr("value",psw);
                        $(".l_inputPassword").slideUp(300);
                    }else if(data=="fail"){
                        swal('定向密码错误','','warning');
                        $("#inputPassword").val("").attr("placeholder",placeholder).css('border','solid 1px #ff5e61');
                    }
                },
                error:function(data){
                    $("#loading").hide();
                    var data = data.responseText;
                    var _data = eval('('+data+')');
                    swal(_data.errorMsg,'','warning');
                }
            });
		},1500);
    });

    $("#btn2").on("click",function() {
        var otype=$("#oType").val();
        if(otype=="1"){
			var style=$(".l_inputPassword").attr("style");
			if(style==undefined){
                swal('请先输入定向密码','','warning');
                return false;
			}
		}
        if (!$(".l_propto>i").hasClass("l_checked")) {
            swal('您还未勾选《借款及服务协议》','','warning');
            return false;
        }
        var money1=rmoney($("#mony2").html());//剩余金额
        var qitou=rmoney($("#qitou").val());//起投金额
        var limied=rmoney($("#loanAmount").attr("data-money"));//限投
        var type=$("#creditor_type").val();
        var mondmoney=0;
        var money=$("#moneyInput").val();
        if(money==""||!/^(([0-9]|([1-9][0-9]{0,9}))((\.[0-9]{0,9})?))$/.test(money)||(money.length>10)){
            mondmoney=0;
            swal('请输入正确的理财金额','','warning');
            return;
        }else{
            mondmoney=parseFloat(money);
        }
        if(mondmoney<qitou){
            swal("请输入大于"+qitou+" 的金额！",'','warning');
            return;
        }else{
            if(money1-money<100&&money1-money>=0){
                if(money!=money1){
                    swal('用户购买完成后剩下的金额不能小于100！','','warning');
                    return;
                }
            }else{
                if(money!=money1){
                    if(mondmoney<100 || mondmoney % 100!=0){
                        swal('请输入大于100 并且是100 倍数的金额！','','warning');
                        return;
                    }else{
                        if(money1<money){
                            swal('购买金额超出剩余金额','','warning');
                            return;
                        }
                        if((parseFloat(money)-money1)>0){
                            swal('购买金额超出最大可投限额','','warning');
                            return;
                        }//
                        if(money>limied){
                            swal('购买金额超出最大可投限额','','warning');
                            return;
                        }
                    }
                }
            }

        }
        $("#mCount").val(money);

        if(type=="zqzr"){
            $("#lendForm").attr("action",rootPath + "/finance/toBuyRightsByPayAmount").submit();
		}else{
            $("#lendForm").attr("action", rootPath + "/finance/toBuyBidLoanByPayAmount").submit();
		}
    });

	$(".l_propto i").on("click",function() {
		$(this).toggleClass("l_checked");
	})
	// 输入框公共方法
	$("input[name=input]").on("focus",function(){
		var placeholder =$(this).attr("placeholder");
		$(this).attr("data-placeholder",placeholder).css('border','solid 1px #b0d3fb').attr("placeholder","").prop("type","password");
				/*$(this).on("blur",function () {
						$(this).attr("placeholder",placeholder).css('border','solid 1px #ff5e61');
						if ($(this).val() == "") {
							$(this).prop("type","text");
						}else {
							alert("后端校验定向密码")
							$(".l_inputPassword").animate({top:-8+"rem"},600);
						}

				})*/
	})

    $("#moneyInput").on("input",function() {
        publicJS.valNum($(this));
    })

	$("input[name=money]").on("focus",function(){
		var placeholder =$(this).attr("placeholder");
		$(this).attr("data-placeholder",placeholder).css('border','solid 1px #b0d3fb').attr("placeholder","");
		$(this).on("blur",function () {
			$(this).attr("placeholder",placeholder).css('border','solid 1px #ff5e61');
		})
	}).on("input",function () {
		publicJS.limitNUM(16);
        var money1=rmoney($("#mony2").html());//剩余金额
        var money=$("#moneyInput").val();
        money=parseFloat(money);
        money1=parseFloat(money1);
		if ($(this).val()<100) {
			$("#shouYi").text("0.00");
		}else if ($(this).val()%100!=0&&money1!=money) {
            $("#shouYi").text("--");
		}else {
            var type=$("#creditor_type").val();
            if(type=="zqzr"){
                $.ajax({
                    url:rootPath+"/finance/getExpectRightProfit",
                    type:"post",
                    data:{"creditorRightsApplyId":$("#creditorRightsApplyId").val(),"amount":$(this).val()},
                    async : false,
                    success:function(data){
                        $("#shouYi").text(data);
                    }
                });
			}else{
                $.ajax({
                    url:rootPath+"/finance/getExpectProfit",
                    type:"post",
                    data:{"loanApplicationId":$("#loanApplicationId").val(),"amount":$(this).val()},
                    async : false,
                    success:function(data){
                        $("#shouYi").text(data);
                    }
                });
			}

		}
	})
})
