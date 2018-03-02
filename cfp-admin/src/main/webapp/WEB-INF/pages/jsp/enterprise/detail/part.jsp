<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../../common/common.jsp" %>
<html>
<body>
<div style="padding: 10px">
	<div style="float: left;margin-left: 10px;">
		<span style="font-size: 12px;font-weight: bold;">企业名称：</span>
		<span style="font-size: 12px;">${enterpriseName }</span>&nbsp;&nbsp;
		<span style="font-size: 12px;font-weight: bold;">组织机构代码：</span>
		<span style="font-size: 12px;">${organizationCode }</span>&nbsp;&nbsp;
		<span style="font-size: 12px;font-weight: bold;">法人代表：</span>
		<span style="font-size: 12px;">${legalPersonName }</span>
	</div>
	<input type="hidden" id="d_enterpriseType" value="${enterpriseInfo.enterpriseType}"/>
	<div id="main" style="display: block;" data-options="closable:false,collapsible:false,minimizable:false,maximizable:false">

		<div id="detail" class="container-fluid" style="padding: 5px 0px 0px 10px">
			<div id="tt" class="easyui-tabs" style="width:auto;height:650px" >

				<div title="基本信息" style="padding:10px;" data-options="href:'${ctx}/jsp/enterprise/to_enterprise_detail_baseInfo?enterpriseId=${enterpriseId }'">1</div>
				<div title="额度记录" style="padding:10px;" data-options="href:'${ctx}/jsp/enterprise/toQuotaRecordList?enterpriseId=${enterpriseId }'">2</div>
				<div title="合作公司" style="padding:10px;" data-options="href:'${ctx}/jsp/enterprise/loan/toCoLtdList?enterpriseId=${enterpriseId }'">3</div>
				<div title="人员列表" style="padding:10px;" data-options="href:'${ctx}/jsp/enterprise/toUserList?enterpriseId=${enterpriseId }'">4</div>
				<div title="银行卡" style="padding:10px;" data-options="href:'${ctx}/jsp/custom/customer/showBankCard?userId=${enterpriseInfo.userId}'">5</div>
				<div title="项目列表" style="padding:10px;" data-options="href:'${ctx}/jsp/loanManage/loan/showEnterpriseBorrow?enterpriseId=${enterpriseId}'">6</div>
				<div title="充值记录" style="padding:10px;" data-options="href:'${ctx}/rechargeOrder/showIncome?userId=${enterpriseInfo.userId}'">7</div>
				<div title="提现记录" style="padding:10px;" data-options="href:'${ctx}/withdraw/showWithDraw?userId=${enterpriseInfo.userId}'">8</div>
				<div title="账户流水" style="padding:10px;" data-options="href:'${ctx}/cashFlow/showCashFlow?userId=${enterpriseInfo.userId}'">9</div>

			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

	// 初始化。
	function init(){
		$("#main").panel({
			width: document.body.clientWidth * 0.985,
			height: document.body.clientHeight * 0.9,
			fit:true,
			border: 0
		});
	}

	$(function(){
		init();
	});
</script>
</body>
</html>
