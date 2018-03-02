<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="../../common/taglibs.jsp"%>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
    pageContext.setAttribute("picPath", Constants.picPath);
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">  
<title></title>
<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE"/>
<title>加息券来了！！！</title>
<link href="${ctx}/css/increaseate/css/base.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/increaseate/css/style.css" rel="stylesheet" type="text/css">
</head>
 <script type="text/javascript">
var rootPath = '<%=ctx%>';
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
function formatterStr(obj){
	var str=dateTimeFormatter(obj);
	str=str.substring(6, str.length-3);
	return str;
}


</script>

<body class="bg">
<!-- linktop start -->
<div class="linktop"> <!--container2-->
	<input type="hidden" value="${result}" id="play_result">
	<input type="hidden" value="${isLogin}" id="isLogin">
	<div class="container  clearFloat">
		<ul class="uWelcome clearFloat">
			<li class="linknone"><a href="http://help.caifupad.com/" class="a_333">帮助中心</a></li>
			<li class="linknone"><a href="${ctx}/zt/yindao/yindao.html" class="a_333">新手指导</a></li>
		</ul>
		<ul class="uSevers">
			<li>客服热线： 400-061-8080</li>
			<li class="linknone">工作日：09:00－18:00</li>
		</ul>
	</div>
</div>
<!-- linktop end -->
<!-- header start -->
<div class="header">
	<div class="container  clearFloat">
		<div class="dlogo">
			<a href="${ctx}/"><img src="${ctx}/images/increaseate/images/logo_03.png" /></a>
		</div>
	</div>
</div>
<!-- header end -->

<div class="cj_box1"></div>
<div class="cj_box2">
	<div class="luckyList">
		<p>
			<marquee direction="left">
				<c:forEach items="${list}" var="vp">
							<a>恭喜<span class="w_party">${vp.mobileNo}</span>&nbsp;&nbsp;抽中<span class="w_money">${vp.prizeName}</span>加息券</a>
				</c:forEach>
			</marquee>
		</p>
		
	</div>
</div>
<div class="cj_box3">
	<div class="cj_box3_main" style="position:relative;">
				<p style="text-align:center;color:red;visibility:hidden;height:25px;line-height:25px;" id="tips-IE">
	您当前使用浏览器版本过低，可能会影响您的抽奖进程，建议您更换其他高版本浏览器。</p>
	    <div id="cj_box3_main" style="position:absolute;top:595px;left:76px;">
			<a href="${ctx}/" target="_blank">
			 	<img src="${ctx}/images/increaseate/images/tz_btn.jpg" />
			 </a>
		</div>
		<div class="cj_box3_left">
			<div class="g-content">
				<div class="g-lottery-case">
					<div class="g-left">
						<div class="g-lottery-box">
							<div class="g-lottery-img">
						            <a class="playbtn" href="javascript:;" title="开始抽奖"><i></i></a> 
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="cj_box3_right">
			<h2>您目前已有<span class="playnum">${chance}</span>次抽奖机会，点击立刻抽奖！~</h2>
			 <div id="controShow">
			<p class="cj_list">获奖记录</p>
			 
			<div class="p_list"     >
			 
				<c:forEach items="${users}" var="vp">
				<p> <fmt:formatDate value="${vp.createTime}" pattern="MM.dd HH:mm"/>&nbsp;&nbsp;${vp.remark} </p>
				</c:forEach>
			 
			 
			</div>
				</div>
				
	
			<div class="p_text">
				<p>参与说明：</p>
				<p>1、活动期间投<font class="sgy_red">三个月</font>期限标，单笔订单满1万元的用户均有
     一次机会参与抽取加息券，最高参与次数不限</p>
    			<p>2、登录后点击开始抽奖，加息券随机抽取，抽奖结果以平台
     显示为准</p>
     			<p>3、中奖结果名单在抽奖结束后的三个工作日内统一公布</p>
     			<p>4、抽得的加息券在五个工作日内发放到账户，请注意查收；
     加息券使用有效期为9月1日-9月30日，限投三个月标的，
     有关加息券的使用<a href="http://help.caifupad.com/guide/caifuquan/" target="_blank" style="color:#FFB21B;font-size:16px;">点此查看</a></p>
			</div>

		</div>

	</div>
</div>
<div class="cj_box4"></div>

