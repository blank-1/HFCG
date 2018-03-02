<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>
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
<link rel="stylesheet" href="${ctx }/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/s_Recharge.css?${version}" type="text/css">
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
<title>支付</title>
</head>

<body class="bodyBj">
<div class="clear_20"></div>
<div class="limitBox">
     <div class="limitBoxTitle">
          <%--<img src="${ctx }/images/userCenter/llzf_icon.png" />--%>
          <span>支付限额表</span>
     </div>
     <div class="limitForm">
          <table width="100%" border="1" cellspacing="1" cellpadding="0" >
               <tr class="tableTr">
                    <td>银行名称</td>
                    <td>单笔限额</td>
                    <td>单日限额</td>
                    <td>单月限额</td>
               </tr>
               <tr>
                    <td>工商银行</td>
                    <td>5万</td>
                    <td>5万</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td>农业银行</td>
                    <td>5万</td>
                    <td>20万(单日仅限6笔成功交易)</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td>建设银行</td>
                    <td>5万</td>
                    <td>10万</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td>中国银行</td>
                    <td>5万</td>
                    <td>20万</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td>邮政储蓄</td>
                    <td>5万</td>
                    <td>-</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td>招商银行</td>
                    <td>5万</td>
                    <td>-</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td>兴业银行</td>
                    <td>5万</td>
                    <td>5万</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td>广发银行</td>
                    <td>5万</td>
                    <td>-</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td>平安银行</td>
                    <td>5万</td>
                    <td>-</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td>中信银行</td>
                    <td>1万</td>
                    <td>1万</td>
                    <td>2万</td>
               </tr>
               <tr>
                    <td>华夏银行</td>
                    <td>5万</td>
                    <td>-</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td>光大银行</td>
                    <td>5万</td>
                    <td>-</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td>浦发银行</td>
                    <td>5万</td>
                    <td>20万</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td>民生银行</td>
                    <td>5万</td>
                    <td>-</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td>交行银行</td>
                    <td>5万</td>
                    <td>5万</td>
                    <td>100万</td>
               </tr>
               <tr>
                    <td colspan="4" class="limitTip">柜面开通银联无卡支付业务支持大部分地区（存在少部分地区不支持）开通银联无卡支付业务建议网上开通，使用网上银行或通过银联官网开通，开通成功率更高。网银如何开通建议咨询银行客服。</td>
               </tr>
               <tr>
                    <td colspan="4" class="limitTip2">注：商户限额、用户银行卡本身限额、认证支付标准限额，3者取最低限额。限额表仅供参考，实际以支付界面提示为准。</td>
               </tr>
          </table>
     </div>
     <p class="limitTip3">*如有疑问请咨询客服，客服电话：<i>400-0618080</i></p>
</div>
</body>
</html>