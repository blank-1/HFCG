/**
* 功能:发送一个ajax请求,验证用户是否登录
* @author 满口蛀牙
* @date 2016年4月18日
* @param url
* @param jsonData
* @return null
* @throws null
**/
var ajaxUserInfo={"crfUser":{"accountStatus":null,"bankCode":null,"bankPicUrl":null,"customerStatus":null,"email":null,"headImgUrl":null,"idNo":null,"openAccountIdno":null,"openBankCardNo":null,"realName":null,"username":null,"riskLevel":null,"mobilePhone":null}};
var emptyUserInfo={"crfUser":{"accountStatus":null,"bankCode":null,"bankPicUrl":null,"customerStatus":null,"email":null,"headImgUrl":null,"idNo":null,"openAccountIdno":null,"openBankCardNo":null,"realName":null,"username":null,"riskLevel":null,"mobilePhone":null}};

//声明全局ajaxUserInfo变量
//var ajaxUserInfo={"crfUser":{"authstep":null,"createtime":null,"creditrating":null,"dealpwd":null,"dueinsum":null,"dueoutsum":null,"email":null,"freezesum":null,"headimg":null,"isnewthree":null,"issend":null,"issortt":null,"isthreeaccount":null,"lastdate":null,"lastip":null,"locktime":null,"count":null,"md5PriNumber":null,"mobilephone":null,"notimeamount":null,"obStatus":null,"otheRecomCode":null,"person":null,"priNumber":null,"promotionType":null,"rating":null,"recomCode":null,"registertype":null,"threeTime":null,"threeaccount":null,"usablesum":null,"userSource":null,"userTpe":null,"userenable":null,"username":null,"userpassword":null,"toGrantAuthorization":null}};
//var emptyUserInfo={"crfUser":{"authstep":null,"createtime":null,"creditrating":null,"dealpwd":null,"dueinsum":null,"dueoutsum":null,"email":null,"freezesum":null,"headimg":null,"isnewthree":null,"issend":null,"issortt":null,"isthreeaccount":null,"lastdate":null,"lastip":null,"locktime":null,"count":null,"md5PriNumber":null,"mobilephone":null,"notimeamount":null,"obStatus":null,"otheRecomCode":null,"person":null,"priNumber":null,"promotionType":null,"rating":null,"recomCode":null,"registertype":null,"threeTime":null,"threeaccount":null,"usablesum":null,"userSource":null,"userTpe":null,"userenable":null,"username":null,"userpassword":null,"toGrantAuthorization":null}};
function chekc(path,jsonData){
	var cookieStr=$.cookie("ajaxUserInfo");
	if(!cookieStr || cookieStr == "null"){//用户未登录
		//console.log("cookie中没有查找到用户信息,用户未登录");
		writeUserInfoToHtml(JSON.stringify(ajaxUserInfo));
	}else{//用户已登录
		//console.log("cookie中查找到用户信息,用户已登录");
		//ajaxUserInfo=JSON.parse(cookieStr);
		//console.log(cookieStr)
		ajaxUserInfo=eval("("+cookieStr+")");
		writeUserInfoToHtml(cookieStr);
	}
}

/**
* 功能:Ajajx请求用户信息并放入到Cookie
* @author 满口蛀牙
* @date 2016年6月13日
* @param url
* @param jsonData
* @return null
* @throws null
**/
/*function ajaxGetUserInfo(callBack){
	var date=new Date();
	var jsonData={"date":date.getTime(),"sessionID":$.cookie('sessionID')};
	var path="home_CrfUser_check.do";
	//var protocol=window.location.protocol;
	//var host = window.location.host;
	var url="/ajax/"+path;
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				//更新全局ajaxUserInfo变量
				ajaxUserInfo=result;
				//更新cookie
				$.cookie('ajaxUserInfo', JSON.stringify(result), { path: '/' });
				if(callBack){
					callBack();
				}

			}
		}
	});
}*/

// 新信息
function ajaxGetUserInfo(callBack){
	var accessToken=$.cookie('accessToken');
	var uuid=$.cookie('uuid');
	var deviceno="CRF_OFFICIAL_WEBSITE";
	var packageName="com.crfchina.webP2p";
	var mobileOs="officialWebsite";
	var showBankFlag;
	$.ajax({
        url:'/webp2p_interface_mysql/auth/userinfo/'+uuid,
        type:'post',
        dataType:'json',
        async:false,
        cache: false,
        contentType: "application/json;charset=UTF-8",
        headers: { 
	        'accessToken': accessToken, 
	        'deviceno': deviceno, 
	        'packageName': packageName, 
	        'mobileOs': mobileOs 
        },
        success:function(data){
			// console.log(data);
			if(data.result==="0000"){
				// 验证通过
				var accountStatus=data.data.accountStatus;		//开户状态 [1 未开户 2已开户]
				var bankCode=data.data.bankCode;	//银行缩写Code
				var bankPicUrl=data.data.bankPicUrl;	//银行背景图地址
				var customerStatus=data.data.customerStatus;	//客户状态 [1 未出资 2 已出资]
				var email=data.data.email;	//邮箱
				var headImgUrl=data.data.headImgUrl;	//头像url
				var idNo=data.data.idNo;	//证件号
				var openAccountIdno=data.data.openAccountIdno;	//开户证件号
				var openBankCardNo=data.data.openBankCardNo;	//银行卡号
				var realName=data.data.realName;	//用户姓名
				var Id=data.data.Id;	//用户姓名
				var riskLevel=data.data.riskLevel;	//风险评估等级[1、稳健性 2、成熟性 3 激进性]
				var tograntauthorization=data.data.tograntauthorization;	//是否授权银行协议[0:未授权；1:已授权]

				//更新全局ajaxUserInfo变量
				ajaxUserInfo.crfUser.accountStatus=accountStatus;
				ajaxUserInfo.crfUser.bankCode=bankCode;
				ajaxUserInfo.crfUser.bankPicUrl=bankPicUrl;
				ajaxUserInfo.crfUser.customerStatus=customerStatus;
				ajaxUserInfo.crfUser.email=email;
				ajaxUserInfo.crfUser.headImgUrl=headImgUrl;
				ajaxUserInfo.crfUser.idNo=idNo;
				ajaxUserInfo.crfUser.openAccountIdno=openAccountIdno;
				ajaxUserInfo.crfUser.openBankCardNo=openBankCardNo;
				ajaxUserInfo.crfUser.realName=realName;
				ajaxUserInfo.crfUser.username=Id;	//username
				ajaxUserInfo.crfUser.riskLevel=riskLevel;
				//更新cookie
				$.cookie('ajaxUserInfo', JSON.stringify(ajaxUserInfo), { path: '/' });
				if(callBack){
					callBack();
				}
				if(accountStatus==2 && tograntauthorization==0){
					showBankFlag=true;
				}
			}else{
        		var errorMsg=JSON.parse(xhr.responseText).message;
			}
        },
        error:function(xhr){
        	// console.log(xhr);
        	var errorCode=JSON.parse(xhr.responseText).code;
        	var errorMsg=JSON.parse(xhr.responseText).message;
        	var errorCodeArr=["FUS_2000","FUS_2001","FUS_2002","FUS_2004","FUS_2005","FUS_2006","FUS_2007","FUS_2008","FUS_2009"];
        	for(var i=0;i<errorCodeArr.length;i++){
        		if(errorCodeArr[i].indexOf(errorCode)!= -1){
        			var timenow = new Date().getTime();
				//	window.location.href="/?_="+timenow;
        		}
        	}
        }
    });
    return showBankFlag;
}


function ajaxGetUserInfoWithOutBack(){
	var date=new Date();
	var jsonData={"date":date.getTime(),"sessionID":$.cookie('sessionID')};
	var path="home_CrfUser_check.do";
	//var protocol=window.location.protocol;
	//var host = window.location.host;
	var url="/ajax/"+path;
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				//更新全局ajaxUserInfo变量
				ajaxUserInfo=result;
				//更新cookie
				$.cookie('ajaxUserInfo', JSON.stringify(result), { path: '/' });
			}
		}
	});
}

/**
* 功能:将用户信息显示到HTML
* @author 满口蛀牙
* @date 2016年4月18日
* @param url
* @param jsonData
* @return null
* @throws null
**/
function writeUserInfoToHtml(result){
	var str=JSON.parse(result);
	//console.log(str);
	$.each(str, function(index, object) {
		if(object.username==null){
		//	var htmlCode="<a href=\"/\">首页</a><a href=\"/helpType.html\">帮助中心</a><span class=\"_yellow\"><a href=\"/reg.html\">免费注册</a></span><a href=\"/\">立即登录</a><a href=\"http://chinarapidfinance.com/\">English</a>";
		//	$(".top--wrap").html(htmlCode);
		$("#head_user").empty();
		$("#head_user_out").css('display','');
		}else{

            var welcomeCode="<span><a>欢迎您</a></span> <span style=\"color:red\">"+object.username+"</span>";
            var logoutCode="<a style=\"cursor: pointer;\" onclick=\"logOut()\">[退出]</a>";
            var messageCount="";
            // if(parseInt(object.userpassword)>0){
            //     messageCount="<span class=\"_yellow\"><a href=\"/systemMessage.html\">消息("+object.userpassword+")</a></span>";
            // }
            var userCenter="<a href=\"/usersafe.html\">安全中心</a> <a href=\"/homeInit.html\">我的账户</a>";
		//	var htmlCode="<a href=\"/\">首页</a>"+welcomeCode+messageCount+logoutCode+userCenter+"<a href=\"http://chinarapidfinance.com/\">English</a>";
			$("#head_user").html(welcomeCode+messageCount+logoutCode+userCenter);
			$("#head_user_out").css('display','none');
		}
	});
}

