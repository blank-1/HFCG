// JavaScript Document

$(document).ready(function() {

 $(".select li").each(function(){

	if($(this).attr("data-select")=="select"){

		$(this).parents("dd").siblings("dt").html($(this).find("a").html()).attr("data-id",$(this).attr("data-id"));

	}

 });
	//银行卡输入框失去焦点时
	$("#address").blur(function(){
		addressf($(this),1);
	});
	$("#lian_city").blur(function(){
		cityf($(this),1);
	});
	$("#text").blur(function(){
		kaihuh($(this),1);
	});

	//点击提交订单，去支付时
	$("#recharge").click(function(){
		var b1=addressf($("#address"));
		var b2=cityf($("#lian_city"));
		var b3=kaihuh($("#text"));
		if( b1=="" && b2=="" && b3==""){
			if($("#cityId").val()==''){
				$("#cityId").val($("#lian_city").attr("data-id"));
			}

			$("#editBank").submit();

		}

	});
	//判断省市
	function cityf(lian_city){
		if($("#lian_city").text()==="--城市--"){
			massage="请选择省市";
			$("#lian_city").parent().parent().siblings("em").html(massage).removeClass("hui");
		}else{
			massage="";
			$("#lian_city").parent().parent().siblings("em").html("").removeClass("ipt-error");
		}
		return massage;
	}
	function addressf(address){
		if($("#address").text()==="--省份--"){
			massage="请选择省市";
			$("#address").parent().parent().siblings("em").html(massage).removeClass("hui");
		}else{
			massage="";
			$("#address").parent().parent().siblings("em").html("").removeClass("ipt-error");
		}
		return massage;
	}


	//开户行
	function kaihuh(text){
		if($("#text").val()==""){
			if($("#text").val()==""){
				massage="请输入开户行地址";
				$("#text").parent().siblings("em").html(massage).removeClass("hui");
			}else{
				massage="";
				$("#text").parent().siblings("em").html("").removeClass("ipt-error");
			}
		}else{
			if (/^([\u4e00-\u9fa5]+|[a-zA-Z0-9]+)$/.test($("#text").val())) {
				massage="";
				$("#text").parent().siblings("em").html(massage).html("").removeClass("ipt-error");
			} else {
				massage="不能输入特殊符号";
				$("#text").parent().siblings("em").html(massage).removeClass("hui");
			}
		}
		return massage;
	}



	$(".yh_search").click(function(){
		$(".yh_name").hide();
		var b1=addressf($("#address"));
		var b2=cityf($("#lian_city"));
		var b3=kaihuh($("#text"));
		if( b1=="" && b2=="" && b3==""){
			//获得大额行号
			var cityId = $("#lian_city").attr("data-id")
			$.ajax({
				url:rootPath+"/bankcard/getBankList",
				type:"post",
				data: {
					"cityId":cityId,
					"bankName":$("#text").val()
				},
				success: function (data) {
					//lian_city
					console.log(data.length)
					var str = "";
					for(var i=0;i<data.length;i++){
						 var _data =  data[i];
					 	str += ' <p class="yh_mian_text" onclick="select_Bank(\''+_data.prcptcd+'\',\''+_data.brabank_name+'\')">'+_data.brabank_name+'</p>';
					}
					if(data.length!=0){

						 $(".yh_name").html(str);
						//$(".yh_name").slideDown(500);
						$(".yh_name").animate({height: 'toggle', opacity: 'toggle'}, "slow");
					}
				}
			});
		}

	});



});
	function select_Bank(v,n){
		$("#text").val(n);
		//$(".yh_name").slideUp(500);
		$(".yh_name").animate({height: 'toggle', opacity: 'toggle'}, "slow");
		$("#prcptcd").val(v);
	}

$(function(){
	$("input:text").focus(function(){
		$("#text").siblings().find(".yh_search").css("display","block");
	});
	$("input:text").blur(function(){
		$(this).css("background","white");
	});

});




function getCity(provinceId){
	//省市联动
	$.ajax({
		url:rootPath+"/bankcard/getCity",
		type:"post",
		data: {
			"provinceId":provinceId
		},
		success: function (data) {
			//lian_city
			var str = "";
			for(var i=0;i<data.length;i++){
				var _data =  data[i];
				str += '<li data-id="'+_data.provinceCityId+'"><a href="javascript:;" onclick="selectCity(this)">'+_data.cityName+'</a></li>';
			}
			$("#cityList").html(str);
		}
	});
}

function selectCity(v){

	var dt=$("#lian_city");
	var s=dt.parent();
	var z=parseInt(s.css("z-index"));
	var dd=$("#cityList").parent();
	var _hide=function(){dd.slideUp(200);dt.removeClass("cur");s.css("z-index",z);};    //关闭效果
	dt.attr("data-id",$(v).parent().attr("data-id"));
	dt.html($(v).html());
	_hide() //选择效果（如需要传值，可自定义参数，在此处返回对应的"value"值 ）


	$("#cityId").val($("#lian_city").attr("data-id"));
}
function toPersonPage(){
	window.location.href = rootPath+'/person/account/overview';
}
//出借利率验证
function borratef(rate,msg){
	var massage="";
	massage=" 请选择出借利率";
	if(msg!=null){
		massage=" 请选择借款利率";
	}
	if(rate.children("dt").attr("data-id")=="0"){
		rate.siblings("em").html(massage);
	}else{
		massage="&nbsp;";
		rate.siblings("em").html(massage);
	}
	return massage;
}