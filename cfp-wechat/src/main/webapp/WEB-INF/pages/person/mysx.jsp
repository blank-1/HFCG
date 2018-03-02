<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
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
<link rel="stylesheet" href="${ctx }/css/s_listShengxin.css" type="text/css">
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
    if ((c + "").indexOf(".") != 0) {
        c = parseFloat(c).toFixed(digit);
    }

    return a + "" + b + "" + (c + "").substr((c + "").indexOf("."));
}

function numFormat(num) {//将数字转换成三位逗号分隔的样式
    if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(num)) {
        return num;
    }
    var nums = (num + "").split(".");
    var num1 = nums[0];
    var numstrs = num1.split("");
    var str = "";
    var inx = 1;
    for (var i = numstrs.length - 1; i >= 0; i--) {
        str += numstrs[i];
        if (inx % 3 == 0) {
            str += ",";
        }
        inx++;
    }
    if (str.substr(str.length - 1, str.length) == ",") {
        str = str.substr(0, str.length - 1);
    }
    var newstr = str.split("");
    str = "";
    for (var i = newstr.length - 1; i >= 0; i--) {
        str += newstr[i];
    }
    if (nums.lenth > 1) {
        str = str + "." + nums[0];
    }
    return str;
}
//格式化时间
function dateTimeFormatter(val) {

    if (val == undefined || val == "")
        return "";
    var date;
    if (val instanceof Date) {
        date = val;
    } else {
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
    var TimeStr = (h < 10 ? ("0" + h) : h) + ':' + (mm < 10 ? ('0' + mm) : mm) + ':'
        + (s < 10 ? ('0' + s) : s);
    return dateStr + ' ' + TimeStr;
}
//处理时间格式 yyyy-MM-dd HH:mm 日期为当天返回 HH:mm 当年返回MM-dd 否则返回 yyyy-MM-dd
function calcDate(d, now) {
    if (d == undefined || d == null || d == "") {
        return "";
    }
    if (now.substr(0, 10) == d.substr(0, 10)) {
        return d.substr(10);
    } else if (now.substr(0, 4) == d.substr(0, 4)) {
        return d.substr(5, 5);
    } else {
        return d.substr(0, 10);
    }
}
//弹出框
function clean() {
    alart.style.display = "none";
}

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
<style type="text/css">
.l_titleIcon_shengxin{text-indent:2rem;height:2rem; line-height:2rem; color:#333; position:relative;  font-size:1.2rem;}
.l_titleIcon_shengxin::before{content: "省";position: absolute;width: 1.2rem;height: 1.2rem;border: solid 1px #fc5f65;background: #fc5f65;color: #fff;font-weight: normal;text-indent: 0;line-height: 1.2rem;font-size: 1rem;text-align: center;border-radius: 2px;top: 50%;left: 0.2rem;margin-top: -.6rem;}

	.noNum{text-align:center; display: block;position:relative;top:0;margin-top:20%}
	.noNum img{width:13rem; margin-top:0;}
	.noNum p{color:#999; font-size:1.2rem; margin-top:2rem;}
</style>
<script data-main="${ctx }/js/s_listShengxin1.js?${version}" src="${ctx }/js/lib/require.js"></script>
<title>省心计划</title>
</head>
<body class="l_NewScroll">
	<div class="mainBox">
		<div class="shengXbox">
			<p class="shengXp2" style="font-size:1.4rem;">省心总资产（元）</p>
			<p class="shengXp3"><fmt:formatNumber value="${totalValue }" pattern="#,##0.00"/></p>
		</div>
		<div class="shengXmain">
			<div class="sXmainbox">
				<div class="lineHeight">
					<span class="sXmainboxLe color_333">预期收益（元）</span>
					<span class="sXmainboxCen color_333">已获收益（元）</span>
					<span class="sXmainboxRi color_333">省心中（元）</span>
				</div>
				<div class="lineHeight">
					<span class="sXmainboxLe color_red"><fmt:formatNumber value="${waitInterest }" pattern="#,##0.00"/></span>
					<span class="sXmainboxCen color_red"><fmt:formatNumber value="${currentProfit }" pattern="#,##0.00"/></span>
					<span class="sXmainboxRi color_666"><fmt:formatNumber value="${financingValue }" pattern="#,##0.00"/></span>
				</div>
			</div>
			<div class="sXmainbox">
				<div class="lineHeight">
					<span class="sXmainboxLe color_333">待出借（元）</span>
					<span class="sXmainboxCen color_333">省心中计划总数</span>
					<span class="sXmainboxRi color_333">累计参与</span>
				</div>
				<div class="lineHeight">
					<span class="sXmainboxLe color_666"><fmt:formatNumber value="${forLendBalance }" pattern="#,##0.00"/></span>
					<span class="sXmainboxCen color_666">${financingOrderSize }项</span>
					<span class="sXmainboxRi color_666">${allOrderSize }项</span>
				</div>
			</div>
		</div>
		<div class="clear_0"></div>
		<div class="s_content">
			<p style="height:2.5rem;line-height:2.5rem;width:100%;text-indent:2rem;font-size:1.2rem;color:#333;background:#fff;border-bottom:1px #e6e6e6 solid;">省心项目</p>
			<div class="mainList l_borrowList" style="margin-top:0;">
				<div class="page">
					<ul class="liushuiUl">
						<li class="liCur">全部</li>
						<li>理财中</li>
						<li>授权结束</li>
						<li>已结清</li>
					</ul>
					<div style="clear:both;"></div>
					<div id="lendOrder" ></div>
				    
			  
					<!-- <p class="l_lastTip">向下滑动加载更多</p> -->
				</div>
			</div>
		</div>
	</div>

	<form action="${ctx}/finance/getAllMyFinanceListDetailBy" id="queryForm" name="queryForm" method="post">
		<input type="hidden" name="lendOrderId" id="lendOrderId" value="">
	</form>
</body>
</html>