/**
* 功能:发送一个ajax请求,退出用户登录
* @author 满口蛀牙
* @date 2016年5月13日
* @param url
* @param jsonData
* @return null
* @throws null
**/
/*function logOut(){
	var protocol=window.location.protocol;
	var host = window.location.host;
	var url="/ajax/tuichu.do";
	var data="date="+new Date();
	$.post(url,data,function(callBack){
		$.cookie("ajaxUserInfo",null,{ path: '/' });
		//window.location.href="/index.html";
		window.event.returnValue = false;//加入这行代码即可
	})
}*/
function logOut(){
	var accessToken=$.cookie('accessToken');
	var uuid=$.cookie('uuid');
	var deviceno="CRF_OFFICIAL_WEBSITE";
	var packageName="com.crfchina.webP2p";
	var mobileOs="officialWebsite";
	$.ajax({
        url:'/webp2p_interface_mysql/auth/logout/'+uuid,
        type:'post',
        dataType:'json',
        // async:false,
        cache: false,
        contentType: "application/json;charset=UTF-8",
        headers: { 
	        'accessToken': accessToken, 
	        'deviceno': deviceno, 
	        'packageName': packageName, 
	        'mobileOs': mobileOs 
        },
        success:function(data){
			// console.log(data);
			if(data.result==="0000"){
				// 成功
			}else{
        		var errorMsg=JSON.parse(xhr.responseText).message;
			}
        },
        error:function(xhr){
        	// console.log(xhr);
        	var errorCode=JSON.parse(xhr.responseText).code;
        	var errorMsg=JSON.parse(xhr.responseText).message;
        }
    });


	$.cookie("ajaxUserInfo",null,{ path: '/' });
	//window.location.href="/index.html";
	//window.event.returnValue = false;
}

/**
* 功能:ajax 获取标的列表
* @author 满口蛀牙
* @date 2016年4月18日
* @param url
* @param jsonData
* @return null
* @throws null
**/
/*function bidList(path,jsonData){
	var protocol=window.location.protocol;
	var host = window.location.host;
	var url="/ajax/"+path;
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				var html="";
				//$.each(result, function(index, object) {
					//var obj = JSON.parse(object);
					var obj=result;
					for(var i=0; i<obj.length;i++){
						html+="<tr><td class=\"width_20\"><label>信</label>"+obj[i][0]+"</td><td class=\"width_20 text_align_center\">"+obj[i][1]+"%</td><td class=\"width_20 text_align_center\">"+toThousands(obj[i][2])+"</td><td class=\"width_20 text_align_center\">"+obj[i][3]+"</td><td class=\"width_20 text_align_center\">成功</td></tr>";
					}
				//});
				$(".index_biaodi_table").append(html);
			}
		}
	});
}*/
function bidList() {
	$.ajax({
		url: "/webp2p_interface_mysql/account/loan/purpose",
		type: "get",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		success: function(data) {
			if (data.result == 0000) {
				var html = "";
				var obj = data.data.lsALP;
				for (var i = 0; i < obj.length; i++) {
					html += "<tr><td class=\"width_20\"><label>信</label>" + obj[i].loanPurposeName + "</td><td class=\"width_20 text_align_center\">" + obj[i].loanRate + "%</td><td class=\"width_20 text_align_center\">" + toThousands(obj[i].loanAmount) + "</td><td class=\"width_20 text_align_center\">" + obj[i].payTerm + "</td><td class=\"width_20 text_align_center\">成功</td></tr>";
				}
				$(".index_biaodi_table").append(html);
			} else {
				// console.log("获取列表失败");
			}
		},
		error: function(xhr) {
			// console.log("ajax请求出错");
		}
	});
}



/**
* 功能:ajax 标的统计
* @author 满口蛀牙
* @date 2016年5月30日
* @param url
* @param jsonData
* @return null
* @throws null
**/
function biaodiCount(path,jsonData){
	var protocol=window.location.protocol;
	var host = window.location.host;
	var url="/ajax/"+path;
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				var dataObj=eval(result);
				var dataObj2=eval(dataObj.sumList);
				$(".biaodi-count").html(toThousands(dataObj2[0][1]));
				$(".biaodi-num").html(toThousands(dataObj2[0][0]));
			}
		}
	});
}

/** 
* 功能:ajax 黑名单查询 
* @date 2017年9月25日 
**/ 
function blacklistQuery(className) {
	var code = $(".black-ID-num").val();
	var username = $(".black-ID-user-name").val();
	// var reg = /^[\u4E00-\u9FA5]{2,4}$/;
	var reg = /^[\u4e00-\u9fa5]+(·[\u4e00-\u9fa5]+)*$/;
	if (!reg.test(username)) {
		return crfErrAlert('errorInfo', "错误", "姓名不合法或者姓名为空", 0, "");
	}
	if (code == "") {
		return crfErrAlert('errorInfo', "错误", "请输入身份证号", 0, "");
	} else {
		if (!IdentityCodeValid(code)) {
			return crfErrAlert('errorInfo', "错误", "身份证或名字不合法", 0, "");
		}
	}
	var param = {
		loanName: username,
		idCard: code
	}
	var url = "/webp2p_interface_mysql/account/penalty/interest";
	$.ajax({
		url: url,
		type: "post",
		data: JSON.stringify(param),
		contentType: "application/json; charset=utf-8",
		success: function(data) {
			// console.log(data);
			if (data.result == 0000) {
				var penaltyInterestCount = data.data.penaltyInterestCount;
				// console.log(penaltyInterestCount)
				if (penaltyInterestCount > 0) {
					crfErrAlert('errorInfo',"通知","信用问题",0,"");
				}else{
					crfInofAlert('notification', "通知", "没有查到不良记录", 0, "");
				}
			} else {
				// console.log("查询失败");
				var errorMsg=data.message;
				crfErrAlert('errorInfo',"通知",errorMsg,0,"");
			}
		},
		error: function(xhr) {
			// console.log("ajax请求出错");
			var errorCode=JSON.parse(xhr.responseText).code;
        	var errorMsg=JSON.parse(xhr.responseText).message;
			crfErrAlert('errorInfo',"通知",errorMsg,0,"");
		}
	});
}


/**
* 功能:提示信息框
* @author 满口蛀牙
* @date 2016年4月20日
* @param className
* @param title
* @param msg
* @param openUrl(是否打开url,为1是打开)
* @param url
* @return null
* @throws null
**/
function crfInofAlert(className,title,msg,openUrl,url){
	showMask(400,className);
	var dialogWidth=550;
	var dialogHeight=190;
	var positionWidth=parseInt(($(document.body).width()-dialogWidth)/2)+"px";
	var positionHeight=parseInt(($(document).scrollTop()+dialogHeight))+"px";
	var css={"width":dialogWidth,"height":dialogHeight,"background":"#fff","top":positionHeight,"left":positionWidth,"position":"absolute","z-index":"450"};
	$(".dialog-info-title").text(title);
	$(".dialog-info").text(msg);
	if(openUrl==1&&url!=""){
		$(".dialog-info-botton").on("click",function(){
			window.location.href=url;
		});
	}else{
		$(".dialog-info-botton").on("click",function(){
			closeInfoDialog('notification');
		});
	}
	$(".dialog-info-wrap").css(css).show();
	$(".dialog-info-wrap").draggabilly({ handle:'.dialog-wrap-top'});
}

function crfInofAlert2(className,title,msg,openUrl,url){
	showMask(400,className);
	var dialogWidth=550;
	var dialogHeight=190;
	var positionWidth=parseInt(($(document.body).width()-dialogWidth)/2)+"px";
	var positionHeight=parseInt(($(document).scrollTop()+dialogHeight))+"px";
	var css={"width":dialogWidth,"height":dialogHeight,"background":"#fff","top":positionHeight,"left":positionWidth,"position":"absolute","z-index":"450"};
	$(".dialog-info-title").text(title);
	$(".dialog-info").text(msg);
	if(openUrl==1&&url!=""){
		$(".dialog-info-botton").on("click",function(){
			window.location.href=url;
		});
	}else{
		$(".dialog-info-botton").on("click",function(){
			closeInfoDialog('notification');
			closeDialog('produceMask');
			ajaxGetUserInfo(callBack2);
		});
	}
	$(".dialog-info-wrap").css(css).show();
	$(".dialog-info-wrap").draggabilly({ handle:'.dialog-wrap-top'});
}

function crfInofAlert3(className,title,msg,openUrl,url,updateUser){
	showMask(400,className);
	var dialogWidth=550;
	var dialogHeight=190;
	var positionWidth=parseInt(($(document.body).width()-dialogWidth)/2)+"px";
	var positionHeight=parseInt(($(document).scrollTop()+dialogHeight))+"px";
	var css={"width":dialogWidth,"height":dialogHeight,"background":"#fff","top":positionHeight,"left":positionWidth,"position":"absolute","z-index":"450"};
	$(".dialog-info-title").text(title);
	$(".dialog-info").text(msg);
	if(updateUser==1){
		ajaxGetUserInfo();
	}
	if(openUrl==1&&url!=""){
		$(".dialog-info-botton").on("click",function(){
			window.location.href=url;
		});
	}else{
		$(".dialog-info-botton").on("click",function(){
			closeInfoDialog('notification');
			closeDialog('produceMask');
			ajaxGetUserInfo(callBack2);
		});
	}
	$(".dialog-info-wrap").css(css).show();
	$(".dialog-info-wrap").draggabilly({ handle:'.dialog-wrap-top'});
}

