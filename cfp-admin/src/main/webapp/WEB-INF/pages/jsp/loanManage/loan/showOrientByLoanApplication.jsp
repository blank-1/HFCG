<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="../../../common/common.jsp"%>
<html>
<head>
<title></title>
</head>
<body>
	<!--  -->

	<!--  -->
	<div></div>


	<input id="" type="hidden" name="loanApplicationId"
		value="${loanApplication.loanApplicationId}"></input>
	<div class="control-group">
		<c:if test="${not empty loanOrientation1}">
			       	 此标的为 定向密码:<input id="radioInputP" style="line-height: 10px;"
				readonly="readonly" type="text" value="${loanOrientation1.oPassVo}"></input>
			</br>
		</c:if>
		<c:if test="${not empty loanOrientation2}">
			此标的为 定向用户 
			<div id="dd"
				style="width: 250px; overflow: auto; height: 380px; background: #fff; border: 1px red solid; z-index: 99; bottom: 20px; right: 306px; width: 281px">


				<table border='1' cellspacing="0" cellpadding="0"
					style="width: 280px">
					<tr>
						<th>用户名</th>
						<th>姓名</th>
						<th>手机号</th>
					</tr>

					<c:forEach items="${loanOrientation2}" var="list">
						<tr>
							<td>${list.userName}</td>
							<td>${list.logName}</td>
							<td>${list.phone}</td>
						</tr>
					</c:forEach>

				</table>
			</div>
		</c:if>
		<c:if test="${empty loanOrientation2 && empty loanOrientation1}">
					此标的为定向 所有用户 
				  
				</c:if>
	</div>

</body>
</html>