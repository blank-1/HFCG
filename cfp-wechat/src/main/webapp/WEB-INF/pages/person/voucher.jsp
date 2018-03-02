<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="0">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<link rel="stylesheet" href="${ctx }/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/s_coupon.css" type="text/css">
<script type="text/javascript">
    var rootPath = '<%=ctx%>';
    function getDateStr(date, num, ymd) {
        var now = null;
        if (typeof(date) == 'undefined' || date == null) {
            now = new Date();
        }
        else {
            now = date;
        }
        var yy = now.getFullYear();//getYear();
        if (ymd == "yy" && num != null && num != "") {
            yy = yy + num;
        }
        if (ymd == "mm" && num != null && num != "") {
            now.setMonth(now.getMonth() + num);
        }
        if ((ymd == null || ymd == "dd") && num != null && num != "") {
            now.setDate(now.getDate() + num);
        }
        var dd = now.getDate();
        if (dd < 10) dd = "0" + dd;
        var mm = now.getMonth() + 1;
        if (mm < 10) mm = "0" + mm;
        var currdate = now.getFullYear() + "-" + mm + "-" + dd;
        return currdate;
    }

    function formatNum(num, digit) //将数字转换成三位逗号分隔的样式
    {
        if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(num)) {
            //alert("wrong!");
            return num;
        }
        var a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
        var re = new RegExp().compile("(\\d)(\\d{3})(,|$)");

        while (re.test(b)) b = b.replace(re, "$1,$2$3");
        if (c && digit && new RegExp("^.(\\d{" + digit + "})(\\d)").test(c)) {
            if (RegExp.$2 > 4) c = (parseFloat(RegExp.$1) + 1) / Math.pow(10, digit);
            else c = "." + RegExp.$1;
        }

        if (!c) {
            c = 0.00;
            c = parseFloat(c).toFixed(digit);
        }
        if ((c+"").indexOf(".") != 0) {
            c = parseFloat(c).toFixed(digit);
        }

        return a + "" + b + "" + (c + "").substr((c + "").indexOf("."));
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
	var TimeStr = (h<10?("0"+h):h) + ':' + (mm < 10 ? ('0' + mm) : mm) + ':'
		+ (s < 10 ? ('0' + s) : s);
	return dateStr + ' ' + TimeStr;
}
//处理时间格式 yyyy-MM-dd HH:mm 日期为当天返回 HH:mm 当年返回MM-dd 否则返回 yyyy-MM-dd
function calcDate(d,now){
	if(d==undefined||d==null||d==""){
		return "";
	}
	if(now.substr(0,10)==d.substr(0,10)){
		return d.substr(10);
	}else if(now.substr(0,4)==d.substr(0,4)){
		return d.substr(5,5);
	}else{
		return d.substr(0,10);
	}
}
//弹出框
function clean(){
	 alart.style.display="none";
	};
	//还原小数
	function rmoney(s) {  
		return parseFloat(s.replace(/[^\d\.-]/g, ""));  
	}
	//格式化金额
	function fmoney(s, n) {  
		n = n > 0 && n <= 20 ? n : 2;  
		s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";  
		var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];  
		t = "";  
		for (i = 0; i < l.length; i++) {  
			t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");  
		}  
		return t.split("").reverse().join("") + "." + r;  
	}
</script>
<title>优惠券</title>

</head>
<script data-main="${ctx }/js/s_coupon.js?${version}" src="${ctx }/js/lib/require.js"></script>
<script type="text/javascript">
//rem自适应字体大小方法
var docEl = document.documentElement,
resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
recalc = function() {
    //设置根字体大小
    docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
};
//绑定浏览器缩放与加载时间
window.addEventListener(resizeEvt, recalc, false);
document.addEventListener('DOMContentLoaded', recalc, false);
</script>
<title>财富券</title>
</head>
<body class="l_NewScroll">
	<div class="s_content">
		<ul class="headNav">
			<li class="cur"><a href="javascript:;">全&nbsp;&nbsp;&nbsp;部</a></li>
			<li><a href="javascript:;">可&nbsp;&nbsp;&nbsp;用</a></li>
			<li><a href="javascript:;">不可用</a></li>
		</ul>
		<div class="clear_10"></div>
		<div class="couponList">
			<div class="page" id="pageContent"></div>
			<div class="page" id="pageContent0"style="display:none;">
				<div class="couponBox cfqBj">
					<div class="couponCon">
						<!-- <p class="couponText1">
							<i class="coupon_i color_red">￥</i> <span class="color_red">100</span>
							<i class="coupon_pre color_red">元</i> <font class="color_red">财富券</font>
						</p>
						<p class="couponText2 color_666">条件：起投金额100元，限3-6个月标的</p>
						<p class="couponText3 color_333">有效期前至：2016-11-18</p> -->
					</div>
				</div>
		<!-- 	<p class="l_lastTip">向下滑动加载更多</p> -->
			</div>
		
		</div>
			<div class="page" id="pageContent1"></div>
	</div>
	</div>


</body>
</html>