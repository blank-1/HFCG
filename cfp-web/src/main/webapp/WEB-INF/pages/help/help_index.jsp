<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="财富派" />
	<meta name="description" content="财富派" />
	<title>帮助中心 - 财富派</title>
	<%@include file="../common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx}/js/person1.js"></script><!-- person1 javascript -->
</head>

<body>
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine/>
<!-- navindex end -->
<%@include file="../login/login.jsp"%>
<!-- helpindex start -->
<div class="helpindex">

</div><!-- helpindex end -->
<!-- helpindex start -->
<div class="hdetail wrapper">
	<h2>热门问题<span class="hhdh2"></span></h2>
    <!-- hotque start -->
    <div class="hotque">
    	<ul class="clearFloat">
        	<li><a href="help2.html">如何进行投资？</a></li>
        	<li><a href="help2.html">什么是信用标？</a></li>
        	<li><a href="help3.html">提现费用和提现处理时间？</a></li>
        	<li><a href="help4.html">本金和收益如何得到保障？</a></li>
        	<li><a href="help4.html">如何保证我的资金安全？</a></li>
        	<li><a href="help4.html">什么是风险准备金？</a></li>
        	<li><a href="help3.html">投资人是否需要支付费用？</a></li>
        	<li><a href="help4.html">如何审核借款项目？</a></li>
        	<li><a href="help2.html">投资后是否可以提前赎回？</a></li>
        	<li><a href="help2.html">如何邀请好友？</a></li>
        	<li><a href="help3.html">首次提现都需要注意什么呢？</a></li>
        	<li><a href="help6.html">如何计算回款续投奖励？</a></li>
        </ul>
    </div><!-- hotque end -->
	<h2>常见问题<span class="hhdh2"></span></h2>
    <!-- usque start -->
    <div class="usque clearFloat">
    	<div class="usq_gr">
        	<a href="help1.html">
            	<div class="grun">
                	<span class="usq11"></span>
                	<span class="usq1"></span>
                </div>
                <p>注册/认证</p>
            </a>
        </div>
    	<div class="usq_gr">
        	<a href="help2.html">
            	<div class="grun">
                	<span class="usq12"></span>
            		<span class="usq2"></span>
                </div>
                <p>投资</p>
            </a>
        </div>
    	<div class="usq_gr">
        	<a href="help3.html">
            	
            	<div class="grun">
                	<span class="usq13"></span>
            		<span class="usq3"></span>
                </div>
                <p>充值/提现</p>
            </a>
        </div>
    	<div class="usq_gr usq_ri">
        	<a href="help4.html">
            	
            	<div class="grun">
                	<span class="usq14"></span>
            		<span class="usq4"></span>
                </div>
                <p>平台计算器</p>
            </a>
        </div>
    	<div class="usq_gr usq_bot">
        	<a href="help4.html">
            	<div class="grun">
                	<span class="usq15"></span>
            		<span class="usq5"></span>
                </div>
                <p>本金收益保障</p>
            </a>
        </div>
    	<div class="usq_gr usq_bot">
        	<a href="help5.html">
            	<div class="grun">
                	<span class="usq16"></span>
            		<span class="usq6"></span>
                </div>
                <p>账户密码管理</p>
            </a>
        </div>
    	<div class="usq_gr usq_bot">
        	<a href="help6.html">
            	<div class="grun">
                	<span class="usq17"></span>
            		<span class="usq7"></span>
                </div>
                <p>债券转让</p>
            </a>
        </div>
    	<div class="usq_gr usq_ri usq_bot">
        	<a href="help7.html">
            	<div class="grun">
                	<span class="usq18"></span>
            		<span class="usq8"></span>
                </div>
                <p>名词解释</p>
            </a>
        </div>
    </div> <!-- usque start -->
	<h2>联系客服<span class="hhdh2"></span></h2>
    <!-- hacser start -->
    <div class="hacser clearFloat">
    	<div class="hac-se">
        	<span class="usq9"></span>
            <span>
            	<h2>在线客服</h2>
                <p>工作时间：9:00 - 18:00</p>
            </span>
        </div>
    	<div class="hac-se">
        	<span class="usq10"></span>
            <span>
            	<h2>400-061-8080<small> (仅收市话费)</small></h2>
                <p>工作时间：9:00 - 18:00</p>
            </span>
        </div>
    </div><!-- hacser end -->
</div><!-- helpindex end -->


<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->
<script>

$(document).ready(function() {
	
});
</script>
</body>
</html>
