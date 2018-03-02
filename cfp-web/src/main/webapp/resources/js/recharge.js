// JavaScript Document
$(function(){

	searchHtml(1,5);
	//银行卡输入框失去焦点时
	$("#bankid").blur(function(){
		
		bankf($(this),"0");
		checkCard($(this).val());//校验卡号

	});
	
	//点击提交订单，去支付时(无卡)
	$("#recharge").click(function(){
		var b2=$("#bankid").is(":visible")?bankf($("#bankid"),"1"):'';

		if( b2==""){
			//$(".zhezhao5").show();
			$.ajax({
				url:rootPath+"/recharge/llRecharge",
				type:"post",
				data:$("#rechargeForm").serialize(),
				error : function(data) {
					var data = data.responseText;
					var _data = eval('('+data+')');
					$("#errorMsg").val(_data.errorMsg);
					$("#form_result").attr("action", rootPath + "/recharge/rechargeFailure").submit();
				},
				success:function(data){
					var _data =  eval("("+data+")");
					if(!_data.isSuccess){
						if(_data.id=='redirect'){
							//跳转值错误页面
							$("#errorMsg").val(_data.info);
							$("#form_result").attr("action", rootPath + "/recharge/rechargeFailure").submit();
						}else{
							//校验错误
							if(typeof(_data.tocken)!="undefined"){
								$("#token").val(_data.tocken);
							}
							//$(".zhezhao5").hide();
							var node = $("#"+_data.id);
							if(node.length>0){
								node.addClass("ipt-error").parent().siblings("em").html(_data.info).removeClass("hui");
							}else{
								$("#errorMsg").html(_data.info);
								$("#with_falure").click();
							}
						}
					}else{
						$("#rechargeCode").val(_data.rechargeCode);
						$("#payshowstate").slideDown(500);
						$(".zhezhao1").show();
						//window.open(_data.info);

						//ie判断
						/*var referForm=document.createElement("form");
						referForm.action= _data.info;
						referForm.method="post";
						referForm.target="_blank";
						document.body.appendChild(referForm);
						referForm.submit();*/
                        var a = $("<a href='"+_data.info+"' target='_blank' ></a>").get(0);
                        var e = document.createEvent('MouseEvents');
                        e.initEvent('click', true, true);
                        a.dispatchEvent(e);
					}
				}
			});

			// 提交操作-【结束】。
		}
	});

	$("#llPaySuccess,#llPayQuestion").click(function(){
		$("#recharge_result").attr("action",rootPath+"/recharge/getRechargeResult");
		$("#recharge_result").submit();
	});

	//充值失败
	$("#f_queren_withdraw").click(function(){
		//本页面刷新
		window.location.href = rootPath+"/person/toIncome";
	});

	//点击提交订单，去支付时（有卡）
	$("#okPay").click(function(){
		var b4=validf($("#valid"),0);
		var b1 = passwordf($("#jypassword"),1);
		var b2 = moneyf($("#moneyp"),0);
		if( b4==""&&b1==""&&b2==""){
			$(".zhezhao5").show();
			$.ajax({
				url:rootPath+"/recharge/confirmRechargeValidate",
				type:"post",
				data:$("#rechargeForm").serialize(),
				error : function(data) {
					var data = data.responseText;
					var _data = eval('('+data+')');
					$("#errorMsg").val(_data.errorMsg);
					$("#form_result").attr("action", rootPath + "/recharge/rechargeFailure").submit();
				},
				success:function(data){
					var _data =  eval("("+data+")");
					if(!_data.isSuccess){
						if(_data.id=='redirect'){
							//跳转值错误页面
							$("#errorMsg").val(_data.info);
							$("#form_result").attr("action", rootPath + "/recharge/rechargeFailure").submit();
						}else{
							//校验错误

							if(typeof(_data.tocken)!="undefined"){

								$("#token").val(_data.tocken);
							}
							$(".zhezhao5").hide();
							$("#"+_data.id).addClass("ipt-error").parent().siblings("em").html(_data.info).removeClass("hui");
						}
					}else{
						var times = 5
						var interval = window.setInterval(function(){
							getResult(times);
							times = times - 1;
							if(times==0){
								clearTimeout(interval);
							}
						}, 2000);
					}
				}
			});

			// 提交操作-【结束】。
		}
	});


	function getResult(times){
		$.post(rootPath + "/recharge/confirmRechargeIncome",function(data){
			if(data=='false'){//失败
				$(".zhezhao5").hide();
				$("#form_result").attr("action", rootPath + "/recharge/rechargeFailure").submit();
			}
			if(data=='success'){//成功
				$(".zhezhao5").hide();
				$("#form_result").attr("action", rootPath + "/recharge/rechargeSuccess").submit();
			}
			if(data=='recharging'){//支付中
				if(times==1){
					//跳到错误页面
					$(".zhezhao5").hide();
					$("#form_result").attr("action", rootPath + "/recharge/recharging").submit();
				}
			}

		})

	}

	//判断交易密码
/*	$("#jypassword").blur(function(){
		passwordf($(this),"0");
	});*/
	//判断银行卡号是否正确
	function bankf(bankv,pa){
		var tip = $("#userBankName").html();
		if(bankv.val()=="" )//
		{
			if(pa=="0"){
				massage="";
				bankv.removeClass("ipt-error").parent().siblings("em").html(tip).addClass("hui");
				$("#bankshow").hide();
				
			}else{
				massage="请您输入银行卡号！";
				bankv.removeClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
				
				$("#bankshow").hide();
			}
				
		}else{
			if(bankv.val().replace(/\s/g, "").length<=19 && bankv.val().replace(/\s/g, "").length>=15){
				massage="";
				bankv.removeClass("ipt-error").parent().siblings("em").html(tip).addClass("hui");
				$("#bankshow").show();
			}else{
				massage="银行卡号格式错误！";
				bankv.removeClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
				
				$("#bankshow").hide();
			}
			 
		}
		return massage;
	}
	
	// 字符替换（原始文本，替换目标，替换结果）
	function ReplaceAll(strAll, strp_old, strp_new){
		while (strAll.indexOf(strp_old) >= 0){
			strAll = strAll.replace(strp_old, strp_new);
		}
		return strAll;
	}

	//校验卡号(根据银行卡号获取所属银行名称)
	function checkCard(cardNo){
		if(/^(\d{16}|\d{19})$/.test(ReplaceAll(cardNo," ",""))){
			$.ajax({
				url:rootPath+'/bankcard/check_card',
				type:"post",
				data:{
					cardNo:cardNo
				},
				beforeSend: function () {
					ShowDiv();
				},
				error : function(XHR) {
					var res = eval("(" + XHR.responseText + ")");
					if(res.errorCode==4){
						window.location.href  = rootPath+"/user/to_login";
					}
				},
				success:function(data){
					HiddenDiv();
					if(data.result == 'success'){
						$("#bankshow").html('<img src="../images/pay_04.png" class="buttonimgdetail" /><font id="bankshow_name">'+data.data.bankname+'</font>');
					}else if(data.result == 'error'){
						if(data.errCode == 'check'){
							$("#bankid").addClass("ipt-error").parent().siblings("em").html(data.errMsg).removeClass("hui");
						}else{
							$("#bankid").addClass("ipt-error").parent().siblings("em").html(data.errMsg).removeClass("hui");
						}
					}else{
						$("#bankid").addClass("ipt-error").parent().siblings("em").html("网络异常，请稍后操作！").removeClass("hui");
					}

				}
			});
		}
	}

	//金额输入验证
	$("#moneyp").blur(function(){
		if(!$(this).val()==""){
			moneyf($(this));	
			
		}else{
			massage="";
			$(this).removeClass("ipt-error").parent().siblings("em").html('*充值最小金额不得小于<font color="#fe2a4d">100</font>元').addClass("hui");
		}
	});
	//金额输入验证
	$("#moneybk").blur(function(){
		if(!$(this).val()==""){
			moneyf($(this));	
			
		}else{
			massage="";
			$(this).removeClass("ipt-error").parent().siblings("em").html('*充值最小金额不得小于<font color="#fe2a4d">100</font>元').addClass("hui");
		}
	});
	
		//借款金额输入验证
	function moneyf(mond){
		var mondint;
		if(mond.val()==""|| !/^([0-9.]+)$/.test(mond.val())){
			mondint=0;
			massage="请输入正确的充值金额";
			mond.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			
			return massage;
		}else{
			
			mondint=parseInt(mond.val());
		}
		
		if(mondint<100 ){
			massage="请输入大于100元的金额！";
			mond.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
		}else{
				massage="";
				
				mond.removeClass("ipt-error").parent().siblings("em").html('*充值最小金额不得小于<font color="#fe2a4d">100</font>元').addClass("hui");
			
		}
		return massage;
	}
		
	$("#phone").change(function(){
		if (/^([0-9.]+)$/.test($(this).val())) {
				
			massage="";
			$(this).removeClass("ipt-error").parent().siblings("em").html(massage).html(massage);
		} else {
			massage="手机号码应为 11 位数字";
			$(this).addClass("ipt-error").parent().siblings("em").html(massage);
		}
	});
	$("#valid").change(function(){
		if (/^([0-9.]+)$/.test($(this).val())){
				
			massage="";
			$(this).removeClass("ipt-error").parent().siblings("em").html(massage).html(massage);
		} else {
			massage="验证码应为 6 位数字";
			$(this).addClass("ipt-error").parent().siblings("em").html(massage);
		}
	});
	//手机号码输入验证
	$("#phone").blur(function(){
		
		phonef($(this),1); 
	});
	//验证码输入验证
	$("#valid").blur(function(){
		
		validf($(this),1);
	});

	//手机验证
	function phonef(phone,ph9){

		var massage="";
		if(phone.val()=="" )
		{
			if(ph9==0){
			massage="手机号码格式错误";
			phone.addClass("ipt-error").parent().siblings("em").html(massage);
			}else{
				
				massage="";
				phone.removeClass("ipt-error").parent().siblings("em").html(massage);
			}
		}else
		{
			if (/(^1[3|4|5|7|8][0-9]{9}$)/.test(phone.val())) {
				massage="";
				phone.removeClass("ipt-error").parent().siblings("em").html(massage).html(massage);
			} else {
				
				massage="手机格式错误";
				phone.addClass("ipt-error").parent().siblings("em").html(massage);
			}
			
		}
		return massage;
	}
	//验证码
	function validf(vali,va9){
		if(vali.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(va9==0){
				massage="请您输入验证码";
				vali.addClass("ipt-error").parent().siblings("em").html(massage);
			}else{
				
				massage="";
				vali.removeClass("ipt-error").parent().siblings("em").html(massage);
			}
		}else{
			
			if(vali.val().length==6 && /^([0-9.]+)$/.test(vali.val())){

				
				massage="";
				vali.removeClass("ipt-error").parent().siblings("em").html(massage);
				
			}else{
				
				massage="验证码6位数字";
				vali.addClass("ipt-error").parent().siblings("em").html(massage);
			}
		}
		return massage;
	}


	//隐藏正在等待
	function HiddenDiv(){
		$("#bankshow").html('');
	}
	//显示正在等待
	function ShowDiv(){
		$("#bankshow").html('<img src="../images/loading220.gif" />');
	}

	//判断交易密码
	function passwordf(passval1,pa){

		var tip = $("#bidpass").html();
		if(passval1.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(pa=="0"){
				massage="";
				passval1.removeClass("ipt-error").parent().siblings("em").html(tip).addClass("hui");

			}else{
				massage="请您输入交易密码！";
				passval1.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");

			}
		}else{

			if(passval1.val().length>=6 && passval1.val().length<=16 && /^[0-9a-zA-Z]+$/.test(passval1.val())){
				massage="";
				passval1.removeClass("ipt-error").parent().siblings("em").html(tip).addClass("hui");
			}else{
				massage="交易密码错误";
				passval1.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}

		}
		return massage;
	}
	
	//快捷支付和网银支付的切换
//	$("#rdi-quick").click(function(){
//		$(".inter-group").slideUp(500);
//		$(".quick-group").slideDown(500,function(){bottomB();});
//		$("#myvoud").appendTo($("#h22"));
//		$("#rdi-inter").attr("checked",false);
//	});
//	$("#rdi-inter").click(function(){
//		$(".quick-group").slideUp(500);
//		$(".inter-group").slideDown(500,function(){bottomB();});
//		$("#myvoud").appendTo($("#h21"));
//		$("#rdi-quick").attr("checked",false);
//	});
	
	//银行卡（网银支付 和 快捷支付）
	$(".rechan_gro h2").click(function(){
		
		/*$(this).addClass("s_show").removeClass("s_hide");
		$(this).siblings(".inter-group").slideDown(function(){bottomB();});
		$(this).find("input").attr("checked",true);
		$(this).parent().siblings().find("h2").removeClass("s_show").addClass("s_hide");
		$(this).parent().siblings().find(".inter-group").slideUp(function(){bottomB();});
		$(this).parent().siblings().find("h2>input").attr("checked",false);*/
		
	});
	
	
	$(".internateBank span").each(function(){
		if($(this).hasClass("choose")){
			setBankCode($(this));
		}
	});
	
	//网银支付银行切换
	$(".internateBank span").click(function(){
		$(this).addClass("choose").siblings("span").removeClass("choose").parent().siblings(".internateBank").find("span").removeClass("choose");
		$(this).parent().after($(".internateTable")[0]);
		var PHtml='';
				
//		if($(this).attr("id")=="gongShang"){
//			PHtml+='<tr class="interTitle"><td colspan="7">工商银行（普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td>开通方法</td></tr><tr><td rowspan="8">全国</td><td rowspan="8">借记卡<br /></td><td>2006年9月1日起未申请口令卡和USBKEY的</td><td>300</td><td>300</td><td>300</td><td rowspan="8">柜台申请开通  <br />申请电子口令卡每张口令卡使用次数为1000次</td></tr><tr><td>E支付</td><td>1000</td><td>1000</td><td>无限额</td></tr><tr><td>手机短信认证网上银行口令卡</td><td>2000</td><td>5000</td><td>无限额</td></tr><tr><td>未认证的网上银行口令卡</td><td>500</td><td>1000</td><td>无限额</td></tr><tr><td>电子密码器</td><td>50万</td><td>100万</td><td>无限额</td></tr><tr><td>一代U盾（无手机验证）</td><td>50万</td><td>100万</td><td>无限额</td></tr><tr><td>一代U盾（手机验证）</td><td>100万</td><td>100万</td><td>无限额</td></tr><tr><td>二代U盾</td><td>100万</td><td>100万</td><td>无限额</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件 </td></tr>';
//		
//		}else if($(this).attr("id")=="jianShe"){
//			PHtml+='<tr class="interTitle"><td colspan="8">建设银行（借记卡）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td width="227">用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>季度累计额度上限（元）</td><td>总限额累计上限（元）</td><td width="100">开通方法</td></tr><tr><td rowspan="5">全国</td><td rowspan="5">借记卡</td><td>文件证书</td><td>0</td><td>0</td><td>0</td><td>0</td><td>网上、电话</td></tr><tr><td>动态口令卡/短信口令卡</td><td>1000</td><td>1000</td><td>无限额</td><td>无限额</td><td>网上、电话</td></tr><tr><td>文件证书+动态口令卡/短信口令卡</td><td>5000</td><td>5000</td><td>无限额</td><td>无限额</td><td>网上、电话</td></tr><tr><td>二代网银盾</td><td>5000</td><td>5000</td><td>无限额</td><td>无限额</td><td>柜台</td></tr><tr><td>二代网银盾+动态口令卡/短信口令卡</td><td>5000</td><td>5000</td><td>无限额</td><td>无限额</td><td>柜台</td></tr><tr><td colspan="8">备注：开通网上银行是进行电子支付的前提条件 </td></tr>';
//		}else if($(this).attr("id")=="nongYe"){
//		PHtml+='<tr class="interTitle"><td colspan="7">农业银行（普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td>开通方法</td></tr><tr><td rowspan="4">全国</td><td rowspan="4">金穗借记卡、准贷记卡</td><td>一代K宝</td><td>50万</td><td>100万</td><td>无限额</td><td rowspan="4">柜台</td></tr><tr><td>二代K宝</td><td>100万</td><td>500万</td><td>无限额</td></tr><tr><td>动态口令卡</td><td>1000</td><td>1000</td><td>无限额</td></tr><tr><td>K码支付</td><td>1000<br /></td><td>3000</td><td>无限额</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件 <br /></td></tr>';
//		}else if($(this).attr("id")=="jiaoTong"){
//			PHtml+='<tr class="interTitle"><td colspan="7">交通银行（普通）</td></tr><tr class="interTitle"><td></td><td colspan="5" width="632">电话：95559</td><td></td></tr><tr><td>覆盖区域</td><td>用户类型</td><td>支持卡种</td><td>每张卡单笔消费上限</td><td>每张卡每日消费上限</td><td>总限额累计上限</td><td>开通方法</td></tr><tr><td rowspan="4">全国</td><td rowspan="2">短信密码用户</td><td>借记卡</td><td>默认5000-可调高至50000</td><td>默认5000-可调高至50000</td><td>-</td><td>柜台</td></tr><tr><td>贷记卡</td><td>默认5000-可调高至50000</td><td>默认5000-可调高至50000</td><td>-</td><td>柜台</td></tr><tr><td rowspan="2">证书用户</td><td>借记卡</td><td>100万</td><td>100万</td><td>-</td><td>柜台</td></tr><tr><td>贷记卡</td><td>默认10000-可调高至50000</td><td>默认10000-可调高至50000</td><td>-</td><td>柜台</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件<br /></td></tr>';
//		}else if($(this).attr("id")=="zhaoShang"){
//			PHtml+='<tr class="interTitle"><td colspan="8">招商银行  （普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td width="100">支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>季度累计额度(上限)</td><td>总限额累计上限（元）</td><td width="100">开通方法</td></tr><tr><td rowspan="3">全国</td><td rowspan="3">一卡通卡<br />（借记卡） </td><td rowspan="2">大众版</td><td rowspan="2">500</td><td rowspan="2">500</td><td rowspan="2">5万</td><td rowspan="2">无限额</td><td rowspan="2">网上、电话</td></tr><tr></tr><tr><td>专业版USBKey</td><td>无限额</td><td>无限额</td><td>无限额</td><td>无限额</td><td>柜台</td></tr><tr><td colspan="8">备注：① 开通网上银行是进行电子支付的前提条件 <br /></td></tr>';
//			
//		}else if($(this).attr("id")=="pingAn"){
//			PHtml+='<tr class="interTitle"><td colspan="7">平安银行（普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td>开通方法</td></tr><tr><td rowspan="3">全国</td><td rowspan="3">借记卡</td><td>USBkey </td><td>50000</td><td>50000</td><td>无</td><td>柜台</td></tr><tr><td>动态口令</td><td>50000</td><td>50000</td><td>无</td><td>柜台</td></tr><tr><td>网银大众版</td><td>50000</td><td>50000</td><td>无</td><td>网站</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件 </td></tr>';
//			
//		}else if($(this).attr("id")=="zhongGuo"){
//			PHtml+='<tr class="interTitle"><td colspan="7">中国银行（借记卡）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td>开通方法</td></tr><tr><td>全国</td><td>借记卡</td><td>口令卡</td><td>5万</td><td>10万</td><td>无限额</td><td>柜台开通网上银行后在中行网银页面中自助开通网上支付功能</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件 </td></tr>';
//			
//		}else if($(this).attr("id")=="ningSheng"){
//			
//			PHtml+='<tr class="interTitle"><td colspan="7">民生银行&nbsp;&nbsp;&nbsp;（普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td>开通方法</td></tr><tr><td rowspan="4">全国</td><td rowspan="4">借记卡</td><td>大众版（民生卡用户）</td><td>300</td><td>300</td><td>300</td><td>网上开通</td></tr><tr><td>贵宾版（文件证书）</td><td>5000</td><td>5000</td><td>无限额</td><td>柜台开通</td></tr><tr><td>贵宾版（U宝）</td><td>2万</td><td>10万</td><td>无限额</td><td>柜台开通</td></tr><tr><td>网上银行VIP+(含单KEY)版</td><td>用户在柜台自行设定</td><td>用户在柜台自行设定</td><td>无限额</td><td>柜台开通</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件 <br /></td></tr>';
//			
//		}else if($(this).attr("id")=="youZheng"){
//			PHtml+='<tr class="interTitle"><td colspan="7">邮政储蓄（普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td>开通方法</td></tr><tr><td rowspan="3">全国</td><td rowspan="3">借记卡</td><td>手机动码</td><td>1万</td><td>1万</td><td>无限额</td><td>柜台开通</td></tr><tr><td>动态口令卡</td><td>10万</td><td>10万</td><td>无限额</td><td>柜台开通</td></tr><tr><td>UKEY+手机动码</td><td>100万</td><td>100万</td><td>无限额</td><td>柜台开通</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件 <br /></td></tr>';
//		}else if($(this).attr("id")=="zhongXin"){
//			PHtml+='<tr class="interTitle"><td colspan="7">中信银行（普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td width="70">支持卡种</td><td width="180">用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td width="190">开通方法</td></tr><tr><td rowspan="2">全国</td><td rowspan="2">理财宝卡</td><td>加强版文件证书</td><td>1000</td><td>5000</td><td>无限额</td><td rowspan="2">请携带银行卡和有效身份证件到中信银行网点柜台申请</td></tr><tr><td>加强版数字证书(USBKEY)</td><td>自定义</td><td>无限额</td><td>无限额</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件 <br /></td></tr>';
//			
//		}else if($(this).attr("id")=="guangDa"){
//			PHtml+='<tr class="interTitle"><td colspan="7">中国光大银行（借记卡）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td width="90">单卡单笔消费上限（元）</td><td width="90">单卡每日消费上限（元）</td><td width="90">总限额累计上限（元）</td><td width="170">开通方法</td></tr><tr><td rowspan="5">全国</td><td rowspan="5">借记卡</td><td>银行卡直接支付（手机动态密码）</td><td>5000</td><td>10000</td><td>无限额</td><td>柜台或网银专业版办理</td></tr><tr><td>银行卡直接支付（令牌动态密码）</td><td>50万</td><td>50万</td><td>无限额</td><td>柜台领取令牌</td></tr><tr><td>网银专业版支付（手机动态密码）</td><td>5000</td><td>10000</td><td>无限额</td><td>柜台或网银专业版办理</td></tr><tr><td>网银专业版支付（令牌动态密码）</td><td>50万</td><td>50万</td><td>无限额</td><td>柜台或网银专业版办理</td></tr><tr><td>网银专业版支付（阳光网盾）</td><td>50万</td><td>50万</td><td>无限额</td><td>柜台或网银专业版办理</td></tr><tr><td colspan="7">备注：开通网上银行是进行电子支付的前提条件 </td></tr>';
//		}else if($(this).attr("id")=="huaXia"){
//			
//			PHtml+='<tr class="interTitle"><td colspan="7">华夏银行（普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td>开通方法</td></tr><tr><td rowspan="3">全国</td><td rowspan="3">借记卡</td><td>非签约客户</td><td>300</td><td>1000</td><td>无限额</td><td>网上开通</td></tr><tr><td>证书/U-key</td><td>5000</td><td>5000</td><td>无限额</td><td>柜台申请开通</td></tr><tr><td>电子钱包用户</td><td>无限额</td><td>无限额</td><td>无限额</td><td>　</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件 <br /></td></tr>';
//		}else if($(this).attr("id")=="xingYe"){
//			PHtml+='<tr class="interTitle"><td colspan="7">兴业银行（普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td>开通方法</td></tr><tr><td rowspan="2">全国</td><td rowspan="2">兴业理财卡、<br />兴业易卡（借记卡）</td><td>开通网上支付<br />手机交易短信保护</td><td>1000元和5000元<br />可以自己在网上选择</td><td>1000元和5000元<br />可以自己在网上选择</td><td>1000元和5000元<br />可以自己在网上选择</td><td>柜台</td></tr><tr><td>证书<br />（普通证书，U盾）</td><td>自行设置限额</td><td>自行设置限额</td><td>自行设置限额</td><td>柜台</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件 <br /></td></tr>';
//			
//		}else if($(this).attr("id")=="puFa"){
//			PHtml+='<tr class="interTitle"><td colspan="7">浦发银行&nbsp;（普通）</td></tr><tr class="interTitle"><td></td><td colspan="5">电话：95528</td><td><a href="http://www.yeepay.com/html/help/bank_spdb_1.shtml">更多帮助</a></td></tr><tr><td>覆盖</td><td>支持</td><td rowspan="2">用户类型</td><td>每张卡单笔</td><td>每张卡每日</td><td rowspan="2">总限额累计上限</td><td rowspan="2">开通方法</td></tr><tr><td>区域</td><td>卡种</td><td>消费上限</td><td>消费上限</td></tr><tr><td rowspan="2">全国</td><td rowspan="2">借记卡</td><td>动态密码客户</td><td rowspan="2">10000/笔</td><td rowspan="2">10000/日</td><td rowspan="2">无限额</td><td rowspan="2">柜台</td></tr><tr><td>数字证书客户</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件</td></tr>';
//		}else if($(this).attr("id")=="guangFa"){
//			PHtml+='<tr class="interTitle"><td colspan="7">广发银行（普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td>开通方法</td></tr><tr><td rowspan="2">全国</td><td>手机动态验证码&nbsp;</td><td>借记卡</td><td>不限</td><td>3000</td><td>无限制</td><td rowspan="2">柜台开通网上<br />银行</td></tr><tr><td>Key盾&nbsp;</td><td>借记卡</td><td>不限</td><td>3000</td><td>无限制</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件 <br /></td></tr>';
//		
//		}else if($(this).attr("id")=="shenFa"){
//			PHtml+='<tr class="interTitle"><td colspan="7">深圳发展银行（普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td>开通方法</td></tr><tr><td rowspan="3">全国</td><td rowspan="3">借记卡</td><td>动态口令刮刮卡用户</td><td>5000元</td><td>5000元</td><td>无限额，限28次</td><td>暂不支持</td></tr><tr><td>动态口令编码器用户</td><td>无限额</td><td>无限额</td><td>无限额</td><td rowspan="2">柜台</td></tr><tr><td>U盾</td><td>无限额</td><td>无限额</td><td>无限额</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件 <br /></td></tr>';
//		}else if($(this).attr("id")=="beiJing"){
//			PHtml+='<tr class="interTitle"><td colspan="7">北京银行（普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td>开通方法</td></tr><tr><td rowspan="3">北京 天津 西安 上海<br />深圳 南京 长沙 济南<br />南昌、杭州<br /></td><td rowspan="3">储蓄卡/京卡（借记卡）</td><td>普通</td><td>300</td><td>300</td><td>300</td><td>网上</td></tr><tr><td>动态密码</td><td>1000</td><td>5000</td><td>无限额</td><td>柜台</td></tr><tr><td>证书</td><td>100万</td><td>100万</td><td>无限额</td><td>柜台</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件 </td></tr>';
//		
//		}else if($(this).attr("id")=="beiNongShang"){
//			PHtml+='<tr class="interTitle"><td colspan="7">北京农商行（普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td>开通方法</td></tr><tr><td rowspan="2">北京</td><td rowspan="2">借记卡</td><td>手机验证客户</td><td>10万</td><td>50万</td><td>无限额</td><td rowspan="2">柜台申请开通</td></tr><tr><td>证书验证客户</td><td>20万</td><td>100万</td><td>无限额</td></tr><tr><td colspan="7">备注：开通网上银行是进行电子支付的前提条件 </td></tr>';
//		
//		}else if($(this).attr("id")=="shangHai"){
//			PHtml+='<tr class="interTitle"><td></td><td colspan="6">电话：95594</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td width="250">用户类型</td><td>每张卡单笔消费上限</td><td>每张卡每日消费上限</td><td>总限额累计上限</td><td width="170">开通方法</td></tr><tr><td rowspan="2">网银客户</td><td rowspan="2">借记卡</td><td>办理 E 盾证书版个人网银，开通网上支付功能</td><td>50万</td><td>100万</td><td>-</td><td rowspan="2">限额范围内客户可自行设置单笔 / 日累计限额</td></tr><tr><td>办理动态密码版个人网银（含文件证书）,开通网上支付功能</td><td>6000元</td><td>1万</td><td>-</td></tr><tr><td>上银快付客户</td><td>借记卡</td><td>签约上银快付业务</td><td>2000元</td><td>-</td><td>5000元</td><td>　</td></tr><tr><td  colspan="7">备注：开通网上银行是进行电子支付的前提条件 </td></tr>';
//		
//		}
		
		PHtml+=LLBankInfo[$(this).attr("id")].tableInfo;

		$(".internateTable").html(PHtml);
		setBankCode($(this));
		bottomB();
	});

	
    $(".zhezhao5").css("height",document.body.clientHeight);
    $(".zhezhao5 .zheimg").css("margin-top",document.body.clientHeight/2-100);
    var yi=1;
    $(".inter_more").click(function(){
    	if(yi==1){
    		$(".cange").slideDown(500);
    		$(".inter_more").html("收起");
    		yi=0;
    	}else{
    		$(".cange").slideUp(500);
    		$(".inter_more").html("展开更多>>");
    		yi=1;
    	}
    });
    
    
	//点击充值页确认支付
	$("#re_mo_btn").click(function(){
//		smtEbank();
		smtLLEbank();
	});
	
    
	
	//点击提交订单，去支付时(无卡)
	function smtEbank(){
		var b2 = moneyf($("#moneybk"),0);
		if(b2==""){
			$.ajax({
				url:rootPath+"/payment/rechargeOnline",
				type:"post",
				data:$("#rechargeEBank").serialize(),
				error : function(data) {
					var data = data.responseText;
					var _data = eval('('+data+')');
					$("#errorMsg").val(_data.errorMsg);
					$("#form_result").attr("action", rootPath + "/recharge/rechargeFailure").submit();
				},
				success:function(data){
					var _data =  eval("("+data+")");
					if(!_data.isSuccess){
						if(_data.id=='redirect'){
							//跳转值错误页面
							$("#errorMsg").val(_data.info);
							$("#form_result").attr("action", rootPath + "/recharge/rechargeFailure").submit();
						}else{
							//校验错误
							if(typeof(_data.tocken)!="undefined"){
								$("#token").val(_data.tocken);
							}
							$(".zhezhao5").hide();
							$("#"+_data.id).addClass("ipt-error").parent().siblings("em").html(_data.info).removeClass("hui");
						}
					}else{
						$(".zhezhao").show();
						$("#interdvi").slideDown(500);
						$("#rechargeCode").val(_data.rechargeCode);
						window.open(_data.info);
					}
				}
			});
		}

			// 提交操作-【结束】。
	}
	function smtLLEbank(){
		var b2 = moneyf($("#moneybk"),0);
		if(b2==""){
			$.ajax({
				url:rootPath+"/recharge/llGatewayRecharge",
				type:"post",
				data:$("#rechargeEBank").serialize(),
				error : function(data) {
					var data = data.responseText;
					var _data = eval('('+data+')');
					$("#errorMsg").val(_data.errorMsg);
					$("#form_result").attr("action", rootPath + "/recharge/rechargeFailure").submit();
				},
				success:function(data){
					var _data =  eval("("+data+")");
					if(!_data.isSuccess){
						if(_data.id=='redirect'){
							//跳转值错误页面
							$("#errorMsg").val(_data.info);
							$("#form_result").attr("action", rootPath + "/recharge/rechargeFailure").submit();
						}else{
							//校验错误
							if(typeof(_data.tocken)!="undefined"){
								$("#token").val(_data.tocken);
							}
							$(".zhezhao5").hide();
							$("#"+_data.id).addClass("ipt-error").parent().siblings("em").html(_data.info).removeClass("hui");
						}
					}else{
						$(".zhezhao").show();
						$("#interdvi").slideDown(500);
						$("#rechargeCode").val(_data.rechargeCode);
//						window.open(_data.info);
						//ie判断
						/*var referForm=document.createElement("form");
						referForm.action= _data.info;
						referForm.method="post";
						referForm.target="_blank";
						document.body.appendChild(referForm);
						referForm.submit();*/
                        var a = $("<a href='"+_data.info+"' target='_blank' ></a>").get(0);
                        var e = document.createEvent('MouseEvents');
                        e.initEvent('click', true, true);
                        a.dispatchEvent(e);
					}
				}
			});
		}

			// 提交操作-【结束】。
	}
	//查询充值结果
	$(".payresult").click(function(){
		$("#recharge_result").attr("action",rootPath+"/recharge/getRechargeResult");
		$("#recharge_result").submit();
	});
	
	  $(".inter-group").find(".choose").find("img").click();
});




