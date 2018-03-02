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
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
	<meta name="format-detection" content="telephone=no"/>
	<meta name="msapplication-tap-highlight" content="no" />
	<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
	<link rel="stylesheet" href="${ctx }/css/reset2.css" type="text/css">
	<link rel="stylesheet" href="${ctx }/css/CapitalFlow.css">
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
function getIndex(inx){
	$("#head_index").val(inx);
}
</script>
<title>资产记录</title>
</head>
<body style="margin:0;">
<input type="hidden" id="accId" value="${cashAccount.accId }"/>
<input type="hidden" value="${w_value }" id="w_value">
	<input type="hidden" value="0" id="head_index">
	<div class="w_container l_NewScroll" style="position:absolute; -webkit-overflow-scrolling: touch; top:0; left:0; bottom:0; right:0">
		<header class="w_header">
			<ul class="w_vouList" id="w_vouList">
			  <li onclick="getIndex(0)"><a <c:if test="${w_value eq '0'}">class="w_active"</c:if> href="javascript:void(0);">全部</a></li>
			  <li onclick="getIndex(1)"><a <c:if test="${w_value eq '1'}">class="w_active"</c:if> href="javascript:void(0);">收入</a></li>
			  <li onclick="getIndex(2)"><a <c:if test="${w_value eq '4'}">class="w_active"</c:if> href="javascript:void(0);">冻结</a></li>
			  <li onclick="getIndex(3)"><a href="javascript:void(0);">支出</a></li>
			  <li onclick="getIndex(4)"><a href="javascript:void(0);">解冻</a></li>
			</ul>
	    </header>

	    <div class="orderList">
        	<div class="w_page page0">
            	<ul class="regular pay_page0">

            	</ul>
            	<p class='l_lastTips'>加载更多</p>
				<div class="noNum">
					<img src="${ctx }/images/userCenter/no.jpg">
					<p>没有数据</p>
				</div>
            </div>
            <div class="w_page page1"><!--收入的数据 -->
	            <ul class="regular pay_page1">

	            </ul>
	            <p class='l_lastTips'>加载更多</p>
				<div class="noNum">
					<img src="${ctx }/images/userCenter/no.jpg">
					<p>没有数据</p>
				</div>
	        </div>
	        <div class="w_page page2"><!--冻结的数据 -->
	            <ul class="regular pay_page2">

	            </ul>
	            <p class='l_lastTips'>加载更多</p>
				<div class="noNum">
					<img src="${ctx }/images/userCenter/no.jpg">
					<p>没有数据</p>
				</div>
	        </div>
	        <div class="w_page page3"><!--支出的数据 -->
	            <ul class="regular pay_page3">

	            </ul>
	            <p class='l_lastTips'>加载更多</p>
				<div class="noNum">
					<img src="${ctx }/images/userCenter/no.jpg">
					<p>没有数据</p>
				</div>
	        </div>
	        <div class="w_page page4"><!--解冻的数据 -->
	            <ul class="regular pay_page4">

	            </ul>
	            <p class='l_lastTips'>加载更多</p>
				<div class="noNum">
					<img src="${ctx }/images/userCenter/no.jpg">
					<p>没有数据</p>
				</div>
	        </div>
        </div>
	</div>
	<form action="${ctx}/person/fundManageInfoDetail" id="queryForm" name="queryForm" method="post">
		<input type="hidden" name="id" id="id" value="">
	</form>
	<script src="${ctx }/js/assetRecord.js"></script>
	<script type="text/javascript">
	var inx=$("#head_index").val();
	inx=parseInt(inx);
	$("#w_vouList").find("li").find("a").removeClass("w_active");
	console.log(inx);
	$("#w_vouList").find("li").eq(inx).find("a").addClass("w_active");
	</script>
</body>
</html>