/**
* 功能:错误信息弹出框
* @author 满口蛀牙
* @date 2016年4月20日
* @param className
* @param title
* @param msg
* @param openUrl(是否打开url,为1是打开)
* @param url
* @return null
* @throws null
**/
function crfErrAlert(className,title,msg,openUrl,url){
	showMask(300,className);
	var dialogWidth=550;
	var dialogHeight=190;
	var positionWidth=parseInt(($(document.body).width()-dialogWidth)/2)+"px";
	var positionHeight=parseInt(($(document).scrollTop()+dialogHeight))+"px";
	var css={"width":dialogWidth,"height":dialogHeight,"background":"#fff","top":positionHeight,"left":positionWidth,"position":"absolute","z-index":"400"};
	$(".dialog-err-title").text(title);
	$(".dialog-err").text(msg);
	if(openUrl==1&&url!=""){
		$(".dialog-err-botton").on("click",function(){
			window.location.href=url;
		});
	}else{
		$(".dialog-err-botton").on("click",function(){
			closeErrDialog('errorInfo');
		});
	}
	$(".dialog-err-wrap").css(css).show();
	$(".dialog-err-wrap").draggabilly({ handle:'.dialog-wrap-top'});
}
/**
* 功能:信息弹出框关闭
* @author 满口蛀牙
* @date 2016年4月23日
* @param className
* @return null
* @throws null
**/
function closeInfoDialog (className){
	hiddenMask(className);
	$(".dialog-info-wrap").css({"display":"none"});
}
/**
* 功能:错误信息弹出框关闭
* @author 满口蛀牙
* @date 2016年4月23日
* @param className
* @return null
* @throws null
**/
function closeErrDialog(className){
	hiddenMask(className);
	$(".dialog-err-wrap").css({"display":"none"});
}
/**
* 功能:跳转登录
* @author 满口蛀牙
* @date 2016年4月24日
* @param className
* @return null
* @throws null
**/
function shipTo(className){
	showMask(260,className);//
	var dialogWidth=550;
	var dialogHeight=210;
	var positionWidth=parseInt(($(document.body).width()-dialogWidth)/2)+"px";
	var positionHeight=parseInt(($(document).scrollTop()+dialogHeight))+"px";
	var css={"width":dialogWidth,"height":dialogHeight,"background":"#fff","top":positionHeight,"left":positionWidth,"position":"absolute","z-index":"300"};
	$(".dialog--wrap").css(css).show();
	$(".dialog--timer").html(6);
	//计时
	timer();
}
/**
* 功能:关闭登录弹出框
* @author 满口蛀牙
* @date 2016年4月24日
* @param className
* @return null
* @throws null
**/
function closeDialog(className){
	hiddenMask(className);
	var css={"width":"0px","height":"0px","background":"#fff"};
	$(".dialog--wrap").css(css).hide();
	clearTimeout(cuntTime);
}
/**
* 功能:计算器
* @author 满口蛀牙
* @date 2016年4月24日
* @param className
* @return null
* @throws null
**/
var cuntTime;
function timer(){
	var timeOld=$(".dialog--timer").html();
	if(timeOld==0){
		//清除计时,跳转到登录页
		hiddenMask("Mask");
		var css={"width":"0px","height":"0px","background":"#fff"};
		$(".dialog--wrap").css(css).hide();
	//	window.location.href="/";
	}else{
		//计时调用
		cuntTime=setTimeout("timer()",1000);
		timeOld--;
		$(".dialog--timer").html(timeOld)
	}
}

/**
* 功能:弹出框
* @author 满口蛀牙
* @date 2016年4月25日
* @param _this
* @param id
* @param className
* @return null
* @throws null
**/
function showDialog(_this,id,className){

	//置空表单
	$(".dialog-chuzhijinge").val("");
	var agree=$(".i-agreement").prop("checked",false);
	$.each(ajaxUserInfo, function(index, object) {
		if(object.username==null){
			shipTo('Mask');
		}else if(!object.accountStatus || object.accountStatus == 1){
			//开户状态 [1 未开户 2已开户]
			return crfInofAlert('notification',"通知","您还没有开户,现在就去",1,"/openaccount.html");
		}else{
			/*var flag = checkGrant();
			if(flag){
				return;
			}*/
			/**ajax加载产品详情**/
	 		//绑定给按钮绑定URL
			$(".dialog-mashangchujie").attr("url",$(_this).attr("url"));
			loadProductInfo(id);
			showMask(100,className);
			shwoPopup(0,0);
			$(".dialog-wrap").draggabilly({ handle:'.dialog-wrap-top'});
		}
	});

}

function showDialog2(_this,id,className){

	var maskPercent=$(_this).find(".botton_mask").attr("maskWidth");
	if(maskPercent){
		$(_this).unbind("click");
		return;
	}

	//置空表单
	$(".product_chuzi_money_input").val("");
	var agree=$(".i-agreement2").prop("checked",false);
	$.each(ajaxUserInfo, function(index, object) {
		if(object.username == null){
			shipTo('Mask');
		}else if(!object.accountStatus || object.accountStatus == 1){
			//开户状态 [1 未开户 2已开户]
			return crfInofAlert('notification',"通知","您还没有开户,现在就去",1,"/openaccount.html");
		}else{
			/*var flag = checkGrant();
			if(flag){
				return;
			}*/

			//获取用户余额
			var uuid=$.cookie('uuid');
			var accessToken=$.cookie('accessToken');
			var deviceno="CRF_OFFICIAL_WEBSITE";
			var packageName="com.crfchina.webP2p";
			var mobileOs="officialWebsite";
			$.ajax({
		        url:'/webp2p_interface_mysql/investment/product/'+uuid+'/balance',
		        type:'post',
		        dataType:'json',
		        // async:false,
		        cache: false,
		        contentType: "application/json;charset=UTF-8",
		        headers: { 
			        'accessToken': accessToken, 
			        'deviceno': deviceno, 
			        'packageName': packageName, 
			        'mobileOs': mobileOs 
		        },
		        success:function(data){
					// console.log(data);
					if(data.result==="0000"){
						var abalanceList=data.data.accountBalanceDetailList[0];
						var html=toThousands(accAdd(abalanceList.availableBalance,abalanceList.notSettleBalance))+"元&nbsp;&nbsp;(可用余额："+toThousands(abalanceList.availableBalance)+"元,待转结金额: "+toThousands(abalanceList.notSettleBalance)+"元)<a href=\"/rechargeInit.html\"  class=\"addMoney\">充值</a>";
						
						/*$(".money_num_all").html(toThousands(accAdd(abalanceList.availableBalance,abalanceList.notSettleBalance)));
						$(".money_num_able").html(toThousands(abalanceList.availableBalance));
						$(".money_num_unable").html(toThousands(abalanceList.notSettleBalance));*/
						$(".dialog-wrap2 .dialog-product-balance").html(html);
						//json
						var jsonStr=$(_this).attr("productPro");
						//console.log($(".dialog-product-closeDay2"));
						var jsonObj=JSON.parse(jsonStr);
						$(".dialog-product-name").html(jsonObj.planName);
						$(".dialog-product-proYearRate").html(jsonObj.properties.PLAN_RATE+"%");
						$(".dialog-product-closeDay2").html(jsonObj.properties.CLOSE_DAYS+"天");

						$(".product_chuzi_money_input").val(jsonObj.properties.MIN_INVEST_AMOUNT/100);
						$(".product_chuzi_money_input").attr("limit_value",jsonObj.properties.MIN_INVEST_AMOUNT/100);
						//$(".product_money_text_num").html(jsonObj.product_money_text_num);
						$(".base_money").html(jsonObj.properties.AMOUNT_UNIT/100);
						$(".limit_money").html(jsonObj.properties.MAX_INVEST_AMOUNT/100);
						// $(".simpleInterest").val(jsonObj.properties.INNER_RATE);
						$(".simpleInterest").val(jsonObj.properties.PLAN_RATE);
						$(".xjdContract").attr("IS_NEW_BIE",jsonObj.properties.IS_NEW_BIE);
						/*if(jsonObj.properties.IS_NEW_BIE==1){
							$(".xjdContract").html("《新手共盈出借人服务协议》");
						}else {
							$(".xjdContract").html("《现金贷出借人服务协议》");
						}*/
						//给按钮绑定URL
						$(".dialog-mashangchujie2").attr("url",$(_this).attr("url"));
						//更新产品进度
						//让点击按钮可用
						$(".dialog-mashangchujie2").removeAttr("disabled");
						// getPlanProgress2(jsonObj.planNo);
						//更新收益
						updataReta();
						showMask(100,className);
						shwoPopup2(0,0);
						$(".dialog-mashangchujie2").attr("rel",jsonObj.planNo);
						$(".dialog-wrap2").draggabilly({ handle:'.dialog-wrap-top'});


					}else{
		        		var errorMsg=data.message;
					}
		        },
		        error:function(xhr){
		        	// console.log(xhr);
		        	var errorCode=JSON.parse(xhr.responseText).code;
		        	var errorMsg=JSON.parse(xhr.responseText).message;
    	        	var errorCodeArr=["FUS_2000","FUS_2001","FUS_2002","FUS_2004","FUS_2005","FUS_2006","FUS_2007","FUS_2008","FUS_2009"];
    	        	for(var i=0;i<errorCodeArr.length;i++){
    	        		if(errorCodeArr[i].indexOf(errorCode)!= -1){
    	        		//	$(".xjd__or_charge").html("<a href=\"/\">登录</a>");
    	        			$.cookie('ajaxUserInfo', null, { path: '/' });
							shipTo('Mask');
    	        		}
    	        	}
		        }
		    });

		}
	});

}

function getPlanProgress(planNo){
	var plan_str = $.cookie('plan_temp')
	if(plan_str){
		var plan_temp = JSON.parse(plan_str);
		if(plan_temp[planNo]  && (new Date().getTime() - plan_temp[planNo].time) <5000){
			//console.log(planNo + "----------"+plan_temp[planNo].time + "-------" + new Date().getTime());
			var result = plan_temp[planNo].result
			$('.'+planNo+' .font_org2').html((toThousands(((Number(result.planInvestmentAmount)/100)-(Number(result.currentInvestmentAmount)/100))))+"元");
			//$(".font_org2").html((toThousands(result.planInvestmentAmount-result.currentInvestmentAmount))+"元");
			var percent=GetPercent(Number(result.currentInvestmentAmount)/100,Number((result.planInvestmentAmount)/100));
			// percent="100%";
			$('.'+planNo+' .extent_num').html(percent);
			//$(".extent_num").html(percent);
			$('.'+planNo+' .botton_mask').css("width",percent);
			if(result.currentInvestmentAmount>=result.planInvestmentAmount){
				$('.'+planNo+' .botton_mask').attr("maskWidth","100");
			}
			return;
		}
	}
	var url="/ajax/home_cashProduct_getPlanProgress.do?";
	var jsonData={"planNo":planNo};
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		//contentType : "application/json; charset=utf-8",
		//data : JSON.stringify(jsonData),
		data: "planNo="+planNo+"&date="+new Date().getTime(),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				$('.'+planNo+' .font_org2').html((toThousands(((Number(result.planInvestmentAmount)/100)-(Number(result.currentInvestmentAmount)/100))))+"元");
				//$(".font_org2").html((toThousands(result.planInvestmentAmount-result.currentInvestmentAmount))+"元");
				var percent=GetPercent(Number(result.currentInvestmentAmount)/100,Number((result.planInvestmentAmount)/100));
				// percent="100%";
				$('.'+planNo+' .extent_num').html(percent);
				//$(".extent_num").html(percent);
				$('.'+planNo+' .botton_mask').css("width",percent);
				if(result.currentInvestmentAmount>=result.planInvestmentAmount){
					$('.'+planNo+' .botton_mask').attr("maskWidth","100");
				}
				var plan_t = {}
				plan_t[planNo] ={
					time : new Date().getTime(),
					result : result
				}

				$.cookie('plan_temp', JSON.stringify(plan_t), { path: '/' });
			}
		}
	});

}


