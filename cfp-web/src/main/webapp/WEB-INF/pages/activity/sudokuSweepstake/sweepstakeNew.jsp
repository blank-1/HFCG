<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>幸运大转盘</title>
<%@include file="../../common/common_js.jsp"%>
<link rel="stylesheet" type="text/css" href="http://caifupad.com/zt/css/base.css" /><!-- index css -->
<link href="${ctx }/css/sudokuSweepstake/cj.css" rel="stylesheet" type="text/css">
</head>
<body>
	<%--登陆--%>
<!-- line2 start -->
<%@include file="../../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="0"/>
<!-- navindex end -->
<%@include file="../../login/login.jsp"%>
<input type="hidden" name="pastUrl" value="/activity/toWebTurntable">
<div class="cj1"></div>
<div <c:if test="${not empty sessionScope.currentUser }">class="cj2"</c:if> <c:if test="${empty sessionScope.currentUser }">class="cj22"</c:if>>
	<div class="cj-con">
		<c:if test="${empty sessionScope.currentUser }">
			<a href="javascript:;" data-id="login" data-mask="mask" class="loginTurn btn-general btn-3d btn-apply btn-apply-enabled">登录</a>
		</c:if>
		<div <c:if test="${empty sessionScope.currentUser }">style="display: none;"</c:if> >
	    	<div class="money">
	        	<b><fmt:formatNumber value="${allBuyBalance }" pattern="#,##0"/></b>
	            <span>元</span>
	        </div>
	        <div class="num">
	        	<i id="i-id">${shareNum }</i>
	            <em>次</em>
	        </div>
        </div>
        <div class="erweima">
        	<ul>
            	<li>
                	<img src="${ctx }/images/sudokuSweepstake/e1.png" alt="">
                    <p>财富派APP<br>苹果版下载</p>
                </li>
                <li>
                	<img src="${ctx }/images/sudokuSweepstake/e2.png" alt="">
                    <p>财富派APP<br>安卓版下载</p>
                </li>
                <li>
                	<img src="${ctx }/images/sudokuSweepstake/e3.png" alt="">
                    <p>财富派<br>微信公众号</p>
                </li>
            </ul>
        </div>
        <div class="cj-wrap">
        	<ol class="cj-ol" id="lottery">
            	<li class="lottery-unit lottery-unit-0"><img src="${ctx }/images/sudokuSweepstake/img1.png" alt=""><div class="div0"></div></li>
                <li class="lottery-unit lottery-unit-1"><img src="${ctx }/images/sudokuSweepstake/img2.png" alt=""><div class="div1"></div></li>
                <li class="lottery-unit lottery-unit-2"><img src="${ctx }/images/sudokuSweepstake/img3.png" alt=""><div class="div2"></div></li>
                <li class="lottery-unit lottery-unit-3"><img src="${ctx }/images/sudokuSweepstake/img4.png" alt=""><div class="div3"></div></li>
                <li class="lottery-unit lottery-unit-4"><img src="${ctx }/images/sudokuSweepstake/img5.png" alt=""><div class="div4"></div></li>
                <li class="lottery-unit lottery-unit-5"><img src="${ctx }/images/sudokuSweepstake/img6.png" alt=""><div class="div5"></div></li>
                <li class="lottery-unit lottery-unit-6"><img src="${ctx }/images/sudokuSweepstake/img7.png" alt=""><div class="div6"></div></li>
                <li class="lottery-unit lottery-unit-7"><img src="${ctx }/images/sudokuSweepstake/img8.png" alt=""><div class="div7"></div></li>
                <li class="lottery-unit lottery-unit-8"><img src="${ctx }/images/sudokuSweepstake/img9.png" alt=""><div class="div8"></div></li>
            </ol>
        </div>
        <c:if test="${not empty sessionScope.currentUser && allBuyBalance != 0 && shareNum != 0}">
        <div class="cj-hand" id="parent">
        	<div class="cj-hanr" id="div1"></div>
        </div>
        </c:if>
    </div>
