<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
%>
<div id="detailTabs" style="width: auto; height: auto;">
	<div title="发标信息编辑 " style="width: auto; padding: 20px;"
         data-options="href:'${ctx}/jsp/loanPublish/loan/to_loan_publish_add?loanApplicationId='+'${loanApplicationId}',cache:true"></div>
         
         <c:if test="${loanType==0 || loanType==1 || loanType==7}">
         	<div title="附件信息编辑" style="width: auto; padding: 20px;"
         		data-options="href:'${ctx}/jsp/loanPublish/loan/to_loan_app_attachment?loanApplicationId='+'${loanApplicationId}',cache:true"></div>
         </c:if>
         
         <c:if test="${loanType==2 }">
         	<div title="企业车贷附件信息编辑" style="width: auto; padding: 20px;"
         		data-options="href:'${ctx}/jsp/loanPublish/loan/to_enterprise_loan_app_attachment?loanType=2&loanApplicationId='+'${loanApplicationId}',cache:true"></div>
         </c:if>
         
         <c:if test="${loanType==3 }">
         	<div title="企业信贷附件信息编辑" style="width: auto; padding: 20px;"
         		data-options="href:'${ctx}/jsp/loanPublish/loan/to_enterprise_loan_app_attachment?loanType=3&loanApplicationId='+'${loanApplicationId}',cache:true"></div>
         </c:if>
         
         <c:if test="${loanType==4 }">
         	<div title="企业保理附件信息编辑" style="width: auto; padding: 20px;"
         		data-options="href:'${ctx}/jsp/loanPublish/loan/to_enterprise_loan_app_attachment?loanType=4&loanApplicationId='+'${loanApplicationId}',cache:true"></div>
         </c:if>
		<c:if test="${loanType==5 }">
         	<div title="企业基金附件信息编辑" style="width: auto; padding: 20px;"
         		data-options="href:'${ctx}/jsp/loanPublish/loan/to_enterprise_loan_app_attachment?loanType=5&loanApplicationId='+'${loanApplicationId}',cache:true"></div>
         </c:if>
          <c:if test="${loanType==6 }">
         	<div title="企业贷附件信息编辑" style="width: auto; padding: 20px;"
         		data-options="href:'${ctx}/jsp/loanPublish/loan/to_enterprise_loan_app_attachment?loanType=6&loanApplicationId='+'${loanApplicationId}',cache:true"></div>
         </c:if>
			<c:if test="${loanType==8 }">
				<div title="附件信息编辑" style="width: auto; padding: 20px;"
					 data-options="href:'${ctx}/jsp/loanPublish/loan/to_loan_app_attachment?isCredit=1&loanApplicationId='+'${loanApplicationId}',cache:true"></div>
			</c:if>
			<c:if test="${loanType==9}">
				<div title="附件信息编辑" style="width: auto; padding: 20px;"
					 data-options="href:'${ctx}/jsp/loanPublish/loan/to_loan_app_attachment?isCredit=2&loanApplicationId='+'${loanApplicationId}',cache:true"></div>
			</c:if>
</div>

<script type="text/javascript">

init();
function init() {
	$('#detailTabs').tabs({
		fit : true,
        cache:true,
		onSelect : function(title, index) {

		}
	});
	

}
</script>