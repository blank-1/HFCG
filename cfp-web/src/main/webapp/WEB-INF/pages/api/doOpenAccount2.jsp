<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/pages/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>开户 - 财富派</title>
	<%@include file="/WEB-INF/pages/common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx }/js/jquery.min.js"></script>
</head>

<body onload="FM.submit()">
<div>
    <form id="FM" action="${params.actionUrl}" method="post">
        <input type="hidden" name="ver" value="${params.ver}"/>
        <input type="hidden" name="mchnt_cd" value="${params.mchnt_cd}"/>
        <input type="hidden" name="mchnt_txn_ssn" value="${params.mchnt_txn_ssn}"/>
        <input type="hidden" name="user_id_from" value="${params.user_id_from}"/>
        <input type="hidden" name="cust_nm" value="${params.cust_nm}"/>
        <input type="hidden" name="artif_nm" value="${params.artif_nm}"/>
        <input type="hidden" name="mobile_no" value="${params.mobile_no}"/>
        <input type="hidden" name="certif_id" value="${params.certif_id}"/>

        <input type="hidden" name="email" value="${params.email}"/>
        <input type="hidden" name="city_id" value="${params.city_id}"/>
        <input type="hidden" name="parent_bank_id" value="${params.parent_bank_id}"/>
        <input type="hidden" name="bank_nm" value="${params.bank_nm}"/>
        <input type="hidden" name="capAcntNo" value="${params.capAcntNo}"/>

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
