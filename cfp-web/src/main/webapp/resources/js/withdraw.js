// JavaScript Document
$(function(){
//	searchHtml(1,10);
	searchHtml(1,5);

	//点击提交订单
	$("#wdbtn").click(function(){
		var t1=moneyf($("#moneyp"));
		var t2=$("#rankm").is(":visible")?passwordf($("#rankm"),"1"):'';
		var t4=validf($("#valid"),0);
		if( t1=="" && t2=="" && t4==""){
			
			//提现输入信息验证，提交
			$.ajax({
				url:rootPath+"/person/withDraw",
				type:"post",
				data:{"moneyp":$("#moneyp").val(),"valid":$("#valid").val(),"rankm":$("#rankm").val(),"voucher":$("#check").is(":checked")},
				error : function(XHR) {
					var res = eval("(" + XHR.responseText + ")");
					$("#errorMsg").html(res.errorMsg);
					$("#with_falure").click();
				},
				success:function(data){
					if(data=='success'){
						//提现成功
						$("#with_success").click();
					}else if(data.toString().indexOf('error')!=-1){
						//提现失败
						var arr = data.split(":");
						$("#errorMsg").html(arr[1]);
						$("#with_falure").click();
					}else{
						var valid =  eval("("+data.valid+")");
						var bidpass =  eval("("+data.bidpass+")");
						var amount =  eval("("+data.amount+")");
						var times =  eval("("+data.times+")");
						validError(valid);
						validError(bidpass);
						validError(amount);
						validError(times);
					}
				}
			});

		}
		
	});

	//提现成功
	$("#queren_withdraw").click(function(){
		//跳转至个人首页
		window.location.href = rootPath+"/person/account/overview";
	});
	//提现失败
	$("#f_queren_withdraw").click(function(){
		//本页面刷新
		window.location.href = rootPath+"/person/toWithDraw";
	});

	$("#close_withdraw").click(function(){
		//跳转至个人首页
		window.location.href = rootPath+"/person/account/overview";
	});

	function validError(v){
		if(!v.isSuccess){
			var id = v.id;
			$("#"+id).addClass("ipt-error").parent().siblings("em").html(v.info).removeClass("hui");;
		}
		return v.isSuccess;
	}
	
	//判断交易密码
	$("#rankm").blur(function(){
		passwordf($(this),"0");
	});
	
	//金额输入验证
	$("#moneyp").blur(function(){
		if(!$(this).val()==""){
			moneyf($(this));	
			
		}else{
			massage="";
			$(this).removeClass("ipt-error").parent().siblings("em").html($("#tip").html()).addClass("hui");;
		}
	});
	//验证码输入验证
	$("#valid").blur(function(){
		validf($(this),1);
	});
	
	//计算打卡金额
	$("#moneyp").keyup(function(){
		$("#bamoney").html(fmoney(bamoneyf($(this)),2));
	});
	function bamoneyf(bamoval){
		var vaba=parseFloat(bamoval.val())>0?parseFloat(bamoval.val()):0;

		var rate=parseFloat($("#barate").val());
		if($("#check").is(":checked")){
			rate = 0;
		}
		if(vaba<rate){
			rate = 0;
		}
		return vaba-rate;
	}
	//验证码
	function validf(vali,va5){
		if(vali.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(va5==0){
				massage="请您输入验证码";
				vali.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");;
			}else{
				
				massage="";
				vali.removeClass("ipt-error").parent().siblings("em").html(massage).addClass("hui");
			}
		}else{
			
			if(vali.val().length==6 && /^([0-9.]+)$/.test(vali.val())){
				/*
				var djs=0;
				djs=1;
				if(djs==1){
						
					$("#getvalid").attr("disabled",false);
					$("#getvalid").addClass("btn-blue").removeClass("btn-gray");
					$("#getvalid").html('重新获取');
					clearTimeout(interval); 
				}
				
				*/
				massage="";
				vali.removeClass("ipt-error").parent().siblings("em").html(massage).addClass("hui");
				
			}else{
				
				massage="验证码6位数字";
				vali.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}
		}
		return massage;
	}
	
	//倒计时
	$("#getvalid").click(function(){

		if(moneyf($("#moneyp"))==''){
			var phoneNo = $("#phone").val();
			$.ajax({
				url:rootPath+'/person/withDraw/getMsg',
				type:"POST",
				success:function(msg){
					//no deal
				}
			});

			timer($(this));
		}

	});
	var intDiff = parseInt(59);//倒计时总秒数量
	function timer(timeval){
		
		timeval.attr("disabled",true);
		timeval.html('60s后重新获取');
		$("#getvalid").addClass("btn-gray").removeClass("btn-blue");
		intDiff=59;
		interval=window.setInterval(function(){
		timeval.html(intDiff+'s后重新获取');
		if(intDiff<=0){
			timeval.attr("disabled",false);
			$("#getvalid").addClass("btn-blue").removeClass("btn-gray");
			timeval.html('重新获取');
			clearTimeout(interval); 
			return;
		}
		intDiff--;
		}, 1000);
	}

	
	//判断交易密码
	function passwordf(passval1,pa){
		if(passval1.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(pa=="0"){
				massage="";
				var tip = $("#bidpass").html();
				passval1.removeClass("ipt-error").parent().siblings("em").html(tip).addClass("hui");
				
			}else{
				massage="请您输入交易密码！";
				passval1.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
				
			}
		}else{
			
			if(passval1.val().length>=6 && passval1.val().length<=16 && /^[0-9a-zA-Z]+$/.test(passval1.val())){

				massage="";
				var tip = $("#bidpass").html();
				passval1.removeClass("ipt-error").parent().siblings("em").html(tip).addClass("hui");
			}else{
				
				massage="请输入6~16位字符，支持字母及数字,字母区分大小写";
				passval1.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}
			
		}
		return massage;
	}

	
		//借款金额输入验证
	function moneyf(mond){
		//提现次数
		var mondint;
		if(mond.val()==""|| !/^([0-9.]+)$/.test(mond.val())){
			mondint=0;
			massage="请输入正确的提现金额";
			mond.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");

			return massage;
		}else{

			mondint=parseFloat(mond.val());
		}

		if(mondint<100 ){

			massage="请输入大于100元的金额！";
			mond.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
		}else{

			if(rmoney($("#usemoney").val())<mondint){

				massage="提现金额不能大于可提现金额";
				mond.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
				
			}else{
				
				if(rmoney($("#sxmon").val())<mondint){
					
					massage="不能超过提现金额上限";
					mond.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
				}else{
					
					massage="";
					mond.removeClass("ipt-error").parent().siblings("em").html($("#tip").html()).addClass("hui");
				
				}
			}
		}
		return massage;
	}
	
	

});