</div>
<div class="cj-new">
	<div class="wrapper clearFloat" style="padding-top:12px;">
    	<div class="scr">
        	<ul id="ul1">
            	<li>恭喜用户136****7150获得了“100元财富券”</li>
                <li>恭喜用户130****6921获得了“10元财富券”</li>
                <li>恭喜用户186****7150获得了“提现券”</li>
                <li>恭喜用户185****2934获得了“5元财富券”</li>
                <li>恭喜用户153****6532获得了“小米手机4”</li>
                <li>恭喜用户132****1420获得了“100元财富券”</li>
            </ul>
        </div>
    </div>
</div>
<div class="cj3"></div>
<div class="cj4"></div>
<div class="cj5"></div>

<div id="moke"></div>
<div id="moke-layer">
	<del id="close"></del>
    <div class="moke-wrap">
    	<div class="moke-left">
        	<div style="height:50px;"></div>
        	<div class="moke-leimg" id="moke-leimg"></div>
        </div>
        <div class="moke-rig">
        	<h4><font id="em1">恭喜您， 开年大好运，<br></font><em id="em0">iPhone6S大奖抱回家！</em></h4>
			<p id="p-text">客服会在活动结束后三个工作日内与您联系，<br>
请保持手机畅通！</p>
			<b id="b-close">确认</b>
        </div>
    </div>
</div>
<script type="text/javascript">
var iNow=$('#i-id').html(); //可以抽奖次数 
$(function() {
        $.ajax({
   			url:rootPath+"/activity/toIsTurntableDraw",
   			type:"post",
   			data:{"userToken":$("#userToken").val()},
   			async : false,
			error : function(XHR) {
				//loginStatus = true; session无效
			},
   			success: function (data) {
   			var _data = eval('('+data+')');
   				if(_data.isSuccess){
   					$('#i-id').html(_data.num);
   					iNow=_data.num;
   				}
   			}
   			});
    });
$('#moke').css("height",$(document).height()+"px");
$('#close').click(function(event){
	$('#moke-layer').fadeOut();
	$('#moke').fadeOut();
	event.preventDefault();
});
$('#b-close').click(function(event){
	$('#moke-layer').fadeOut();
	$('#moke').fadeOut();	
	event.preventDefault();	
});
var t=setInterval(function(){
	$('#div1').animate({
	"top":"40px"	
	},'fast',function(){
		$('#div1').animate({
			"top":"0px"
		},'fast');
	});
},150)
var timer=null;
var i=0;

