<%@ page  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title>密码重置 - 财富派</title>
</head>

<body onload="FM.submit()">
<div>
    <form id="FM" action="${params.actionUrl}" method="post">
        <input type="hidden" name="mchnt_cd" value="${params.mchnt_cd}"/>
        <input type="hidden" name="mchnt_txn_ssn" value="${params.mchnt_txn_ssn}"/>
        <input type="hidden" name="login_id" value="${params.login_id}"/>
        <input type="hidden" name="back_url" value="${params.back_url}"/>
        <input type="hidden" name="busi_tp" value="${params.busi_tp}"/>
        <input type="hidden" name="signature" value="${params.signature}"/>
    </form>
</div>
</body>
</html>
