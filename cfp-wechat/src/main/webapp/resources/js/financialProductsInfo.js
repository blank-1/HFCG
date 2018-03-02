// JavaScript Document
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"publicJS":["main"],
		"swal":["lib/sweetalert2"],
	}
})
require(['jquery','publicJS','swal'],function($,publicJS,swal) {
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

	perLineAnimate();
	})
	//进度条动画
	function  perLineAnimate() {
		var money = $("#money").attr("data-money"),
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

    $("#moneyInput").on("input",function() {
        publicJS.valNum($(this));
    })

    $("#BTNcheck").on("click",function() {
        var val=$("#moneyInput").val();
        var qitou=$("#sx_startsAt").val();
        var dizen=$("#sx_upAt").val();
        var yue=$("#sx_yue").val();
        if (yue == ""||yue == undefined) {
            swal('请用户登录','','warning');
            setTimeout(function(){
                toLogin();
			},2000);
            return false;
        }
        if (val == "") {
            swal('请填写省心金额','','warning');
            return false;
        }
        if (!$(".l_propto>i").hasClass("l_checked")) {
            swal('您还未勾选《省心计划投资协议》','','warning');
            return false;
        }
        val=parseInt(val);
        qitou=parseInt(qitou);
        dizen=parseInt(dizen);
        var type=$("#sx_type").val();
        if (val<qitou) {
            swal('输入大于起投的省心金额','','warning');
            return false;
        }else if (val%dizen != 0) {
            swal('请输入正确的省心金额','','warning');
            return false;
        }
        if(type=="2"){
            var balance=$("#sx_balance").val();
            balance=parseInt(balance);
            if(val>balance){
                swal('请输入正确的省心金额','','warning');
                return false;
            }
        }
        var isVerified=$("#isVerified").val();
        if(isVerified=="2"){
            $("#amount").val(val);
            $("#finance_detail_form").attr("action", rootPath + "/finance/toBuyFinanceByPayAmount").submit();
        }else{
            location.href=rootPath+"/finance/toRealName?url="+location.href;
        }
    })

	$(".l_propto i").on("click",function() {
		$(this).toggleClass("l_checked");
	})
	// 输入框公共方法
	$("input[name=input]").on("focus",function(){
		var placeholder =$(this).attr("placeholder");
		$(this).attr("data-placeholder",placeholder).css('border','solid 1px #b0d3fb').attr("placeholder","").prop("type","password");
				$(this).on("blur",function () {
						$(this).attr("placeholder",placeholder).css('border','solid 1px #ff5e61');
						if ($(this).val() == "") {
							$(this).prop("type","text");
						}else {
							alert("后端校验定向密码")
							$(".l_inputPassword").animate({top:-8+"rem"},600);
						}

				})
		})
		$("input[name=money]").on("focus",function(){
			var placeholder =$(this).attr("placeholder");
			$(this).attr("data-placeholder",placeholder).css('border','solid 1px #b0d3fb').attr("placeholder","");
			$(this).on("blur",function () {
				$(this).attr("placeholder",placeholder).css('border','solid 1px #ff5e61');
			})
		}).on("input",function () {
			publicJS.limitNUM(16);
			if ($(this).val()<100) {
				$("#shouYi").text("0.00");
			}else if ($(this).val()%100!=0) {
				$("#shouYi").text("--");
			}else {
				$("#shouYi").text(($(this).val()*1.007).toFixed(2));
			}
		})
})
