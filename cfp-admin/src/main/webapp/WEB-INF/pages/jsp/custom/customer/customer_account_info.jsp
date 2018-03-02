<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="../../../common/common.jsp"%>
<html>
<head>
<title></title>
</head>
<body>
	<div class="cf"
		style="float: left; width: 97%; margin: 0 10px 5px 10px;">
		<fieldset style="height: auto; margin: 10px auto">
			<table id="customer_header" align="center" border="0" width="97%">
				<tr>
					<td align="left" style="font-size: 12px;">用户名：${user.loginName}</td>
					<td align="left" style="font-size: 12px;">性别： 
					<c:if test="${user.sex eq '0'}">女</c:if>
					<c:if test="${user.sex eq '1'}">男</c:if>
					</td>
					<td align="left" style="font-size: 12px;">出生日期： 
					<fmt:formatDate value="${user.birthday}" pattern="yyyy年MM月dd日" />
					</td>
					<td align="left" style="font-size: 12px;" colspan="2">身份证号：${user.idCard}</td>
				</tr>
				<tr>
					<td align="left" style="font-size: 12px;">客户姓名：${user.realName}</td>
					<td align="left" style="font-size: 12px;">学历：
						<c:if test="${user.educationLevel == '0' }">小学</c:if>
						<c:if test="${user.educationLevel == '1' }">初中</c:if>
						<c:if test="${user.educationLevel == '2' }">高中</c:if>
						<c:if test="${user.educationLevel == '3' }">专科</c:if>
						<c:if test="${user.educationLevel == '4' }">本科</c:if>
						<c:if test="${user.educationLevel == '5' }">硕士</c:if>
						<c:if test="${user.educationLevel == '6' }">博士</c:if>
						<c:if test="${user.educationLevel == '7' }">博士后</c:if>
					</td>
					<td align="left" style="font-size: 12px;">手机号：${user.mobileNo}</td>
					<td align="left" style="font-size: 12px;" id="status">客户状态：
						<c:if test="${user.status == '0' }">正常</c:if>
						<c:if test="${user.status == '1' }">冻结</c:if>
						<c:if test="${user.status == '2' }">禁用</c:if>
					</td>
					<td align="left" style="font-size: 12px;">
					<mis:PermisTag code="03000205">
					<a href="#" class="easyui-linkbutton" plain="false"
						onclick="toEditCustomer('${user.userId}','1')">冻结</a>
					<a href="#" class="easyui-linkbutton" plain="false"
						onclick="toEditCustomer('${user.userId}','0')">解冻</a>
					</mis:PermisTag>
					</td>
				</tr>
			</table>
		</fieldset>
	</div>
	<div id="detailTabs" style="width: auto; height: auto;">
		<div title="账户总览" style="width: auto; padding: 20px;"></div>
		<div title="借款" style="width: auto; padding: 20px;"></div>
		<div title="出借" style="width: auto; padding: 20px;"></div>
		<div title="账户流水" style="width: auto; padding: 20px;"></div>
		<div title="充值记录" style="width: auto; padding: 20px;"></div>
		<div title="提现记录" style="width: auto; padding: 20px;"></div>
		<div title="银行卡信息" style="width: auto; padding: 20px;"></div>
		<div title="操作记录" style="width: auto; padding: 20px;"></div>
	</div>
	<script type="text/javascript">
		init();
		function init() {
			$('#detailTabs').tabs({
				fit : true,
				onSelect : function(title, index) {
					if (index == 0) {
						showAccount("${user.userId}", title, index);
					} else if (index == 1) {
						showLoan("${user.userId}", title, index);
					} else if (index == 2) {
						showLend("${user.userId}", title, index);
					} else if (index == 3) {
						showCashFlow("${user.userId}", title, index);
					} else if (index == 4) {
						showIncome("${user.userId}", title, index);
					} else if (index == 5) {
						showWithDraw("${user.userId}", title, index);
					} else if (index == 6) {
						showBankCard("${user.userId}", title, index);
					} else if (index == 7) {
						
					}
				}
			});
		}
		//账户总览
		function showAccount(id, title, index) {
			var tabPanel = $('#detailTabs').tabs('getTab', index);
			tabPanel.panel({
				href : '${ctx}/jsp/custom/customer/showAccount?userId=' + id
			});
		}
		//借款
		function showLoan(id, title, index) {
			var tabPanel = $('#detailTabs').tabs('getTab', index);
			tabPanel.panel({
				href : '${ctx}/jsp/custom/customer/showLoan?userId=' + id
			});
		}
		//出借
		function showLend(id, title, index) {
			var tabPanel = $('#detailTabs').tabs('getTab', index);
			tabPanel.panel({
				href : '${ctx}/jsp/custom/customer/showLend?userId=' + id
				//href : 'jsp/rights/all/to_rights_list'
			});
		}

		//充值记录
		function showIncome(id, title, index) {
			var tabPanel = $('#detailTabs').tabs('getTab', index);
			tabPanel.panel({
				href : '${ctx}/rechargeOrder/showIncome?userId=' + id
			});
		}
		
		//提现记录
		function showWithDraw(id, title, index) {
			var tabPanel = $('#detailTabs').tabs('getTab', index);
			tabPanel.panel({
				href : '${ctx}/withdraw/showWithDraw?userId=' + id
			});
		}
		
		//账户流水
		function showCashFlow(id, title, index) {
			var tabPanel = $('#detailTabs').tabs('getTab', index);
			tabPanel.panel({
				href : '${ctx}/cashFlow/showCashFlow?userId=' + id
			});
		}
		
		//银行卡
		function showBankCard(id, title, index) {
			var tabPanel = $('#detailTabs').tabs('getTab', index);
			tabPanel.panel({
				href : '${ctx}/jsp/custom/customer/showBankCard?userId=' + id
			});
		}

		function toEditCustomer(userId, status) {
			var msg = status == '0' ? '是否解冻' : '是否冻结';
			$.messager.confirm("确认", msg, function(r) {
				if (r) {
					$.post('${ctx}/jsp/custom/customer/toEditCustomer?userId='
							+ userId + '&status=' + status, function(date) {
						if (date == "success") {
							$.messager.alert("系统提示", "修改冻结状态成功", "info");
							var text = '客户状态：' + (status == '0' ? '正常' : '冻结');
							$('#status').html(text);
						} else {
							$.messager.alert("系统提示", "修改冻结状态失败", "error");
						}
					});
				}
			});
		}
	</script>
</body>
</html>
