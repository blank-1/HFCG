// JavaScript Document(
//环形进度条函数

$(document).on("touchstart keydown",function(e){
    var keycode = event.which;
    if(keycode==9||keycode==13){
    	$("#lendForm").attr("action",
				rootPath + "/finance/toBuyRightsByPayAmount")
				.submit();
    }
})

$(function() {
	//成法
	  function accMul(arg1, arg2) {
		  var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
		      try {
		          m += s1.split(".")[1].length;
		   }
		    catch (e) {
		      }
		     try {
		         m += s2.split(".")[1].length;
		     }
		      catch (e) {
		      }
		      return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
		  }
		 
	//除法
	   function accDiv(arg1, arg2) {
		     var t1 = 0, t2 = 0, r1, r2;
		     try {
	          t1 = arg1.toString().split(".")[1].length;
		      }
		      catch (e) {
		    }
		     try {
		         t2 = arg2.toString().split(".")[1].length;
		      }
		      catch (e) {
		    }
		      with (Math) {
		        r1 = Number(arg1.toString().replace(".", ""));
		       r2 = Number(arg2.toString().replace(".", ""));
		        return (r1 / r2) * pow(10, t2 - t1);
		    }
	}
	$('.circle').each(
			function(index, el) {
				var x = $(this).find('span').text();
				var y = $("#limited").text();
				if (/^[\u4e00-\u9fa5]+$/.test(x)) {
					x = 0.00;
				}
				x.replace(/,/ig,'');
				y.replace(/,/ig,'')
				var num = parseFloat(accDiv(x.replace(/,/ig,''),y.replace(/,/ig,''))) * 360;
				if (num <= 180) {
					$(this).find('.right').css('transform',
							"rotate(" + num + "deg)");
				} else {
					$(this).find('.right').css('transform', "rotate(180deg)");
					$(this).find('.left').css('transform',
							"rotate(" + (num - 180) + "deg)");
				}
				;
				console.log(num)
			});
	$.each($(".xsize"), function() {
		var len = $(this).text().length;
		if (len < 8) {
			$(this).css("font-size", "2rem");
		} else if (len >= 8 && len < 10) {
			$(this).css("font-size", "1.6rem");
		} else if (len >= 10) {
			$(this).css("font-size", "1.2rem");
		}
	});
	searchHtml(1, 20);
	// 投标详情页面点击提交订单，去支付时
	$("#btn2").click(
			function() {
				console.log("====");
				var b1 = moneyf($("#money"));
				if (b1 == "") {
					massage = '';
					$("#lendForm").attr("action",
							rootPath + "/finance/toBuyBidLoanByPayAmount")
							.submit();
				}
			});

	$("#btn333").click(
			function() {
				console.log("====");
				var b1 = moneyf($("#money"));
				if (b1 == "") {
					massage = '';
					$("#lendForm").attr("action",
							rootPath + "/finance/toBuyRightsByPayAmount")
							.submit();
				}
			});

	$("#login").click(
			function() {
				$("#lendForm").attr(
						"action",
						rootPath + "/user/toLogin?flag="
								+ $("#loanApplicationId").val()).submit();
			});

	$("#login1").click(
			function() {
				$("#lendForm").attr(
						"action",
						rootPath + "/user/toLoginCreditor?flag="
								+ $("#creditorRightsApplyId").val()).submit();
			});
	$("#verified").click(
			function() {
				$("#alart").hide();
				location.href = rootPath + "/finance/toRealName?url="+location.href;
			});

	showDialog();
	autoPlayPic();

});
var pageNo = 1, pageSize = 20, totalPage;
// 出借列表查询
function searchHtml(page, rows) {
	var thtml = "";
	$.ajax({
		url : rootPath + "/finance/getCreditorRightsLender",
		type : "post",
		data : {
			"pageSize" : rows,
			"pageNo" : page,
			"creditorRightsId" : $("#creditorRightsApplyId").val()
		},
		success : function(data) {
			var d_rows = data.rows;
			totalPage = data.totalPage;
			pageNo = data.currentPage;
			for (var i = 0; i < d_rows.length; i++) {
				var data = d_rows[i];
				thtml += '<tr>';
				thtml += '<td class="td1">' + data.jmLendName + '</td>';
				thtml += '<td class="td2">' + data.lendAmount.toFixed(2)
						+ '元</td>';
				thtml += '<td class="td3">' + dateTimeFormatter(data.lendTime)
						+ '</td>';
				thtml += '</tr>';
			}
			;
			$("#totalNum").html("共" + d_rows.length + "人");
			$('#touzi_list').append(thtml);
		}
	});

}
function expectedProfit() {
    
	if (money.value != '起投金额(100元)') {
		var a = document.getElementById('money').value;
		var moneyLeg = a.length;
		var limied=rmoney($("#limited").text());//限投
		console.log(moneyLeg)
		if(moneyLeg >10){
			a1 = a.substring(0,10);
			$("#money").val(a1);
		}else {
			if (a >= 100) {
				
				$.ajax({
							url : rootPath + "/finance/getExpectRightProfit",
							type : "post",
							data : {
								"creditorRightsApplyId" : $(
										"#creditorRightsApplyId").val(),
								"amount" : a
							},
							async : false,
							success : function(data) {
								document.getElementById("sy").innerHTML = data;
							}
						});

			}else {
				document.getElementById("sy").innerHTML = "0.00"
			}
		}
		
	
	} else {
		document.getElementById("sy").innerHTML = "0.00"
	}
}