<div class="footer_small footer">
	<div class="footer_t">
		<ul class="ft_ulleft">
			<li><a href="${ctx}/about" target="_blank">公司介绍</a></li>
			<li><a href="${ctx}/about" target="_blank">团队介绍</a></li>
			<li><a href="http://help.caifupad.com/notice/" target="_blank">网站公告</a></li>
			<li><a href="http://help.caifupad.com/newschannal/" target="_blank">新闻中心</a></li>
			<li><a href="${ctx}/about" target="_blank">加入我们</a></li>
			<li class="border-no"><a href="${ctx}/about" target="_blank">联系我们</a></li>
		</ul>
		<ul class="ft_ulright">
			<li>400-061-8080</li>
			<li>myservice@mayitz.com</li>
			<li>工作日：9:00~18:00</li>
		</ul>
	</div>
	<div class="footer_c">
		<span>
			<a id="___szfw_logo___" href="https://search.szfw.org/cert/l/CX20150630010536010662" target="_blank">
				<i class="ichengxin"></i>
			</a>
			<a id="" href="http://www.itrust.org.cn/yz/pjwx.asp?wm=1306536537" target="_blank">
				<i class="ixin"></i>
			</a>
		</span>
	</div>
	<div class="clear_0"></div>
	<div class="footer_b">
		<span>京ICP备14051030号 Copyright 2015 caifupad.com,All Rights Reserved</span>
	</div>
	<div class="clear_0"></div>
</div>
<div class="zhezhao"></div>
<div class="bombBox">
	<p class="text_main"></p>
	<button class="button">我知道了</button>
</div>

<div class="guoqiBox">
	<p class="guoqi_main">${result}</p>
	<a href="${ctx}/" class="guoqi_btn">我知道了</a>
</div>
<div class="guoqiBox2" id="guoqiBox2">
		<div class="guoqi_main">
			<h2></h2>
			<a  class="guoqi_btn" href="${ctx }/">快去投标吧</a>
		</div>
</div>
	<div class="guoqiBox2">
		<div class="guoqi_main">
			<h2></h2>
			<a  class="guoqi_btn" href="${ctx }/activity/increaseRate?activityNum=1">刷新</a>
		</div>
	</div>