// 产品进度
function getPlanProgressBatch() {
	var url = "/webp2p_interface_mysql/investment/product/list";
	$.ajax({
		url: url,
		type: "post",
		//data : JSON.stringify(jsonData), 
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		success: function(data) {
			// console.log(data)
			if (data.result == 0000) {
				var type = data.data.lsProductType;
				for (i = 0; i < type.length; i++) {
					// console.log(type[i].productType)
					var proType = type[i].productType;
					var list = type[i].lsProduct;
					//3是现金贷共赢 
					if (proType == 3) {
						for (var j = 0; j < list.length; j++) {
							var planNo = list[j].contractPrefix; //编号 
							var percent = GetPercent(Number(list[j].finishAmount), Number(list[j].planAmount));
							$('.' + planNo + ' .font_org2').html((toThousands(((Number(list[j].planAmount)) - (Number(list[j].finishAmount))))) + "元"); //剩余可投金额 
							$('.' + planNo + ' .extent_num').html(percent); //已完成 
							$('.' + planNo + ' .botton_mask').css("width", percent); //马上加入的进度 

							var showTime=$('.'+planNo).attr("showTime");
							var hiddenTime=$('.'+planNo).attr("hiddenTime");
							var publishTime=$('.'+planNo).attr("publishTime");
							var endTime=$('.'+planNo).attr("endTime");
							var tips_end=$('.'+planNo).attr("TIPS_END");
							var tips_start=$('.'+planNo).attr("TIPS_START");
							if($('.'+planNo+'[IS_NEW_BIE]') && timeCompare(publishTime)==1){
								$('.'+planNo+' .botton_mask').css("width","100%");
								$('.'+planNo+' button').css("cursor","auto");
								$('.'+planNo+' button').attr("disabled","disabled");
							}
							if((Number(list[j].finishAmount)>=Number(list[j].planAmount)) || (list[j].isFull==1)){
								$('.'+planNo+' .font_org2').html("0元").show();
								$('.'+planNo+' .extent_num').html("100%");
								$('.'+planNo+' .botton_mask').css("width","100%");
								$('.'+planNo+' button').css("cursor","auto");
								$('.'+planNo+' button').attr("disabled","disabled");
								if(tips_end && $('.'+planNo).find(".deadline")){
									$('.'+planNo).find(".deadline").html(tips_end).show();
								}
							}
							if($('.'+planNo+'[IS_NEW_BIE]') && timeCompare(endTime)!=1){
								$('.'+planNo+' .font_org2').html("0元").show();
								$('.'+planNo+' .extent_num').html("100%");
								$('.'+planNo+' .botton_mask').css("width","100%");
								$('.'+planNo+' button').css("cursor","auto");
								$('.'+planNo+' button').attr("disabled","disabled");
								if(tips_end && $('.'+planNo).find(".deadline")){
									$('.'+planNo).find(".deadline").html(tips_end).show();
								}
							}
							if((Number(list[j].finishAmount)>=Number(list[j].planAmount))){
								$('.'+planNo+' .botton_mask').attr("maskWidth","100");
							}
						}
					}
				}
			} else {
				// console.log("获取产品列表失败");
			}
		},
		error: function(xhr) {
			// console.log("ajax请求出错");
		}
	});
}

// 时间比较
function timeCompare(time){
	var timeType;
	if(!time){
		return timeType;
	}
	var a=time.split(":");
	var i= Number(a[0])*60*60+Number(a[1])*60+Number(a[2]);
	var now=new Date();
	var n= now.getHours()*60*60+now.getMinutes()*60+now.getSeconds();
	if(i>n){
			// console.log("未到指定时间");
			timeType=1;
	}else if(i<n){
			// console.log("已过指定时间");
			timeType=2;
	}else{
			// console.log("指定时间");
			timeType=0;
	}
	return timeType;
}

function getPlanProgress2(planNo){
	var url="/ajax/home_cashProduct_getPlanProgress.do?planNo="+planNo;
	var jsonData={"planNo":planNo};
	$.ajax({
		type : "GET",
		url : url,
		dataType : "json",
		//contentType : "application/json; charset=utf-8",
		//data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				var percent=GetPercent(Number(result.currentInvestmentAmount)/100,Number((result.planInvestmentAmount)/100));
				$(".dialog-mashangchujie2 .botton_mask").css("width",percent);
				if(percent=="100%"||result.fullFlag==1){
					$('.dialog-mashangchujie2').attr('disabled',"true").css("cursor","auto");
					$(".dialog-mashangchujie2 .botton_mask").css("width","100%");
				}
			}
		}
	});

}

//获取历史收益
function getHistoryReta(_this,classNo){
	var url="/ajax/home_cashProduct_queryHistoryRate.do?classNo="+classNo;
	$.ajax({
		type : "GET",
		url : url,
		dataType : "json",
		//contentType : "application/json; charset=utf-8",
		//data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				var m3=result[0].rate;
				var m4=result[1].rate;
				var m5=result[2].rate;
				var m6=result[3].rate;
				chart(_this,m3,m4,m5,m6);
			}
		}
	});
}