function searchHtml(page,rows){
	var thtml="";
	$.ajax({
		url:rootPath+"/recharge/rechargeList",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<d_rows.length;i++){
				var _data = d_rows[i];
				var status = "";
				if(_data.status=='0')
					status='充值中';
				if(_data.status=='1')
					status='充值成功';
				if(_data.status=='2')
					status='充值失败';

//				thtml+='<tr class="huip">';
//				thtml+=	'<td>'+dateTimeFormatter(_data.createTime)+'</td>';
//				thtml+=	'<td>'+fmoney(_data.amount,2)+'元</td>';
//				thtml+='<td>'+_data.encryptCardNo+'</td>';
//				thtml+='<td>'+status+'</td>';
//				thtml+='</tr>';
				thtml += '<li class="txjl-border">' ;
				thtml += '<ul class="tx-ul-list">' ;
				thtml += '<li class="tx-ul-list-li">'+dateTimeFormatter(_data.createTime)+'</li>' ;
				thtml += '<li><b>'+fmoney(_data.amount,2)+'</b></li>' ;
				thtml += '<li>'+_data.encryptCardNo+'</li>' ;
				thtml += '<li>'+status+'</li>';
				thtml+='</ul></li>';
				
			}
			var th = $('.txjl-list #rechargeRecords li')[0];
			$('.txjl-list #rechargeRecords').html("");
			$('.txjl-list #rechargeRecords').append($(th)).append(thtml);
			if(d_rows.length>0){
				$(".tcdPageCode").createPage({
					pageCount:pageCount,
					current:1,
					backFn:function(p){
						//console.log(p);
						//点击分页效果
						searchHtml_1(parseInt(p),5);
					}
				});
			}else{
				$(".tcdPageCode").html("");
			}
			
			bottomB();
		}
	});
}
$(document).ready(function() {
	//默认触发银行卡是焦点事件
	$("#bankid").blur();
	formatBankNo($("#bankid").get(0));
});


