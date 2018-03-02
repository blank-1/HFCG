<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.xt.cfp.core.pojo.LoanApplication" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../../../common/common.jsp" %>

<html>
<body>
	<div id="loan_app_publish_detail" class="container-fluid" style="padding: 10px 0px 0px 10px; width: auto;">
		<span style="font-size: 16px;">
			标的名称:${loanApplication.loanApplicationName }&nbsp;&nbsp;
			标的编号:${loanApplication.loanApplicationCode }&nbsp;&nbsp;
			标的状态: <c:if test="${loanApplication.applicationState eq '0' }">草稿</c:if>
					<c:if test="${loanApplication.applicationState eq '1' }">风控审核中</c:if>
					<c:if test="${loanApplication.applicationState eq '2' }">发标审核中</c:if>
					<c:if test="${loanApplication.applicationState eq '3' }">投标中</c:if>
					<c:if test="${loanApplication.applicationState eq '4' }">放款审核中</c:if>
					<c:if test="${loanApplication.applicationState eq '5' }">待放款</c:if>
					<c:if test="${loanApplication.applicationState eq '6' }">还款中</c:if>
					<c:if test="${loanApplication.applicationState eq '7' }">已结清</c:if>
					<c:if test="${loanApplication.applicationState eq '8' }">提前还贷</c:if>
					<c:if test="${loanApplication.applicationState eq '9' }">取消</c:if>
					<c:if test="${loanApplication.applicationState eq '10' }">流标</c:if></span>
		<span style="float: right;margin-right: 100px">
		    <input style="width: 100px"  type="button" class="btn btn-primary" value="编辑发标信息" onclick="loan_publish_tab();" >
		</span>
	</div>
	<iframe style="width: 100%;height: 1000px" src="${frontURL }?loanApplicationNo=${loanApplication.mainLoanApplicationId }" id="iframepage" ></iframe>

	<script type="text/javascript">
	function loan_publish_tab() {
        $("#loan_app_publish_detail").after("<div id='loan_publish_edit_tab' style=' padding:10px; '></div>");
        $("#loan_publish_edit_tab").dialog({
            resizable: false,
            title: '借款标的发标信息编辑',
            href: '${ctx}/jsp/loanPublish/loan/to_loan_publish_tab?loanApplicationId=${loanApplication.mainLoanApplicationId }',
            width: 800,
            modal: true,
            height: 600,
            buttons: [
                {
                    text: '提交',
                    iconCls: 'icon-ok',
                    handler: function () {
                        try {
                        	$.messager.confirm('借款标的发标信息提交','您是否要提交发标信息?',function(r){
                        		if(r){
                        		 	$("#loan_publish_edit_tab").contents().find("#loan_publish_add_form").submit();
                        		}
                        	});

                        } catch (e) {
                            alert(e);
                        }

                    }
                },

                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#loan_publish_edit_tab").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });


	}

	</script>
</body>
</html>
