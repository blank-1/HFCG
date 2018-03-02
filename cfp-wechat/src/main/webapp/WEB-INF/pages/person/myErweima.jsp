<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!doctype html>
<html style="width: 100%;height: 100%;">
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />    
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx }/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/main_Invitation.css?${version}" type="text/css">
<script type="text/javascript">
    //rem自适应字体大小方法
    var docEl = document.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function() {
            //设置根字体大小
            docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
        };
    //绑定浏览器缩放与加载时间
    window.addEventListener(resizeEvt, recalc, false);
    document.addEventListener('DOMContentLoaded', recalc, false);

    function inviteF(){
        $(".mask").show();
    }

    function toInvite(){
        location.href="${ctx }/person/to_invite_friends";
    }
</script>
<style>
    .mask{display: none;position: fixed;background: rgba(0,0,0,0.4);top: 0;left: 0;width: 100%;height: 100%;}
    .mask img{width: 88%;margin:2% 0 2% 10%;clear: both; }
    .mask p{width:3rem ;height: 3rem;float:right;background: url(../images/icon_userListC.png) 50% 50% no-repeat;background-size: contain;margin:20% 10% 0 0; }
    .mask span{display:block;width: 100%;text-align: center;color: #fff;font-size: 1rem;height: 3rem;line-height: 3rem;}
    .mask i{display:block;width:14rem;height: 3rem;line-height: 3rem;background: #ffcc00; font-size: 1.4rem;color:#bd5400;text-align: center;border-radius:3rem;margin:2% auto;  }
</style>
<title>我的二维码</title>
</head>
<body class="l_NewScroll">
<div class="w_container">
    <div class="invitBox">
        <header>
            <h2>邀请好友</h2>
            <%--<p>
                每邀请一位好友注册并成功投标就奖
                </br><span>5</span>元现金＋<span>10</span>元财富券
            </p>--%>
        </header>
        <div class="code">
            <img src="${ctx }/person/getErWeiMa?invitecode=${invitecode}" alt="">
            <p>您的邀请码：<span class="count">${invitecode}</span></p>
        </div>
        <div class="bottom"></div>
    </div>
    <div class="rule">
        <p onclick="toInvite()">我的邀请</p>
        <p style="display: none;">活动规则</p>
    </div>
    <a onclick="inviteF()" class="request" id="request">立即邀请</a>
</div>
<div class="mask" id="listInfoClose">
    <img src="${ctx}/images/l_invite.png" alt="">
    <%--<span>未参与用户不获得相应奖励</span>
    <i id="listInfoBTN" onclick="location.href='${ctx}/person/distribution?invite_code=${invitecode}'">了解活动详情</i>--%>
</div>
<script>
    $("#listInfoClose").on("click",function(){
        $(".mask").hide();
    })
</script>
</body>
</html>
