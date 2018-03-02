<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>回款日历</title>
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="yes" name="apple-touch-fullscreen">
    <meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,maximum-scale=1, minimum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="${ctx }/css/common.css">
    <link rel="stylesheet" href="${ctx }/css/indexDate.css">
    <link rel="stylesheet" href="${ctx }/css/reset.css">
    <link rel="stylesheet" href="${ctx }/css/swiper.min.css">
    <script src="${ctx }/js/lib/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="${ctx }/js/swiper.min.js"></script>
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
    
    //请求根路径
    var urlBase = '${ctx}';
    </script>
</head>

<body>

    <!-- 加载ing -->
        <div class="loading loading1 covers" >
            <div class="loading-bj"></div>
            <p>加载中...</p>
        </div>
    <!-- 加载ing END -->
    
    <!-- 上部分-开始 -->
    <section class="out-wrap">
        <div class="header-main">
             <section class="header">
                  <div class="child return"><!-- <i></i> --></div>
                  <div class="child date">
                     <em class="f34" id="ymym">2016年06月</em>
                     <i></i>
                  </div>
                  <p class="l_mInfo">当月总回款：<span id="l_money">0.00</span> 元</p>
                  <div class="day-right clearfix">
                      <div class="yun">
                          <span class="js_jin">今</span>
                      </div>
                  </div>
             </section>
             <section class="day">
                    <section class="grid clearfix">
                        <div class="day-left clearfix">
                            <h1 id="top_shu">20</h1>
                            <div class="top-contrl">
                                <p><em id="top_week">星期二</em></p>
                                <div><i id="top_yue">八月廿十</i></div>                            
                            </div>
                        </div>

                    </section>
             </section>
             <!-- end day -->
        </div>
        <!-- end header -->
        <section class="content">

            <section class="week">
                <ul class="clearfix f28">
                    <li><a href="#">日</a></li>
                    <li><a href="#">一</a></li>
                    <li><a href="#">二</a></li>
                    <li><a href="#">三</a></li>
                    <li><a href="#">四</a></li>
                    <li><a href="#">五</a></li>
                    <li><a href="#">六</a></li>
                </ul>
            </section>
            <section class="calenda">
                <div class="swiper-container">
                    <div class="swiper-wrapper">
                        <div class="swiper-slide">
                            <table id="now1" class="tables">
                                <tr class="">
                                    <td class=" list"><i>12</i><em>中秋</em></td>
                                </tr>
                            </table>
                        </div>
                        <div class="swiper-slide">
                            <table id="now2" class="tables">
                                <tr class="">
                                    <td class=" list"><i>12</i><em>中秋</em></td>
                                    <td class=" list"><i>12</i><em>中秋</em></td>
                                    <td class=" list"><i>12</i><em>中秋</em></td>
                                    <td class=" list"><i>12</i><em>中秋</em></td>
                                    <td class=" list"><i>12</i><em>中秋</em></td>
                                    <td class=" list"><i>12</i><em>中秋</em></td>
                                    <td class=" list"><i>12</i><em>中秋</em></td>
                                </tr>
                            </table>
                        </div>
                        <div class="swiper-slide">
                            <table id="now3" class="tables">
                                <tr class="">
                                    <td class="list"><i>12</i><em>中秋</em></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </section>

            <!-- end calenda -->
        </section>
        <!-- end content -->
        <section class="prorup"></section>
        <!-- 弹窗 -->
        <div class="tc"></div>
        <div class="tc-innner">
            <h2>
               <!--  <div class="title-right">
                    <div class="gl zblck active">公历</div>
                    <div class="nl zblck">农历</div>
                </div> -->
               	 选取你所需要的日期
            </h2>
            <div class="current-time">
                <i class="i-year">1900</i><i class="i-month">1</i><i class="i-date">1</i>
            </div>
            <div class="swiper-box">
                <i class="i1"></i>
                <i class="i2"></i>
                <i class="i3"></i>
                <div class="swiper-box-li">
                    <div class="swiper-container2 nbxs">
                        <div class="swiper-wrapper year">
                            <div class="swiper-slide">1900年</div>
                            <div class="swiper-slide">1901年</div>
                            <div class="swiper-slide">1902年</div>
                            <div class="swiper-slide">1903年</div>
                            <div class="swiper-slide">1904年</div>
                            <div class="swiper-slide">1905年</div>
                            <div class="swiper-slide">1906年</div>
                            <div class="swiper-slide">1907年</div>
                            <div class="swiper-slide">1908年</div>
                            <div class="swiper-slide">1909年</div>
                            <div class="swiper-slide">1910年</div>
                            <div class="swiper-slide">1911年</div>
                            <div class="swiper-slide">1912年</div>
                            <div class="swiper-slide">1913年</div>
                            <div class="swiper-slide">1914年</div>
                            <div class="swiper-slide">1915年</div>
                            <div class="swiper-slide">1916年</div>
                            <div class="swiper-slide">1917年</div>
                            <div class="swiper-slide">1918年</div>
                            <div class="swiper-slide">1919年</div>
                            <div class="swiper-slide">1920年</div>
                            <div class="swiper-slide">1921年</div>
                            <div class="swiper-slide">1922年</div>
                            <div class="swiper-slide">1923年</div>
                            <div class="swiper-slide">1924年</div>
                            <div class="swiper-slide">1925年</div>
                            <div class="swiper-slide">1926年</div>
                            <div class="swiper-slide">1927年</div>
                            <div class="swiper-slide">1928年</div>
                            <div class="swiper-slide">1929年</div>
                            <div class="swiper-slide">1930年</div>
                            <div class="swiper-slide">1931年</div>
                            <div class="swiper-slide">1932年</div>
                            <div class="swiper-slide">1933年</div>
                            <div class="swiper-slide">1934年</div>
                            <div class="swiper-slide">1935年</div>
                            <div class="swiper-slide">1936年</div>
                            <div class="swiper-slide">1937年</div>
                            <div class="swiper-slide">1938年</div>
                            <div class="swiper-slide">1939年</div>
                            <div class="swiper-slide">1940年</div>
                            <div class="swiper-slide">1941年</div>
                            <div class="swiper-slide">1942年</div>
                            <div class="swiper-slide">1943年</div>
                            <div class="swiper-slide">1944年</div>
                            <div class="swiper-slide">1945年</div>
                            <div class="swiper-slide">1946年</div>
                            <div class="swiper-slide">1947年</div>
                            <div class="swiper-slide">1948年</div>
                            <div class="swiper-slide">1949年</div>
                            <div class="swiper-slide">1950年</div>
                            <div class="swiper-slide">1951年</div>
                            <div class="swiper-slide">1952年</div>
                            <div class="swiper-slide">1953年</div>
                            <div class="swiper-slide">1954年</div>
                            <div class="swiper-slide">1955年</div>
                            <div class="swiper-slide">1956年</div>
                            <div class="swiper-slide">1957年</div>
                            <div class="swiper-slide">1958年</div>
                            <div class="swiper-slide">1959年</div>
                            <div class="swiper-slide">1960年</div>
                            <div class="swiper-slide">1961年</div>
                            <div class="swiper-slide">1962年</div>
                            <div class="swiper-slide">1963年</div>
                            <div class="swiper-slide">1964年</div>
                            <div class="swiper-slide">1965年</div>
                            <div class="swiper-slide">1966年</div>
                            <div class="swiper-slide">1967年</div>
                            <div class="swiper-slide">1968年</div>
                            <div class="swiper-slide">1969年</div>
                            <div class="swiper-slide">1970年</div>
                            <div class="swiper-slide">1971年</div>
                            <div class="swiper-slide">1972年</div>
                            <div class="swiper-slide">1973年</div>
                            <div class="swiper-slide">1974年</div>
                            <div class="swiper-slide">1975年</div>
                            <div class="swiper-slide">1976年</div>
                            <div class="swiper-slide">1977年</div>
                            <div class="swiper-slide">1978年</div>
                            <div class="swiper-slide">1979年</div>
                            <div class="swiper-slide">1980年</div>
                            <div class="swiper-slide">1981年</div>
                            <div class="swiper-slide">1982年</div>
                            <div class="swiper-slide">1983年</div>
                            <div class="swiper-slide">1984年</div>
                            <div class="swiper-slide">1985年</div>
                            <div class="swiper-slide">1986年</div>
                            <div class="swiper-slide">1987年</div>
                            <div class="swiper-slide">1988年</div>
                            <div class="swiper-slide">1989年</div>
                            <div class="swiper-slide">1990年</div>
                            <div class="swiper-slide">1991年</div>
                            <div class="swiper-slide">1992年</div>
                            <div class="swiper-slide">1993年</div>
                            <div class="swiper-slide">1994年</div>
                            <div class="swiper-slide">1995年</div>
                            <div class="swiper-slide">1996年</div>
                            <div class="swiper-slide">1997年</div>
                            <div class="swiper-slide">1998年</div>
                            <div class="swiper-slide">1999年</div>
                            <div class="swiper-slide">2000年</div>
                            <div class="swiper-slide">2001年</div>
                            <div class="swiper-slide">2002年</div>
                            <div class="swiper-slide">2003年</div>
                            <div class="swiper-slide">2004年</div>
                            <div class="swiper-slide">2005年</div>
                            <div class="swiper-slide">2006年</div>
                            <div class="swiper-slide">2007年</div>
                            <div class="swiper-slide">2008年</div>
                            <div class="swiper-slide">2009年</div>
                            <div class="swiper-slide">2010年</div>
                            <div class="swiper-slide">2011年</div>
                            <div class="swiper-slide">2012年</div>
                            <div class="swiper-slide">2013年</div>
                            <div class="swiper-slide">2014年</div>
                            <div class="swiper-slide">2015年</div>
                            <div class="swiper-slide">2016年</div>
                            <div class="swiper-slide">2017年</div>
                            <div class="swiper-slide">2018年</div>
                            <div class="swiper-slide">2019年</div>
                            <div class="swiper-slide">2020年</div>
                            <div class="swiper-slide">2021年</div>
                            <div class="swiper-slide">2022年</div>
                            <div class="swiper-slide">2023年</div>
                            <div class="swiper-slide">2024年</div>
                            <div class="swiper-slide">2025年</div>
                            <div class="swiper-slide">2026年</div>
                            <div class="swiper-slide">2027年</div>
                            <div class="swiper-slide">2028年</div>
                            <div class="swiper-slide">2029年</div>
                            <div class="swiper-slide">2030年</div>
                            <div class="swiper-slide">2031年</div>
                            <div class="swiper-slide">2032年</div>
                            <div class="swiper-slide">2033年</div>
                            <div class="swiper-slide">2034年</div>
                            <div class="swiper-slide">2035年</div>
                            <div class="swiper-slide">2036年</div>
                            <div class="swiper-slide">2037年</div>
                            <div class="swiper-slide">2038年</div>
                            <div class="swiper-slide">2039年</div>
                            <div class="swiper-slide">2040年</div>
                            <div class="swiper-slide">2041年</div>
                            <div class="swiper-slide">2042年</div>
                            <div class="swiper-slide">2043年</div>
                            <div class="swiper-slide">2044年</div>
                            <div class="swiper-slide">2045年</div>
                            <div class="swiper-slide">2046年</div>
                            <div class="swiper-slide">2047年</div>
                            <div class="swiper-slide">2048年</div>
                            <div class="swiper-slide">2049年</div>
                        </div>
                    </div>
                </div>
                <div class="swiper-box-li">
                    <div class="swiper-container3 nbxs">
                        <div class="swiper-wrapper month">
                            <div class="swiper-slide">1月</div>
                            <div class="swiper-slide">2月</div>
                            <div class="swiper-slide">3月</div>
                            <div class="swiper-slide">4月</div>
                            <div class="swiper-slide">5月</div>
                            <div class="swiper-slide">6月</div>
                            <div class="swiper-slide">7月</div>
                            <div class="swiper-slide">8月</div>
                            <div class="swiper-slide">9月</div>
                            <div class="swiper-slide">10月</div>
                            <div class="swiper-slide">11月</div>
                            <div class="swiper-slide">12月</div>
                        </div>
                    </div>
                </div>
                <div class="swiper-box-li" style="width:0">
                    <div class="swiper-container4 nbxs">
                        <div class="swiper-wrapper date">
                            <div class="swiper-slide">1日</div>
                            <div class="swiper-slide">2日</div>
                            <div class="swiper-slide">3日</div>
                            <div class="swiper-slide">4日</div>
                            <div class="swiper-slide">5日</div>
                            <div class="swiper-slide">6日</div>
                            <div class="swiper-slide">7日</div>
                            <div class="swiper-slide">8日</div>
                            <div class="swiper-slide">9日</div>
                            <div class="swiper-slide">10日</div>
                            <div class="swiper-slide">11日</div>
                            <div class="swiper-slide">12日</div>
                            <div class="swiper-slide">13日</div>
                            <div class="swiper-slide">14日</div>
                            <div class="swiper-slide">15日</div>
                            <div class="swiper-slide">16日</div>
                            <div class="swiper-slide">17日</div>
                            <div class="swiper-slide">18日</div>
                            <div class="swiper-slide">19日</div>
                            <div class="swiper-slide">20日</div>
                            <div class="swiper-slide">21日</div>
                            <div class="swiper-slide">22日</div>
                            <div class="swiper-slide">23日</div>
                            <div class="swiper-slide">24日</div>
                            <div class="swiper-slide">25日</div>
                            <div class="swiper-slide">26日</div>
                            <div class="swiper-slide">27日</div>
                            <div class="swiper-slide">28日</div>
                            <div class="swiper-slide">29日</div>
                            <div class="swiper-slide">30日</div>
                            <div class="swiper-slide">31日</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="tc-bot">
                <div class="tc-bot-left">回到今天</div>
                <div class="tc-bot-right">确定</div>
                <i></i>
            </div>
        </div>
    </section>
    <!-- 上部分-结束 -->
    
    <!-- 下部分-结束 -->
    <section class="l_infoBox l_noData">
   	  <a href="${ctx}/finance/list">立即去投标</a>
      <h1 class="l_titleInfo">当日共<span id="legDay">2</span>笔回款，回款总额：<span id="moneyDay">4374.32</span>元</h1>
      <ul class="l_infoList">
        <li>
          <h2 class="text-overflow l_geren">F17033114103-6-1440万个人房抵借款标第6期</h2>
          <p><span class="l_li"></span><span id="timer">1/12</span></p>
          <p><span id="benOrLi">123.12</span><span>待回金额</span></p>
          <p><span id="limitTime">2017.06.20</span><span>到期时间</span></p>
        </li>
        <li>
          <h2 class="text-overflow l_geren">F17033114103-6-1440万个人房抵借款标第6期</h2>
          <p><span class="l_ben"></span><span id="timer">1/12</span></p>
          <p><span id="benOrLi">123.12</span><span>待回金额</span></p>
          <p><span id="limitTime">2017.06.20</span><span>到期时间</span></p>
        </li>

      </ul>
    </section>
    <!-- 下部分-结束 -->
    
    <script type="text/javascript" src="${ctx }/js/datajs.js"></script>
</body>

</html>

