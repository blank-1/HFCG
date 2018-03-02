<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
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
    <title>领取成功</title>
    <link rel="stylesheet" href="${ctx }/gamecss/reset.css">
    <link rel="stylesheet" href="${ctx }/gamecss/Anniversary.css">
</head>
<body style="background: url(${ctx}/gameimg/anniversary/bg.jpg) repeat-y;">
    <div id="wrapper" class="wrapper">
        <div class="scroller">
            <div class="w_titFrame">
                <p class="w_titImg"><img src="${ctx}/gameimg/anniversary/coupon_${voucher }.png" alt=""></p>
                <p class="w_titImg"><img src="${ctx}/gameimg/anniversary/result_guide.png" alt=""></p>
                <div class="w_tit" style="border: 2px solid #ff7800;padding: 2rem 2%;">
                    <span class="w_sucess">领取成功</span></br>
                    财富券已放入<span class="w_phoneNum">${phone }</span>的账户中
                </div>
            </div>
            <div class="w_attention" style="height: 5rem;">
                           
            </div>
            <div class="timeVariable">
                	活动时间：12月18-12月31日；</br>活动期间通过邀请码注册的新用户投标满1万就奖88现金；</br>88元现金奖于活动结束后的5个工作日统一发放；</br>本次活动解释权归财富派所有。
                <div class="btnBox">
                    <a href="http://m.caifupad.com/close" class="offBtn" id="offBtn">关闭</a>
                    <a href="http://m.caifupad.com/shareClose" class="shareBtn" id="shareBtn">分享</a>
                </div>
            </div>
        </div>
    </div>
	<script type="text/javascript">
    	var rootPath = '<%=ctx%>';
    </script>
</body>
</html>