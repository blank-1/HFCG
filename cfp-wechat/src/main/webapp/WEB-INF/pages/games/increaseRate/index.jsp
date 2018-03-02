<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
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
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes" />    
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
	<meta name="format-detection" content="telephone=no"/>
	<meta name="msapplication-tap-highlight" content="no" />
	<link rel="stylesheet" href="${ctx }/css/reset.css">
	<link rel="stylesheet" href="${ctx }/css/M_InterestCoupons.css">
	<title>加息券来了</title>	
</head>
<body>
	<input type="hidden" value="${result}" id="play_result">
	<input type="hidden" value="${isLogin}" id="isLogin">
	<div class="w_wrapper">
		<div class="w_banner">
			<img src="${ctx }/images/gamebanner.jpg" alt="">
		</div>
		<div class="w_theme">
			<div class="w_WinningBox"> 
				<h2>幸运名单</h2>
				<div class="w_writing">
					<marquee width=100%> 
						<%-- <c:if test="${fn:length(list)<3}">
							<a>恭喜<span class="w_party">138****1454</span>&nbsp;&nbsp;抽中<span class="w_money">%1</span>加息券</a>
							<a>恭喜<span class="w_party">155****2412</span>&nbsp;&nbsp;抽中<span class="w_money">%1</span>加息券</a>
							<a>恭喜<span class="w_party">139****1136</span>&nbsp;&nbsp;抽中<span class="w_money">%0.5</span>加息券</a>
							<a>恭喜<span class="w_party">187****6551</span>&nbsp;&nbsp;抽中<span class="w_money">%1.2</span>加息券</a>
							<a>恭喜<span class="w_party">188****3214</span>&nbsp;&nbsp;抽中<span class="w_money">%0.8</span>加息券</a>
							<a>恭喜<span class="w_party">138****1454</span>&nbsp;&nbsp;抽中<span class="w_money">%0.8</span>加息券</a>
							<a>恭喜<span class="w_party">133****4459</span>&nbsp;&nbsp;抽中<span class="w_money">%0.5</span>加息券</a>
							<a>恭喜<span class="w_party">188****4451</span>&nbsp;&nbsp;抽中<span class="w_money">%1</span>加息券</a>
						</c:if> --%>
						<c:forEach items="${list}" var="vp">
							<a>恭喜<span class="w_party">${vp.mobileNo}</span>&nbsp;&nbsp;抽中<span class="w_money">${vp.prizeName}</span>加息券</a>
						</c:forEach>
					</marquee>
				</div>				
			</div>
			<div class="w_turntable"><!--  转盘区域  -->
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
				<div class="w_investBtn"><img src="${ctx }/images/investBtn.jpg" alt=""></div>
			</div>
			<div class="w_Explain"><!--  说明区域  -->
				<h2>您目前有<span class="playnum">${chance}</span>次机会</h2>
				<div class="w_minutes">
					<p>获奖记录</p>	
					<ul>
						<c:forEach items="${users}" var="vp">
							<li><fmt:formatDate value="${vp.createTime}" pattern="MM.dd HH:mm"/>&nbsp;&nbsp;${vp.remark}</li>	
						</c:forEach>
					</ul>
					<p style="margin: 0;display: none;font-size: 1rem;color: #dbbefb;" class="more">滑动记录，查看更多</p>
				</div>							
				<div class="w_parameter">
					参与说明：<br/>
					1、活动期间投<span style="font-size: 1.4rem;color:#fe2b4e; ">三个月</span>期限标，单笔订满1万元的用户均有一次机会参与抽取加息券，最高参与次数不限<br/>
					2、登录后点击开始抽奖，加息券随机抽取，抽奖结果以平台显示为准<br/>
					3、中奖结果名单在抽奖结束后的三个工作日内统一公布<br/>
					4、抽得的加息券在五个工作日内发放到账户，请注意查收； 加息券使用有效期为9月1日-9月30日，限投三个月标的
				</div>
			</div>
			<footer class="w_footer">
				<img src="${ctx }/images/footer.jpg" alt="">
			</footer>
		</div>
	</div>
	<div class="zhezhao">
		<div class="bombBox">
			<p class="text_main">抽得的加息券在五个工作日内发</p>
			<button class="button">我知道了<tton>
		</div>
	</div>
	<div class="w_overdueBox">
		<div class="w_overdue">
			<h2>抱歉，该活动已过期!</h2>
			<a href="${ctx }/">返回首页</a>
		</div>
	</div>
	<div class="w_overdueBox2">
		<div class="w_overdue">
			<h2></h2>
			<a href="${ctx }/game/increaseRate?activityNum=1">刷新</a>
		</div>
	</div>
	<div class="w_overdueBox3">
		<div class="w_overdue">
			<h2></h2>
			<a href="${ctx }/finance/list">快去投标吧</a>
		</div>
	</div>
	<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
	<script src="${ctx }/gamejs/fuli4/wechat_fuli.js" type="text/javascript"></script> 
	<script type="text/javascript" src="${ctx}/gamejs/fuli4/jquery.rotate.min.js"></script>
	<script type="text/javascript">
		$(function() {
		$(".w_investBtn").on("click",function(){
			location.href=rootPath+"/finance/list";
		});
		var isLogin=$("#isLogin").val();
		var thisLen = $(".w_minutes ul").find("li").length;
		if(thisLen >0){
			$(".w_minutes").show();
		}
		var $btn = $('.playbtn');
		var $cBut = $('.button');
		var playnum = $('.playnum').text(); //初始次数，由后台传入
		if(playnum==0){
			var isOk=$("#play_result").val();
			if(isOk!="ok"){
				if(isOk=="活动还没开始"){
					$(".playbtn").css({
						"background":"url("+rootPath+"/images/gamestart.png) no-repeat",
						"background-size":"contain"
					});
				}else{
					$(".playbtn").css({
						"background":"url("+rootPath+"/images/gameover.png) no-repeat",
						"background-size":"contain"
					});
					$(".playbtn").find("i").css({
						"border-bottom":"15px solid #b3b3b3",
						"top":"-12px"
					})
				}
			}else{
				$(".w_investBtn").show();
				if(isLogin==0){
					$(".playbtn").css({
						"background":"url("+rootPath+"/images/playbtn2.png) no-repeat",
						"background-size":"contain"
					});
					$(".playbtn").attr("href",rootPath+"/user/toLogin?pastUrl=/game/increaseRate?activityNum=1");
				}else{
					$(".playbtn").css({
						"background":"url("+rootPath+"/images/playbtn3.png) no-repeat",
						"background-size":"contain"
					});
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
				url:rootPath+'/game/startIncreaseRateGame',
				type:"post",
				dataType:"json",
				success:function(data){
					if(data.result=="0"){
						if(data.chance==0){
							$(".w_overdueBox3").find("h2").html(data.msg);
							$(".w_overdueBox3").show();
						}else{
							if(data.msg=="活动还没开始"||data.msg=="活动已经结束"){
								$(".w_overdueBox").find("h2").html(data.msg);
								$(".w_overdueBox").show();
							}else{
								$(".w_overdueBox2").find("h2").html(data.msg);
								$(".w_overdueBox2").show();
							}
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
					$(".w_overdueBox2").find("h2").html("服务器或网络异常");
					$(".w_overdueBox2").show();
				}
			});
		}
		$btn.click(function() {
			var thisLen = $(".w_minutes ul").find("li").length;
			if(thisLen >= 3){
				$(".more").show();
			}
			if(isture) return; // 如果在执行就退出
			isture = true; // 标志为 在执行
			//先判断是否登录,未登录则执行下面的函数
			if(isLogin==0) {
				var isOk=$("#play_result").val();
				if(isOk=="ok"){
					location.href=rootPath+"/user/toLogin";
				}
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
								"background":"url("+rootPath+"/images/playbtn3.png) no-repeat",
								"background-size": "contain"
							})
							$(".w_investBtn").show();
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
							"background":"url("+rootPath+"/images/tc_bj1.png) no-repeat",
							"background-size":"contain"
						});
						$(".button").text("感谢参与");
					}else{
						$(".bombBox").css({
							"background":"url("+rootPath+"/images/tc_bj2.png) no-repeat",
							"background-size":"contain"
						});
						$(".button").text("我知道了");
					}
				}
			});
			
			$cBut.click(function() {
				$.ajax({
					url:rootPath+'/game/reflushIncrease',
					type:"post",
					dataType:"json",
					success:function(data){
						if(data!=undefined&&data!=""){
							var users=data.users;
							var str="";
							$(users).each(function(i, v) {
								str+="<li>"+formatterStr(v.createTime)+"  "+v.remark+"</li>";
							});
							$(".w_minutes ul").html(str);
							var list=data.list;
							str="";
							$(list).each(function(i, v) {
								str+="<a>"+
										"恭喜<span class='w_party'>"+v.mobileNo+"</span>  "+
										"抽中<span class='w_money'>"+v.prizeName+"</span>加息券"+
									 "</a>";
							});
							/* if(list.length<3){
								str+="<a>恭喜<span class='w_party'>138****1454</span>&nbsp;&nbsp;抽中<span class='w_money'>%1</span>加息券</a>"+
									 "<a>恭喜<span class='w_party'>155****2412</span>&nbsp;&nbsp;抽中<span class='w_money'>%1</span>加息券</a>"+
									 "<a>恭喜<span class='w_party'>139****1136</span>&nbsp;&nbsp;抽中<span class='w_money'>%0.5</span>加息券</a>"+
									 "<a>恭喜<span class='w_party'>187****6551</span>&nbsp;&nbsp;抽中<span class='w_money'>%1.2</span>加息券</a>"+
									 "<a>恭喜<span class='w_party'>188****3214</span>&nbsp;&nbsp;抽中<span class='w_money'>%0.8</span>加息券</a>"+
									 "<a>恭喜<span class='w_party'>138****1454</span>&nbsp;&nbsp;抽中<span class='w_money'>%0.8</span>加息券</a>"+
									 "<a>恭喜<span class='w_party'>133****4459</span>&nbsp;&nbsp;抽中<span class='w_money'>%0.5</span>加息券</a>"+
									 "<a>恭喜<span class='w_party'>188****4451</span>&nbsp;&nbsp;抽中<span class='w_money'>%1</span>加息券</a>";
							} */
							$(".w_writing marquee").html(str);
							$(".playnum").html(data.chance);
							playnum =data.chance;
						}
						$(".zhezhao").hide();
						$(".bombBox").hide();
						$(".w_minutes").show();
					},
					error:function(data){
						$(".w_overdueBox2").find("h2").html("服务器或网络异常");
						$(".w_overdueBox2").show();
					}
				});
			});
		};

	});	
	</script>
</body>
</html>