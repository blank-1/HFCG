<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="../../../common/common.jsp"%>
<html>
<head>
<title></title>
</head>
<body>
<c:if test="${not empty errorMsg}">
	错误信息：${errorMsg}
	<script type="text/javascript">
		$("#tj").html("").removeAttr("class");
	</script>
</c:if>
<c:if test="${empty errorMsg}">
	<form  class="form-horizontal" method="post" id="binding_card_form" action="${ctx}/jsp/custom/customer/bindingCard">
		<div class="control-group">
			<label class="control-label">银行卡号：</label>
			<div class="controls">
					${card.cardCode}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">预留手机号：</label>
			<div class="controls">
					${card.mobile}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">手机验证码：</label>
			<div class="controls">
				<input type="text" name="smscode" value="${smscode}"
					   class="easyui-validatebox" required="true" missingMessage="手机验证码不能为空" >
				<input type="hidden" name="customerCardId" value="${card.customerCardId}">
				<input type="hidden" name="requestId" value="${requestId}">
			</div>
		</div>
	</form>
</c:if>
</body>
</html>