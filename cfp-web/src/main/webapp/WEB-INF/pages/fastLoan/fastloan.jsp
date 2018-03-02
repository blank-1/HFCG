<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />

<title>我要借款</title>
<%@include file="../common/common_js.jsp"%>
<link href="${ctx }/css/jiekuan.css" rel="stylesheet" type="text/css">
</head>

<body>
	<%--登陆--%>
<%@include file="../person/authenticationShenfen.jsp"%>
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="5"/>
<!-- navindex end -->
<!-- wrapper start -->
<div class="banner"></div>
<%@include file="../login/login.jsp"%>
<div class="container_jk">
	<ul class="item-ul">
    	<li>
        	<div><img src="${ctx}/images/jiekuan/j1.jpg" alt=""></div>
            <h2>无抵押</h2>
            <p>不需要任何抵押物</p>
        </li>
        <li>
        	<div><img src="${ctx}/images/jiekuan/j2.jpg" alt=""></div>
            <h2>额度高</h2>
            <p>申请额度最高可达50万</p>
        </li>
        <li style="margin-right:0">
        	<div><img src="${ctx}/images/jiekuan/j3.jpg" alt=""></div>
            <h2>到帐快</h2>
            <p>资料齐全，最快1天放款</p>
        </li>
    </ul>
    <ol class="item-ol">
    	<li class="clear">
        	<span>-申请额度：</span>
            <b>3,000</b>
            <cite>元至</cite>
            <b>50</b>
            <cite>万元</cite>
        </li>
        <li class="clear">
        	<span>-还款方式：</span>
            <i>先息后本</i>
        </li>
        <li class="clear">
        	<span>-区域限制：</span>
            <i>开放</i>
            <b>北京、</b>
            <b>上海、</b>
            <b>天津、</b>
            <b>山西</b>
            <i>等城市的借款申请</i>
        </li>
        <li class="clear">
        	<dl class="dl-item clear">
            	<dt>-申请条件：</dt>
                <dd>
                	<p>① 申请人为年龄22-55周岁的有行为能力的中国公民；</p>
                    <p>② 申请人为学校、医院、企事业单位、政府机关、国有企业的任职人员且现单位工作时间不少于3个月</p>
                    <p>③ 申请人的手机号码为本人的实名认证号码；</p>
                    <p>④ 申请人具有稳定的收入、且信用状况良好；</p>
                    <p>⑤ 申请过程中提交的所有资料均为本人真实信息。</p>
                </dd>
            </dl>
        </li>
    </ol>
    <form id="fastLoanForm" action="http://fast.caifupad.com/fastloan/login/cfplogin" method="post" target="_blank" >
            		<input type="hidden" id="fastLoanUserId" name="fastLoanUserId" value="${userId }">
            		<input type="hidden" id="fastLoanIdCard" name="fastLoanIdCard" value="${idCard }">
            		<input type="hidden" id="fastLoanRealName" name="fastLoanRealName" value="${realName }">
            		<input type="hidden" id="fastLoanMobile" name="fastLoanMobile" value="${mobile }">
            	</form>
    <div class="btn1">
    		<c:if test="${not empty sessionScope.currentUser }">
            	<a href="javascript:void(0);"  class="btn-general btn-3d btn-apply btn-apply-enabled" onclick="toFastLoan();">立即申请</a>
            </c:if>
            <c:if test="${empty sessionScope.currentUser }">
            	<a class="btn-general btn-3d btn-apply btn-apply-enabled" data-mask='mask' data-id="login">立即申请</a>
            </c:if>
    </div>
    <div style="clear:both;"></div>
</div>
<div style="clear:both;"></div>
<div class="zhezhao6"></div>
<!-- wrapper end -->
   <%@include file="../common/footLine3.jsp"%>
<script type="text/javascript">
	function toFastLoan(){
   	$.ajax({
			url:rootPath+"/user/to_fastLoan",
			type:"post",
			data:{},
			async : false,
			error:function(XHR){
				var res = eval("(" + XHR.responseText + ")");
				if(res.errorCode == 4){
					window.location.href=rootPath+"/user/to_login";
				}
			},
			success:function(data){
				var _data =  eval("("+data+")");
				if(!_data.isSuccess){
					$(".zhezhao6").show();
					$("#shenfen").show();
				}else{
					$("#fastLoanUserId").val(_data.userId);
					$("#fastLoanIdCard").val(_data.idCard);
					$("#fastLoanRealName").val(_data.realName);
					$("#fastLoanMobile").val(_data.mobile);
					$("#fastLoanForm").submit();
				}
			}
		});
   }
</script>
</body>
</html>
