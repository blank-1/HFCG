// JavaScript Document
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"publicJS":["main"],
		"swal":["lib/sweetalert2"]
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

        $('#load').on('click',function() {
            var ipt1 = $(".l_flag").attr("data-userName"),
                ipt2 = $(".l_flag").attr("data-telNumber"),
                ipt3 = $(".l_flag").attr("data-checkCode"),
                ipt4 = $(".l_flag").attr("data-passWord"),
                iptCheck = $('.l_uncheck').hasClass('l_checked');
            if ( ipt1 === 'true' && ipt2 === 'true' && ipt3 === 'true' && ipt4 === 'true' && iptCheck ) {
                var val1 = $('#userName').val(),
                    val2 = $('#telNumber').val(),
                    val3 = $('#checkCode').val(),
                    val4 = $('#passWord').val();
                goAjax(val1,val2,val3,val4);
            }else if (!iptCheck) {
                swal('请勾选注册协议！','','warning');
            }else if (ipt1 === 'false') {
                swal('请填写用户名！','','warning');
            }else if (ipt2 === 'false') {
                swal('请填写手机号！','','warning');
            }else if (ipt3 === 'false') {
                swal('请填写验证码！','','warning');
            }else if (ipt4 === 'false') {
                swal('请填写密码！','','warning');
            }else {
                return false;
            }
            return false;
        })

        var FormCheck =	function(iptName,el,newReg) {
            if($(".l_flag").length == 0){
                var iptHidden = $("<input>").addClass("l_flag").attr({"type":"hidden","data-userName":"false","data-telNumber":"false","data-checkCode":"false","data-passWord":"false"}).appendTo("body");
            }
            if (this instanceof FormCheck) {
                var f = new this[iptName](el,newReg);
                return f;
            }else{
                return new FormCheck(iptName,el,newReg);
            }
        }
        FormCheck.prototype = {
            bindFun:function() {
                $('.l_del').on('touchend',function (e) {
                    e.stopPropagation();
                    FormCheck('delete',$(this));
                });
                $('input').on('focus',function() {
                    FormCheck('focus',$(this))
                }).on('blur',function() {
                    $('.l_del').hide();
                    FormCheck($(this).attr('id'),$(this));
                });
                // 密码切换按钮
                $(".l_pswBtn").on("click",function(e) {
                    e.stopPropagation();
                    e.preventDefault(); //阻止浏览器元素默认行为
                    $(this).toggleClass("l_pswBtn2");
                    $(this).hasClass("l_pswBtn2")?$("#passWord").prop("type","text"):$("#passWord").prop("type","password");
                })
                // 勾选切换按钮
                $(".l_uncheck").on("click",function(e) {
                    e.stopPropagation();
                    e.preventDefault(); //阻止浏览器元素默认行为
                    $(this).toggleClass("l_checked");
                })

                $('#checkCodeB').on('touchend',function(e) {
                    e.stopPropagation();
                    $('#telNumber').blur();
                    if ($(".l_flag").attr("data-telNumber") === 'true') {
                        postMSG();
                    }
                    return false;
                })
				$('#telNumber').on("input",function(){
                    FormCheck('input',$(this),'numberOnly');
				})
                $('#checkCode').on("input",function(){
                    FormCheck('input',$(this),'numberOnly');
                })
                $('#checkCode').on("input",function(){
                    FormCheck('input',$(this),'numAndLetter');
                })
            },

            countDown:function (el) {
                var num = el;
                num-=1;
                if (num == 0) {
                    $("#checkCodeB").text("获取验证码");
                    $("#checkCodeB").removeAttr('disabled');
                    $("#checkCodeB").removeClass("l_disable");
                }else{
                    $("#checkCodeB").attr("disabled","");
                    $("#checkCodeB").addClass("l_disable");
                    $("#checkCodeB").text(num + "秒");
                    setTimeout(function(){FormCheck('countDown',num--)},1000);
                }
            },

            focus:function(el) {
                var e = window.event || arguments.callee.caller.arguments[0];
                e.stopPropagation();
                var placeholder = el.attr("data-placeholder"),
                    regTip = el.next();
                el.attr("data-placeholder",placeholder).css('border-bottom','solid 1px #b0d3fb').attr("placeholder","").siblings(".l_del").show();
                regTip.text(regTip.attr("data-regTip")).removeClass("pjWrong").addClass("pjTip").slideDown();
            },

            input:function(el,newReg) {
                var val = el.val();
                switch (newReg) {
                    case 'numberOnly':
                        if (!/^[0-9]*$/.test(val)) {
                            el.val(val.substring(0,val.length-1));
                        }
                        break;
                    case 'twoNumAfterDot':
                        if (!/^\d*\.{0,1}\d*$/.test(val)) {
                            el.val(val.substring(0,val.length-1));
                        }
                        break;
                    case 'numAndLetter':
                        if (/[^A-Za-z0-9]/g.test(val)) {
                            el.val(val.substring(0,val.length-1));
                        }
                        break;

                }
            },

            delete:function(el) {
                var e = window.event || arguments.callee.caller.arguments[0];
                e.stopPropagation();
                e.preventDefault();
                el.siblings("input:visible").val("");
            },

            userName:function(el,newReg) {
                // 默认的正则验证
                var regX = (/[^\w\u4e00-\u9fa5,-]/g.test(val)),
                    val = el.val();
                if (arguments[2] !== undefined)//如果有额外传入的正则方法，则使用新传入的规则校验
                {
                    regX = arguments[2];
                    console.log("本次校验使用新传入的校验规则！");
                }
                if(val=="")//用户名为空
                {
                    el.attr("placeholder",el.attr("data-placeholder")).css({"border-bottom":"solid 1px #ffc281"}).next().text("用户名不能为空").removeClass("pjTip").addClass("pjWrong");
                    $(".l_flag").attr("data-userName",false);
                }
                else if(regX)//用户名不合法
                {
                    el.css({"border-bottom":"solid 1px #ffc281"}).next().text("您输入的用户名含有非法字符").removeClass("pjTip").addClass("pjWrong");
                    $(".l_flag").attr("data-userName",false);
                }
                else if(val.length<4||val.length>20)//用户名长度不合法
                {
                    el.css({"border-bottom":"solid 1px #ffc281"}).next().text("您输入的用户名长度不正确").removeClass("pjTip").addClass("pjWrong");
                    $(".l_flag").attr("data-userName",false);
                }
                else
                {
                    el.css({"border-bottom":"solid 1px #1ad8b0"}).next().text("").slideUp();
                    $(".l_flag").attr("data-userName",true);
                }
                el.attr("placeholder",el.attr("data-placeholder"));
            },

            telNumber:function(el,newReg) {
                // 默认的正则验证
                var val = el.val(),
                    regX = (/^1[3|4|5|8|7][0-9]\d{4,8}$/.test(val));
                console.log(val);
                if (arguments[1] !== undefined)//如果有额外传入的正则方法，则使用新传入的规则校验
                {
                    regX = arguments[1];
                    console.log("本次校验使用新传入的校验规则！");
                }
                if (val == "")
                {
                    el.attr("placeholder",el.attr("data-placeholder")).css({"border-bottom":"solid 1px #ffc281"}).next().text("请输入手机号码").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
                    $(".l_flag").attr("data-telNumber",false);
                }
                else if (!(regX))
                {
                    el.css({"border-bottom":"solid 1px #ffc281"}).next().text("请输入正确的手机号码").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
                    $(".l_flag").attr("data-telNumber",false);
                }
                else
                {
                    el.css({"border-bottom":"solid 1px #1ad8b0"}).next().text("").slideUp();
                    $(".l_flag").attr("data-telNumber",true);
                };
                el.attr("placeholder",el.attr("data-placeholder"));

            },
            checkCode:function(el) {
                var val = el.val(),
                    leg = val.length;
                if (val == '')
                {
                    el.css({"border-bottom":"solid 1px #ffc281"}).next().text("请输入验证码").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
                    $(".l_flag").attr("data-checkCode",false);
                }
                else if (leg < 6)
                {
                    el.css({"border-bottom":"solid 1px #ffc281"}).next().text("验证码位数错误").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
                    $(".l_flag").attr("data-checkCode",false);
                }else{
                    el.css({"border-bottom":"solid 1px #1ad8b0"}).next().text("").slideUp();
                    $(".l_flag").attr("data-checkCode",true);
                }
                el.attr("placeholder",el.attr("data-placeholder"));
            },
            passWord:function(el) {
                var e = window.event || arguments.callee.caller.arguments[0];
                e.stopPropagation();
                var val = el.val();
                //密码为空
                if(val=="")
                {
                    el.css({"border-bottom":"solid 1px #ffc281"}).next().text("密码不能为空").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
                    $(".l_flag").attr("data-passWord",false);
                }
                else if(val.length<6||val.length>16)
                {
                    el.css({"border-bottom":"solid 1px #ffc281"}).next().text("您设置的密码长度不正确").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
                    $(".l_flag").attr("data-passWord",false);
                }else{
                    $(".l_flag").attr("data-passWord",true);
                    el.css({"border-bottom":"solid 1px #1ad8b0"}).next().text("").slideUp();
                }
                el.attr("placeholder",el.attr("data-placeholder"));
            },
            code:function(el) {
                var e = window.event || arguments.callee.caller.arguments[0];
                e.stopPropagation();
                el.attr("placeholder",el.attr("data-placeholder"));
            },

        }

        FormCheck('bindFun');

        function postMSG() {
            var phoneNo = $("#telNumber").val();
            $.ajax({
                url:rootPath+"/user/regist/validate_name_phone",
                type:"post",
                data:{"mobile":phoneNo,"loginName":$("#userName").val()},
                success:function(data){
                    var phone =  eval("("+data.phone+")");
                    var username =  eval("("+data.username+")");
                    if(validError(phone)&&validError(username)){
//						time(document.getElementById("btn"));
                        $.post(rootPath+'/user/regist/getRegisterMsg',{"mobileNo":phoneNo});
                        FormCheck('countDown',60);
                    }
                }
            });
        }
	})
	/*$("input:visible").each(function(){
		$(this).val("");
	})
	// 输入框公共方法
	$("input[name=loginName]").on("focus",function(){
		publicJS.inpputFocus($(this));
	})
	


	
	
	// 手机号输入框
	$("input[name=mobileNo]").on("focus",function(){
		publicJS.inpputFocus($(this));
		$("#sclTest").text($(".l_NewScroll").scrollTop())
	})
	// 验证码输入框
	$("input[name=validCode]").on("focus",function(){
		publicJS.inpputFocus($(this));
		$("#sclTest").text($(".l_NewScroll").scrollTop())
	})
		// 密码输入框
	$("input[name=loginPass]").on("focus",function(){
		publicJS.inpputFocus($(this));
		$("#sclTest").text($(".l_NewScroll").scrollTop())
	})

	$("input").on("blur",function(){
		$(this).siblings(".l_del").hide();
	})
	$(".l_del").on("touchend",function(e) {
		e.stopPropagation();
		e.preventDefault(); //阻止浏览器元素默认行为
		$(this).siblings("input").val("");
	})
	//用户名前端验证
	$("#userName").on("blur",function(e){
		e.stopPropagation();
		var name = $("#userName").val(),
				el = $(this);

		//用户名为空
		if(name==""){
			el.css({"border-bottom":"solid 1px #ffc281"}).next().text("用户名不能为空").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
			$("#load").removeClass("test1");

		//用户名不合法
		}else if((/[^\w\u4e00-\u9fa5,-]/g.test(name))){
			el.css({"border-bottom":"solid 1px #ffc281"}).next().text("您输入的用户名含有非法字符").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
			$("#load").removeClass("test1");

			//用户名长度不合法
		}else if(name.length<4||name.length>20){
			el.css({"border-bottom":"solid 1px #ffc281"}).next().text("您输入的用户名长度不正确").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
			$("#load").removeClass("test1");
		}else{
				el.css({"border-bottom":"solid 1px #1ad8b0"}).next().hide();
				$("#load").addClass("test1");
				return false;
		}
		publicJS.inpputBlur($(this));
	})
	//手机号码格式前端验证
	$("#telNumber").on("blur",function(e){
		e.stopPropagation();
		var el = $(this);
		if ($("#telNumber").val() == "") {
			el.css({"border-bottom":"solid 1px #ffc281"}).next().text("请输入手机号码").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
			$("#load").removeClass("test2");
		}else if ((/^1[3|4|5|8|7][0-9]\d{4,8}$/.test($("#telNumber").val()))) {
			el.css({"border-bottom":"solid 1px #ffc281"}).next().text("请输入正确的手机号码").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
			$("#load").removeClass("test2");
		}else{
			$("#load").addClass("test2");
			el.css({"border-bottom":"solid 1px #1ad8b0"}).next().hide();
			$("#telNumber").css({"border-bottom":"solid 1px #1ad8b0"});
		};

	});

	// 获取验证码按钮点击验证手机号码
	$("#checkCodeB").on("click",function(e){
		e.stopPropagation();
		var el = $(this);
		if ($("#load").hasClass("test2")) {
			//此处添加向用户手机发送验证码短信功能
			var phoneNo = document.getElementById("telNumber").value;
			$.ajax({
				url:rootPath+"/user/regist/validate_name_phone",
				type:"post",
				data:{"mobile":$("#telNumber").val(),"loginName":$("#userName").val()},
				success:function(data){
					var phone =  eval("("+data.phone+")");
					var username =  eval("("+data.username+")");
					if(validError(phone)&&validError(username)){
//						time(document.getElementById("btn"));
						$.post(rootPath+'/user/regist/getRegisterMsg',{"mobileNo":phoneNo});
						publicJS.countDown();
					}
				}
			});
		}else{
			$("#telNumber").css({"border-bottom":"solid 1px #ffc281"}).next().text("请输入正确的手机号码").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
			$("#load").removeClass("test2");
		};
		return false;
	});

	//短信验证码前端验证
	$("#checkCode").on("blur",function(e){
		e.stopPropagation();
		var el = $(this);
			//验证码为空
		if(el.val()==""){
			el.css({"border-bottom":"solid 1px #ffc281"}).next().text("验证码不能为空").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
			$("#load").removeClass("test3");

			//验证码不合法
		}else if((/[^0-9]/g.test(el.val()))){
			el.css({"border-bottom":"solid 1px #ffc281"}).next().text("您输入的验证码含有非法字符").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
			$("#load").removeClass("test3");

			//验证码长度不合法
		}else if(el.val().length>6){
			el.css({"border-bottom":"solid 1px #ffc281"}).next().text("您输入的验证码长度不正确").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
			$("#load").removeClass("test3");
		}else{
			$("#load").addClass("test3");
			$("#checkCode").css({"border-bottom":"solid 1px #1ad8b0"}).next().hide();
			publicJS.inpputBlur($(this));
		}
	})

	//密码前端验证
	$("#passWord").on("blur",function(e){
		e.stopPropagation();
		var el = $(this);
			//密码为空
		if($(this).val()==""){
			el.next().text("密码不能为空").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
			el.css({"border-bottom":"solid 1px #ffc281"});
			$("#load").removeClass("test4");
			// console.log(index);

			//密码不合法
		}else if((/[^A-Za-z0-9]/g.test($(this).val()))){
			el.next().text("您设置的密码含有非法字符").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
			el.css({"border-bottom":"solid 1px #ffc281"});
			$("#load").removeClass("test4");

			//密码长度不合法
		}else if($(this).val().length<6||$(this).val().length>16){
			el.css({"border-bottom":"solid 1px #ffc281"}).next().text("您设置的密码长度不正确").removeClass("pjTip").addClass("pjWrong").fadeOut(1500);
			$("#load").removeClass("test4");
		}else{
			$("#load").addClass("test4");
			el.css({"border-bottom":"solid 1px #1ad8b0"}).next().hide();
			publicJS.inpputBlur($(this));
			$(".l_regTip").hide();
		}
	})
	
	
	if($("#code").attr("value")!= ''){
		var val = $("#code").attr("value");
		$("#code").attr({"placeholder":val,"readonly":"readonly"})
	}
	//登录按钮异步验证
	$("#load").on("click",function() {
		if( $("#load").hasClass("test1") && $("#load").hasClass("test2") && $("#load").hasClass("test3") && $("#load").hasClass("test4") ){
			if ($(".l_uncheck").hasClass("l_checked")) {
				$("#loading").show();
				setTimeout(function(){
                    $.ajax({
                        url:rootPath+"/user/regist/validate",
                        type:"post",
                        data:{"mobile":$("#telNumber").val(),"validCode":$("#checkCode").val(),"loginName":$("#userName").val(),"password":$("#passWord").val(),"inviteCode":$("#in_code").val()},
                        success:function(data){
                            $("#loading").hide();
                            var valid =  eval("("+data.valid+")");
                            var password =  eval("("+data.password+")");
                            var phone =  eval("("+data.phone+")");
                            var username =  eval("("+data.username+")");
                            if(data.visate!=null){
                                var visate =  eval("("+data.visate+")");
                                if(!validError(visate))
                                    return ;
                            }
                            if(validError(valid)&validError(password)&validError(phone)&validError(username)){
                                alert("ok");
                            	//$("#frm").submit();
                            }else{
                                return ;
                            }
                        }
                    });
				},1500);
			}else{
				swal('请勾选同意《注册协议》','','warning')
			}
		}else{
			swal('请完善注册信息','','warning')
		}
		return false;
	})

	// 密码切换按钮
	$(".l_pswBtn").on("click",function(e) {
		e.stopPropagation();
		e.preventDefault(); //阻止浏览器元素默认行为
		$(this).toggleClass("l_pswBtn2");
		$(this).hasClass("l_pswBtn2")?$("#passWord").prop("type","text"):$("#passWord").prop("type","password");
	})
	// 密码切换按钮
	$(".l_uncheck").on("click",function(e) {
		e.stopPropagation();
		e.preventDefault(); //阻止浏览器元素默认行为
		$(this).toggleClass("l_checked");
	})*/




    function goAjax(val1,val2,val3,val4) {
        $("#loading").show();
        setTimeout(function(){
            $.ajax({
                url:rootPath+"/user/regist/validate",
                type:"post",
                data:{"mobile":val2,"validCode":val3,"loginName":val1,"password":val4,"inviteCode":$("#code").val()},
                success:function(data){
                    $("#loading").hide();
                    var valid =  eval("("+data.valid+")");
                    var password =  eval("("+data.password+")");
                    var phone =  eval("("+data.phone+")");
                    var username =  eval("("+data.username+")");
                    if(data.visate!=null){
                        var visate =  eval("("+data.visate+")");
                        if(!validError(visate))
                            return ;
                    }
                    if(validError(valid)&validError(password)&validError(phone)&validError(username)){
                        $("#frm").submit();
                    }else{
                        return ;
                    }
                }
            });
        },1500);
    }

	/****回调验证***/
	function validError(v){
		if(!v.isSuccess){
			swal(v.info,'','warning')
		}
		return v.isSuccess;
	}
})
