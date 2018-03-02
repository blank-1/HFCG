<%@ page language="java"  pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
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
<link rel="stylesheet" href="${ctx }/css/reset1.css" type="text/css">
<link rel="shortcut icon" href="${ctx }/images/favicon.ico">
<style>
  body,html{background:#fff!important;}
  p,span{display: block;color:#666;font-size: 1rem;line-height: 1.8rem;}
  section{width: 90%;padding:1rem 5% 4rem 5%; }
  p{font-weight: bold;}
  section ul li h1{font-size: 1.2rem;font-weight: normal;line-height: 2.4rem;margin-top: 1rem;}
  section ul li h1::before{content: "";display: inline-block;background: #ff5e61;border-radius: 100%;width: .8rem;height: .8rem;margin:0 .5rem .1rem 0;}
  button{width: 100%;border: none;color:#fff;background: #ff5e61;line-height: 4rem;font-size: 1.8rem;border-radius: 0;}
</style>
<title>协议</title>
</head>

<body>
<section>
  <p style="line-height: 3rem;">授权委托书</p>
  <span>编号：HJRD--SQWT-16XX-XXXXXX</span>
  <span>甲方（委托方）： </span>
  <span>身份证号码：</span>
  <span>联系电话： </span>
  <span style="margin-bottom:2rem;">住址：</span>
  <span>乙方（受托方）：北京汇聚融达网络科技有限公司 </span>
  <span style="margin-bottom:1rem;">地址：北京市朝阳区西店村86号 </span>
  <span>鉴于：委托方参加受托方经营的P2P借贷交易居间服务的互联网平台（即“财富派”）推出的一项“省心服务计划”，为更好的为委托方提供服务，最大限度满足委托方的投资收益需求，现委托方特向受托方做出如下授权，由此产生的责任均由委托方本人承担：</span>
  <ul>
    <li>
      <h1>授权委托事项：</h1>
      <p>1、受托方代委托方在财富派平台上进行账号注册，并将委托方姓名、身份 证号码及委托方名下使用的一个银行卡账号提交给委托方出借资金托管 第三方机构，为委托方开设账户，供出借资金托管使用</p>
      <p>2、受托方代委托方在财富派平台上点击确认出借资金；</p>
      <p>3、注册成功后，受托方在授权服务期（省心期）内代委托方进行资金的出 借、帮助其选择并自动投资平台线上散标、回款管理及回款再出借；</p>
    </li>
    <li>
      <h1>授权期限：</h1>
      <p>1、本授权期限为6个自然月，自委托方与受托方签署本《授权委托书》 生效之日起算，除委托人书面做出相反意思表示外，本《授权委托书》 至授权期限届满之日前具有法律效力； </p>
      <p>2、省心计划退出方式为授权期限届满即省心期到期后，债权到期退出；</p>
      <p>3、期限届满，如甲方仍有委托意向，双方可续签相应协议。 </p>
    </li>
    <li>
      <h1>授权出借信息： </h1>
      <p>1、授权出借账户<br> 财富派平台账号： </p>
      <p>2、授权出借金额：人民币（大写）元（￥）</p>
    </li>
    <li>
      <h1>声明： </h1>
      <p>1、授权期限（省心期）不同于收益期限，具体收益期限以线上实际生成标 的对应的《借款及服务协议》中约定为准； </p>
      <p>2、预期收益为范围值不同于实际收益率，具体收益率以线上实际生成标的 对应的《借款及服务协议》中约定为准； </p>
    </li>
    <li>
      <h1>其他： </h1>
      <p>1、本委托书经委托方在财富派网络平台以线上点击确认的方式签署； </p>
      <p>2、本委托书生效后，协议各方应严格遵守，任何一方违约，应承担由此造 成的守约方的损失。 </p>
    </li>
  </ul>
  <p style="line-height: 3rem;">委托方：</p>
  <span>财富派ID：</span>
  <span>签署日期：年月日</span>
</section>
<script type="text/javascript" src="${ctx }/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx }/js/mainIndex.js"></script>
<button onclick='window.location.href="${ctx}/finance/toSxDetail?lendProductPublishId=${id }"    '>关闭</button>

</body>
</html>
