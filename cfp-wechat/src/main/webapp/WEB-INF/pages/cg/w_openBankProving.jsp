<%@ page language="java"  pageEncoding="UTF-8"%>
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
<link rel="stylesheet" href="${ctx}/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx}/css/openBankProving.css" type="text/css">
<link rel="stylesheet" href="${ctx}/css/sweetAlert.css" type="text/css">
<script data-main="${ctx}/js/openBankProving.js?${version}" src="${ctx}/js/lib/require.js"></script>

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
<title>银行存管交易账户开通</title>
</head>
<body class="l_NewScroll">
    <section class="wrapper">
        <div class="title_msg">
            持卡人信息
        </div>
        <div class="mes_box">
            <div class="nameBox">
                <label for="nameInput">姓<span style="visibility: hidden;">隐</span>名</label>
                <input type="text" name="cust_nm" value="${userInfoExt.realName}" class="nameInput" id="nameInput">
            </div>
            <div class="idBox">
                <label for="idInput">身份证</label>
                <input type="tel" name="certif_id" value="${userInfoExt.idCard}" class="idInput" id="idInput">
            </div>
        </div>
         <div class="title_msg">
            银行信息
        </div>
        <div class="mes_box">
            <div class="bankBox">
                <label for="bankInput">银<span style="visibility: hidden;">隐</span>行</label>
                <input type="text" value="工商银行" name="" class="bankInput" id="bankInput" readonly="readonly">
            </div>
             <div class="bankCarBox">
                <label for="bankCar">卡<span style="visibility: hidden;">隐</span>号</label>
                <input type="tel" name="capAcntNo" value="6226221205644756" class="bankCar" id="bankCar" readonly="readonly">
            </div>
            <div class="BranchNameBox">
                <label for="BranchName">开户行名称</label>
                <input type="text" value="北京市朝阳区高碑店支行" name="bank_nm" class="BranchName" id="BranchName" readonly="readonly" style="text-indent: 1.5rem;">
            </div>
        </div>
        <div class="mes_box" style="margin: 1rem auto;">
           <div class="phoneBox">
                <label for="phone">手机号</label>
                <input type="tel" class="phone" id="phone" name="mobile_no" value="${currentUser.mobileNo}">
           </div>
        </div>
        <div class="mes_box" style="margin: 1rem auto;">
           <div class="userBox">
                <label for="user">支付密码</label>
                <input type="password" class="user" id="user" placeholder="请输入支付密码">
           </div>
           <div class="pwdBox">
                <label for="pwd">密码确认</label>
                <input type="password" class="pwd" id="pwd" placeholder="请再次输入密码">
           </div>
        </div>
        <div class="AgreeBox">
            <div class="selAgree">
                <p id="UnAgree" class="UnAgree"></p>
               <label for="UnAgree"> 我已阅读并同意<a href="">《恒丰银行网络交易资金存管第三方协议》</a></label>
            </div>
        </div>
        <button class="NextStep" id="NextStep">下一步</button>
    </section>
</body>
</html>
