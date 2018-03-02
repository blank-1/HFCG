<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../common/common.jsp" %>
<html>
<body>
<div style="padding: 10px">
	<div style="float: left;margin-left: 10px;">
		<span style="font-size: 16px;font-weight: bold;">编辑企业</span>
	</div>
	<div id="main" style="display: block;" data-options="closable:false,collapsible:false,minimizable:false,maximizable:false">

		<div id="detail" class="container-fluid" style="padding: 5px 0px 0px 10px">
			<div id="tt" class="easyui-tabs" style="width:auto;height:650px" >
				
				<div title="基本信息" style="padding:10px;" data-options="href:'${ctx}/jsp/enterprise/to_enterprise_edit_part1?enterpriseId=${enterpriseId }'">1</div>
				<div title="相关附件" style="padding:10px;" data-options="href:'${ctx}/jsp/enterprise/to_enterprise_edit_part2?isCode=1&enterpriseId=${enterpriseId }'">2</div>
				
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
