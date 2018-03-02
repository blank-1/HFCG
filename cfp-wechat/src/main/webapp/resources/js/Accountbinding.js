// JavaScript Document
window.onload=function(){
	
	var btn2=document.getElementById("btn2");
	var light=document.getElementById('light');
	var fade=document.getElementById('fade');
	var backgd=document.getElementById("backgd");
	var closebtx=document.getElementById("closebtx");
	var light1=document.getElementById('light1');
	var fade1=document.getElementById('fade1');
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
var isVerified =document.getElementById("isVerified");
//判断是否实名认证
if('1'==isVerified.value){
	light1.style.display='none';
	fade1.style.display='none';
	backgd.style.overflow='visible'
}else{
	light1.style.display='block';
	fade1.style.display='block';
	backgd.style.overflow='hidden'; 
}
document.getElementById("btn").onclick=function(){
	var b3=phonef($("#telnum2"),0);
	var b2=$("#bankid").is(":visible")?bankf($("#bankid"),"1"):'';
	if(""==b2){
	if(phonef($("#telnum2"),0)==""){
		time(this);
		var phone = $("#telnum2").val();
		var carNo = $("#bankid").val();
		$.post(rootPath+'/bankcard/send_sms',{
			phone:$("#telnum2").val(),cardNo:$("#bankid").val()},
	    		function(data){
	    	    	if(data.result == 'success'){
	    	    		
	    	    	}else if(data.result == 'error'){
	    	    		if(data.errCode == 'check'){
	    	    		    publicFailAndTip(alart,btn2,data.errMsg);
	    	    		}else{
	    	    			publicFailAndTip(alart,btn2,data.errMsg);
	    	    		}

	    	    	}else{
    	    		    publicFailAndTip(alart,btn2,"网络异常，请稍后操作！");
	    	    	}
	    	},'json');
			// 获取验证码-【结束】。 
	}else{
		publicFailAndTip(alart,btn2,b3);
	}
	}else{
		 publicFailAndTip(alart,btn2,b2);
	}
}

btn2.onclick=function(){
	var b2=$("#bankid").is(":visible")?bankf($("#bankid"),"1"):'';
	var b3=phonef($("#telnum2"),0);
	var b4=validf($("#yzm"),0);
	if( b2=="" && b3=="" && b4==""){// 提交操作-【开始】。
		var bankid = $("#bankid").val();//银行卡号
		var phone = $("#telnum2").val();//银行预留手机号
		var valid = $("#yzm").val();//验证码
		$.post(rootPath+'/bankcard/save_bankcard_add',{bankid:bankid,phone:phone,valid:valid},
    	function(data){
    	    	if(data.result == 'success'){
    	    		light.style.display='block';
    	    		fade.style.display='block';
    	    		backgd.style.overflow='hidden'
    	    	}
			else if(data.result == 'error'){
    	    		if(data.errCode == 'check'){
    	    			publicFailAndTip(alart,btn2,data.errMsg);
    	    		}else{
    	    			publicFailAndTip(alart,btn2,data.errMsg);
    	    		}
    	    	}else{
    	    		publicFailAndTip(alart,btn2,"网络异常，请稍后操作！");
    	    	}
    	},'json');// 提交操作-【结束】。
	}

}
//closebtx.onclick=function(){
//light.style.display='none';
//fade.style.display='none';
////backgd.style.overflow='visible'
//}
};
function ps(){
if (psw.type="password")
box.innerHTML="<input type=\"html\" name=\"psw\" id=\"psw\" style=\"color:#4b4b4b;\" value="+psw.value+">";
click.innerHTML="<a onClick=\"return txt()\"><img src=\"img/eye.png\" /></a>"};
	function txt(){
if (psw.type="text")
box.innerHTML="<input type=\"password\" name=\"psw\" id=\"psw\" style=\"color:#4b4b4b;\"  value="+psw.value+">";
click.innerHTML="<a onClick=\"return ps()\"><img src=\"img/eye1.png\" /></a>"};

$('input[type="button"]').on('click', function(){
    $('#frm').submit();
}); 
$('#test').load(function(){
    var res = $(this).contents().find('body').text();
});
function checkInput5(){
	var b4=validf($("#yzm"),0);
	var b3=phonef($("#telnum2"),0);
	var b2=$("#bankid").is(":visible")?bankf($("#bankid"),"1"):'';
	if( b2=="" && b3=="" && b4==""){// 提交操作-【开始】。
		publicSucess(alart,btn2);
	}
}
//////////////////////////////////////////////////////////
//判断银行卡号是否正确
function bankf(bankv,pa){
	if(bankv.val()=='请输入银行卡号' ){
		if(pa=="0"){
			massage="";
		}else{
			massage="请输入银行卡号";
//			publicFailAndTip(alart,btn2,massage,bankid);
		}	
	}else{
		if(bankv.val().replace(/\s/g, "").length<=19 && bankv.val().replace(/\s/g, "").length>=15){
				checkCard(bankv.val());//校验卡号
			massage="";
		}else{
			massage="银行卡号格式错误！";
			publicFailAndTip(alart,btn2,massage,bankid);
		} 
	}
	return massage;
}