/**
功能:折线图显示历史利率
**/
function chart(_this,m3,m4,m5,m6){
	// 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init($(_this).parent().find('.echarts_wrap').eq(0).find('#echarts_box')[0]);
	// 指定图表的配置项和数据
    var option = {
        title: {
            text: '出借期限越长收益越高',
            left: 'center',
            textStyle: {
            	color: '#fa5e60',
            	fontSize: 16,
            	fontWeight: 'normal'
            }
        },
        grid: [
	        {x: '20%', y: '25%', width: '60%', height: '60%'}
	    ],
        tooltip: {},
        legend: {
            // data:['销量']
        },
        xAxis: {
        	type: 'category',
        	name: '产品类型',
        	boundaryGap: false,
            data: ["3M","4M","5M","6M"]
        },
        yAxis: {
        	type:'value',
        	min: 7.00,
        	max: 10.0,
        	splitNumber : 6,
        	axisLabel: {
	            formatter: '{value} .00'
	        },
        	name: '历史年化收益（%）'
        },
        series: [{
            // name: '销量',
            type: 'line',
            symbol: 'circle',
            symbolSize: 10,
            // data: [ 7.20, 7.50, 8.20, 8.51],
            data: [ (m3*100).toFixed(2), (m4*100).toFixed(2), (m5*100).toFixed(2), (m6*100).toFixed(2)],
            markLine: {
                data: [
                    { name: '最大值',
                    	type: 'max'
                    },
                    { name: '最小值',
                    	type: 'min'
                    }
                ]
            }
        }]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
		var h_s = 0
		$(".echarts_wrap").mouseout(function(e){
				h_s = 1;
				setTimeout(function(){
					if(h_s == 1){
						$(".echarts_wrap").hide();
					}
				},1000)
		});
		$(".echarts_wrap").mouseover(function(e){
				h_s = 0;
		});
   $(_this).parent().find('.echarts_wrap').eq(0).show();
}

/**
* 功能:弹出层
* @author 满口蛀牙
* @date 2016年4月25日
* @param className
* @return null
* @throws null
**/
/*function shwoPopup(positionWidth,positionHeight){
	var dialogWidth=650;
	var dialogHeight=440;
	if(positionWidth==""||positionWidth==0){
		var positionWidth=parseInt(($(document.body).width()-dialogWidth)/2)+"px";
	}else{
		positionWidth=positionWidth+"px";
	}
	if(positionHeight==""||positionHeight==0){
		//var positionHeight=parseInt(($(window).height()-dialogHeight)/2)+"px";
		var positionHeight=parseInt($(document).scrollTop()+(dialogHeight/2))+"px";
	}else{
		positionHeight=positionHeight+"px";
	}
	dialogWidth=dialogWidth+"px";
	dialogHeight=dialogHeight+"px";
	var css={"width":dialogWidth,"height":dialogHeight,"background":"#fff","top":positionHeight,"left":positionWidth,"position":"absolute","z-index":"200"};
	$(document).css("overflow","hidden");
	$(document.body).css("overflow","hidden");
	$("html").css("overflow","hidden");
	$(".dialog-wrap").show().css(css);
}*/

function shwoPopup(positionWidth,positionHeight){
	var scrollTop = $(document).scrollTop();
	var dialogWidth=650;
	var dialogHeight=440;
	// if(positionWidth==""||positionWidth==0){
	// 	var positionWidth=parseInt(($(document.body).width()-dialogWidth)/2)+"px";
	// }else{
	// 	positionWidth=positionWidth+"px";
	// }
	// if(positionHeight==""||positionHeight==0){
	// 	//var positionHeight=parseInt(($(window).height()-dialogHeight)/2)+"px";
	// 	var positionHeight=parseInt($(document).scrollTop()+(dialogHeight/2))+"px";
	// }else{
	// 	positionHeight=positionHeight+"px";
	// }
	// dialogWidth=dialogWidth+"px";
	// dialogHeight=dialogHeight+"px";
	var css={"width":dialogWidth+"px","height":dialogHeight+"px","background":"#fff","top":'50%',"left":'50%',"margin-left":-dialogWidth/2+'px',"margin-top":scrollTop-dialogHeight/2+'px',"position":"absolute","z-index":"200"};
	$(document).css("overflow","hidden");
	$(document.body).css("overflow","hidden");
	$("html").css("overflow","hidden");
	$(".dialog-wrap").css(css).show();

}

function shwoPopup2(positionWidth,positionHeight){
	var scrollTop = $(document).scrollTop();
	var dialogWidth=650;
	var dialogHeight=440;
	// if(positionWidth==""||positionWidth==0){
	// 	var positionWidth=parseInt(($(document.body).width()-dialogWidth)/2)+"px";
	// }else{
	// 	positionWidth=positionWidth+"px";
	// }
	// if(positionHeight==""||positionHeight==0){
	// 	//var positionHeight=parseInt(($(window).height()-dialogHeight)/2)+"px";
	// 	var positionHeight=parseInt($(document).scrollTop()+(dialogHeight/2))+"px";
	// }else{
	// 	positionHeight=positionHeight+"px";
	// }
	// dialogWidth=dialogWidth+"px";
	// dialogHeight=dialogHeight+"px";
	var css={"width":dialogWidth+"px","height":dialogHeight+"px","background":"#fff","top":'50%',"left":'50%',"margin-left":-dialogWidth/2+'px',"margin-top":scrollTop-dialogHeight/2+'px',"position":"absolute","z-index":"200"};
	$(document).css("overflow","hidden");
	$(document.body).css("overflow","hidden");
	$("html").css("overflow","hidden");
	$(".dialog-wrap2").css(css).show();

}


/**
* 功能:显示遮罩层
* @author 满口蛀牙
* @date 2016年4月25日
* @param index
* @param className
* @return null
* @throws null
**/
function showMask(index,className){
	//var width=($(document.body).width()+$(document).scrollLeft())+"px";
	if(index<0||index>9999){
		index=100;
	}
	if(className==""){
		return;
	}
	var html="<div class=\"dialog-mask"+" "+className+"\"></div>";
	$(document.body).append(html);
	var width=window.screen.width;
	var height=$(document.body).height()+"px";
	var css={"width":width,"height":height,"background":"#000","top":"0px","left":"0px","position":"absolute","z-index":index,"filter":"alpha(opacity=50)","opacity":0.5,"-moz-opacity":0.5};
	$("."+className).css(css).show;
}
/**
* 功能:关闭遮罩层
* @author 满口蛀牙
* @date 2016年4月26日
* @param className
* @return null
* @throws null
**/
function hiddenMask(className){
	//var css={"width":"0px","height":"0px"};
	//$(".dialog-mask").css(css);
	//$(".dialog-mask").hide();
	$("."+className).remove();
}
/**
* 功能:关闭弹出层
* @author 满口蛀牙
* @date 2016年4月27日
* @param className
* @return null
* @throws null
**/
function closePopup(){
	var css={"width":"0px","height":"0px"};
	$(document).css("overflow","auto");
	$(document.body).css("overflow","auto");
	$("html").css("overflow","auto");
	$(".dialog-wrap").css(css).hide();
	$(".dialog-wrap2").css(css).hide();
}
/**
* 功能:关闭弹出层
* @author 满口蛀牙
* @date 2016年4月27日
* @param className
* @return null
* @throws null
**/
function closeDialog(className){
	if(closeDialog==""){
		return;
	}
	closePopup();
	hiddenMask(className);
}
/**
* 功能:拖拽弹出框
* @author 满口蛀牙
* @date 2016年4月30日
* @param _this
* @return null
* @throws null
**/
function dragDiaLog(_this){
	//获取屏幕可视区域宽高
	var visibleWidth=$(window).width();
	var visibleHeight=$(window).height();
	var offsetTop=$(_this).parent().css("top");
	var offsetLeft=$(_this).parent().css("left");
	var mouseLeft=0;
	var mouseTop=0;
	$(_this).mousemove(function (event){
        mouseLeft=event.pageX;
        mouseTop=event.pageY;
        //alert(mouseLeft);
    	shwoPopup(mouseLeft,mouseTop);
    });
	//var positionWidth=parseInt((ox-550)/2);
    //var positionHeight=parseInt((oy-440)/2);
    //shwoPopup(positionWidth,positionHeight);


}
/**
* 功能:停止拖拽弹出框
* @author 满口蛀牙
* @date 2016年4月30日
* @param _this
* @return null
* @throws null
**/
function stopDialog(_this){
	$(_this).mouseup(function (event){
		mouseLeft=event.pageX;
        mouseTop=event.pageY;
        //alert(mouseLeft);
    	shwoPopup(mouseLeft,mouseTop);
        $(_this).unbind("mousemove");
        $(_this).unbind("mousedown");

    });
}
/**
* 功能:身份证校验
* @author 满口蛀牙
* @date 2016年4月31日
* @param code
* @return null
* @throws null
**/
function IdentityCodeValid(code) {
	//console.log("IdentityCodeValid调用了");
    var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
    var tip = "";
    var pass= true;

    if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
        tip = "身份证号格式错误";
        pass = false;
    }

   else if(!city[code.substr(0,2)]){
        tip = "地址编码错误";
        pass = false;
    }
    else{
        //18位身份证需要验证最后一位校验位
        if(code.length == 18){
            code = code.split('');
            //∑(ai×Wi)(mod 11)
            //加权因子
            var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
            //校验位
            var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
            var sum = 0;
            var ai = 0;
            var wi = 0;
            for (var i = 0; i < 17; i++)
            {
                ai = code[i];
                wi = factor[i];
                sum += ai * wi;
            }
            var last = parity[sum % 11];
            if(parity[sum % 11] != code[17]){
                tip = "校验位错误";
                pass =false;
            }
        }
    }
    if(!pass) {
   		crfErrAlert('errorInfo',"错误",tip,0,"");
    }
    return pass;
}

function IdentityCodeValid2(code) {
	//console.log("IdentityCodeValid调用了");
    var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
    var tip = "";
    var pass= true;

    if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
        tip = "身份证号格式错误";
        pass = false;
    }

   else if(!city[code.substr(0,2)]){
        tip = "地址编码错误";
        pass = false;
    }
    else{
        //18位身份证需要验证最后一位校验位
        if(code.length == 18){
            code = code.split('');
            //∑(ai×Wi)(mod 11)
            //加权因子
            var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
            //校验位
            var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
            var sum = 0;
            var ai = 0;
            var wi = 0;
            for (var i = 0; i < 17; i++)
            {
                ai = code[i];
                wi = factor[i];
                sum += ai * wi;
            }
            var last = parity[sum % 11];
            if(parity[sum % 11] != code[17]){
                tip = "校验位错误";
                pass =false;
            }
        }
    }
    if(!pass) {
   		return false;
    }
    return pass;
}

///计算两个整数的百分比值
function GetPercent(num, total) {
	num = parseFloat(num);
	total = parseFloat(total);
	if (isNaN(num) || isNaN(total)) {
		return "-";
	}
	return total <= 0 ? "0%" : (Math.round(num / total * 10000) / 100.00 + "%");
}
//去除百分号
function removePercent(num){
	var num=num.substring(0,num.length-1);
	return parseFloat(num/100);
}
function getHome(){
	return getCookie("_home");
}

function getCookie(c_name)
{
if (document.cookie.length>0)
  {
  c_start=document.cookie.indexOf(c_name + "=")
  if (c_start!=-1)
    {
    c_start=c_start + c_name.length+1
    c_end=document.cookie.indexOf(";",c_start)
    if (c_end==-1) c_end=document.cookie.length
    return unescape(document.cookie.substring(c_start,c_end))
    }
  }
return ""
}
/**
* 功能:数组千分位格式
* @author 满口蛀牙
* @date 2016年5月02日
* @param num
* @return null
* @throws null
**/
function format(num) {
    var result = '', counter = 0;
    num = (num || 0).toString();
    for (var i = num.length - 1; i >= 0; i--) {
        counter++;
        result = num.charAt(i) + result;
        if (!(counter % 3) && i != 0) { result = ',' + result; }
    }
    return result;
}

function toThousands(number) {
	if(Number(number)!=0){
		var num = number + "";
         num = num.replace(new RegExp(",","g"),"");
         // 正负号处理
         var symble = "";
         if(/^([-+]).*$/.test(num)) {
             symble = num.replace(/^([-+]).*$/,"$1");
             num = num.replace(/^([-+])(.*)$/,"$2");
         }

         if(/^[0-9]+(\.[0-9]+)?$/.test(num)) {
             var num = num.replace(new RegExp("^[0]+","g"),"");
             if(/^\./.test(num)) {
             num = "0" + num;
             }

             var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1");
             var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1");

             var re=/(\d+)(\d{3})/;

             while(re.test(integer)){
                 integer = integer.replace(re,"$1,$2");
             }
             return symble + integer + decimal;

         } else {
             return number;
         }
	}else{
		return "0.00";
	}

}

/**
 * 去除千分位
 *@param{Object}num
 */
function delcommafy(num){
   num = num.replace(/[ ]/g, "");//去除空格
   num=num.replace(/,/gi,'');
   return num;
}
/**
* 功能:日期转时间戳
* @author 满口蛀牙
* @date 2016年5月02日
* @param datetime
* @return null
* @throws null
**/
function datetimeToUnix(datetime){
    var tmp_datetime = datetime.replace(/-/g,'/');
   // tmp_datetime = tmp_datetime.replace(/ /g,'/');
    //var arr = tmp_datetime.split("-");
    //var now = new Date(Date.UTC(arr[0],arr[1]-1,arr[2],arr[3]-8,arr[4],arr[5]));
    //return parseInt(now.getTime());
	var date = new Date(tmp_datetime);
    return date.getTime();
}
/**
* 功能:时间戳转日期
* @author 满口蛀牙
* @date 2016年5月02日
* @param unix
* @return null
* @throws null
**/
function unixToDatetime(unix) {
    var now = new Date(parseInt(unix));
    //return now.toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
    //return now.toLocaleString();
    var year=now.getFullYear();
    var month=now.getMonth()+1;
    var date=now.getDate();
    var hours=now.getHours();
    var minutes=now.getMinutes();
    var seconds=now.getSeconds();
    //2015-08-22 15:47:05
    return year+"-"+Appendzero(month)+"-"+Appendzero(date)+" "+Appendzero(hours)+":"+Appendzero(minutes)+":"+Appendzero(seconds);
}
function Appendzero(obj)
    {
        if(obj<10) return "0" +""+ obj;
        else return obj;
    }
//除法函数，用来得到精确的除法结果
//说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
//调用：accDiv(arg1,arg2)
//返回值：arg1除以arg2的精确结果
function accDiv(arg1,arg2){
    var t1=0,t2=0,r1,r2;
    try{t1=arg1.toString().split(".")[1].length}catch(e){}
    try{t2=arg2.toString().split(".")[1].length}catch(e){}
    with(Math){
    r1=Number(arg1.toString().replace(".",""))
    r2=Number(arg2.toString().replace(".",""))
    return (r1/r2)*pow(10,t2-t1);
    }
}
//给Number类型增加一个div方法，调用起来更加方便。
Number.prototype.div = function (arg){
    return accDiv(this, arg);
}