function searchHtml(page,rows){
	var thtml="";
	$.ajax({
		url:rootPath+"/person/withDrawList",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<d_rows.length;i++){
				var _data = d_rows[i];
				var status = "";
				if(_data.verifyStatus=='0'&&_data.transStatus=='0')
					status='审核中';
				if(_data.verifyStatus=='2')
					status='审核驳回';
				if(_data.verifyStatus=='1'&&(_data.transStatus=='0'||_data.transStatus=='1'||_data.transStatus=='2'))
					status='审核通过待打款';
				if(_data.verifyStatus=='1'&&_data.transStatus=='3')
					status='提现成功';
				if(_data.verifyStatus=='1'&&_data.transStatus=='4')
					status='提现失败';
//				if(i % 2 !=0)
//					thtml +='<li class="txjl-border"><ul class="tx-ul-list">';
//				else
//					thtml +='<tr class="huip">';
//				thtml+=	'<td>'+dateTimeFormatter(_data.createTime)+'</td>';
//				thtml+=	'<td>'+fmoney(_data.withdrawAmount,2)+'元</td>';
//				thtml+='<td>'+_data.shortCardNo+'</td>';
//				thtml+='<td>'+status+'</td>';
//				thtml+='</tr>';
				thtml += '<li class="txjl-border"><ul class="tx-ul-list">' ;
				thtml += '<li class="tx-ul-list-li">'+dateTimeFormatter(_data.createTime)+'</li>' ;
				thtml += '<li><b>'+fmoney(_data.withdrawAmount,2)+'元</b></li>' ;
				thtml += '<li>'+_data.shortCardNo+'</li>' ;
				thtml += '<li>'+status+'</li></ul></li>' ;
			}
			
		
//			var th = $('.tablep tbody').children()[0];
//			$('.tablep').html("");
//			$('.tablep').append($(th)).append(thtml);
			var th = $('.txjl-list ul.txjl-ul-big li')[0];
			$('.txjl-list ul.txjl-ul-big').html("");
			$('.txjl-list ul.txjl-ul-big').append($(th)).append(thtml);
			bottomB();
			if(d_rows.length>0){
				$(".tcdPageCode").createPage({
					pageCount:pageCount,
					current:1,
					backFn:function(p){
						//console.log(p);
						//点击分页效果
//						searchHtml_1(parseInt(p),10);
						searchHtml_1(parseInt(p),5);
					}
				});
			}else{
				$(".tcdPageCode").html("");
			}
		}
	});
}

