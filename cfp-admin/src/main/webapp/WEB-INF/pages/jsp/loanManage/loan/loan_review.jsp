<%@ page import="com.xt.cfp.core.util.PropertiesUtils" %>
<%--
  User: ren yulin
  Date: 15-7-18
  Time: 下午3:04
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<%
    String webServer = "http://localhost/cfp/";
%>
<html>
<head>
    <title></title>
</head>
<body style="padding: 10px">
借款申请标题：${loanTitle}
借款申请ID：${loanApplication.loanApplicationCode}
借款状态：${stateName}
<span style="float: right;margin-right: 100px">
    <input style="width: 100px"  type="button" class="btn btn-primary" value="发标" onclick="toPublish();" >
</span>
<iframe id="review" style="width: 100%;height: 1000px" src="<%=PropertiesUtils.getInstance().get("FRONT_PATH")%>?loanApplicationNo=${loanApplication.mainLoanApplicationId }">

</iframe>
<script language="JavaScript">
    var loanApplicationId = '${loanApplication.mainLoanApplicationId}';
    function toPublish() {
        $("#review").before("<div id='loan_publish' style=' padding:10px; '></div>");
        $("#loan_publish").dialog({
            resizable: false,
            title: '借款申请发标',
            href: '${ctx}/jsp/loanPublish/loan/to_publish?loanApplicationId='+loanApplicationId,
            width: 800,
            modal: true,
            height: 500,
            onClose: function () {
                $(this).dialog('destroy');
            },
            buttons: [
                {
                    text: '发标',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $("#loan_publish").contents().find("#loan_publish_form").submit();
                    }
                },

                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#loan_publish").dialog('close');
                    }
                }
            ]
        });
    }
</script>
</body>
</html>
