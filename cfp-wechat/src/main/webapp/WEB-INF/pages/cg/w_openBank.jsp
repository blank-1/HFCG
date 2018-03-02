<%@ page  pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="0">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<meta name="from-url" content="${_u}" />
<meta name="userToken" content="${userToken}" />
<meta name="source" content="${source}" />
<link rel="stylesheet" href="${ctx}/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx}/css/openBank.css" type="text/css">
<link rel="stylesheet" href="${ctx}/css/sweetAlert.css" type="text/css">
<script data-main="${ctx}/js/openBank.js?${version}" defer async="true" src="${ctx}/js/lib/require.js"></script>

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
</script>
<title>银行存管开通</title>
</head>
<body class="l_NewScroll">
    <form id="FM_save" method="post">
        <input name="userToken" type="hidden" value="${userToken}" />
        <input name="source" type="hidden" value="${source}" />
    <section class="wrapper">
        <div class="title_msg">
            持卡人信息
        </div>
        <div class="mes_box">
            <div class="nameBox">
                <label for="nameInput">姓<span style="visibility: hidden;">隐</span>名</label>
                <input type="text" name="cust_nm" value="${userInfoExt.realName}" placeholder="请输入姓名" class="nameInput" id="nameInput">
            </div>
            <div class="idBox">
                <label for="idInput">身份证</label>
                <input type="tel" name="certif_id" value="${userInfoExt.idCard}" _c="211121199008161005" placeholder="请输入身份证号" class="idInput" id="idInput">
            </div>
        </div>
         <div class="title_msg">
            银行信息
        </div>
        <div class="mes_box">
            <div class="bankCarBox">
                <label for="bankCar">卡<span style="visibility: hidden;">隐</span>号</label>
                <input type="tel" name="capAcntNo" value="${card.cardCode}" placeholder="请输入银行卡号" class="bankCar" id="bankCar">
            </div>
            <div class="bankBox">
                <label for="bankInput">银<span style="visibility: hidden;">隐</span>行</label>
                <%--placeholder="请选择银行"--%>
                <input type="text"  class="bankInput" readonly="readonly" id="bankInput">
                <input type="hidden" name="parent_bank_id" id="bankInput_code">
                <%--<span class="rightArr"></span>--%>
            </div>
             <div class="addressBox" id="addressBox">
                <label for="address">开户地区</label>
                 <div class="address" id="address" style="width: 60%!important;">请选择开户省市</div>
                <input type="hidden" name="city_name">
                <input type="hidden" name="city_id" id="city_id">
                <span class="rightArr"></span>
            </div>
            <div class="BranchBox" id="BranchBox">
               <section>
                    <label for="Branch">开户支行</label>
                    <input type="text" placeholder="请输入支行关键字" name="bank_nm" class="Branch" id="Branch">
                    <span class="search"></span>
               </section>
                <ul class="Branch-list" id="Branch-list"><!--  搜索框  -->
					<li>北京市朝阳区光华路支行</li>
					<li>北京市朝阳区光华路支行</li>
				</ul>
            </div>
        </div>
         <div class="title_msg">
             银行卡预留手机号码
        </div>
        <div class="mes_box">
           <div class="phoneBox">
                <label for="phone">手机号</label>
                <input type="tel" class="phone" id="phone" name="mobile_no" readonly="readonly" value="${userInfoExt.mobileNo}">
           </div>
        </div>
        <div class="mes_box" style="margin: 1rem auto;">
            <div class="userBox">
                <label for="password">支付密码</label>
                <input type="password" name="password" class="user" id="password" placeholder="请输入支付密码">
            </div>
            <div class="pwdBox">
                <label for="password2">密码确认</label>
                <input type="password" name="password2" class="pwd" id="password2" placeholder="请再次输入支付密码">
            </div>
        </div>
        <div class="mes_box" style="margin: 1rem auto;">
            <div class="userBox">
                <label for="password">登陆密码</label>
                <input type="password" name="lpassword" class="user" id="lpassword" placeholder="请输入登陆密码">
            </div>
            <div class="pwdBox">
                <label for="password2">密码确认</label>
                <input type="password" name="lpassword2" class="pwd" id="lpassword2" placeholder="请再次输入登陆密码">
            </div>
        </div>
        <button type="button" class="NextStep" id="NextStep">下一步</button>
        <button style="width: 100%;height: 2rem;visibility: hidden;"></button>
    </section>
    <div class="select-mask" id="select-mask">
        <div class="Selected" id="Selected">
            <span class="city cur">选择省</span>
            <span class="area">选择市</span>
        </div>
        <div class="cityBox" id="cityBox">
            <ul class="citylisting listing" id="citylisting">
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
            </ul>
            <ul class="areaListing listing" id="areaListing">
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
                <li>北京</li>
            </ul>

        </div>
    </div>
    </form>
</body>
</html>