//详情页页面的收益计算
function sy(){
if (money.value != '起投金额(100元)' ){
	var a=document.getElementById('money').value;
//	var b1 = moneyf($("#money"));
//	if(b1 == ""){
		$.ajax({
			url : rootPath + "/finance/getExpectRightProfit",
			type : "post",
			data : {
				"creditorRightsApplyId" : $("#creditorRightsApplyId").val(),
				"amount" : a
			},
			async : false,
			success:function(data){
				document.getElementById("sy").innerHTML = data;
			}
		});
//	}else{
//		document.getElementById("sy").innerHTML = "0.00"
//	}
}else{
	document.getElementById("sy").innerHTML = "0.00"
}
};
//借款人基本信息展开效果
function down(){
	var down = document.getElementById('down');
	var arrow = document.getElementById('arrow');
if (down.style.display == 'block' ){
	down.style.display = 'none'
	arrow.src=rootPath+'/images/arrow.png'; 
}else{
	down.style.display = 'block'
	arrow.src=rootPath+'/images/arrow1.png';
}
};
//资金信息切换效果
	var lefttab = document.getElementById('lefttab');
	var righttab = document.getElementById('righttab');
	var info = document.getElementById('info');
	var list = document.getElementById('list');
function a(){
	 $('.lefttab').removeClass('borderh');
			 $('.righttab').addClass('borderh');
			 $('.info').css('display','block');
			 $('.list').css('display','none');

};
function b() {
	$('.lefttab').addClass('borderh');
	$('.righttab').removeClass('borderh');
	$('.list').css('display', 'block');
	$('.info').css('display', 'none');
};

// 借款金额输入验证
function moneyf(mond) {
	var money1 = rmoney($("#mony2").html());// 剩余金额
	var qitou = rmoney($("#qitou").val());// 起投金额
	var limied = rmoney($("#limited").html());// 限投
	var mondmoney = 0;
	if (mond.val() == ""
			|| !/^(([0-9]|([1-9][0-9]{0,9}))((\.[0-9]{0,9})?))$/.test(mond
					.val()) || (mond.val().length > 10)) {
		mondmoney = 0;
		massage = "请输入正确的理财金额";
		$("#alart").text(massage);
		alart.style.display = "block";
		setTimeout("clean()", 3000);
		return massage;
	} else {
		mondmoney = parseFloat(mond.val());
	}

/*	if (mondmoney < qitou) {

		massage = "请输入大于" + qitou + " 的金额！";
		$("#alart").text(massage);
		alart.style.display = "block";
		setTimeout("clean()", 3000);
	} else {*/
	/*	if (mondmoney < 100) {
			massage = "请输入大于100 并且是100 倍数的金额！";
			$("#alart").text(massage);
			alart.style.display = "block";
			setTimeout("clean()", 3000);
		} else */
	if (mondmoney % 100 != 0) { /* 并且是100 倍数 */
			if (mondmoney == money1) {
				massage = "";
			} else {
				massage = "请输入大于100 并且是100 倍数的金额或者为剩余金额！";
				$("#alart").text(massage);
				alart.style.display = "block";
				setTimeout("clean()", 3000);
				return massage;
			}
		} else {

			massage = "";
			// 检查剩余金额是否满足
			if (money1 < mond.val()) {
				massage = "购买金额超出剩余金额";
				$("#alart").text(massage);
				alart.style.display = "block";
				setTimeout("clean()", 3000);
				return massage;
			}
			var moneyipt = 0;
			moneyipt = Math.min(money1, limied) >= qitou ? Math.min(money1,
					limied) : 0;
			if ((parseFloat(mond.val()) - money1) > 0) {
				massage = "购买金额超出最大可投限额";
				$("#alart").text(massage);
				alart.style.display = "block";
				setTimeout("clean()", 3000);
				return massage;
			}
			if(mond.val()>limied){
				massage="购买金额超出最大可投限额";
				$("#alart").text(massage);
				alart.style.display="block";
				setTimeout("clean()",3000);
				return massage;
			}
			
			if(!parseInt(money1-mond.val())==0){
				if(parseInt(money1-mond.val())<100){
					massage="小于100的债权需要全部购买";
					$("#alart").text(massage);
					alart.style.display="block";
					setTimeout("clean()",3000);
				}
			}
		}
	//}
	return massage;
}

