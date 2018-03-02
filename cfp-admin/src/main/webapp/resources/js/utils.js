/**
 * User: yulei
 * Date: 13-1-11
 * Time: 下午2:31
 */

var Utils = new Object();

/**
 * 按照某对象的的宽度乘以某比例，计算出一个新的宽度
 * @param obj 对象（jquery对象）
 * @param p 比例
 * @return {Number}
 */
Utils.getWidth = function(obj, p){
    var parentWidth = obj.width();
    return Math.round(parentWidth * p);
};

/**
 * 显示提示信息
 * @param msg
 */
Utils.showMsg = function(msg) {
    $.messager.show({
        title:'注意',
        msg:msg,
        timeout:5000,
        showType:'slide'
    });
};
/**
 * 去除字符串中空格
 * @param str
 * @param is_global 为g时则去除全局空格
 * @returns {XML|string|void|*}
 * @constructor
 */
function Trim(str,is_global)
{
    var result;
    result = str.replace(/(^\s+)|(\s+$)/g,"");
    if(is_global.toLowerCase()=="g")
        result = result.replace(/\s/g,"");
    return result;
}

/**
 * 显示确定对话框，确定后执行fn
 * @param msg
 * @param fn
 */
Utils.confirm = function(msg, fn) {
    $.messager.confirm('注意', msg,
        function(r) {
            if(r)
                fn();
        }
    );
};

Utils.loading = function(){
	$("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",zIndex:9999999999,height:$(window).height()}).appendTo("body");
	$("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",zIndex:9999999999,left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
}

Utils.loaded = function(){
	$('body').find("div.datagrid-mask-msg").remove(); 
	$('body').find("div.datagrid-mask").remove(); 	
}

/**
 * 日历控件填值（今天、近7天、近3个月...）
 * @param val（0：今天   1：近7天   2：近3个月   3：近6个月  4：近12个月）
 * @param startDate 日历开始日期ID
 * @param endDate 日历结束日期ID
 * @param Func 回调函数 
 */
Utils.selectDate = function(val,startDate,endDate,Func){
    var curr_time = new Date();
    var strDateStart="";
    var strDateEnd = getDateStr(curr_time);
    switch (val){
        case 0:
			break;
        case 1:
        	curr_time.setDate(curr_time.getDate()-6);
        	break;
        case 2:
        	curr_time.setMonth(curr_time.getMonth() - 3);
        	break;
        case 3:
        	curr_time.setMonth(curr_time.getMonth() - 6);
        	break;
        case 4:
        	curr_time.setMonth(curr_time.getMonth() - 12);
        	break;
        default:
    }
    strDateStart = getDateStr(curr_time);
    $('#'+startDate).datebox('setValue', strDateStart);
    $('#'+endDate).datebox('setValue', strDateEnd);
    Func();
}