//乘法函数，用来得到精确的乘法结果
//说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
//调用：accMul(arg1,arg2)
//返回值：arg1乘以arg2的精确结果
function accMul(arg1,arg2){
    var m=0,s1=arg1.toString(),s2=arg2.toString();
    try{m+=s1.split(".")[1].length}catch(e){}
    try{m+=s2.split(".")[1].length}catch(e){}
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
}
//给Number类型增加一个mul方法，调用起来更加方便。
Number.prototype.mul = function (arg){
    return accMul(arg, this);
}

//加法函数，用来得到精确的加法结果
//说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
//调用：accAdd(arg1,arg2)
//返回值：arg1加上arg2的精确结果
function accAdd(arg1,arg2){
    var r1,r2,m;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2))
    return (arg1*m+arg2*m)/m
}
//给Number类型增加一个add方法，调用起来更加方便。
Number.prototype.add = function (arg){
    return accAdd(arg,this);
}

//在你要用的地方包含这些函数，然后调用它来计算就可以了。
//比如你要计算：7*0.8 ，则改成 (7).mul(8)
//其它运算类似，就可以得到比较精确的结果。
//减法函数
function Subtr(arg1,arg2){
    var r1,r2,m,n;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
     //last modify by deeka
    //动态控制精度长度
    n=(r1>=r2)?r1:r2;
    return ((arg1*m-arg2*m)/m).toFixed(n);
}

String.prototype.trim = function() {
	return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
}
/**
* 功能:获取页面referrer
* @author 满口蛀牙
* @date 2016年8月07日
* @param unix
* @return null
* @throws null
**/
function getReferrer() {
    var referrer = '';
    try {
        referrer = window.top.document.referrer;
    } catch(e) {
        if(window.parent) {
            try {
                referrer = window.parent.document.referrer;
            } catch(e2) {
                referrer = '';
            }
        }
    }
    if(referrer === '') {
        referrer = document.referrer;
    }
    return referrer;
};

/**
* 功能:ajax加载产品详情
* @author 满口蛀牙
* @date 2016年5月03日
* @param unix
* @return null
* @throws null
**/
function loadProductInfo(preId){
	$.ajax({
        url:'/webp2p_interface_mysql/investment/product/'+preId+'/detail',
        type:'get',
        dataType:'json',
        async:false,
        cache: false,
        contentType: "application/json;charset=UTF-8",
        success:function(data){
			// console.log(data);
			if(data.result==="0000"){
				// 成功
				var obj = data.data;
				$(".dialog-product-name").text(toThousands(obj.productName));
				$(".dialog-product-proYearRate").text(obj.yInterestRate+"%");
				$(".dialog-product-closeDay").text(obj.freezePeriod+"天");
				$(".dialog-mashangchujie").attr("rel",obj.contractPrefix);
				$(".dialog-product-minInvestAmount").text(toThousands(obj.lowestAmount));
				$(".dialog-product-amountUnit").text(toThousands(obj.investunit));

				var cookieStr=$.cookie("ajaxUserInfo");
				var ajaxUserInfo=JSON.parse(cookieStr);
				$.each(ajaxUserInfo, function(index, object) {
					if(!object.accountStatus || object.accountStatus==1){
						html="<a href=\"/openaccount.html\">开户</a>";
						$(".dialog-wrap .dialog-product-balance").html(html);
					}else{
						shdAssetsTotal();
					}
				});
			}else{
				var errorMsg=JSON.parse(xhr.responseText).message;
			}
        },
        error:function(xhr){
        	// console.log(xhr);
        	var errorCode=JSON.parse(xhr.responseText).code;
        	var errorMsg=JSON.parse(xhr.responseText).message;
        }
    });


}

// 连盈月盈账户可投余额
function shdAssetsTotal(){
	var uuid=$.cookie('uuid');
	var accessToken=$.cookie('accessToken');
	var deviceno="CRF_OFFICIAL_WEBSITE";
	var packageName="com.crfchina.webP2p";
	var mobileOs="officialWebsite";
	$.ajax({
        url:'/webp2p_interface_mysql/investment/product/'+uuid+'/balance',
        type:'post',
        dataType:'json',
        // async:false,
        cache: false,
        contentType: "application/json;charset=UTF-8",
        headers: { 
	        'accessToken': accessToken, 
	        'deviceno': deviceno, 
	        'packageName': packageName, 
	        'mobileOs': mobileOs 
        },
        success:function(data){
			// console.log(data);
			if(data.result==="0000"){
				var abalanceList=data.data.accountBalanceDetailList[0];
				var html=toThousands(abalanceList.availableBalance)+"元<a href=\"/rechargeInit.html\" class=\"addMoney\">充值</a>";
				$(".dialog-wrap .dialog-product-balance").html(html);
			}else{
        		var errorMsg=data.message;
			}
        },
        error:function(xhr){
        	// console.log(xhr);
        	var errorCode=JSON.parse(xhr.responseText).code;
        	var errorMsg=JSON.parse(xhr.responseText).message;
        	var errorCodeArr=["FUS_2000","FUS_2001","FUS_2002","FUS_2004","FUS_2005","FUS_2006","FUS_2007","FUS_2008","FUS_2009"];
        	for(var i=0;i<errorCodeArr.length;i++){
        		if(errorCodeArr[i].indexOf(errorCode)!= -1){
        		//	$(".xjd__or_charge").html("<a href=\"/\">登录</a>");
        			$.cookie('ajaxUserInfo', null, { path: '/' });
					shipTo('Mask');
        		}
        	}
        }
    });
}

/**
* 功能:马上出借
* @author 满口蛀牙
* @date 2016年5月05日
* @param _this
* @return null
* @throws null
**/
function lendNow(_this){

	var id=$(_this).attr("rel");
	var money=Number($(".dialog-chuzhijinge").val());
	var agree=$(".i-agreement").prop("checked");
	var minInvestAmount=Number(delcommafy($(".dialog-product-minInvestAmount").text()));
	var amountUnit=Number(delcommafy($(".dialog-product-amountUnit").text()));
	$.each(ajaxUserInfo, function(index, object) {
		if( object.username=="null" || !object.username){
			//window.location.href="/";
			shipTo('Mask');
			return;
		}
		/*var flag = checkGrant();
		if(flag){
			return;
		}*/
		if(object.accountStatus==1){
			return crfInofAlert('notification',"通知","您还没有开户,现在就去",1,"/openaccount.html");
		}
		if(money<=0){
			return crfErrAlert('errorInfo',"错误","出资金额不能为空",0,"");
		}
		if(!agree){
        	return crfErrAlert('errorInfo',"错误","请勾选协议",0,"");
		}
		if(money<minInvestAmount){
			return crfErrAlert('errorInfo',"错误","出资金额不能小于"+toThousands(minInvestAmount)+"元",0,"");
		}
		if((money%amountUnit)!=0){
			return crfErrAlert('errorInfo',"错误","出资金额需为"+toThousands(amountUnit)+"的整数倍",0,"");
		}
		// if(money>Number(object.usablesum)){
		// 	return crfErrAlert('errorInfo',"错误","对不起,您的投资金额大于您账户金额的最大值"+toThousands(object.usablesum)+"元",0,"");
		// }
		/**出资检测**/
		ajaxCheckFincInvest(money,id);
	});
}

function lendNow2(_this){

	var id=$(_this).attr("rel");
	var money=Number($(".product_chuzi_money_input").val());
	var agree=$(".i-agreement2").prop("checked");
	// var minInvestAmount=Number($(".dialog-product-minInvestAmount").text());
	var amountUnit=Number(delcommafy($(".base_money").text()));
	var maxInvestAmount=Number(delcommafy($(".limit_money").text()));
	$.each(ajaxUserInfo, function(index, object) {
		if( object.username=="null" || !object.username){
			//window.location.href="/";
			shipTo('Mask');
			return;
		}
		/*var flag = checkGrant();
		if(flag){
			return;
		}*/
		if(object.accountStatus==1){
			return crfInofAlert('notification',"通知","您还没有开户,现在就去",1,"/openaccount.html");
		}
		if(money<=0){
			return crfErrAlert('errorInfo',"错误","出资金额不能为空",0,"");
		}
		if(!agree){
        	return crfErrAlert('errorInfo',"错误","请勾选协议",0,"");
		}
		if(money<amountUnit){
			return crfErrAlert('errorInfo',"错误","出资金额不能小于"+toThousands(amountUnit)+"元",0,"");
		}
		if(money>maxInvestAmount){
			return crfErrAlert('errorInfo',"错误","出资金额不能大于"+toThousands(maxInvestAmount)+"元",0,"");
		}
	    if((money%amountUnit)!=0){
		 	return crfErrAlert('errorInfo',"错误","出资金额需为"+toThousands(amountUnit)+"的整数倍",0,"");
		}
		// if(money>Number(object.usablesum)){
		// 	return crfErrAlert('errorInfo',"错误","对不起,您的投资金额大于您账户金额的最大值"+toThousands(object.usablesum)+"元",0,"");
		// }
		/**出资检测**/
		ajaxCheckFincInvest2(money,id);
	});
}
/**现金贷产品出资**/
function ajaxCheckFincInvest2(money,financeId){
	$(".dialog-load-wrap").ajaxStart(function(){
		showMask(600,"doFincInvest");
		$(".dialog-load-title").text("提示信息");
		$(".dialog-load").text("正在出资，请稍等...");
		var dialogWidth=550;
		var dialogHeight=190;
		var positionWidth=parseInt(($(document.body).width()-dialogWidth)/2)+"px";
		var positionHeight=parseInt(($(document).scrollTop()+dialogHeight))+"px";
		var css={"width":dialogWidth,"height":dialogHeight,"background":"#fff","top":positionHeight,"left":positionWidth,"position":"absolute","z-index":"650"};
        $(".dialog-load-wrap").css(css).show();
    });
    $(".dialog-load-wrap").ajaxStop(function(){
    	hiddenMask("doFincInvest");
        $(".dialog-load-title").text("");
		$(".dialog-load").text("");
        $(".dialog-load-wrap").hide();
     });

	var uuid=$.cookie('uuid');
	var accessToken=$.cookie('accessToken');
	var deviceno="CRF_OFFICIAL_WEBSITE";
	var packageName="com.crfchina.webP2p";
	var mobileOs="officialWebsite";
	var param = {
		planNo: financeId, 
		amount: Math.round(money*10000/100),	 
		source: "1"	//官网
	}
	$.ajax({
        url:'/webp2p_interface_mysql/investment/'+uuid+'/invest',
        type:'post',
        data:JSON.stringify(param),
        dataType:'json',
        // async:false,
        cache: false,
        contentType: "application/json;charset=UTF-8",
        headers: { 
	        'accessToken': accessToken, 
	        'deviceno': deviceno, 
	        'packageName': packageName, 
	        'mobileOs': mobileOs 
        },
        success:function(data){
			// console.log(data);
			if(data.result==="0000"){
				// 成功
				getPlanProgressBatch();
				return crfInofAlert3('notification',"成功","恭喜您!您的出资已经处理...",0,"",1);
			}else{
        		var errorMsg=data.message;
        		crfErrAlert('errorInfo',"温馨提示",errorMsg,0,"");
			}
        },
        error:function(xhr){
        	// console.log(xhr);
        	var errorCode=JSON.parse(xhr.responseText).code;
        	var errorMsg=JSON.parse(xhr.responseText).message;
        	crfErrAlert('errorInfo',"温馨提示",errorMsg,0,"");
        	var errorCodeArr=["FUS_2000","FUS_2001","FUS_2002","FUS_2004","FUS_2005","FUS_2006","FUS_2007","FUS_2008","FUS_2009"];
        	for(var i=0;i<errorCodeArr.length;i++){
        		if(errorCodeArr[i].indexOf(errorCode)!= -1){
        			var timenow = new Date().getTime();
				//	window.location.href="/?_="+timenow;
        		}
        	}
        }
    });

}


