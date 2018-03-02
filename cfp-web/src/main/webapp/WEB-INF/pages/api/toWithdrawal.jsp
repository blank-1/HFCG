<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%@include file="/WEB-INF/pages/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<!-- <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">  -->
	<title>开户 - 财富派</title>
	<%@include file="/WEB-INF/pages/common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx }/js/jquery.min.js"></script>
</head>

<body onload="FM.submit()">
<div>
    <form id="FM" action="${params.actionUrl}" method="post">
        <input type="hidden" name="mchnt_cd" value="${params.mchnt_cd}"/>
        <input type="hidden" name="mchnt_txn_ssn" value="${params.mchnt_txn_ssn}"/>
        <input type="hidden" name="login_id" value="${params.login_id}"/>
        <input type="hidden" name="amt" value="${params.amt}"/>

        <input type="hidden" name="page_notify_url" value="${params.page_notify_url}"/>
        <input type="hidden" name="back_notify_url" value="${params.back_notify_url}"/>
        <input type="hidden" name="signature" value="${params.signature}"/>
    </form>
</div>
<div style="clear:both;"></div>
<div class="section">
	<%@include file="/WEB-INF/pages/common/footLine1.jsp"%>
</div>
<script type="text/javascript">

</script>
</body>
</html>
