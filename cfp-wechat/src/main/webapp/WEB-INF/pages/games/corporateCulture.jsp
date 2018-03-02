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
    <title>汇聚融达招商加盟微信书</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
    <!-- styles -->
    <link rel="stylesheet" href="${ctx }/gamecss/swiper.min.css">
    <link rel="stylesheet" href="${ctx }/gamecss/CompanyIntroduction.css">    
</head>
<body>
    <!-- 主题内容 -->
    <div class="swiper-container">
        <div class="swiper-wrapper">
           <div class="swiper-slide page1">
               <p class="Share">分享给好友</p>
               <div class="w_UIMaskB">
                   <p class="imgBox" style="padding-top: 50%;"><img src="${ctx }/gameimg/corporate/book_p8_img1.png" alt=""></p>
                   <p class="imgBox kown" style="padding-top: 25%;width: 40%"><img src="${ctx }/gameimg/corporate/book_p8_img2.png" alt=""></p>
               </div>
            </div>
            <div class="swiper-slide page2">
                <h2>CONTENTS</h2>
                <h1>目录</h1>
                <ul class="catalog" id="catalog">
                    <li><span>1</span>当企业文化遇到互联网金融</li>
                    <li><span>2</span>CEO致辞</li>
                    <li><span>3</span>蚂蚁文化 赢的策略</li>
                    <li><span>4</span>聚微服微 共享成长</li>
                    <li><span>5</span>最强合伙人 携手智远</li>
                </ul>
            </div>
            <div class="swiper-slide page3">
                <p class="swiper-button-prev catalogue" style="top: .5rem;left: 0;">
                    <img src="${ctx }/gameimg/corporate/book_icon1.png" alt="">
                </p>
                <p class="title">当企业文化遇到互联网金融</p>
                <p class="imgBox"><img src="${ctx }/gameimg/corporate/book_p3_img.jpg" alt=""></p>
                <p class="official">
                    互联网时代下的企业文化，活力、人本、变革成为最显著的文化DNA。当金融服务文化遇到互联网，互联网元素瞬间让金融文化更加平民亲和、更富创造力，同时金融元素也使互联网文化更加稳健踏实。</br></br>
                    北京汇聚融达网络有限公司成立于2014年，位于北京CBD国贸商圈，是一家创新型的科技企业。互联网专家和金融界企业家的跨界融合，使汇聚融达的企业文化在与时俱进中迸发出了“创新”、“诚信”、“思变”、“人本”等优秀的文化基因，并伴随平台健康成长。
                </p>
                <p class="imgBox"><img src="${ctx }/gameimg/corporate/book_p3_img1.jpg" alt=""></p>
            </div>
            <div class="swiper-slide page4">
                <p class="swiper-button-prev catalogue" style="top: .5rem;left: 0;">
                    <img src="${ctx }/gameimg/corporate/book_icon1.png" alt="">
                </p>
            </div>
            <div class="swiper-slide">
                <p class="swiper-button-prev catalogue" style="top: .5rem;left: 0;">
                    <img src="${ctx }/gameimg/corporate/book_icon1.png" alt="">
                </p>
                <p class="title">蚂蚁文化 聚微服微</p>
                <p class="imgBox"><img src="${ctx }/gameimg/corporate/book_p5_img.jpg" alt=""></p>
                <p class="official"> 
                    汇聚融达以蚂蚁文化自居，这个生物界最小的精灵貌似渺小，却有着惊人的凝聚力。成千上万只蚂蚁一旦团结起来，能做出惊天动地的事情。有一个古老的寓言：在非洲的草原上，如果见到羚羊在奔逃，那一定是狮子来了；如果见到狮子在躲避，那一定是象群在发怒了；如果见到成百上千的狮子和大象集体逃命的壮观景象，那是蚂蚁军团来了。
                </p>
                <p class="imgBox" style="margin: 1.5rem auto;"><img src="${ctx }/gameimg/corporate/book_p5_img1.jpg" alt=""></p>
            </div> 
            <div class="swiper-slide">
                <p class="swiper-button-prev catalogue" style="top: .5rem;left: 0;">
                    <img src="${ctx }/gameimg/corporate/book_icon1.png" alt="">
                </p>
                <p class="title">聚微服微 共享成长</p>
                <p class="imgBox"><img src="${ctx }/gameimg/corporate/book_p6_img.jpg" alt=""></p>
                <p class="official bg">
                    <i style="visibility: hidden;">你好</i>汇聚融达将以综合性的互联网金融中介平台作为发展方向，以为客户提供全方位金融服务为己任，坚持O2O的业务发展模式，与加盟商共同成长。</br></br>
                    <i style="visibility: hidden;">你好</i>在合作业务模式上，汇聚融达将根据加盟商的级别和保证金数量，给予加盟商一定比例的资产项目授信，使加盟商形成完善的金融中介链条；同时，还将在海内外保险、投资等多领域为加盟商的客户提供全方位产品中介服务。</br></br>
                    <i style="visibility: hidden;">你好</i>展望未来的汇聚融达，蚂蚁投资体验店总数将超过500家，分布在全国31个省、市、自治区，注册用户将突破百万，其中线上客户占据一半以上；“财富派”成为线上理财的知名品牌，单月交易量突破8亿，年度在百亿以上。蚂蚁投资和财富派实现强互动，客户在线上和线下都能体验到优质服务，透明、安全和信赖成为市场公认的品牌内涵。</br></br>
                    <i style="visibility: hidden;">你好</i>在资本运作方面，汇聚融达也有清晰的发展思路，公司现在已经开始着手策划在H股市场的资本运作。未来，公司将会把这些资本运作成就通过股权收购或受让等方式与核心加盟商共享。
                </p>
                <p class="imgBox"><img src="${ctx }/gameimg/corporate/book_p6_img2.jpg" alt=""></p>
                <p class="official bg">
                    汇聚融达与加盟商携手创造良好的金融生态环境,实现双方互惠共赢发展。
                </p>
                <p class="imgBox"><img src="${ctx }/gameimg/corporate/book_p6_img3.jpg" alt=""></p>
                <p class="official bg" style="text-align: center;">
                    智慧赢财富</br>创新赢品质</br>诚信赢信誉
                </p>
                <p class="imgBox"><img src="${ctx }/gameimg/corporate/book_p6_img4.jpg" alt=""></p>
                <p class="official bg" style="text-align: center;">
                    聚集微小、服务微小。
                </p>
            </div>
            <div class="swiper-slide page5">
                <p class="swiper-button-prev catalogue" style="top: .5rem;left: 0;">
                    <img src="${ctx }/gameimg/corporate/book_icon1.png" alt="">
                </p>
                <p class="official" style="text-align: center;margin-top: 5rem;font-size: 1.6rem;font-weight: bold;">
                    最强合伙人 携手智远
                </p>
                <p class="imgBox" style="width: 100%;"><img src="${ctx }/gameimg/corporate/book_p7_img.jpg" alt=""></p>
            </div>
           <div class="swiper-slide page6">
                <p class="Share">分享给好友</p>
                <p class="erweima">
                    <img src="${ctx }/gameimg/corporate/book_p8_icon.jpg" alt="">
                </p>
                <div class="w_UIMaskB">
                    <p class="imgBox" style="padding-top: 50%;"><img src="${ctx }/gameimg/corporate/book_p8_img1.png" alt=""></p>
                    <p class="imgBox kown" style="padding-top: 25%;width: 40%"><img src="${ctx }/gameimg/corporate/book_p8_img2.png" alt=""></p>
                </div>
                
            </div>
        </div>
        <!-- 添加下面小图标 -->
        <div class="swiper-pagination"></div>
        <!-- 添加左右按钮 -->
        <div class="swiper-button-next" style="width: 44px;height: 22px;"></div>
    </div>

    <!-- Swiper JS -->
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
    <script src="${ctx }/gamejs/zepto.min.js"></script>
	<script src="${ctx }/gamejs/wechat_culture.js"></script>
    <script src="${ctx }/gamejs/swiper.min.js"></script>

    <!-- 调用 Swiper -->
    <script>
    var rootPath = '<%=ctx%>';
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev'
    });
    $(".Share").on("click",function(){
        $(".w_UIMaskB").show();
        $(this).hide();
        $(".erweima").hide();
    })
    $(".kown").on("click",function(){
        $(".w_UIMaskB").hide();
        $(".Share").show();
         $(".erweima").show();
    })

   function stopDefault(e) {  
        e = e || window.event;
                //如果提供了事件对象，则这是一个非IE浏览器   
        if(e && e.preventDefault) {  
        　　//阻止默认浏览器动作(W3C)  
        　　e.preventDefault();  
        } else {  
        　　//IE中阻止函数器默认动作的方式   
        　　e.returnValue = false;   
        }  
        return false;  
    }   
    </script>
</body>
</html>