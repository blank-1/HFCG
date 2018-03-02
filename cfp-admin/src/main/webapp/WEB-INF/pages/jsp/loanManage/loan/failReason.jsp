<%--
  User: Ren yulin
  Date: 15-7-25 下午2:31
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
%>
<form class="form-horizontal" id="dropLoanForm" method="post"  >
<div style="height:auto;width:100%;">
    <input type="hidden" id="loanApplicationId" name="loanApplicationId" value="${loanApplicationId}">
    <div class="control-group">
    <div class="control-group">
        <label class="control-label">原因：</label>

        <div class="controls">
            <textarea rows="6" id="logDesc" name="logDesc" cols="60" style="width: 400px;height: 150px"   required="true"  ></textarea>
        </div>
    </div>
</div>
</form>

<script language="JavaScript">

    /* $("#dropLoanForm").form({
        url: '${ctx}/jsp/loanManage/loan/drop_loan',
        onSubmit:function(data) {
            console.info($("#repayment_form").serialize());
            var result = $(this).form('validate');

            if(result){
            	
            }
            Utils.loading();
            if(!result){
                Utils.loaded();
            }

            return result;
        },
        success: function (data) {
            Utils.loaded();
            console.info("data:" + data);
            alert(333);
	   	    	if(data.result == 'success'){
	   	    		$.messager.alert("操作提示", "操作成功！", "info");
	   	    	}else if(data.result == 'error'){
	   	    		if(data.errCode == 'check'){
	   	    			$.messager.alert("验证提示", data.errMsg, "info");
	   	    		}else{
	   	    			$.messager.alert("系统提示", data.errMsg, "info");
	   	    		}
	   	    	}else{
	   	    		$.messager.alert("系统提示", "网络异常，请稍后操作！", "info");
	   	    	}
        }
    }) */
    
    function submitFailForm(){
    	$.ajax({
    		url:'${ctx}/jsp/loanManage/loan/drop_loan',
    		type:"post",
    		data:$("#dropLoanForm").serialize(),
    		dataType:'json',
    		async:false,
    		success:function(data){
                if(data.result == 'success'){
                    $.messager.alert("操作提示", "操作成功！", "info");
                }else if(data.result == 'error'){
                    if(data.errCode == 'check'){
                        $.messager.alert("验证提示", data.errMsg, "warning");
                    }else{
                        $.messager.alert("系统提示", data.errMsg, "warning");
                    }
                }else{
                    $.messager.alert("系统提示", "网络异常，请稍后操作！", "error");
                }
                
                $("#failLoanDialog").dialog('destroy');
                doSearch();
         }
    	});
    	
    }
    
</script>
