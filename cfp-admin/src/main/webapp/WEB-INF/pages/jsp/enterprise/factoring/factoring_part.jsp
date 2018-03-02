<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../../common/common.jsp" %>
<html>
<body>
<div style="padding: 10px">
	<div style="float: left;margin-left: 10px;">
		<c:if test="${actionType == 'add'}">
			<span style="font-size: 16px;font-weight: bold;">新增企业保理</span>	
		</c:if>
		<c:if test="${actionType == 'edit'}">
			<span style="font-size: 16px;font-weight: bold;">编辑企业保理</span>
		</c:if>
	</div>
	<div style="float: right;margin-right: 100px" >
		<c:if test="${applicationState == 0 }">
			<input type="button" class="btn btn-primary" value="提交初审" onclick="submitButton();" >
		</c:if>
	</div>
	<div id="main" style="display: block;" data-options="closable:false,collapsible:false,minimizable:false,maximizable:false">

		<div id="detail" class="container-fluid" style="padding: 5px 0px 0px 10px">
			<div id="tt" class="easyui-tabs" style="width:auto;height:650px" >
				
				<c:if test="${actionType == 'add'}">
					<div title="新增保理项目" style="padding:10px;" data-options="href:'${ctx}/jsp/enterprise/loan/to_factoring_add_part1?loanApplicationId=${loanApplicationId }'">1</div>
					<div title="相关附件" style="padding:10px;" data-options="href:'${ctx}/jsp/enterprise/loan/to_factoring_add_part2?loanApplicationId=${loanApplicationId }'">2</div>
				</c:if>
				<c:if test="${actionType == 'edit'}">
					<div title="编辑保理项目" style="padding:10px;" data-options="href:'${ctx}/jsp/enterprise/loan/to_factoring_edit_part1?loanApplicationId=${loanApplicationId }'">1</div>
					<c:if test="${applicationState == 0 }">
						<div title="相关附件" style="padding:10px;" data-options="href:'${ctx}/jsp/enterprise/loan/to_factoring_edit_part2?loanApplicationId=${loanApplicationId }'">2</div>
					</c:if>
				</c:if>
				
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

	//执行：提交。
	function submitButton(){
		
	    $.messager.confirm("系统提示", "确定要提交初审吗？", function (r) {
	        if (r) {
	        	
	        	$.post('${ctx}/jsp/enterprise/loan/save_loan_submit',
	        		{
	        			loanApplicationId:${loanApplicationId},
	        			loanType:4
	        		},
	        		function(data){
	        	    	if(data.result == 'success'){
	        	    		$.messager.alert("操作提示", "提交成功！", "info",function(){
	                            window.close();
	                            // 刷新主页面列表
	                            var params = window.opener.$('#loan_app_list').datagrid("options").queryParams;
	                            window.opener.$('#loan_app_list').datagrid("options").queryParams = params;
	                            window.opener.$('#loan_app_list').datagrid('reload');
	                        });
	        	    	}else if(data.result == 'error'){
	        	    		if(data.errCode == 'check'){
	        	    			$.messager.alert("验证提示", data.errMsg, "info");
	        	    		}else{
	        	    			$.messager.alert("系统提示", data.errMsg, "warning");
	        	    		}
	        	    	}else{
	        	    		$.messager.alert("系统提示", "网络异常，请稍后操作！", "error");
	        	    	}
	        	 },'json');
	        	
	        }
	    })
	}

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
