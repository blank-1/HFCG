// JavaScript Document
window.onload=function(){
	var btn3=document.getElementById("btn3");
	var btn2=document.getElementById("btn2");
	var light=document.getElementById('light');
	var fade=document.getElementById('fade');
	var closebt=document.getElementById("closebt");
	var backgd=document.getElementById("backgd");
	var closebtx=document.getElementById("closebtx");
//设定数字位数随字符数改变字体
$.each($(".xsize"),function(){
	var len = $(this).text().length;
	if(len<8){
		$(this).css("font-size","2rem");
	}else if(len>=8 && len<11){
	$(this).css("font-size","1.6rem");
	}else if(len>=11){
	$(this).css("font-size","1.2rem");	
	}
});
$.each($(".ysize"),function(){
	var len = $(this).text().length;
	if(len<8){
		$(this).css("font-size","3rem");
	}else if(len>=8 && len<11){
	$(this).css("font-size","2.6rem");
	}else if(len>=11){
	$(this).css("font-size","2.2rem");	
	}
});
    
    
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
document.getElementById("btn").onclick=function(){time(this);}

btn2.onclick=function(){
light.style.display='block';
fade.style.display='block';
backgd.style.overflow='hidden';
}
btn3.onclick=function(){
light.style.display='block';
fade.style.display='block';
backgd.style.overflow='hidden';
}
closebt.onclick=function(){
light.style.display='none';
fade.style.display='none';
backgd.style.overflow='visible'
}
closebtx.onclick=function(){
light.style.display='none';
fade.style.display='none';
backgd.style.overflow='visible'
}
};
//  密码显示隐藏效果
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
showHidePwd()

$('input[type="button"]').on('click', function(){
    $('#frm').submit();
}); 
$('#test').load(function(){
    var res = $(this).contents().find('body').text();
    console.log(res);
});


//提现页面的非空验证
function checkinput4(){
if (recharge.value == '请输入提现金额（最少100元）' || psw.value == '' || yzm.value == '请输入短信验证码'){
	document.getElementById('btn2').disabled="true";
document.getElementById('btn2').style.backgroundColor="#cacaca";
document.getElementById('btn2').style.color="#989898";
}else{
	/*-----------------	
	//在此执行ajax校验
	-------------------*/
	document.getElementById('btn2').disabled="";
document.getElementById('btn2').style.backgroundColor="#fe2a4d";
document.getElementById('btn2').style.color="white";
}
};

function clean(){
 alart.style.display="none";
 telnum.style.border="solid 1px #d2d4dc"
 psw.style.border="solid 1px #d2d4dc"
};
//  金额输入框输入限制
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