function searchHtml_1(page,rows){
	var thtml="";
	$.ajax({
		url:rootPath+"/person/withDrawList",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<d_rows.length;i++){
				var _data = d_rows[i];
				var status = "";
				if(_data.verifyStatus=='0'&&_data.transStatus=='0')
					status='审核中';
				if(_data.verifyStatus=='2')
					status='审核驳回';
				if(_data.verifyStatus=='1'&&(_data.transStatus=='0'||_data.transStatus=='1'||_data.transStatus=='2'))
					status='审核通过待打款';
				if(_data.verifyStatus=='1'&&_data.transStatus=='3')
					status='提现成功';
				if(_data.verifyStatus=='1'&&_data.transStatus=='4')
					status='提现失败';
//				thtml+='<tr class="huip">';
//				thtml+=	'<td>'+dateTimeFormatter(_data.createTime)+'</td>';
//				thtml+=	'<td>'+fmoney(_data.withdrawAmount,2)+'元</td>';
//				thtml+='<td>'+_data.shortCardNo+'</td>';
//				thtml+='<td>'+status+'</td>';
//				thtml+='</tr>';
				
				thtml += '<li class="txjl-border"><ul class="tx-ul-list">' ;
				thtml += '<li class="tx-ul-list-li">'+dateTimeFormatter(_data.createTime)+'</li>' ;
				thtml += '<li><b>'+fmoney(_data.withdrawAmount,2)+'元</b></li>' ;
				thtml += '<li>'+_data.shortCardNo+'</li>' ;
				thtml += '<li>'+status+'</li></ul></li>' ;
				
			}
//			var th = $('.tablep tbody').children()[0];
//			$('.tablep').html("");
//			$('.tablep').append($(th)).append(thtml);
			var th = $('.txjl-list ul.txjl-ul-big li')[0];
			$('.txjl-list ul.txjl-ul-big').html("");
			$('.txjl-list ul.txjl-ul-big').append($(th)).append(thtml);
		}
	});
}


$(function(){
	$("#check").attr("checked",false);
	$(".labelwidth,#check").click(function(){
		if($("#check").is(":checked")){
			$(".labelwidth").addClass("checked");
			$("#shouyufei").css("text-decoration","line-through");
			getUseCFQ();
		}else{
			$(".labelwidth").removeClass("checked");
			$("#shouyufei").css("text-decoration","none");
			getNotUseCFQ();
		}
	});

	function getUseCFQ(){
		var vaba=parseFloat($("#moneyp").val())>0?parseFloat($("#moneyp").val()):0;
		$("#bamoney").html(fmoney(vaba,2));

	}
	function getNotUseCFQ(){
		var vaba=parseFloat($("#moneyp").val())>0?parseFloat($("#moneyp").val()):0;
		rate=parseFloat($("#barate").val());
		if(vaba>rate){

			$("#bamoney").html(fmoney(vaba-rate,2));
		}

	}


});


