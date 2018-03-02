<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%
	String ctx = request.getContextPath();
	pageContext.setAttribute("ctx", ctx);
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort();
	pageContext.setAttribute("basePath", basePath);
%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style"
	content="black-translucent" />
<meta name="format-detection" content="telephone=no" />
<meta name="msapplication-tap-highlight" content="no" />
<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
<title>佣金明细</title>
<link rel="stylesheet" href="${ctx}/css/reset.css">
<link rel="stylesheet" href="${ctx}/css/s_style.css">
</head>
<body>
	<input type="hidden" value="${userLevel}" id="userLevel">
	
	<p class="l_tab">
		<span data-tabLevel="0" class="l_tabFocus">全部</span>
		<span data-tabLevel="1">一级佣金</span>
		<span data-tabLevel="2">二级佣金</span>
		<span data-tabLevel="3">三级佣金</span>
	</p>
	
	<div class="scroller">
		<ul></ul>
		<p id="count_p" class="l_lastTips"></p>
	</div>
	<div class="actGotop" style="display:none;"><a href="#"><img src="${ctx}/images/top_icon.png" /></a></div>
	
	<div class="l_noData">
		<img src="${ctx}/images/icon_noData.png">
		<p>还没有记录</p>
		<c:if test="${empty invite_code}">
			<a>去邀请</a>
		</c:if>
		<c:if test="${not empty invite_code}">
			<a href="${ctx}/person/distribution?invite_code=${invite_code}">去邀请</a>
		</c:if>
	</div>
	
	<script type="text/javascript">
	$(function() { 
		var userLevel=$("#userLevel").val();
		searchHtml(1,20); 
		$(".l_tab span").removeClass("l_tabFocus");
		$(".l_tab span[data-tabLevel='"+userLevel+"']").addClass("l_tabFocus");
	});
	var rootPath = '<%=ctx%>';
	var pageNo = 1, pageSize = 20, totalPage;
	function searchHtml(page, rows) {
		var thtml = "";
		var userLevel=$("#userLevel").val();
		$.ajax({
			url : rootPath+"/person/commisionList",
			type : "post",
			data : {
				"rows" : rows,
				"page" : page,
				"userLevel":userLevel
			},
			success : function(data) {
				var d_rows = data.rows;
				totalPage = data.totalPage;
				if(totalPage==0){
					$(".l_noData").show();
				}else{
					thtml += '';
					for (var j = 0; j < d_rows.length; j++) {
						var time=dateTimeFormatter(d_rows[j].changeDate);
						thtml +="<li data-level="+d_rows[j].userLevel+"><dl>"+
									"<dt>"+
										"<p class='s_money'>佣金"+d_rows[j].balance+"元</p>"+
										"<p class='s_describe'>"+d_rows[j].lendOrderName+"</p>"+
									"</dt>"+
									"<dd>"+
										"<p class='s_time'>"+time+"</p>"+
										"<p class='s_user'>用户投资"+d_rows[j].comiRatioBalance+"元</p>"+
									"</dd>"+
								"</dl></li>";
					}
					$(".scroller ul").append(thtml);
				}
			}
		});
	}
	
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
	
	$(window).scroll(
		function() {
			var MainBoxH = $(".scroller").outerHeight(), scrollTop = $(window).scrollTop()+ $(window).height();
			if ($(window).scrollTop() >= 100) {
				$('.actGotop').fadeIn(300);
			} else {
				$('.actGotop').fadeOut(300);
			}
			if (MainBoxH == scrollTop) {
				if(totalPage!=undefined){
					if(pageNo<=totalPage){
						pageNo++;
						searchHtml(pageNo,20);
						if(pageNo>=totalPage){
							$("#count_p").text("已加载完全部数据");
						}else{
							$("#count_p").text("向下滑动加载更多");
						}
					}
				}
			}
	});

	$('.actGotop').click(function() {
		$('html,body').animate({
			scrollTop : '0px'
		}, 300);
	});
	
	function loadNow(val){
		$(".l_tab span").removeClass("l_tabFocus");
		$(".l_tab span[data-tabLevel='"+val+"']").addClass("l_tabFocus");
		if (val != 0) {
			$(".scroller li").hide();
			$(".scroller li[data-level='"+val+"']").show();
		}else{
			$(".scroller li").show();
		}
	}
		
	
	$(".l_tab span").on("touchend",function(e){
		$(".scroller ul").html("");
		var tabLevel = $(this).attr("data-tabLevel");
		$("#userLevel").val(tabLevel);
		$(".l_tab span").removeClass("l_tabFocus");
		$(this).addClass("l_tabFocus");
		/* if (tabLevel != 0) {
			$(".scroller li").hide();
			$(".scroller li[data-level='"+tabLevel+"']").show();
		}else{
			$(".scroller li").show();
		} */
		searchHtml(1,20);	
	})
	</script>
</body>
</html>