/**
* 功能:出资检测
* @author 满口蛀牙
* @date 2016年5月06日
* @param path
* @param money
* @param financeId
* @return null
* @throws null
**/
function ajaxCheckFincInvest(money,financeId){
	closeDialog('produceMask');
	$(".dialog-load-wrap").ajaxStart(function(){
		showMask(600,"doFincInvest");
		$(".dialog-load-title").text("提示信息");
		$(".dialog-load").text("正在出资，请稍等...");
		var dialogWidth=550;
		var dialogHeight=190;
		var positionWidth=parseInt(($(document.body).width()-dialogWidth)/2)+"px";
		var positionHeight=parseInt(($(document).scrollTop()+dialogHeight))+"px";
		var css={"width":dialogWidth,"height":dialogHeight,"background":"#fff","top":positionHeight,"left":positionWidth,"position":"absolute","z-index":"650"};
        $(".dialog-load-wrap").css(css).show();
    });
    $(".dialog-load-wrap").ajaxStop(function(){
    	hiddenMask("doFincInvest");
        $(".dialog-load-title").text("");
		$(".dialog-load").text("");
        $(".dialog-load-wrap").hide();
     });
	var uuid=$.cookie('uuid');
	var accessToken=$.cookie('accessToken');
	var deviceno="CRF_OFFICIAL_WEBSITE";
	var packageName="com.crfchina.webP2p";
	var mobileOs="officialWebsite";
	var param = {
		planNo: financeId,  
		amount: Math.round(money*10000/100),	 
		source: "1"	//官网
	}
	$.ajax({
        url:'/webp2p_interface_mysql/investment/'+uuid+'/invest',
        type:'post',
        data:JSON.stringify(param),
        dataType:'json',
        // async:false,
        cache: false,
        contentType: "application/json;charset=UTF-8",
        headers: { 
	        'accessToken': accessToken, 
	        'deviceno': deviceno, 
	        'packageName': packageName, 
	        'mobileOs': mobileOs 
        },
        success:function(data){
			// console.log(data);
			if(data.result==="0000"){
				// 成功
				return crfInofAlert3('notification',"成功","恭喜您!您的出资已经处理...",0,"",1);
			}else{
        		var errorMsg=data.message;
        		crfErrAlert('errorInfo',"温馨提示",errorMsg,0,"");
			}
        },
        error:function(xhr){
        	// console.log(xhr);
        	var errorCode=JSON.parse(xhr.responseText).code;
        	var errorMsg=JSON.parse(xhr.responseText).message;
        	crfErrAlert('errorInfo',"温馨提示",errorMsg,0,"");
        	var errorCodeArr=["FUS_2000","FUS_2001","FUS_2002","FUS_2004","FUS_2005","FUS_2006","FUS_2007","FUS_2008","FUS_2009"];
        	for(var i=0;i<errorCodeArr.length;i++){
        		if(errorCodeArr[i].indexOf(errorCode)!= -1){
        			var timenow = new Date().getTime();
					//window.location.href="/?_="+timenow;
        		}
        	}
        }
    });
}
//用户出资成功过后更新用户信息(回调)
function callBack2(){
	//console.log("用户出资成功过后更新用户信息(回调)");
	//window.location.href="/index.html";
}
/**重新请求用户信息**/
function reloadUserInfo(){
		var date=new Date();
	 		var jsonData={"date":date.getTime(),"sessionID":$.cookie('sessionID')};
	 		var path="home_CrfUser_check.do";
	 		var host = window.location.host;
			var url="/ajax/"+path;
			$.ajax({
				type : "POST",
				url : url,
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(jsonData),
				success : function(result) {
					if (result === null || result == "null" || result === "" || !isNaN(result)) {
						//console.log("ajax请求出错");
					} else {
						ajaxUserInfo=result;
						$.cookie('ajaxUserInfo', JSON.stringify(result), {path:'/'});
						//跳转到用户登录页面
						if(document.referrer==""||document.referrer=="undefined"){
						//	window.location.href="/index.html";
						}else{
							window.location.href=document.referrer;
						}
					}
				}
			});
}

function limitInputMoney(){
	var inputMoney=Number($(".product_chuzi_money_input").val());
	var baseMoney=Number(delcommafy($(".base_money").eq(0).text()));
	var limit_money=Number(delcommafy($(".limit_money").text()));
	if(inputMoney>=limit_money){
		$(".product_chuzi_money_input").val(limit_money);
	}
	if(inputMoney<=baseMoney){
		$(".product_chuzi_money_input").val(baseMoney);
	}
}

/**
* 功能:显示协议
* @author 满口蛀牙
* @date 2016年5月12日
* @param null
* @return null
* @throws null
**/
function showProtocol(className){
	$.each(ajaxUserInfo, function(index, object) {
		showMask(500,className);
		//ajax加载出家人服务协议
		var host = window.location.host;
		var path="home_FinanceProduct_financeAgreement.do?typeId=2";
		var url="/ajax/"+path;
		$.ajax({
			type:"GET",
			url:url,
			success:function(data){
				if(data!=""&&data!=null){
					createProtocol("线上出借人服务协议",data.center);
				}else{
					return crfErrAlert('errorInfo',"错误","没有找到相关协议",0,"");
				}
			}
		});
	});
}

function showProtocol2(className,_this){
	var IS_NEW_BIE=$(_this).attr("IS_NEW_BIE");
	if(IS_NEW_BIE==1){
		IS_NEW_BIE=13;
	}else{
		IS_NEW_BIE=10;
	}
	$.each(ajaxUserInfo, function(index, object) {
		showMask(500,className);
		//ajax加载出家人服务协议
		var host = window.location.host;
		var path="home_FinanceProduct_financeAgreement.do?typeId="+IS_NEW_BIE;
		var url="/ajax/"+path;
		$.ajax({
			type:"GET",
			url:url,
			success:function(data){
				if(data!=""&&data!=null){
					createProtocol("线上出借人服务协议",data.center);
				}else{
					return crfErrAlert('errorInfo',"错误","没有找到相关协议",0,"");
				}
			}
		});
	});
}

/**
*质保计划协议
*date 2017-03-27
*/
function showProtocol3(className){
	$.each(ajaxUserInfo, function(index, object) {
		showMask(500,className);
		//ajax加载出家人服务协议
		var host = window.location.host;
		var path="home_FinanceProduct_financeAgreement.do?typeId=20";
		var url="/ajax/"+path;
		$.ajax({
			type:"GET",
			url:url,
			success:function(data){
				if(data!=""&&data!=null){
					createProtocol("质保计划",data.center);
				}else{
					return crfErrAlert('errorInfo',"错误","没有找到相关协议",0,"");
				}
			}
		});
	});
}

/**
*委托信而富匹配借款人
*date 2017-03-27
*/
function showProtocol4(className){
	$.each(ajaxUserInfo, function(index, object) {
		showMask(500,className);
		//ajax加载出家人服务协议
		var host = window.location.host;
		var path="home_FinanceProduct_financeAgreement.do?typeId=21";
		var url="/ajax/"+path;
		$.ajax({
			type:"GET",
			url:url,
			success:function(data){
				if(data!=""&&data!=null){
					createProtocol("委托信而富匹配借款人",data.center);
				}else{
					return crfErrAlert('errorInfo',"错误","没有找到相关协议",0,"");
				}
			}
		});
	});
}

function showGrantProtocol(className,title,typeId){
		showMask(500,className);
		//ajax加载出家人服务协议
		var host = window.location.host;
		var path="querytips.do";
		var url="/user/"+path;
		$.ajax({
			type:"POST",
			url:url,
			data:{'typeId':typeId},
			success:function(data){
				if(data!=""&&data!=null){
					createProtocol(title,data);
				}else{
					return crfErrAlert('errorInfo',"错误","没有找到相关协议",0,"");
				}
			}
		});
}
//更新预期收益
function updataReta(){
	var money=Number($(".product_chuzi_money_input").val());
	var simpleInterest=$(".simpleInterest").val();
	if(simpleInterest){
		var day=$(".dialog-product-closeDay2").html();
		var day=Number(day.substring(0,day.length-1));
		simpleInterest=(parseFloat(simpleInterest)*100)/10000;
		var moneyTemp=simpleInterest*day*money/360;
		$(".product_money_text_num").html(moneyTemp.toFixed(2));
	}
}

