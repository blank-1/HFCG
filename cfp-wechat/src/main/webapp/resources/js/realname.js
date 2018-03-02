// JavaScript Document
//弹窗效果
window.onload=function(){
	
	var btn2=document.getElementById("btn2");
	var light=document.getElementById('light');
	var fade=document.getElementById('fade');
	var backgd=document.getElementById("backgd");
	var closebtx=document.getElementById("closebtx");
	var realname= document.getElementById("realname");
	document.getElementById("btn2").onclick=function(){
	$.ajax({
	    url: rootPath+"/person/identityAuthentication",    //请求的url地址
	    dataType:"json",   //返回格式为json
	    async:true,//请求是否异步，默认为异步，这也是ajax重要特性
	    type:"POST",   //请求方式
	    data:{"trueName":$("#realname").val(),"idCard":$("#idcard").val()},
	    success:function(req){
	        //请求成功时处理 result
	    	if(req.result == 'success'){
				 light.style.display='block';
				 fade.style.display='block';
				 backgd.style.overflow='hidden';
	    	}else {
	    		document.getElementById("alart").innerHTML = req.errMsg;
				alart.style.display="block";
				setTimeout("clean()",3000);
	    	}
	    } 
	});
}

//closebtx.onclick=function(){
//light.style.display='none';
//fade.style.display='none';
//backgd.style.overflow='visible'
//}


};

$('input[type="button"]').on('click', function(){
    $('#frm').submit();
}); 
$('#test').load(function(){
    var res = $(this).contents().find('body').text();
    //console.log(res);
});

//实名验证的非空验证  wangyadong

function checkinput6(){
	    var realname= document.getElementById("realname");
		var idcard= document.getElementById("idcard");
		var alart= document.getElementById("alart");
if (idcard.value == '请输入身份证号'){
	document.getElementById('btn2').disabled="false";
	document.getElementById('btn2').style.backgroundColor="#cacaca";
	document.getElementById('btn2').style.color="#989898";
	document.getElementById("alart").innerHTML= '身份证为空';
	alart.style.display="block";
	setTimeout("clean()",3000);
} else if(/(^\d{15}$)|(^\d{17}(\d|X)$)/.test(idcard.value )==false){
	document.getElementById('btn2').disabled="false";
	document.getElementById("alart").innerHTML = '身份证格式不正确';
	document.getElementById('btn2').style.backgroundColor="#cacaca";
	document.getElementById('btn2').style.color="#989898";
	realname.style.border="solid 1px red"
	alart.style.display="block";
	setTimeout("clean()",3000);
}else {
	document.getElementById("alart").innerHTML= '';
	document.getElementById('btn2').disabled="";
	document.getElementById('btn2').style.backgroundColor="#fe2a4d";
	document.getElementById('btn2').style.color="white";

}




};

function checkinput16(){
	  var realname= document.getElementById("realname");
	  var alart= document.getElementById("alart");
		if (realname.value == '请输入身份证登记姓名' ){
			document.getElementById('btn2').disabled="false";
			document.getElementById('btn2').style.backgroundColor="#cacaca";
			document.getElementById('btn2').style.color="#989898";
			document.getElementById("alart").innerHTML= '身份证登记姓名为空';
			alart.style.display="block";
			setTimeout("clean()",3000);
		}
}

function clean(){
	 alart.style.display="none";
	 realname.style.border="solid 1px #d2d4dc"
	 idcard.style.border="solid 1px #d2d4dc"
	};
	function checkSueccess(){
		var realname= document.getElementById("realname");
		var idcard= document.getElementById("idcard");
		var alart= document.getElementById("alart");
		if(idcard.value != '请输入身份证号' && idcard.value != '' && realname.value != '请输入身份证登记姓名' && realname.value != ''){
			document.getElementById('btn2').disabled="";
//			document.getElementById("alart").innerHTML= '';
//			document.getElementById('btn2').disabled="";
			document.getElementById('btn2').style.backgroundColor="#fe2a4d";
			document.getElementById('btn2').style.color="white";
		}else{
			document.getElementById('btn2').disabled="true";
		}
	}

