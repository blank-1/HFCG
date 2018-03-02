<%@page import="com.xt.cfp.core.util.PropertiesUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="customUI" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.cfp-admin.com/jsp/taglib" prefix="mis" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ctx;
    pageContext.setAttribute("basePath", basePath);
    
    //pageContext.setAttribute("picPath", PropertiesUtils.getInstance().get("PIC_PATH"));
    pageContext.setAttribute("picPath", basePath);
%>
<link rel="stylesheet" href="${ctx}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${ctx}/css/bootstrap.css">

<link rel="stylesheet" href="${ctx}/js/themes/bootstrap/easyui.css">
<link rel="stylesheet" href="${ctx}/js/themes/icon.css">


<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctx}/js/languages/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.validationEngine.js"></script>

<script type="text/javascript" src="${ctx}/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/js/utils.js"></script>
<script type="text/javascript" src="${ctx}/js/datagrid-detailview.js"></script>

<!-- 引入自定义标签 -->


<script language="javascript">

    Array.prototype.indexOf = function (val) {
        for (var i = 0; i < this.length; i++) {
            if (this[i] == val) return i;
        }
        return -1;
    };
    Array.prototype.remove = function (val) {
        var index = this.indexOf(val);
        if (index > -1) {
            this.splice(index, 1);
        }
    };

    /**
     * 将数字转换成三位逗号分隔的样式
     */
    function formatNum(num, digit) //
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

	/**
	 * 供 datagrid 时间格式化。
	 */
	Date.prototype.format = function(format){
		var o = {
			"M+" : this.getMonth()+1, //month
			"d+" : this.getDate(),    //day
			"h+" : this.getHours(),   //hour
			"m+" : this.getMinutes(), //minute
			"s+" : this.getSeconds(), //second
			"q+" : Math.floor((this.getMonth()+3)/3),  //quarter
			"S" : this.getMilliseconds() //millisecond
		};
		if(/(y+)/.test(format)) {
			format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));	
		}
		for(var k in o){
			if(new RegExp("("+ k +")").test(format)){
				format = format.replace(RegExp.$1,RegExp.$1.length==1 ? o[k]:("00"+ o[k]).substr((""+ o[k]).length));
			}
		}
		return format;
	};
	
	/**
	 * 阿拉伯数字转中文大写。
	 */
	 function DX(n) {
	      if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(n))
	          return "数据非法";
	      var unit = "千百拾亿千百拾万千百拾元角分", str = "";
	          n += "00";
	      var p = n.indexOf('.');
	      if (p >= 0)
	          n = n.substring(0, p) + n.substr(p+1, 2);
	          unit = unit.substr(unit.length - n.length);
	      for (var i=0; i < n.length; i++)
	          str += '零壹贰叁肆伍陆柒捌玖'.charAt(n.charAt(i)) + unit.charAt(i);
	      return str.replace(/零(千|百|拾|角)/g, "零").replace(/(零)+/g, "零").replace(/零(万|亿|元)/g, "$1").replace(/(亿)万|壹(拾)/g, "$1$2").replace(/^元零?|零分/g, "").replace(/元$/g, "元整");
	  }
	
	 /**
	  * 时间格式化，参数为毫秒。(格式：yyyy-MM-dd HH:mm:ss)
	  */
	 function getDateTimeStr(date, num) {
	        var now = null;
	        if (typeof(date) == 'undefined' || date == null) {
	            now = new Date();
	        }
	        else {
	            now = date;
	        }
	        var yy = now.getFullYear();//getYear();
	        var mm = now.getMonth() + 1;
	        if (mm < 10) mm = "0" + mm;
	        var dd = now.getDate();
	        if (num != null && num != "") {
	            dd = dd + num;
	        }
	        if (dd < 10) dd = "0" + dd;
	        var hh = now.getHours();
	        if (hh < 10) hh = "0" + hh;
	        var mi = now.getMinutes();
	        if (mi < 10) mi = "0" + mi;
	        var ss = now.getSeconds();
	        if (ss < 10) ss = "0" + ss;
	        var currdate = '' + yy + "-" + mm + "-" + dd + " " + hh + ":" + mi + ":" + ss;
	        return currdate;
	    }
	 
	 	/**
	 	 * 时间格式化，参数为毫秒。(格式：yyyy-MM-dd)
	 	 */
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


    var vcity={ 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",
        21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",
        33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",
        42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",
        51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",
        63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"
    };

    checkCard = function(obj)
    {
        //var card = document.getElementById('card_no').value;
        //是否为空
        // if(card == '')
        // {

        //     return false;
        //}
        //校验长度，类型
        if(!isCardNo(obj))
        {

            return false;
        }
        //检查省份
        if(!checkProvince(obj))
        {

            return false;
        }
        //校验生日
        if(!checkBirthday(obj))
        {

            return false;
        }
        //检验位的检测
        if(!checkParity(obj))
        {

            return false;
        }
        return true;
    };


    //检查号码是否符合规范，包括长度，类型
    isCardNo = function(obj)
    {
        //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
        var reg = /(^\d{15}$)|(^\d{17}(\d|X)$)/;
        if(reg.test(obj) == false)
        {
            return false;
        }

        return true;
    };

    //取身份证前两位,校验省份
    checkProvince = function(obj)
    {
        var province = obj.substr(0,2);
        if(vcity[province] == undefined)
        {
            return false;
        }
        return true;
    };

    //检查生日是否正确
    checkBirthday = function(obj)
    {
        var len = obj.length;
        //身份证15位时，次序为省（3位）市（3位）年（2位）月（2位）日（2位）校验位（3位），皆为数字
        if(len == '15')
        {
            var re_fifteen = /^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/;
            var arr_data = obj.match(re_fifteen);
            var year = arr_data[2];
            var month = arr_data[3];
            var day = arr_data[4];
            var birthday = new Date('19'+year+'/'+month+'/'+day);
            return verifyBirthday('19'+year,month,day,birthday);
        }
        //身份证18位时，次序为省（3位）市（3位）年（4位）月（2位）日（2位）校验位（4位），校验位末尾可能为X
        if(len == '18')
        {
            var re_eighteen = /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/;
            var arr_data = obj.match(re_eighteen);
            var year = arr_data[2];
            var month = arr_data[3];
            var day = arr_data[4];
            var birthday = new Date(year+'/'+month+'/'+day);
            return verifyBirthday(year,month,day,birthday);
        }
        return false;
    };

    //校验日期
    verifyBirthday = function(year,month,day,birthday)
    {
        var now = new Date();
        var now_year = now.getFullYear();

        //年月日是否合理
        if(birthday.getFullYear() == year && (birthday.getMonth() + 1) == month && birthday.getDate() == day)
        {
            //判断年份的范围（3岁到100岁之间)
            var time = now_year - year;
            if(time >= 0 && time <= 130)
            {
                return true;
            }
            return false;
        }
        return false;
    };

    //校验位的检测
    checkParity = function(obj)
    {
        //15位转18位
        obj = changeFivteenToEighteen(obj);
        var len = obj.length;
        if(len == '18')
        {
            var arrInt = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
            var arrCh = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
            var cardTemp = 0, i, valnum;
            for(i = 0; i < 17; i ++)
            {
                cardTemp += obj.substr(i, 1) * arrInt[i];
            }
            valnum = arrCh[cardTemp % 11];
            if (valnum == obj.substr(17, 1))
            {
                return true;
            }
            return false;
        }
        return false;
    };

    //15位转18位身份证号
    changeFivteenToEighteen = function(obj)
    {
        if(obj.length == '15')
        {
            var arrInt = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
            var arrCh = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
            var cardTemp = 0, i;
            obj = obj.substr(0, 6) + '19' + obj.substr(6, obj.length - 6);
            for(i = 0; i < 17; i ++)
            {
                cardTemp += obj.substr(i, 1) * arrInt[i];
            }
            obj += arrCh[cardTemp % 11];
            return obj;
        }
        return obj;
    };



    $.extend($.fn.validatebox.defaults.rules, {
        mobile: {
            validator: function(value, param){

                if(/(^1[3|4|5|7|8][0-9]{9}$)/.test(value)){
                    return true;
                }else{
                    return false;
                }
            },
            message: '手机号格式不正确'
        },
        userName:{
            validator: function(value, param){

                if(value.length>=4 && value.length<=20 && !/[^a-zA-Z0-9\u4E00-\u9FA5\_-]/.test(value)){
                    if(/(^1[3|4|5|7|8][0-9]{9}$)/.test(value)){
                        this.message='手机号不可作为用户名使用';
                        return false;
                    }else{
                        return true;
                    }
                }else{
                    return false;
                }
            },
            message: '请输入4 - 20位字符，支持汉字、字母、数字及"-"、"_组合'
        },

        idCard:{
            validator: function(value, param){

               return checkCard(value);
            },
            message: '身份证格式不正确'
        },
        amount:{
            validator: function(value, param){
                if(/^(([0-9]|([1-9][0-9]{0,9}))((\.[0-9]{1,2})?))$/.test(value)){
                    return true;
                }
                return false;
            },
            message: '金额格式错误'
        },
        selectV:{
            validator: function(value, param){
                if(value=='请选择'||value==''){
                    return false;
                }
                return true;
            },
            message: '请选择'
        },
        removeSelectV:{
            validator: function(value, param){
                return true;
            },
            message: '请选择'
        },

    });


    $.extend($.fn.validatebox.methods, {
        remove: function(jq, newposition){
            return jq.each(function(){
                $(this).removeClass("validatebox-text validatebox-invalid").unbind('focus').unbind('blur');
            });
        },
        selectedRemove:function(jq, newposition){
            return jq.each(function(){
                $(this).next().children().eq(0).removeClass("validatebox-text validatebox-invalid").unbind('focus').unbind('blur');
            });
        },
        reduce: function(jq, newposition){
            return jq.each(function(){
                var opt = $(this).data().validatebox.options;
                $(this).addClass("validatebox-text").validatebox(opt);
            });
        }
    });
</script>

