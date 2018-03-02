// JavaScript Document
define(['jquery'],function($) {
	return {
		"BTNcolor":function(){
			//按钮点击增加变色效果代码
			$("#next").on("touchstart", function (e) {
				e.stopPropagation();
				e.preventDefault();
			    $(this).addClass("iconShadow");
		    });
			$("#next").on("touchend", function (e) {
				e.stopPropagation();
				e.preventDefault();
			    $(this).removeClass("iconShadow");
		    });
		},

		"PasswordChange":function(){
			//密码切换效果
			$(".l_pswBtn").on("touchend", function (e){
				e.stopPropagation();
				e.preventDefault();
				if ($(".l_pswBtn").hasClass("l_pswC")) {
					$(".l_pswBtn").removeClass("l_pswC");
					$("#passWord").prop("type","password");
				}else{
					$(".l_pswBtn").addClass("l_pswC");
					$("#passWord").prop("type","text");
				}
			});
		},
		"countDown":function(){
			//验证码倒计时效果
			function countDown(num) {
				num-=1;
				if (num == 0) {
					$("#checkCodeB").text("获取验证码");
					$("#checkCodeB").removeAttr('disabled');
					$("#checkCodeB").removeClass("l_disable");

				}else{

					$("#checkCodeB").attr("disabled","");
					$("#checkCodeB").addClass("l_disable");
					$("#checkCodeB").text(num + "秒");
					setTimeout(function(){countDown(num--)},1000);
				}
			}
			countDown(60)

		},
		"swiper":function(el){
			//全屏滚动效果
			// $(".l_focusDot").remove();
			$("."+el+"").css("left","0");
			var winWid = $("body").width(),
				eqSpan = 0,
				leg = $("."+el+" li").length,
				focusDot = $("<p>").addClass("l_focusDot").appendTo($("."+el).parent()),
				left, startX,startY,X,Y;
			$("."+el+"").css("width",100*leg+"%");//计算父级ul的宽度
			$("."+el+" li").css("width",winWid);//设置子li的宽度
			//插入焦点span
			for (var i = 0; i < leg; i++) {
				$("<span>").appendTo(focusDot);
			};
	        $(".l_focusDot").css({"width":leg+"rem","margin-left":-leg/2+"rem"});
	        $("."+el).siblings(".l_focusDot").children("span").eq(eqSpan).addClass("l_pointFocus");
			// console.log((leg-1)*winWid);

			// 监听touchmove事件
			$("."+el+"").on("touchstart", function(e) {
			    e.preventDefault();
			    e.stopPropagation();
			    startX = e.originalEvent.changedTouches[0].pageX,
			    startY = e.originalEvent.changedTouches[0].pageY;
			    leftStart = $("."+el+"").offset().left;
			    // console.log(leftStart);
			});
			$("."+el+"").on("touchmove", function(e) {
			    e.preventDefault();
			    e.stopPropagation();
			    moveEndX = e.originalEvent.changedTouches[0].pageX,
			    moveEndY = e.originalEvent.changedTouches[0].pageY,
			    X = moveEndX - startX,
			    Y = moveEndY - startY;
			    $("."+el+"").css("left",leftStart+X);
			});
			$("."+el+"").on("touchend", function(e) {
			    e.preventDefault();
			    e.stopPropagation();
			    moveEndX = e.originalEvent.changedTouches[0].pageX,
			    moveEndY = e.originalEvent.changedTouches[0].pageY,
			    X2 = moveEndX - startX,
			    // console.log(X2);
			    // console.log(leftStart);
			    leftEnd = $("."+el+"").offset().left;
			    // console.log(leftEnd);
			    // console.log(leftStart);
				if (Math.abs(X) < winWid/4 && X2!=0 && X!=0) {//当滑动距离没超过半屏
					$("."+el+"").css("left",leftEnd-X);//返回原位
				}else if(Math.abs(X) >= winWid/4 &&X>0&& X2!=0){
					if (leftStart >= 0) {
						//防止滑动超出边界
			  			$("."+el+"").animate({left:"0"},"fast","swing");
			  		}else{
			  			eqSpan -= 1
			  			$("."+el+"").animate({left:leftStart+winWid},"fast","swing");
			  			$("."+el).siblings(".l_focusDot").children("span").eq(eqSpan).addClass("l_pointFocus").siblings().removeClass("l_pointFocus");
			  		}
			  	}else if(Math.abs(X) >= winWid/4 &&X<0&& X2!=0){
			  		if (leftStart <= -(leg-1)*winWid) {
			  			//防止滑动超出边界
			  			$("."+el+"").animate({left:-(leg-1)*winWid},"fast","swing");
			  		}else{
			  			eqSpan += 1
			  			$("."+el+"").animate({left:leftStart-winWid},"fast","swing");
			  			$("."+el).siblings(".l_focusDot").children("span").eq(eqSpan).addClass("l_pointFocus").siblings().removeClass("l_pointFocus");
			  		}

			  	}else{
			  		// 点击时获取页面当前li的序号;
			  		// if (X2==0) {
			  		// 	var legEq = -leftEnd/winWid,
			  		// 		linkTo = $("."+el+" li:eq("+ legEq +")").attr("data-href")
			  		// 	// alert(legEq)
			  		// 	if (linkTo) {
			  		// 		location.href = $("."+el+" li:eq("+ legEq +")").attr("data-href");
			  		// 	}

			  		// };
			  		return false;
				}
			});
		},

		"AutoEnter":function(){
			//回车键自动换行方法
			var $load = $("#load"),
			inputLeg = $("input:visible[name='input']").length;
			$("input[name='input']").on("input keydown", function(){
				var inputNam = $(this).attr("data-inputNum");
				// console.log(inputNam)
				// console.log(inputLeg);
			    if(event.keyCode ==13)
			    {
				   if (inputNam == inputLeg) {
				   		$load.focus();
				   		return load();
				   }else{
				   		 var newNum = Math.floor(inputNam)+1;
				   		$(".input"+newNum).focus();
				   };
			    }
			 })
		},

		"limitNUM":function(n){
			//输入框字符数量限制方法
		    var leg = $(".limitNUM"+n).val().length;
		    if (leg > n) {
				$(".limitNUM"+n).val($(".limitNUM"+n).val().substring(0,n));
		    }
		},

        "valNum":function(n){
            //输入框字符数量限制方法
            if (!/^\d*\.{0,1}\d*$/.test(n.val())) {
                n.val(n.val().substring(0,n.val().length-1));
            }
        },

		"inpputFocus":function(el){
			var placeholder =el.attr("placeholder"),
					regTip = el.next();
			// $(".l_regTip").hide();
			el.siblings(".l_del").show();
			el.attr("data-placeholder",placeholder).css('border-bottom','solid 1px #b0d3fb').attr("placeholder","");
			regTip.text(regTip.attr("data-regTip")).removeClass("pjWrong").addClass("pjTip").slideDown();
			el.on("blur",function () {
					el.attr("placeholder",placeholder);
			})
		},

		"inpputBlur":function(el){
			var placeholderOld =el.attr("data-placeholder"),
					regTip = el.next();
			regTip.removeClass("pjTip");
			el.attr("placeholder",placeholderOld);
		},

		"scrollLoading":function(el,box) {
			$(".l_lastTip").show();
			var boxH = $("."+box).outerHeight(),
					sclTop = $("."+box).scrollTop(),
					elHight = $("."+el).outerHeight();
			if (boxH+sclTop == elHight) {
				return true;
			}
		}
	}

})
