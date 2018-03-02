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
<title>邀请记录</title>
<link rel="stylesheet" href="${ctx}/css/reset2.css">
<link rel="stylesheet" href="${ctx}/css/s_style.css">
</head>
<body>
	<div class=''>
		<div class="wrapper" id="wrapper">
			<div class="scroller" style="padding:0;">
				<ul class="s_invitation_list"></ul>
				<p id="count_p" class="l_lastTips"></p>
			</div>
		</div>
		<div class="actGotop" style="display:none;">
			<a><img src="${ctx}/images/top_icon.png" /></a>
		</div>
	</div>
	<div class="w_noDate" style="display: none;">
		<img src="${ctx}/images/icon_noData.png">
		<p style="text-align:center;font-size:1.8rem;color:rgb(178,178,178);">暂无记录</p>
	</div>
	<script type="text/javascript">
	$(function() { 
		searchHtml(1,20); 
	});
	var rootPath = '<%=ctx%>';
	var pageNo = 1, pageSize = 20, totalPage;
	function searchHtml(page, rows) {
		var thtml = "";
		$.ajax({
			url : rootPath+"/person/fiendsList",
			type : "post",
			data : {
				"rows" : rows,
				"page" : page
			},
			success : function(data) {
				if(page==1&&data.rows.length==0){
					$(".w_noDate").show();
				}else{
					var d_rows = data.rows;
					totalPage = data.totalPage;
					thtml += '';
					for (var j = 0; j < d_rows.length; j++) {
						var level=d_rows[j].distributionInvite.disLevel;
						var imagePath="<img src='${ctx}/images/s_yqhy_0"+level+".png' />";
						thtml +="<li><dl>"+
									"<dt>"+
										"<p class='s_username'>"+d_rows[j].name+"</p>"+
										"<p class='s_describe'>"+d_rows[j].createTime+"</p>"+
									"</dt>"+
									"<dd>"+imagePath+"</dd>"+
								"</dl></li>";
					}
					$(".scroller ul").append(thtml);
				}
			}
		});
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
	</script>
</body>
</html>