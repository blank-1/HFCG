<%@ page language="java"  pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<title>提现结果</title>
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx}/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/s_presentSuccess.css?${version}" type="text/css">
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
</head>

<body>
  <div class="container">
    <div class="imgBox">
      <img class="l_topimg" src="${ctx}/images/userCenter/tixianBj.jpg" alt="">
    </div>
    <div class="l_midbox">
      <div class="l_midinfo1">
        提现申请已提交<br /><span><fmt:formatDate value="${createTime}" type="both" pattern="MM-dd HH:mm" /></span>
      </div>
      <div class="l_midinfo2">
        预计审核完成<br /><span><fmt:formatDate value="${auditTime}" type="both" pattern="MM-dd" /> 24:00前</span>
      </div>
      <div class="l_midinfo3">
        预计到账时间<br /><span>审核完成后3个工作日内</span>
      </div>
      <div class="l_midicon1">
        <span class="iconImg01"></span>
        <a href=""></a>
      </div>
      <div class="l_midicon2">
        <span class="iconImg02"></span>
        <a href=""></a>
        <a href=""></a>
      </div>
      <div class="l_midicon3">
        <span class="iconImg03"></span>
      </div>
    </div>
    <p class="l_midtip"><i>＊</i>如遇节假日，审核和银行处理时间可能会有延迟，请以实际到帐时间为准</p>
    <button onclick="location.href='${ctx }/person/account/overview'" class="madBtn">确定</button>
  </div>
</body>
</html>