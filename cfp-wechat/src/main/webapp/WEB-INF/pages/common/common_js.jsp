<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%@ page import="com.external.llpay.LLPayUtil" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
    pageContext.setAttribute("picPath", "http://m.caifupad.com");
    pageContext.setAttribute("llWapPayUrl", LLPayUtil.getPayUrlWap());
%>
<!-- jquery 类库 javascript -->
<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
<script src="${ctx }/js/wechat_cfp.js"></script>
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

    function formatBankNo(BankNo) {
        if (BankNo.value == "") return;
        var account = new String(BankNo.value);
        account = account.substring(0, 22);
        /*帐号的总数, 包括空格在内 */
        if (account.match(".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}") == null) {
            /* 对照格式 */
            if (account.match(".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" + ".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" +
                    ".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" + ".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}") == null) {
                var accountNumeric = accountChar = "", i;
                for (i = 0; i < account.length; i++) {
                    accountChar = account.substr(i, 1);
                    if (!isNaN(accountChar) && (accountChar != " ")) accountNumeric = accountNumeric + accountChar;
                }
                account = "";
                for (i = 0; i < accountNumeric.length; i++) {    /* 可将以下空格改为-,效果也不错 */
                    if (i == 4) {
                        account = account + " ";
                    }
                    if (i == 8) {
                        account = account + " ";
                    }
                    if (i == 12) {
                        account = account + " ";
                    }
                    account = account + accountNumeric.substr(i, 1)
                }
            }
        } else {
            account = " " + account.substring(1, 5) + " " + account.substring(6, 10) + " " + account.substring(14, 18) + " " + account.substring(18, 25);
        }
        if (account != BankNo.value) BankNo.value = account;
    }
</script>