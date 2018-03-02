<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
    pageContext.setAttribute("picPath", Constants.picPath);
%>
<!-- <meta http-equiv=”X-UA-Compatible” content=”IE=edge” > -->
<meta http-equiv="X-UA-Compatible" content="IE=7;IE=9;IE=8;IE=10;IE=11"></meta>
<meta name="renderer" content="webkit|ie-comp|ie-stand"></meta>
<!-- <meta http-equiv="content-type" content="text/html; charset=UTF-8"/> -->
<link rel="stylesheet" type="text/css" href="${ctx}/css/index.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/hDate.css" /><!-- index css -->
<link href="${ctx}/css/index_new/style.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/cxm_index.css" rel="stylesheet" type="text/css" />
<!-- jquery 类库 javascript -->
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<!-- public javascript -->
<script type="text/javascript" src="${ctx}/js/public.js"></script>
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
    $(document).ready(function() {
    	if($("#mcount").is(":visible")){
    		messageCount();
    	}
    	
     })
    var d_count = [2];
    var w_count = [0];
    function messageCount(){
    	$.ajax({
    		url:rootPath+"/message/userMessageCount",
    		type:"post",
    		data:{"d_count":d_count,"w_count":w_count},
    		success: function (data) {
                if(data==0)
                    $("#mcount").hide();
    			$("#mcount").html(""+data+"")
    		}
    	})	
    }
</script>