<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="../../../common/common.jsp"%>
<html>
<head>
<title></title>
</head>
<body>

	<table style="border: 1px dashed #000; width: 98%; height: 10%; margin: 20px">
		<tr>
			<th style="font-size: 14">账户总资产：${sumMoney }</th>
			<th style="font-size: 14">累计出借金额：${buyBalance }</th>
			<th style="font-size: 14">累计收益：${profit }</th>
			<th style="font-size: 14">累计借款：${loanBalance }</th>
		</tr>
	</table>

	<fieldset style="height: auto; margin: 10px">
		<legend>资产</legend>
		<table align="center" style="border: 1px dashed #000; margin: 10px; width: 98%; height: 30%" >
			<tr>
				<th style="font-size: 14; text-align: center" width="50">可用金额</th>
				<th style="font-size: 14; text-align: center" width="50">冻结金额</th>
				<th style="font-size: 14; text-align: center" width="50">财富券</th>
			</tr>
			<tr>
				<td align="center">${userAccount.availValue2 }</td>
				<td align="center">${userAccount.frozeValue2 }</td>
				<td align="center">${voucherValue}</td>
			</tr>
			<tr>
				<th style="font-size: 14; text-align: center" width="50">待回本金</th>
				<th style="font-size: 14; text-align: center" width="50">待回利息</th>
				<th style="font-size: 14; text-align: center" width="50">持有省心计划</th>
			</tr>
			<tr>
				<td align="center">${capitalRecive }</td>
				<td align="center">${interestRecive }</td>
				<td align="center">${totalHoldFinancePlan }</td>
			</tr>
		</table>
	</fieldset>

	<fieldset style="height: auto; margin: 10px">
		<legend>负债</legend>
		<table align="center" style="border: 1px dashed #000; margin: 10px; width: 98%; height: 15%" >
			<tr>
				<th style="font-size: 14; text-align: center" width="50">待还本金</th>
				<th style="font-size: 14; text-align: center" width="50">待还利息</th>
				<th style="font-size: 14; text-align: center" width="50">待交费用</th>
				<th style="font-size: 14; text-align: center" width="50">待交罚息</th>
			</tr>
			<tr>
				<td align="center">${replaymentCapital }</td>
				<td align="center">${replaymentInterest }</td>
				<td align="center">${loanFeeNopaied }</td>
				<td align="center">${interestPaid }</td>
			</tr>
		</table>
	</fieldset>
</body>
</html>