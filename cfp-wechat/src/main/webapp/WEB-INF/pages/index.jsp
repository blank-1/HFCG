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
    html,body{
        width: 100%;
        height: 100%;
    }
    body{
        background: #90e9ff; 
    }
    body a{
        display: block;
        font-size: 0;
    }
   .l_indeximg1bg{
    background: url(${ctx }/images/indeximg1.jpg) no-repeat;
    background-size: cover;
    padding-bottom: 6%;
  /*  flex: 3;*/
}
    .l_indeximg1{
        width: 90%;
        margin:5%; 
    }
    .l_indeximg2{
       display: block;
       width: 100%;
       font-size: 1.6rem;
       line-height: 7rem;
       vertical-align:middle;
       color: #95b0cc;
       background: url(${ctx }/images/indeximg2.jpg) no-repeat; 
       background-size: cover;
       text-align: center;      
    }
    .l_indextipbox{
        width: 80%;
        position: absolute;
        
        left:50%;
        margin-left: -40%;
        margin-top: -3.4rem;
        text-align: center;
    }
    .l_indextip{
        display: block;
        background: #ff3a5b;
        color: #fff;
        font-size: 2rem;
        line-height: 3rem;
        height: 3rem;
        width: 100%;
        text-align: center;
        font-weight: bold;
        border-radius: 200px;
    }
    .l_indexMid{
        background: #fff;
        width: 100%;
       /* padding-top:2% ;*/
        text-align: center;
        /*flex: 1;*/
        padding: 6% 0
    }
    .l_indexMid p{
        color: #95b0cc;
        font-size: 1.4rem;
        line-height: 2rem;
        margin:0; 
        padding-top: 2%;
    }
    .l_indexMid p span{
        color: #ff3a5b;
    }
    .l_indexMid img{
        width: 90%;
        margin: 4% 0;
    }
 
    .l_indexTip2{
        margin:0;
        background:#90e9ff; 
        color: #06b4e0;
        font-size: 1.6rem;
        line-height: 4rem; 
        text-align: center;
    }
   
    .l_indexload{
        background: url(${ctx }/images/disable.png) no-repeat 98%;
        background-size: 4%;
        width: 74%;
        overflow: hidden;
        margin:0 auto; 
    }
    .l_indexload p{
        text-align: left;
        padding: 0;   
    }
    .l_indexload img{
        width: 4rem;
        float: left;
        margin:0 4% 0 0; 
    }
    .zsize{
        font-size: 1.4rem;
    }
</style>
</head>

<body>
    <div class="l_indeximg1bg">
        <div style="position:relative;">
        <img class="l_indeximg1" src="${ctx }/images/index_border1.png">
        <div class="l_indextipbox">
            <a href="${ctx }/finance/list"><div class="l_indextip">开&nbsp;始&nbsp;赚&nbsp;钱</div></a>
        </div>
        </div>   
    </div>
    <div class="l_indexMid">
        <c:if test="${not empty sessionScope.currentUser  }">
        		 <p>[&nbsp;您的累计出借金额(元)&nbsp;<span  class="zsize"><fmt:formatNumber value="${allBuyBalance}"/></span>&nbsp;]</p>
        	</c:if>
        	<c:if test="${empty sessionScope.currentUser  }">
        		<p>[&nbsp;平台累计出借金额(元)&nbsp;<span  class="zsize"><fmt:formatNumber value="${allBuyBalance}"/></span>&nbsp;]</p>
        	</c:if>
        <img src="${ctx }/images/index_border2.png" alt="">
        <c:if test="${not empty sessionScope.currentUser  }">
    		<a href="${ctx }/person/account/overview">
		        <div class="l_indexload">
		            <img src="${ctx }/images/indeximg3.png" alt="">
		            <p>您好：</p>
		            <p>${sessionScope.currentUser.loginName}</p>
		        </div>
			</a>  
    	</c:if>
 		<c:if test="${empty sessionScope.currentUser  }">
	    	<a href="${ctx }/user/toLogin">	
	    		<div class="l_indexload">
		            <img src="${ctx }/images/indeximg3.png" >
		            <p>马上登录</p>
		            <p>开启赚钱模式</p>
		        </div>
	    	</a>  
	    </c:if> 
 	</div>
    <div class="l_indexFoot">
        <a href="${ctx }/person/toEntityShop" class="l_indeximg2" ><span>实体店></span> </a>
        <p class="l_indexTip2">◆ 第三方资金托管+有效保障资金安全 ◆</p>
    </div>
</body>
</html>


