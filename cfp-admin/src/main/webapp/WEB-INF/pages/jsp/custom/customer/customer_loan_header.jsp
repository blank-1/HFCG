<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div region="west" split="true" title="菜单" style="width: 150px;">
	<ul>
		<li><a href="javascript:void(0)"
			onclick="showcontent('${ctx}/jsp/custom/customer/showLoan?userId=${userId }')">借款明细</a></li>
		<li><a href="javascript:void(0)"
			onclick="showcontent('${ctx}/jsp/custom/customer/showRepayment?userId=${userId }')">还款明细</a></li>
	</ul>
</div>