//验证码
function validf(vali,va8){
	if(vali.val()=="请输入短信验证码" ){//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		if(va8==0){
			massage='请您输入验证码';
//			publicFailAndTip(alart,btn2,massage,yzm);
		}else{
			massage="";
		}
	}else{
		if(vali.val().length==6 && /^([0-9.]+)$/.test(vali.val())){
			massage="";
		}else{
			massage="验证码6位数字";
			publicFailAndTip(alart,btn2,massage,yzm);
		}
	}
	return massage;
}

//手机验证 请输入银行预留手机
function phonef(phone,ph8){
	var massage="";
	if(phone.val()==""||phone.val()=="请输入银行预留手机号"){
		if(ph8==0){
			massage="手机号码格式错误";
//			publicFailAndTip(alart,btn2,massage,telnum2);
		}else{
			massage="";
		}
	}else{
		if (/(^1[3|4|5|7|8][0-9]{9}$)/.test(phone.val())) {
			massage="";
		} else {
			massage="手机号码格式错误";
			publicFailAndTip(alart,btn2,massage,telnum2);
		}
	}
	return massage;
}
//////////////////////////////////////////////////////////
//校验卡号(根据银行卡号获取所属银行名称)
function checkCard(cardNo){
	$.ajax({
		url:rootPath+'/bankcard/check_card',
		type:"post",
		data:{
			cardNo:cardNo,
		},
		success:function(data){
			if(data.result == 'success'){

			}else if(data.result == 'error'){
				if(data.errCode == 'check'){
					//王亚东  页面上方提示框id 、 提交表单按钮id 、错误信息 默认的可为空、replaceId 错误框提示位置（变红）
					publicFailAndTip(alart,btn2,data.errMsg);
				}else{
					publicFailAndTip(alart,btn2,data.errMsg);
				}
			}else{
				    publicFailAndTip(alart,btn2,data.errMsg);
			}
		}
	});
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
    if(replaceId=""){
    	replaceId.style.border="solid 1px red"
    }
    setTimeout("clean()",3000);
}

/////////////////////////////////////////////////////////
//clean 方法
function clean(){
	 document.getElementById("alart").style.display="none";
	 yzm.style.border="solid 1px #d2d4dc";
	 telnum2.style.border="solid 1px #d2d4dc";
	 bankid.style.border="solid 1px #d2d4dc";
	};

////////////////////////////////////////////////////////
	function formatBankNo (BankNo){
		if (BankNo.value == "") return;
		var account = new String (BankNo.value);
		account = account.substring(0,22); /*帐号的总数, 包括空格在内 */
		if (account.match (".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}") == null){
			/* 对照格式 */
			if (account.match (".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" + ".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" +
			".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" + ".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}") == null){
				var accountNumeric = accountChar = "", i;
				for (i=0;i<account.length;i++){
					accountChar = account.substr (i,1);
					if (!isNaN (accountChar) && (accountChar != " ")) accountNumeric = accountNumeric + accountChar;
				}
				account = "";
				for (i=0;i<accountNumeric.length;i++){	/* 可将以下空格改为-,效果也不错 */
					if (i == 4) account = account + " "; /* 帐号第四位数后加空格 */
					if (i == 8) account = account + " "; /* 帐号第八位数后加空格 */
					if (i == 12) account = account + " ";/* 帐号第十二位后数后加空格 */
					account = account + accountNumeric.substr (i,1)
				}
			}
		}else{
			account = " " + account.substring (1,5) + " " + account.substring (6,10) + " " + account.substring (14,18) + "-" + account.substring(18,25);
		}
		if (account != BankNo.value) BankNo.value = account;
	}
	//
	function checkSueccess(){
		var phone = $("#telnum2").val();
		var carNo = $("#bankid").val();
		var yzm = $("#yzm").val();
		if(yzm!=''&&yzm!='请输入短信验证码' &&phone != '请输入银行预留手机号' && phone != '' && carNo != '请输入银行卡号'&&carNo != ''){
			document.getElementById('btn2').disabled="";
//			document.getElementById("alart").innerHTML= '';
//			document.getElementById('btn2').disabled="";
			document.getElementById('btn2').style.backgroundColor="#fe2a4d";
			document.getElementById('btn2').style.color="white";
		}else{
			document.getElementById('btn2').disabled="true";
		}
	}

