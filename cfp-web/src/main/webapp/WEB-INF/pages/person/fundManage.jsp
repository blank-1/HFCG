<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" /> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	
	<title>账户中心-资金管理-资金流水</title>
	<%@include file="../common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx }/js/jquery_page.js"></script><!-- jquery_page javascript 分页 -->
	<script type="text/javascript" src="${ctx }/js/fund_zj.js"></script><!-- jquery_page javascript 分页 -->
</head>

<body  class="body-back-gray">
	<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->
 <input type="hidden" id="titleTab" value="2-0" />
 
 <!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">资金管理</a>></li>
        <li><span>资金流水</span></li>
    </ul>
</div>
<!-- person-link end -->

<!-- container start -->
<div class="container clearFloat">
     <!-- pLeft start -->
    <div class="pLeft clearFloat">
    </div>
    <!-- pLeft end -->

    <!-- pRight start -->
    <input type="hidden" id="accId" value="${cashAccount.accId }"/>
    <div class="pRight clearFloat">
        <div class="p-Right-top">
			<p class="zjls-title">资金流水</p>
			<div class="zjls-title-box">
				<div class="zjls-box">
					<p><img src="${ctx }/images/news_icon/zjls-box1.png" /></p>
					<p><span class="zjls-word">账户余额(元)</span></p>
					<p><span class="zjls-num"><font><fmt:formatNumber value="${cashAccount.value2 }" pattern="#,##0.00"/></font></p>
				</div>
				<div class="zjls-box">
					<p><img src="${ctx }/images/news_icon/zjls-box2.png" /></p>
					<p><span class="zjls-word">可用金额(元)</span></p>
					<p><span class="zjls-num"><font><fmt:formatNumber value="${cashAccount.availValue2 }" pattern="#,##0.00"/></font></span></p>
				</div>
				<div class="zjls-box">
					<p><img src="${ctx }/images/news_icon/zjls-box3.png" /></p>
					<p><span class="zjls-word">冻结金额(元)</span></p>
					<p><span class="zjls-num"><font><fmt:formatNumber value="${cashAccount.frozeValue2 }" pattern="#,##0.00"/></font></span></p>
				</div>
				<div class="zjls-box">
					<p><img src="${ctx }/images/news_icon/zjls-box4.png" /></p>
					<p><span class="zjls-word">财富券(元)</span></p>
					<p><span class="zjls-num"><font><fmt:formatNumber value="${voucherValue }" pattern="#,##0.00"/></font></span></p>
				</div>
				<div class="clear_0"></div>
			</div>
			<div class="clear_0"></div>
			<div class="pre_zt">
				<div class="th-sorts clearFloat">
					<span class="a-head">流水类型</span>
					<a class="a-head zjlstype" href="javascript:;">全部</a>
					<a href="javascript:;" value="1" class="zjlstype">收入</a>
					<a href="javascript:;" value="2"  class="zjlstype">支出</a>
					<a href="javascript:;" value="4" class="zjlstype">冻结</a>
					<a href="javascript:;" value="5" class="zjlstype">解冻</a>
				</div>
				<div class="th-sorts clearFloat">
					<span class="a-head">查询日期</span>
					<a class="a-head zjlstime" href="javascript:;">全部</a>
					<a href="javascript:;" value="t_7" class="zjlstime">近7天</a>
					<a href="javascript:;" value="t_1" class="zjlstime">近1月</a>
					<a href="javascript:;" value="t_6" class="zjlstime">近半年</a>
				</div>
			</div>
		</div>
        <div class="pre_zqlist mt-5">
				<div id="zjlist">
				</div>
		</div>
		<div class="tcdPageCode mt-20"></div>
    </div>
    <!-- pRight start -->
</div>
<!-- container end -->

	
<div class="zhezhao"></div>
<div class="zhezhao1"></div>
<!-- masklayer start  -->
<div class="masklayer" id="login">
	<div class="logind">
		<h2><span>登录财富派</span> <a href="javascript:" class="logina" data-id="close"></a></h2>
             <!-- equity start -->
            <div class="login">
                <form action="" method="post" name="form" class="">
                    <div class="input-group">
                        <label for="username">
                            <input type="text" value="" id="unlogin" maxlength="20" name="username" placeholder="用户名/手机号" class="ipt-input" />
                        </label>
                        <em class="hui"></em>
                    </div>
                    <div class="input-group clearFloat">
                        <label for="password">
                            <input type="password" value="" id="pwlogin" maxlength="16" name="password" placeholder="密码" class="ipt-input" />
                        </label>
                        <em class="passwordem floatLeft"></em><a class="write floatRight passworda mr-10"  href="re_password1.html">忘记密码？</a>
                    </div>
                    <div class="btn-group">
                        <button type="button" id="submit-login" class="btn btn-error mt-0">登录</button>
                        <a class="write floatRight passworda mr-10" href="register.html">账号</a>
                    </div>
                </form>
                
            </div><!-- equity start -->
	</div>
</div><!-- masklayer end -->
<div style="height:50px;clear:both;"></div>

<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->
<script>
	//分页
   
</script>
</body>
</html>