/**
* 功能:显示注册协议
* @author 满口蛀牙
* @date 2016年5月12日
* @param null
* @return null
* @throws null
**/
function showRegProtocol(className){
	showMask(500,className);
	//ajax加载出家人服务协议
	var host = window.location.host;
	var path="querytips.do";
	var url="/user/"+path;
	$.ajax({
		type:"POST",
		url:url,
		data:"date="+new Date(),
		success:function(data){
			if(data!=""&&data!=null){
				createProtocol("信而富平台注册协议",data);
			}else{
				return crfErrAlert('errorInfo',"错误","没有找到相关协议",0,"");
			}
		}
	});
}

// 新获取协议
function getProtocol(wrapDom,protocolType,contentDom){
	$.ajax({
        url:'/webp2p_interface_mysql/apppageconfig/getAppPageConfig/'+protocolType,
        type:'get',
        dataType:'json',
        async:true,
        cache: false,
        contentType: "application/json;charset=UTF-8",
        success:function(data){
			// console.log(data);
			if(data.result==="0000"){
				// 发送成功
				var protocolList=data.data[protocolType];
				for(var i=0;i<protocolList.length;i++){
					var protocolContent=protocolList[i].content;
					var protocolLink=protocolList[i].jumpUrl;
					var protocolName=protocolList[i].name; 
					var a = document.createElement('a');  
	                a.setAttribute('href', protocolLink);  
	              	a.setAttribute('target', '_blank'); 
	              	a.innerHTML=protocolName;
	              	$(wrapDom).append(a); 
	              	if(contentDom){
	              		$(contentDom).html(protocolContent); 
	              	}
				}
			}else{
				var errorMsg=JSON.parse(xhr.responseText).message;
			}
        },
        error:function(xhr){
        	// console.log(xhr);
        	var errorCode=JSON.parse(xhr.responseText).code;
        	var errorMsg=JSON.parse(xhr.responseText).message;
        }
    });
}

/**
* 功能:协议弹框
* @author 满口蛀牙
* @date 2016年5月12日
* @param null
* @return null
* @throws null
**/
function createProtocol(title,msg){
	var dialogWidth=800;
	var dialogHeight=400;
	var positionWidth=parseInt(($(document.body).width()-dialogWidth)/2)+"px";
	var positionHeight=parseInt(($(document).scrollTop()+(dialogHeight/2)))+"px";
	var css={"width":dialogWidth,"height":dialogHeight,"background":"#fff","top":positionHeight,"left":positionWidth,"position":"absolute","z-index":"600"};
	$(".dialog-protocol-title").text(title);
	$(".dialog-protocol-wrap-content").html(msg);
	$(".dialog-protocol-wrap").css(css).show();
	$(".dialog-protocol-wrap").draggabilly({ handle:'.dialog-protocol-top'});
}
/**
* 功能:关闭协议弹框
* @author 满口蛀牙
* @date 2016年5月12日
* @param null
* @return null
* @throws null
**/
function closeProtocol(className){
	hiddenMask(className);
	$(".dialog-protocol-wrap").css({"display":"none"});
}
/**
* 功能:发送一个ajax请求,获取网站公告
* @author 满口蛀牙
* @date 2016年4月18日
* @param url
* @param jsonData
* @return null
* @throws null
**/
function getAnnouncement(url,jsonData) {
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				//放置到全局变量
				window.user=result;
				$.each(result, function(index, object) {
					//$("").val(object.message);
					alert(object.createTime);
				});
				//window.location.reload();
			}
		}
	});
}

/**
* 功能:发送一个ajax请求,获取媒体报道
* @author 满口蛀牙
* @date 2016年4月18日
* @param url
* @param jsonData
* @return null
* @throws null
**/
function getMediaReport(url,jsonData){
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				//放置到全局变量
				window.user=result;
				$.each(result, function(index, object) {
					//$("").val(object.message);
					//alert(object.createTime);
				});
				//window.location.reload();
			}
		}
	});
}

/**
* 功能:发送一个ajax请求,获取新而富动态
* @author 满口蛀牙
* @date 2016年4月18日
* @param url
* @param jsonData
* @return null
* @throws null
**/
function getNewsDynamic(url,jsonData){
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				//放置到全局变量
				window.user=result;
				$.each(result, function(index, object) {
					//$("").val(object.message);
					//alert(object.createTime);
				});
				//window.location.reload();
			}
		}
	});
}

/**
* 功能:产品详情页"质保计划"(弹出框)
* @author 满口蛀牙
* @date 2016年4月18日
* @param url
* @param null
* @return null
* @throws null
**/
function openRiskTips(){
	var dialogWidth=800;
	var dialogHeight=400;
	var positionWidth=parseInt(($(document.body).width()-dialogWidth)/2)+"px";
	var positionHeight=parseInt(($(document).scrollTop()+(dialogHeight/2)))+"px";
	var css={"width":dialogWidth,"height":dialogHeight,"background":"#fff","top":positionHeight,"left":positionWidth,"position":"absolute","z-index":"600"};
	$.get("/security/plan.html", function(text){
		//console.log(text)
		var html = $(text).find(".secondcontent");
		$(html).find('h3').eq(0).css("text-align","center");
		$(".dialog-protocol-title").text("质保计划");
		$(".dialog-protocol-wrap-content").html($(html).html()).css("padding","20px");
		$(".dialog-protocol-wrap").css(css).show();
		$(".dialog-protocol-wrap").draggabilly({ handle:'.dialog-protocol-top'});
	});
}

/**
* 功能:ajax获取质保计划
* @author 满口蛀牙
* @date 2016年4月18日
* @param url
* @param null
* @return null
* @throws null
**/
function getQualityPlane(){
	var result="";
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonData),
		//data: eval('('+jsonData+')'),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				//放置到全局变量
				window.user=result;
				$.each(result, function(index, object) {
					//$("").val(object.message);
					//alert(object.createTime);
				});
				//window.location.reload();
			}
			return result;
		}
	});
}
/**
* 功能:发送一个ajax请求,获取新而富产品
* @author 满口蛀牙
* @date 2016年4月18日
* @param url
* @param jsonData
* @return null
* @throws null
**/
function getFinanceProduct(url,jsonData){
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				//放置到全局变量
				window.user=result;
				$.each(result, function(index, object) {
					//$("").val(object.message);
					//alert(object.createTime);
				});
				//window.location.reload();
			}
		}
	});
}

/**
* 功能:发送一个ajax请求,获取投资记录
* @author 满口蛀牙
* @date 2016年4月18日
* @param url
* @param jsonData
* @return null
* @throws null
**/
function getDepositsHistory(url,jsonData){
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				//放置到全局变量
				window.user=result;
				$.each(result, function(index, object) {
					//$("").val(object.message);
					//alert(object.createTime);
				});
				//window.location.reload();
			}
		}
	});
}

/**
* 功能:发送一个ajax请求,获取地区数据
* @author 满口蛀牙
* @date 2016年4月18日
* @param url
* @param jsonData
* @return null
* @throws null
**/
function getCrfRegion(url,jsonData){
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				//放置到全局变量
				window.user=result;
				$.each(result, function(index, object) {
					//$("").val(object.message);
					//alert(object.createTime);
				});
				//window.location.reload();
			}
		}
	});
}

/**
* 功能:发送一个ajax请求,获取借款交易列表
* @author 满口蛀牙
* @date 2016年4月18日
* @param url
* @param jsonData
* @return null
* @throws null
**/
function getCLSPacount(url,jsonData){
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				//放置到全局变量
				window.user=result;
				$.each(result, function(index, object) {
					//$("").val(object.message);
					//alert(object.createTime);
				});
				//window.location.reload();
			}
		}
	});
}

/**
* 功能:发送一个ajax请求,获取借款交易统计
* @author 满口蛀牙
* @date 2016年4月18日
* @param url
* @param jsonData
* @return null
* @throws null
**/
function getCLSPacountSUM(url,jsonData){
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonData),
		success : function(result) {
			if (result === null || result == "null" || result === "" || !isNaN(result)) {
				//console.log("ajax请求出错");
			} else {
				//放置到全局变量123
				window.user=result;
				$.each(result, function(index, object) {
					//$("").val(object.message);
					//alert(object.createTime);
				});
				//window.location.reload();
			}
		}
	});
}

/*
恒丰协议授权提示
*/
function shipToGrant(className){
	showMask(260,className);//
	var dialogWidth=420;
	var dialogHeight=330;
	var positionWidth=parseInt(($(document.body).width()-dialogWidth)/2)+"px";
	var positionHeight=parseInt(($(document).scrollTop()+dialogHeight))+"px";
	var css={"width":dialogWidth,"height":dialogHeight,"background":"#fff","top":positionHeight,"left":positionWidth,"position":"absolute","z-index":"300"};
	$(".dialog-grant-wrap").css(css).show();
}

function closeGrantDialog(className){
	hiddenMask(className);
	var css={"width":"0px","height":"0px","background":"#fff"};
	$(".dialog-grant-wrap").css(css).hide();
}

/*
恒丰协议授权提示
*/
function shipToGrantConfirm(id){
	var agree=$(".i-agreement4").prop("checked");
	if(!agree){
        	return crfErrAlert('errorInfo',"错误","请勾选授权协议",0,"");
	}
	$.ajax({
			type:"POST",
			url:'/user/toGrantHF.do',
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			success:function(data){
				if(null!=data){
					if(data.status=="1"){
					//出资操作
					if(id){
						$('#'+id).val('N');
					}
						closeGrantDialog('grantMask');
						ajaxGetUserInfo(callBack2);
					}else if(data.status=="2"){
					//	window.location.href="/";
					}else{
						return crfErrAlert('errorInfo',"错误","系统异常",0,"");
					}
				}else{
					return crfErrAlert('errorInfo',"错误","系统异常",0,"");
				}

			}
		});

}

function checkGrant(){
	var toGrantAuthorization = ajaxUserInfo.crfUser.toGrantAuthorization;
	var threeaccount = ajaxUserInfo.crfUser.threeaccount;
	if(toGrantAuthorization!='1'&&threeaccount=='1'){
		shipToGrant('grantMask');
		return true;
	}
	return false;
}
