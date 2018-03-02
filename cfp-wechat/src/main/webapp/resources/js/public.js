// JavaScript Document

	//总在最底部
	function bottomB(){

		var Sys = {};

		var ua = navigator.userAgent.toLowerCase();
		
		var s;
		
		(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
		
		(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
		
		(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
		
		(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
		
		(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
		
		//以下进行测试
		
		var banben;//文档高度
		
		var liulanqi;//浏览器高度
		
		if (Sys.ie){
			
			banben = document.body.clientHeight;
			liulanqi =  document.documentElement.clientHeight ;
		}else if (Sys.firefox){
			
			banben =document.body.clientHeight ;
			liulanqi = document.documentElement.clientHeight ;
		}else if (Sys.chrome){
			
			banben = document.body.clientHeight;
			liulanqi =  document.documentElement.clientHeight ;
		}else if (Sys.opera){
			
			banben = document.documentElement.clientHeight ;
			liulanqi = document.body.clientHeight ;
		}else if (Sys.safari){
			
			banben = document.body.clientHeight;
			liulanqi =  document.documentElement.clientHeight ;
			
		}else{
			
			banben = document.body.clientHeight;
			liulanqi =  document.documentElement.clientHeight ;
			
		}
		
		if($(".footer").html()===undefined){
			$(".zhezhao1,.zhezhao,.zhezhao5,.zhezhao2,.zhezhao6").css("height",document.body.clientHeight);
			
		}else{

			if(banben>=liulanqi){
				
				$(".footer").css({"position":"inherit"});
				$(".zhezhao1,.zhezhao,.zhezhao5,.zhezhao2,.zhezhao6").css("height",document.body.clientHeight);
			}else{
				
				$(".footer").css({"position":"absolute","bottom":"0px","width":"100%"});
				$(".zhezhao1,.zhezhao,.zhezhao5,.zhezhao2,.zhezhao6").css("height",document.documentElement.clientHeight);
				
			}
		}
	}


	 

	//还原小数
	function rmoney(s) {  
		return parseFloat(s.replace(/[^\d\.-]/g, ""));  
	}  

/**
× JQUERY 模拟淘宝控件银行帐号输入
* @Author 312854458@qq.com 旭日升
**/

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
				if (i == 4){
					account = account + " "; 
				}
				if (i == 8){
					account = account + " "; 
				}
				if (i == 12){
					account = account + " ";
				}
				account = account + accountNumeric.substr (i,1)
			}
		}
	}else{
		account = " " + account.substring (1,5) + " " + account.substring (6,10) + " " + account.substring (14,18) + " " + account.substring(18,25);
	}
	if (account != BankNo.value) BankNo.value = account;
}
function checkBankNo (BankNo){
	if (BankNo.value == "") return;
	if (BankNo.value.match (".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}") == null){
		if (BankNo.value.match ("[0-9]{19}") != null)
			formatBankNo (BankNo)
	}
}
function checkEnterForFindListing(e){
	var characterCode;
	if(e && e.which){
		e = e;
		characterCode = e.which ;
	}else{
		e = event;
		characterCode = e.keyCode;
	}
	if(characterCode == 22){
		document.forms[getNetuiTagName("findListingForm")].submit();
		return false;
	}else{
		return true ;
	}
}


//格式化时间
function dateTimeFormatter(val) {

	if (val == undefined || val == "")
		return "";
	var date;
	if(val instanceof Date){
		date = val;
	}else{
		date = new Date(val);
	}
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();

	var h = date.getHours();
	var mm = date.getMinutes();
	var s = date.getSeconds();

	var dateStr = y + '-' + (m < 10 ? ('0' + m) : m) + '-'
		+ (d < 10 ? ('0' + d) : d);
	var TimeStr = h + ':' + (mm < 10 ? ('0' + mm) : mm) + ':'
		+ (s < 10 ? ('0' + s) : s);

	return dateStr + ' ' + TimeStr;
}
//让字符串中间显示*号
function plusXing (str,frontLen,endLen) { 
    var len = str.length-frontLen-endLen;
    var xing = '';
    for (var i=0;i<len;i++) {
        xing+='*';
    }
    return str.substr(0,frontLen)+xing+str.substr(str.length-endLen);
}
//百度统计代码
//var _hmt = _hmt || [];
//(function() {
//	var hm = document.createElement("script");
//	hm.src = "//hm.baidu.com/hm.js?a170dfa53a64a17e8bc1477c5d7f9ac6";
//	var s = document.getElementsByTagName("script")[0];
//	s.parentNode.insertBefore(hm, s);
//})();