//弹出框
function showDialog(){
	$("#mortgage").on("click",function(){
		$("#maskBox").show();
		$("#dialog").show();
		$("#pledgeInfo").show();
	})
	$("#offBtn").on("click",function(){//关闭
		$("#maskBox").hide();
		$("#dialog").hide();
		$("#identificationInfo").hide();
		$("#pledgeInfo").hide();
		$("#projectProve").hide();
		$("#PjInfo").hide();
		$("#RiskControlScheme").hide();
		$("#GuaranteeScheme").hide();
		$("#FactoringCompany").hide();
		$("#CompanyProve").hide();
		$("#CompanyInformation").hide();
		$("#blPjInfo").hide();
		$("#MortgageInformation").hide();
	})
	$("#authentication").on("click",function(){
		$("#maskBox").show();
		$("#dialog").show();
		$("#identificationInfo").show();
	})
	$("#prove").on("click",function(){
		$("#maskBox").show();
		$("#dialog").show();
		$("#projectProve").show();	
	})
	$("#PjInfoBTN").on("click",function(){
		$("#maskBox").show();
		$("#dialog").show();
		$("#PjInfo").show();
	})
	$("#blPjInfoBTN").on("click",function(){
		$("#maskBox").show();
		$("#dialog").show();
		$("#blPjInfo").show();
	})
	$("#RiskControlSchemeBTN").on("click",function(){
		$("#maskBox").show();
		$("#dialog").show();
		$("#RiskControlScheme").show();
	})
	$("#CompanyInformationBTN").on("click",function(){
		$("#maskBox").show();
		$("#dialog").show();
		$("#CompanyInformation").show();
	})
	$("#GuaranteeSchemeBTN").on("click",function(){
		$("#maskBox").show();
		$("#dialog").show();
		$("#GuaranteeScheme").show();
	})
	$("#MortgageInformationBTN").on("click",function(){
		$("#maskBox").show();
		$("#dialog").show();
		$("#MortgageInformation").show();
	})
	$("#FactoringCompanyBTN").on("click",function(){
		$("#maskBox").show();
		$("#dialog").show();
		$("#FactoringCompany").show();
	})
	$("#CompanyProveBTN").on("click",function(){
		$("#maskBox").show();
		$("#dialog").show();
		$("#CompanyProve").show();
	})
}

// 焦点图
function autoPlayPic() {
	var index = 0, picList = $("#picList"), len = $("#picList li").size(), w = picList
			.find('li:first').outerWidth(), dotBtn = $("#dotBtn li");
	rightBtn = $("#rightBtn"), leftBtn = $("#leftBtn"),
			dotBtn = $(".dotBtn li");
	picList.width(w * (len + 1))
	titFont = $("#picList li").eq(1).attr("data-type");
	$("#titFont").html(titFont);
	rightBtn.on("click", function() {// 下一张
		index += 1;
		if (index >= len - 1) {
			index = len - 1;
		}
		picList.css({
			'marginLeft' : -w * index + "px",
			'-webkit-transition' : 'all .5s ease'
		}, 500)
		titFont = $("#picList li").eq(index).attr("data-type");
		$("#titFont").html(titFont);
		dotBtn.eq(index).addClass('cur').siblings().removeClass("cur");
	})
	leftBtn.on('click', function() {// 上一张
		index -= 1;
		if (index < 0) {
			index = 0
		}
		picList.css({
			'marginLeft' : index * -w + "px",
			'-webkit-transition' : 'all .5s ease'
		}, 500)
		titFont = $("#picList li").eq(index).attr("data-type");
		$("#titFont").html(titFont);
		dotBtn.eq(index).addClass('cur').siblings().removeClass("cur");
	})
}

//投标信息
function toubiaoMsg(){
	var panrentH=$("#w_tenderBox").offset().top,//510
		lastChild=$("#touzi_list :last").offset().top-100;//576
	//console.log(panrentH , lastChild)
	if(panrentH>=lastChild){
		pageNo+=1;		
	    if(pageNo<=totalPage){
	    	searchHtml(pageNo,20);
	    	return false;
	    }
	}
}

function callAndroid(){
    try{
        if (/Android/i.test(navigator.userAgent)) {
            window.jsObj.HtmlcallJava($('#money').offset().top);
        }
    }catch(e){
        console.log(e);
    }
}