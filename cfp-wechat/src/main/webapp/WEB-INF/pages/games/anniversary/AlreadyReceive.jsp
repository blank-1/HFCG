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
    <title>已领取</title>
    <link rel="stylesheet" href="${ctx }/gamecss/reset.css">
    <link rel="stylesheet" href="${ctx }/gamecss/Anniversary.css">
</head>
<body style="background: url(${ctx}/gameimg/anniversary/bg.jpg) repeat-y;">
    <div id="wrapper" class="wrapper">
        <div class="scroller">
            <div class="w_titFrame">
                <p class="w_titImg"><img src="${ctx}/gameimg/anniversary/result_repeat.png" alt=""></p>
                <div class="w_tit" style="border: 2px solid #ff7800;">
                    您的手机号<span class="w_phoneNum">${phone }</span></br>
                    <span class="w_sucess">已经领取过了！</span>
                </div>
            </div>
            <div class="w_attention">
                 <p style="color:#7f9ad9;margin-top:1rem;">长按二维码识别图关注我们</p>
                <div class="w_Frame">             
                    <div style="margin-right:20%;"><img src="${ctx}/gameimg/anniversary/p1_icon2.jpg" alt=""></div>
                    <div class="w_Line" id="w_Line">
                        <img src="${ctx}/gameimg/anniversary/p1_icon3.png" alt="">
                        <span class="w_liner"></span>
                    </div>
                </div>               
            </div>
            <div class="timeVariable">
                	活动时间：12月18-12月31日；</br>活动期间通过邀请码注册的新用户投标满1万就奖88现金；</br>88元现金奖于活动结束后的5个工作日统一发放；</br>本次活动解释权归财富派所有。
                <a href="#" class="w_share" id="w_share">分享</a>
            </div>
        </div>
    </div>
    <div class="markBox" id="markBox"></div>
    <script src="${ctx }/js/jquery-1.8.3.min.js"></script>
    <script src="${ctx }/gamejs/iscroll.js"></script>
    <script src="${ctx }/gamejs/Anniversary.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
	<script src="${ctx }/gamejs/wechat_anniversary.js"></script>
    <script type="text/javascript">
    	var rootPath = '<%=ctx%>';
    	//显示与隐藏蒙层
    </script>
</body>
</html>