<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="../common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="msapplication-tap-highlight" content="no"/>
    <%@include file="../common/common_js.jsp" %>
    <link rel="stylesheet" href="${ctx }/css/reset.css" type="text/css">
    <link rel="stylesheet" href="${ctx }/css/about_us.css">
    <script data-main="${ctx }/js/about_us.js" src="${ctx }/js/lib/require.js"></script>
    <script type="text/javascript">
        //rem自适应字体大小方法
        var docEl = document.documentElement,
            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
            recalc = function () {
                //设置根字体大小
                docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
            };
        //绑定浏览器缩放与加载时间
        window.addEventListener(resizeEvt, recalc, false);
        document.addEventListener('DOMContentLoaded', recalc, false);
    </script>
    <title>关于我们</title>
</head>
<body class="">
<section class="about-theme l_NewScroll">
    <div class="about-box">
        <div class="logo">
            <img src="${ctx }/images/logo.png" alt="">
        </div>
        <article>
            财富派是由北京汇聚融达网络科技有限公司推出的互联网金融服务平台，总部位于北京市朝阳区西店村86号。</br>
            <span>蚂蚁</span>财富派采用先进的管理模式，具备完善的金融风险评估体系、以及资深政策法规的专业操作团队，帮助小微企业主、工薪阶层建立信用体制，释放信用价值，获取信用资金。并始终以“安全”为核心，构筑可信赖的互联网金融服务平台。</br>
            <span>蚂蚁</span>北京汇聚融达网络科技有限公司创使人及管理团队从业年限超过10年。公司设立产品创新、风险控制、品牌推广、技术支持、运营结算等多个部门，利用互联网技术搭建起融资担保、商业保理、融资租赁、债权、基金、众筹等全方位的信息中介平台，基于大数据的分析，为企业及个人提供量身定制的金融服务。</br>
        </article>
        
        <a href="${ctx }/person/toEntityShop" class="store-list">实体店列表</a>
        <div class="liner"></div>
        
        <div class="aboutDz">
			<img src="${ctx }/images/about.png" alt="">
		</div>
        
        <ul class="info-list">
            <li><span><img src="${ctx }/images/icon/telephone.png" alt=""></span>客户热线 ：<a href="tel:400-061-8080">400-061-8080</a></li>
            <li><span><img src="${ctx }/images/icon/mail.png" alt=""></span>企业邮箱 ：myservice@mayitz.com</li>
            <li><span><img src="${ctx }/images/icon/clock.png" alt=""></span>工作时间 ：<p>工作日 9:00-18:00</br>
                周<i>六</i>六 9:00-17:00</p></li>
        </ul>
    </div>
</section>
</body>
</html>