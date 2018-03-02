<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="./common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />    
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />

<%@include file="./common/common_js.jsp"%>
<title>首页</title>
<style>
    body a{
        display: block;
        font-size: 0;
    }
    .l_logo{
        width: 35%;
        margin:5.5% auto;
    }
    .l_indeximg1,.l_indeximg2{
        width: 100%;        
    }
    .l_indeximg2{
        
    }
    .l_indextipbox{
        width: 80%;
        position: absolute;
        bottom:-1.5rem;
        left:50%;
        margin-left: -40%;
        text-align: center;
    }
    .l_indextip{
        background: #1ad8b0;
        color: #1f7865;
        font-size: 1.2rem;
        line-height: 3rem;
        width: 100%;
        text-align: center;
        font-weight: bold;
    }
    .l_indextip_left{
        width:0px;
        height: 0px;
        border: 1.5rem solid transparent;
        border-right: 1.5rem solid #1ad8b0;
        position: absolute;
        left: -3rem;
        top: 0;
    }
    .l_indextip_right{
        width:0px;
        height: 0px;
        border: 1.5rem solid transparent;
        border-left: 1.5rem solid #1ad8b0;
        position: absolute;
        right: -3rem;
        top: 0;
    }
    .l_indextip span{
        font-weight: bolder;
        text-align: center;
    }
    .l_login{
        background: #262545;
        padding: 14% 20%;
        overflow: hidden;
    }
    .l_login p{
        margin: 0 0 0 34%;
    }
    .l_login img{
        float: left;
        width: 20%;
        margin: 0 0 0 4%;
    }
    .zsize{
        font-size: 1.4rem;
    }

</style>
</head>
<html>
<body id="backgd" style="padding: 0;background: #262545">
    <div style="width: 100%;text-align:center;background: #fff;"><img class="l_logo" src="${ctx }/images/logo.png"></div>
    
    <a href="${ctx }/finance/list">
        <img class="l_indeximg1" src="${ctx }/images/indeximg1.jpg">
    </a>
    <a href="${ctx }/person/toEntityShop" style="position: relative;">
        <img class="l_indeximg2" src="${ctx }/images/indeximg2.jpg">
        <div class="l_indextipbox">
            <div class="l_indextip_left"></div>
            <div class="l_indextip">
            <c:if test="${not empty sessionScope.currentUser  }">
        		您的累计出借金额(元)&nbsp;<span  class="zsize"><fmt:formatNumber value="${allBuyBalance}"/></span>
        	</c:if>
        	<c:if test="${empty sessionScope.currentUser  }">
        		平台累计出借金额(元)&nbsp;<span  class="zsize"><fmt:formatNumber value="${allBuyBalance}"/></span>
        	</c:if>
            </div>
            <div class="l_indextip_right"></div>
        </div>
    </a>
    
    <c:if test="${not empty sessionScope.currentUser  }">
    	<a href="${ctx }/person/account/overview">	
	    	<div class="l_login">
	            <img src="${ctx }/images/indeximg3.png" >
	            <p style="font-size: 1.8rem;color: #fe2a4d">您好</p>
	            <p style="font-size: 1.4rem;color: #817fc3">${sessionScope.currentUser.loginName}</p>
	        </div>
    	</a>  
    </c:if>
    <c:if test="${empty sessionScope.currentUser  }">
    	<a href="${ctx }/user/toLogin">	
    		<div class="l_login">
	            <img src="${ctx }/images/indeximg3.png" >
	            <p style="font-size: 1.8rem;color: #fe2a4d">马上登录</p>
	            <p style="font-size: 1.4rem;color: #817fc3">开启赚钱模式</p>
	        </div>
    	</a>  
    </c:if>  
</body>
</html>