function searchHtml_1(page,rows){
	var thtml="";
	$.ajax({
		url:rootPath+"/recharge/rechargeList",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<d_rows.length;i++){
				var _data = d_rows[i];
				var status = "";
				if(_data.status=='0')
					status='充值中';
				if(_data.status=='1')
					status='充值成功';
				if(_data.status=='2')
					status='充值失败';

//				thtml+='<tr class="huip">';
//				thtml+=	'<td>'+dateTimeFormatter(_data.createTime)+'</td>';
//				thtml+=	'<td>'+fmoney(_data.amount,2)+'元</td>';
//				thtml+='<td>'+_data.encryptCardNo+'</td>';
//				thtml+='<td>'+status+'</td>';
//				thtml+='</tr>';
				thtml += '<li class="txjl-border">' ;
				thtml += '<ul class="tx-ul-list">' ;
				thtml += '<li class="tx-ul-list-li">'+dateTimeFormatter(_data.createTime)+'</li>' ;
				thtml += '<li><b>'+fmoney(_data.amount,2)+'</b></li>' ;
				thtml += '<li>'+_data.encryptCardNo+'</li>' ;
				thtml += '<li>'+status+'</li>';
				thtml+='</ul></li>';
			}
//			var th = $('.tablep tbody').children()[0];
//			$('.tablep').html("");
//			$('.tablep').append($(th)).append(thtml);
			var th = $('.txjl-list #rechargeRecords li')[0];
			$('.txjl-list #rechargeRecords').html("");
			$('.txjl-list #rechargeRecords').append($(th)).append(thtml);
		}
	});
}

	function setBankCode(obj){
		$("#bkcode").val(obj.attr("code"));
	}


var flag = 0;
function start(){
	var text = document.getElementById("bindcard");
	if(text==null)
		return;
	if (!flag)
	{
		text.style.boxShadow = "0px 0px 8px #ff7088";
		flag = 1;
	}else{
		text.style.boxShadow = "none";
		flag = 0;
	}
	setTimeout("start()",1000);
}