$('#div1').click(function ()
	{
		var isStatus = false;
  		var loginStatus = false;
  		$.ajax({
   			url:rootPath+"/activity/toIsTurntableDraw",
   			type:"post",
   			data:{"userToken":$("#userToken").val()},
   			async : false,
			error : function(XHR) {
				loginStatus = true;// session无效
			},
   			success: function (data) {
   			var _data = eval('('+data+')');
   				if(!_data.isSuccess){
   					if(_data.id == 'nolend'){
   						isStatus = true;
						alert(_data.info)
						return false;
					}else if(_data.id == 'nonum'){
						isStatus = true;
						alert(_data.info)
						return false;
					}else{
						isStatus = true;
						alert("网络错误！");
					    return false;
					}
   				}else{
   					$('#i-id').html(_data.num);
   				}
   			}
   			});
   		if(isStatus){
   			return false;
   		}
   		if(loginStatus){
   			$(".zhezhao1").show();
			$("#login").slideDown(500);
			return false;
   		}
		clearInterval(t);
		$('#div1').stop(true);
		$('#div1').css('background','url(${ctx}/images/sudokuSweepstake/hand2.png) no-repeat');
		if($('#i-id').html()==-1){
			alert("您还未登录");
			return false;
		}else if($(".money").html() == 0){
			alert("您还未投资");
			return false;
		}else if($('#i-id').html()==0){
			alert('您抽奖机会已用完');
			return false;
		}else{
			if($('#i-id').html()>0){
			$('#parent').hide();
			roll();
			iNow--;	
			$('#i-id').html(iNow);
			}
		}
	}
);
function roll(){
	clearInterval(timer);
	timer=setInterval(function(){
		i++;
		var index=Math.floor(Math.random()*9);
		if(i>=30){
		var num=8;
		$.ajax({
    			url:rootPath+"/activity/toTurntableDraw",
    			type:"post",
    			data:{"userToken":$("#userToken").val()},
    			async : false,
				error : function(XHR) {
					//loginStatus = false; session无效
				},
    			success: function (data) {
    			var _data = eval('('+data+')');
    					if(_data.isSuccess){
    						if(data.id != -2){
    							num=_data.id;
    						}
    					}else if(_data.id == 'nolend'){
    						alert(_data.info)
    						return false;
    					}else if(_data.id == 'nonum'){
    						alert(_data.info)
    						return false;
    					}else{
    						alert("网络错误！");
    					    return false;
    					}
    				}	
    			});
			$('#parent').show();
			clearInterval(timer);
			i=0;
			$('#div1').css('background','url(${ctx}/images/sudokuSweepstake/hand3.png) no-repeat');
			$("#lottery").find("li").removeClass("liactive");
			$("#lottery").find("div").show();
			if(num == 0){
				$("#lottery").find(".div"+4).hide();		
				$("#lottery").find(".lottery-unit-"+4).addClass("liactive");
			}else if(num == 1){
				$("#lottery").find(".div"+0).hide();		
				$("#lottery").find(".lottery-unit-"+0).addClass("liactive");
			}else if(num == 2){
				$("#lottery").find(".div"+8).hide();		
				$("#lottery").find(".lottery-unit-"+8).addClass("liactive");
			}else if(num == 3){
				$("#lottery").find(".div"+6).hide();		
				$("#lottery").find(".lottery-unit-"+6).addClass("liactive");
			}else if(num == 4){
				$("#lottery").find(".div"+2).hide();		
				$("#lottery").find(".lottery-unit-"+2).addClass("liactive");
			}else if(num == 5){
				$("#lottery").find(".div"+3).hide();		
				$("#lottery").find(".lottery-unit-"+3).addClass("liactive");
			}else if(num == 6){
				$("#lottery").find(".div"+7).hide();		
				$("#lottery").find(".lottery-unit-"+7).addClass("liactive");
			}else if(num == 7){
				$("#lottery").find(".div"+1).hide();		
				$("#lottery").find(".lottery-unit-"+1).addClass("liactive");
			}else if(num == 8){
				$("#lottery").find(".div"+5).hide();		
				$("#lottery").find(".lottery-unit-"+5).addClass("liactive");
			}	
			setTimeout(function(){
				$('#moke').fadeIn();
				$('#moke-layer').fadeIn();
				if(num==1){
				$('#p-text').prev().show();
				$("#em1").html("恭喜您， 开年大好运，<br>");
				$('#em0').html('iPhone6S大奖抱回家！');
				$('#moke-leimg').html('<img src="'+rootPath+'/images/sudokuSweepstake/img1.png" alt="">');
				$('#p-text').css({"color":"white","font-size":"12px","line-height":"18px","margin":"10px 0"}).html("客服会在活动结束后三个工作日内与您联系，<br>请保持手机畅通！");
				}
				else if(num==7){
				$('#p-text').prev().show();
				$("#em1").html("恭喜您， 获得");
				$('#em0').html('提现券，运气好到爆！');
				$('#moke-leimg').html('<img src="'+rootPath+'/images/sudokuSweepstake/img2.png" alt="">');
				$('#p-text').css({"color":"white","font-size":"12px","line-height":"18px","margin":"10px 0"}).html('请到【账户中心】<a href="'+rootPath+'/person/account/overview" style="font-size:12px; color:white;">查看</a>');
				}
				else if(num==4){
				$('#p-text').prev().show();
				$("#em1").html("恭喜您， 获得");
				$('#em0').html('100元财富券，运气好到爆！');
				$('#moke-leimg').html('<img src="'+rootPath+'/images/sudokuSweepstake/img3.png" alt="">');
				$('#p-text').css({"color":"white","font-size":"12px","line-height":"18px","margin":"10px 0"}).html('请到【账户中心】<a href="'+rootPath+'/person/account/overview" style="font-size:12px; color:white;">查看</a>');
				}
				else if(num==5){
				$('#p-text').prev().show();
				$("#em1").html("恭喜您， 获得");
				$('#em0').html('10元财富券，运气好到爆！');
				$('#moke-leimg').html('<img src="'+rootPath+'/images/sudokuSweepstake/img4.png" alt="">');
				$('#p-text').css({"color":"white","font-size":"12px","line-height":"18px","margin":"10px 0"}).html('请到【账户中心】<a href="'+rootPath+'/person/account/overview" style="font-size:12px; color:white;">查看</a>');
				}
				else if(num==0){
				$('#p-text').prev().show();
				$("#em1").html("恭喜您， 开年大好运，<br>");
				$('#em0').html('iMac 大奖抱回家');
				$('#moke-leimg').html('<img src="'+rootPath+'/images/sudokuSweepstake/img5.png" alt="">');
				$('#p-text').css({"color":"white","font-size":"12px","line-height":"18px","margin":"10px 0"}).html("客服会在活动结束后三个工作日内与您联系，<br>请保持手机畅通！");
				}
				else if(num==8){
				$('#em0').html('感谢参与');
				$('#moke-leimg').html('<img src="'+rootPath+'/images/sudokuSweepstake/img6.png" alt="">');
				$('#p-text').prev().hide();
				$('#p-text').css({"fontSize":"20px","marginTop":"30px","marginBottom":"12px"}).html('神马都没中，换个姿势再来吧！');
				}
				else if(num==3){
				$('#p-text').prev().show();
				$("#em1").html("恭喜您， 开年大好运，<br>");
				$('#em0').html('小米手机大奖抱回家');
				$('#moke-leimg').html('<img src="'+rootPath+'/images/sudokuSweepstake/img7.png" alt="">');
				$('#p-text').css({"color":"white","font-size":"12px","line-height":"18px","margin":"10px 0"}).html("客服会在活动结束后三个工作日内与您联系，<br>请保持手机畅通！");
				}
				else if(num==6){
				$('#p-text').prev().show();
				$("#em1").html("恭喜您， 获得");
				$('#em0').html('5元财富券，运气好到爆！');
				$('#moke-leimg').html('<img src="'+rootPath+'/images/sudokuSweepstake/img8.png" alt="">');
				$('#p-text').css({"color":"white","font-size":"12px","line-height":"18px","margin":"10px 0"}).html('请到【账户中心】<a href="'+rootPath+'/person/account/overview" style="font-size:12px; color:white;">查看</a>');
				}
				else if(num==2){
				$('#p-text').prev().show();
				$("#em1").html("恭喜您， 开年大好运，<br>");
				$('#em0').html('ipad Air 2 大奖抱回家');
				$('#moke-leimg').html('<img src="'+rootPath+'/images/sudokuSweepstake/img9.png" alt="">');
				$('#p-text').css({"color":"white","font-size":"12px","line-height":"18px","margin":"10px 0"}).html("客服会在活动结束后三个工作日内与您联系，<br>请保持手机畅通！");
				}			
			},100);			
		}else{		
			$("#lottery").find("li").removeClass("liactive");
			$("#lottery").find("div").show();
			$("#lottery").find(".div"+index).hide();
			$("#lottery").find(".lottery-unit-"+index).addClass("liactive");
		}		
	},200);
}

var oUl=document.getElementById('ul1');
var aLi=oUl.getElementsByTagName('li');
var Timer=null;
var sudu=-2;
oUl.innerHTML+=oUl.innerHTML;	
oUl.style.width=aLi[0].offsetWidth*aLi.length+'px';

Timer=setInterval(function(){
	
	oUl.style.left=oUl.offsetLeft+sudu+'px';						
	if(oUl.offsetLeft<-oUl.offsetWidth/2)
	{
		
		oUl.style.left='0px';	
	}
	else if(oUl.offsetLeft>0){
		
		oUl.style.left = -oUl.offsetWidth/2 + 'px';
	}	
	
},30);

</script>
</body>
</html>
