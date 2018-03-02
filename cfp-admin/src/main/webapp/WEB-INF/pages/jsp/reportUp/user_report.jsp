<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="../../common/common.jsp"%>
<head>
</head>
<body>
	<c:if test="${!empty result.message}">
		<div style="margin-left: 100px">${result.message}</div>
	</c:if>
	<c:if test="${!empty result.text}">
		<form method="post" action="" id="formTxt" class="form-inline" style="margin-left: 100px">
			<textarea name="text" id="txt" style="width:80%;height: 400px;">${result.text}</textarea>
			<br><br>
			<button type="button" onclick="reportUp()" style="margin-left: 100px">开始补报</button>
		</form>
	</c:if>

	<script type="text/javascript">
		function reportUp() {
			var text = encodeURIComponent($("#txt").val());
			$.ajax({
				method : "POST",
				data : {
					"text" : text
				},
				url : "${ctx}/manualReport/userReportUp",
				dataType : "json",
				success : function(res) {
					$("#formTxt").parent().append('<div style="margin-left: 100px">'+res.message+'</div>');
					$("#formTxt").hide();
				}
			});
		}
	</script>
</body>

