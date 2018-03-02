// JavaScript Document
	//还原小数
	function rmoney(s) {  
		return parseFloat(s.replace(/[^\d\.-]/g, ""));  
	}  
window.onload=function(){
	showHidePwd();
	var btn2=document.getElementById("btn2");
	var light=document.getElementById('light');
	var fade=document.getElementById('fade');
	var light1=document.getElementById('light1');
	var fade1=document.getElementById('fade1');
	var closebt=document.getElementById("closebt");
	var backgd=document.getElementById("backgd");
	//var closebtx=document.getElementById("closebtx");

$.each($(".xsize"),function(){
	var len = $(this).text().length;
	if(len<8){
		$(this).css("font-size","2rem");
	}else if(len>=8 && len<11){
	$(this).css("font-size","1.6rem");
	}else if(len>=11){
	$(this).css("font-size","1rem");	
	}

	
});
var isVerified =document.getElementById("isVerified");

var isCustomerCard =document.getElementById("isCustomerCard");

//判断是否实名认证
if('1'==isVerified.value){
	light1.style.display='none';
	fade1.style.display='none';
//	backgd.style.overflow='visible'
}else{
	light1.style.display='block';
	fade1.style.display='block';
//	backgd.style.overflow='hidden'; 
}
//判断绑卡
if('false' == isCustomerCard.value){
	light.style.display='block';
	fade.style.display='block';
	//backgd.style.overflow='hidden'; 
}else{
	light.style.display='none';
	fade.style.display='none';
	//backgd.style.overflow='visible'
}

var wait=60;
document.getElementById("btn").disabled = false;  
function time(o) {
        if (wait == 0) {
            o.removeAttribute("disabled");          
            o.value="获取验证码";
            wait = 60;
        } else {
            o.setAttribute("disabled", true);
            o.value="" + wait + "秒";
            wait--;
            setTimeout(function() {
                time(o)
            },
            1000)
        }
    }
var phoneFlag=false;
//发送验证码  王亚东  手机号码在后端获取
document.getElementById("btn").onclick=function(){
 
	var t4=validf($("#yzm"),0);
	var t1=moneyf(recharge);
	if(t1=="" ){
 
	$.ajax({
		url:rootPath+"/person/withDraw/getMsg",
		type:"post",
		success:function(data){
			time(document.getElementById("btn"));
		}
	});
	//time(this);
	 
	}else{
		publicFailAndTipNoReplaceId(alart,btn2,t1);
	}
}
function validError(v){
	if(!v.isSuccess){
		 document.getElementById("alart").innerHTML = v.info;
		 alart.style.display="block";
		 setTimeout("clean()",3000);
	}
	return v.isSuccess;
}
$("#btn2").bind("click",function(){
		var t1=moneyf(recharge);
		var t2=$("#psw").is(":visible")?passwordf($("#psw"),"1"):'';
		var t4=validf($("#yzm"),0);
		if( t1=="" && t2=="" && t4==""){
			//提现输入信息验证，提交
			$.ajax({
				url:rootPath+"/person/withDraw",
				type:"post",
				data:{"moneyp":document.getElementById("recharge").value,"valid":document.getElementById("yzm").value,"rankm":document.getElementById("psw").value,"voucher":$("#checkbtn").hasClass("l_check")},
				success:function(data){
					if(typeof(data)=="string"){
						if(data.indexOf('success')>=0){
							//提现成功
							var withdrawId=data.split(",")[1];
							window.location.href = rootPath+"/person/withDrawResult?withdrawId="+withdrawId;
							/*var light2 = document.getElementById("light2");
							var fade2 = document.getElementById("fade2");
							light2.style.display='block';
							fade2.style.display='block';*/
//							backgd.style.overflow='hidden'; 
//						window.location.href = rootPath+"/person/account/overview";
						}else if(data=='error'){
							//提现失败
							publicFailAndTip(alart,btn2,"提现失败",recharge);
							$("#btn2").bind("click");
						}
					}else{
						var valid =  eval("("+data.valid+")");
						var bidpass =  eval("("+data.bidpass+")");
						var amount =  eval("("+data.amount+")");
						var times =  eval("("+data.times+")");
						validError(valid);
						validError(bidpass);
						validError(amount);
						validError(times);
						$("#btn2").bind("click");
					}
				}
			});
		}

})

//密码显示和隐藏的效果
var flag=0;
function showHidePwd(){
	$("#clicks").on("click",function(){
		if(flag==0){
			$("#psw").prop("type","text");
			$(this).addClass("click");
			flag=1;
		}else if(flag==1){
			$("#psw").prop("type","password");
			$(this).removeClass("click");
			flag=0;
		}
	})
}

$('input[type="button"]').on('click', function(){
    $('#frm').submit();
}); 
$('#test').load(function(){
    var res = $(this).contents().find('body').text();
});
 
};
//提现页面提现
function checkinput4(){
		var pa =passwordf($(psw));
		var va =validf($(yzm));
		var mo = moneyf(recharge);
		if( mo=="" && va=="" && pa==""){
			publicSucess(alart,btn2);
		}
}
function clearNoNum(obj)
    {
        //先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d.]/g,"");
        //必须保证第一个为数字而不是.
        obj.value = obj.value.replace(/^\./g,"");
        //保证只有出现一个.而没有多个.
        obj.value = obj.value.replace(/\.{2,}/g,".");
        //保证.只出现一次，而不能出现两次以上
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
    }
