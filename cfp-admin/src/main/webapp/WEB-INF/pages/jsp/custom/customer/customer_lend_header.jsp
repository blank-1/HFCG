<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div region="west" split="true" title="菜单" style="width: 150px;">
	<ul>
		<li><a href="javascript:void(0)"
			onclick="showcontent('${ctx}/jsp/custom/customer/showLend?userId=${userId }')">出借明细</a></li>
		<li><a href="javascript:void(0)"
			onclick="showcontent('${ctx}/jsp/rights/all/to_rights_list?userId=${userId }')">债权转让明细</a></li>
		<li><a href="javascript:void(0)"
			onclick="showcontent('${ctx}/jsp/custom/customer/toSxjhLendList?userId=${userId }')">省心计划明细</a></li>
	</ul>
</div>