<script src="${ctx}/js/increaseate/js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script> 
<script type="text/javascript" src="${ctx}/js/increaseate/js/jquery.rotate.min.js"></script>
	<script type="text/javascript">
	//alert("===="+${users}+"=====");
	
	$("#guoqiBox2").hide();
		$(function() {
		
			$("#cj_box3_main").hide();
	
		var isLogin=$("#isLogin").val();
		if(isLogin!=0){
			var thisLen = $(".p_list p").length;
			if(thisLen <=0){
				$(".cj_list").hide();
			}
		}
		var thisLen = $(".p_list p").length;
		if(thisLen >0){
			$(".p_list").show();
		}
		var $btn = $('.playbtn');
		var $cBut = $('.button');
		var playnum = $('.playnum').text(); //初始次数，由后台传入
		var isOk=$("#play_result").val();

		if(playnum==0){
			if(isOk!="ok"){  
				/* $(".guoqiBox").show();
				$(".zhezhao").show(); */  
				console.log("========");
				//$(".cj_box3_main").hide();
				if(isOk=="活动还没开始"){
					$(".playbtn").css({
						"background":"url(../images/increaseate/images/notstart.png) no-repeat",
						"background-size":"contain"
					});
				}else if (isOk=="活动已经结束"){
					$(".playbtn").css({
						"background":"url(../images/increaseate/images/hdend.png) no-repeat",
						"background-size":"contain"
					});
					$(".playbtn").find("i").css({
						"border-bottom":"15px solid #b3b3b3",
						"top":"-12px"
					})
				}
				$(".cj_list").hide();
				return false;
			//	$("a").removeClass("playbtnhidden")
				//$(".playbtn").remove("playbtn");
			//	$(".playbtn").attr("href",rootPath+"/user/to_login?flag=ratehd");
			}else{
				if(isLogin==0){
					$("#cj_box3_main").show();//点我投资 获取抽奖机会
					$(".playbtn").css({
						"background":"url(../images/increaseate/images/playbtn2.png) no-repeat",
						"background-size":"contain"
					});
					$(".playbtn").attr("href",rootPath+"/user/to_login?flag=ratehd");
					$(".cj_list").hide();
					$(".p_list").hide();
				}else{
				  	$(".playbtn").css({
						"background":"url(../images/increaseate/images/playbtn3.png) no-repeat",
						"background-size":"contain"
					});
					
					
					//
				}
			}
		}
		
		
		$('.playnum').html(playnum);
		var isture = 0;
		var clickfunc = function() {
			var data1 = [1, 2, 3, 4, 5];
			var data2 = [0, 288 ,72, 144,216];
			var data3 = ['感谢参与！','三个月加息0.5%','三个月加息0.8%','三个月加息1%','三个月加息1.2%'];
			$.ajax({
				url:rootPath+'/activity/startIncreaseRateGame',
				type:"post",
				dataType:"json",
				success:function(data){
					if(data.result=="0"){
						//您当前的抽奖次数已用完
						if(data.chance==0){
							$("#guoqiBox2").find("h2").html("您当前的抽奖次数已用完");
							$("#guoqiBox2").show();
							$(".zhezhao").show();
						}else{
							$(".guoqiBox2").find("h2").html(data.msg);
							$(".guoqiBox2").show();
							$(".zhezhao").show();
						}
						
					}else if(data.result=="1"){
						var numResult=parseInt(data.lottery);
						for(var i=0;i<data1.length;i++){
							if(data1[i]==numResult){
								rotateFunc(data[i], data2[i], data3[i]);
								break;
							}
						}
					}
				},
				error:function(data){
					$(".guoqiBox2").find("h2").html("服务器或网络异常");
					$(".guoqiBox2").show();
					$(".zhezhao").show();
				}
			});
		}
		$btn.click(function() {
			var thisLen = $(".p_list").length;
			if(thisLen >= 3){
				$(".cj_list").show();
			}
			if(isture) return; // 如果在执行就退出
			isture = true; // 标志为 在执行
			//先判断是否登录,未登录则执行下面的函数
			if(isLogin==0) {
				location.href=rootPath+"/user/to_login?flag=ratehd";
			} else { //登录了就执行下面
				if(playnum <= 0) { //当抽奖次数为0的时候执行
					//alert("没有次数了");
					$btn.unbind("click");
					$('.playnum').html(0);
					isture = false;
				} else { //还有次数就执行
					playnum = playnum - 1; //执行转盘了则次数减1
					if(playnum <= 0) {
						playnum = 0;
						$cBut.click(function(){
							$("a.playbtn").css({
								"background":"url(../images/increaseate/images/playbtn3.png) no-repeat",
								"background-size": "contain"
							})

						});
					}
					$('.playnum').html(playnum);
					clickfunc();
				}
			}
		});

		var rotateFunc = function(awards, angle, text) {
			isture = true;
			$btn.stopRotate();
			$btn.rotate({
				angle: 0,
				duration: 4000, //旋转时间
				animateTo: angle + 1440, //让它根据得出来的结果加上1440度旋转
				callback: function() {
					isture = false; // 标志为
					//alert(text);
					$(".zhezhao").show();
					$(".bombBox").show();
					$(".text_main").html(text);
					if($(".text_main").html() == "感谢参与！"){
						$(".bombBox").css({
							"background":"url(../images/increaseate/images/tc_bj1.png) no-repeat",
							"background-size":"contain"
						});
						$(".button").text("感谢参与");
					}else{
						$(".bombBox").css({
							"background":"url(../images/increaseate/images/tc_bj2.png) no-repeat",
							"background-size":"contain"
						});
						$(".button").text("我知道了");
					}
				}
			});
			
			$cBut.click(function() {
				$.ajax({
					url:rootPath+'/activity/reflushIncrease',
					type:"post",
					dataType:"json",
					success:function(data){
						if(data!=undefined&&data!=""){
							var users=data.users;
							var str="";
							$(users).each(function(i, v) {
								str+="<p>"+formatterStr(v.createTime)+"  "+v.remark+"</p>";
							});
							$(".p_list").html(str);
							var list=data.list;
							str="";
							$(list).each(function(i, v) {
								str+="恭喜"+v.mobileNo+
									  "抽中"+v.prizeName
									;
							});
							$(".text_main").html(str);
							$(".playnum").html(data.chance);
							$(".cj_list").show();
							playnum =data.chance;
						}
						$(".zhezhao").hide();
						$(".bombBox").hide();
						//$(".p_list").show();
					},
					error:function(data){
						$(".guoqiBox2").find("h2").html("服务器或网络异常");
						$(".guoqiBox2").show();
						$(".zhezhao").show();
					}
				});
			});
		};

	});	
		
		///ie 浏览器判断
		 var browser=navigator.appName 
		    var b_version=navigator.appVersion 
		    var version=b_version.split(";"); 
		    var trim_Version=version[1].replace(/[ ]/g,""); 
		    if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE6.0") { 
		      //alert("IE 6.0"); 
		      $("#tips-IE").css("visibility","visible");
		    } 
		    else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE7.0") { 
		      //alert("IE 7.0"); window.location.href="http://xxxx.com";
		      $("#tips-IE").css("visibility","visible");
		    } 
		    else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE8.0") { 
		      //alert("IE 8.0");
		      $("#tips-IE").css("visibility","visible"); 
		    } 
		    else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE9.0") { 
		      //alert("IE 9.0");
		      $("#tips-IE").css("visibility","hidden"); 
		    } 
	</script>
</body>
</html>