function limitNUM(n){
    var leg = $(".limitNUM"+n).val().length;
    if (leg > n) {
		$(".limitNUM"+n).val($(".limitNUM"+n).val().substring(0,n));
    }
}
//借款金额输入验证
function moneyf(mond){
	//提现次数
	var mondint;
	if(mond.value==''|| !/^([0-9.]+)$/.test(mond.value)){
		mondint=0;
		    massage="请输入正确的提现金额";
		    publicFailAndTip(alart,btn2,massage,recharge);
		    setTimeout("clean()",3000);
			return massage;
	}else{
		mondint=parseFloat(mond.value);
	}
	if(mondint<100 ){
		    massage="请输入大于100元的金额！";
		    publicFailAndTip(alart,btn2,massage,recharge);
		    setTimeout("clean()",3000);
		    return massage;
	}else{
		if(rmoney(document.getElementById('sxmon').value)<mondint){
			massage="提现金额不能大于可提现金额";
		    publicFailAndTip(alart,btn2,massage,recharge);
		    setTimeout("clean()",3000);
		    return massage;
		}else{
			if(rmoney(document.getElementById('usemoney').value)<mondint){
				massage="不能超过提现金额上限";
				publicFailAndTip(alart,btn2,massage,recharge);
			    setTimeout("clean()",3000);
			    return massage;
			}
			else{
				massage="";
				return massage;
			}
		}
	}
	return massage;
}

//验证码
function validf(vali,va5){
	if(vali.val()=="请输入短信验证码" ){ //只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
			massage="请您输入验证码";
//			publicFailAndTip(alart,btn2,massage,yzm);
//		    setTimeout("clean()",3000);
		    return massage;
	}else{
		if(vali.val().length==6 && /^([0-9.]+)$/.test(vali.val())){
			return "";
		}else{
			massage="验证码6位数字";
			publicFailAndTip(alart,btn2,massage,yzm);
		    setTimeout("clean()",3000);
		    return massage;
		}
	}
	return massage;
} ;
//判断交易密码
function passwordf(passval1,pa){
//	alert(passval1.val()+"shiyi");
	if(passval1.val()=="" ){//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		if(pa=="0"){
			massage="交易密码默认与登录密码相同，为保证您的账号安全，请尽快修改！";
			publicFailAndTip(alart,btn2,massage,psw);
		    setTimeout("clean()",3000);
		    return massage;
		}else{
			massage="请您输入交易密码！";
//			publicFailAndTip(alart,btn2,massage,psw);
//		    setTimeout("clean()",3000);
		    return massage;
		}
	}else{
		if(passval1.val().length>=6 && passval1.val().length<=16 && /^[0-9a-zA-Z]+$/.test(passval1.val())){
			return "";
		}else{
			massage="请输入6~16位字符，字母加数字组合，字母区分大小写";
			publicFailAndTip(alart,btn2,massage,psw);
		    setTimeout("clean()",3000);
		    return massage;
		  
		}	
	}
	return massage;
}
//王亚东  页面上方提示框id 、 提交表单按钮id  默认的可为空
function publicSucess(alertID ,btn2Id){
	alertID.innerHTML= '';
	btn2Id.disabled="";
	btn2Id.style.backgroundColor="#fe2a4d";
	btn2Id.style.color="white";
}

//王亚东  页面上方提示框id 、 提交表单按钮id 、错误信息 默认的可为空、replaceId 错误框提示位置（变红）
function publicFailAndTip(alertID ,btn2Id,massage,replaceId){
	btn2Id.disabled="false";
	btn2Id.style.backgroundColor="#cacaca";
	btn2Id.style.color="#989898";
	alertID.innerHTML= massage;
    alertID.style.display="block";
    replaceId.style.border="solid 1px red"
    setTimeout("clean()",3000);
}
//王亚东  页面上方提示框id 、 提交表单按钮id 、错误信息 默认的可为空
function publicFailAndTipNoReplaceId(alertID ,btn2Id,massage){
	btn2Id.disabled="false";
	btn2Id.style.backgroundColor="#cacaca";
	btn2Id.style.color="#989898";
	alertID.innerHTML= massage;
    alertID.style.display="block";
    setTimeout("clean()",3000);
}
function clean(){
	 document.getElementById("alart").style.display="none";
	 yzm.style.border="solid 1px #d2d4dc";
	 psw.style.border="solid 1px #d2d4dc";
	 recharge.style.border="solid 1px #d2d4dc";
	};
	//
	function checkSueccess(){
		var recharge = document.getElementById("recharge").value;
		var yzm = document.getElementById("yzm").value;
		var psw =document.getElementById("psw").value;
		if(recharge != '' && yzm != '' && yzm != '请输入短信验证码' && psw != ''&& psw != '输入支付密码'){
			document.getElementById('btn2').disabled="";
//			document.getElementById("alart").innerHTML= '';
//			document.getElementById('btn2').disabled="";
			document.getElementById('btn2').style.backgroundColor="#fe2a4d";
			document.getElementById('btn2').style.color="white";
		}else{
			document.getElementById('btn2').disabled="true";
		}
	}