<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="../../../common/common.jsp"%>
<html>
<head>
	<title></title>


</head>
<body>
<div id="customer_cardList" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <input type="hidden" id="mobile_no" value="${userInfoExt.mobileNo}">
	<input type="hidden" id="user_id_from" value="${userInfoExt.userId}">
	<c:if test="${not empty card }">
		<input type="hidden" id="capAcntNo" value="${card.cardCode}">
	</c:if>
	<c:if test="${ empty card }">
		<input type="hidden" id="capAcntNo" value="${card.cardCode}">
	</c:if>
	<input type="hidden" id="cust_nm" value="${userInfoExt.realName}">
	<input type="hidden" id="certif_id" value="${userInfoExt.idCard}">

	<fieldset style="height: auto; margin: 10px">
		<legend>${cardsize}张银行卡</legend>
		<%--<table align="center" border="1" style="border: 1px; margin: 5px; width: 98%">
            <tr>
                <th style="font-size: 13">开户行</th>
                <th style="font-size: 13">开户地址</th>
                <th style="font-size: 13">卡号</th>
                <th style="font-size: 13">状态</th>
            </tr>
            <c:forEach items="${cards}" var="card">
                <tr>
                    <td style="font-size: 12">${card.bankName}</td>
                    <td style="font-size: 12">${card.registeredBank}</td>
                    <td style="font-size: 12">${card.cardCode}</td>
                    <td style="font-size: 12">
                    <c:if test="${card.status eq '1'}">有效</c:if>
                    <c:if test="${card.status eq '0'}">无效</c:if></td>
                </tr>
            </c:forEach>
        </table>--%>
		<div id="customer_card_list_toolbar" style="height:auto">
			<a href="#" class="easyui-linkbutton" id="" plain="true" iconCls="icon-add" onclick="toAddCustomerCard('${userId}','1')">添加银行卡</a>
		</div>

		<table id="customer_card_list"></table>
		<div id="bindingCard"></div>
	</fieldset>
	<%@include file="../../../common/hengfengCard.jsp"%>
</div>

<script type="text/javascript">
    var rootPath = '<%=ctx%>';
    var kefu='';

    $(function(){
        <mis:PermisTag code="030004">
        $("#kefuHidden").hide();
        kefu='kefu';
        </mis:PermisTag >
        loadCardList();
    });

    function reloadCard(){
        $("#customer_card_list").datagrid("reload",{});
    }

    function toAddCustomerCard(userId){
        var verify = '${userInfoExt.isVerified }';
        if(verify!='1'&&'${userInfo.type}'!='6'){
            $.messager.alert('提示', '你未进行身份验证！', 'info');
            return ;
        }
        $("#customer_cardList").after("<div id='toAddCustomerCard' style=' padding:10px;'></div>");
        $("#toAddCustomerCard").dialog({
            resizable: false,
            title: '客户卡管理',
            href: '${ctx}/jsp/custom/customer/toAddCustomerCard?userId=' + userId,
            width: 400,
            modal: true,
            height: 350,
            top: 100,
            left: 500,
            buttons: [
                {
                    text: '提交',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $("#toAddCustomerCard").contents().find("#add_customer_Card_form").submit();
                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#toAddCustomerCard").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }

    function toEditCustomerCard(customCardId){
        var verify = '${userInfoExt.isVerified }';
        if(verify!='1'&&'${userInfo.type}'!='6'){
            $.messager.alert('提示', '你未进行身份验证！', 'info');
            return ;
        }
        $("#customer_cardList").after("<div id='toAddCustomerCard' style=' padding:100px;'></div>");
        $("#toAddCustomerCard").dialog({
            resizable: false,
            title: '客户卡管理',
            href: '${ctx}/jsp/custom/customer/toAddCustomerCard?customCardId='+customCardId,
            width: 900,
            modal: true,
            height: 850,
            top: 100,
            left: 500,
            buttons: [
                {
                    text: '提交',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $("#toAddCustomerCard").contents().find("#add_customer_Card_form").submit();
                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#toAddCustomerCard").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }


    function loadCardList(){
        $("#customer_card_list").datagrid({
            idField: 'bondSourceId',
            title: '银行卡列表',
            url: '${ctx}/jsp/custom/customer/bankCards?userId=${userId}',
            pagination: true,
            pageSize: 10,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#customer_card_list_toolbar',
            columns:[[
            /*    {field:'bankName', width:100,title:'开户行'},*/
                {field:'registeredBank', width:120,title:'开户地址'},
                {field:'cardCode', width:200,title:'卡号'},
            /*    {field:'mobile', width:200,title:'预留手机号'},*/
                {field:'belongChannel', width:200,title:'所属渠道',formatter:channelFormatter},
                {field:'status', width:80,title:'状态',formatter:statusFormatter},
                {field:'bindStatus', width:60,title:'绑卡状态',formatter:bindStatusFormatter},
                {field:'mobile1',title:'操作',width:200,align:'center',
                    formatter: function (value, row, index) {
                        var result = "";

                        if (row.status == '1' && row.bindStatus != '1')
                            result += "<a class='label label-info' id='baddingA' onclick='binding(" + row.mobile + "," + row.customerCardId + ")'>绑卡</a> &nbsp;";

                        result += "<a class='label label-error' onclick='updatingCard(" + row.customerCardId + ")'>修改</a> &nbsp;";

                        return result;
                    }
                }
            ]],
        });
    }

    function binding(mobile,id){
        $('#user_id_from').val(id);

        toBind(id);

    }
    function updatingCard(id){
        $("#customer_cardList").after("<div id='toAddCustomerCard' style=' padding:10px;'></div>");
        $("#toAddCustomerCard").dialog({
            resizable: false,
            title: '客户卡管理',
            href: '${ctx}/jsp/custom/customer/showBankCardforCarId?cardId=' + id,
            width: 400,
            modal: true,
            height: 350,
            top: 100,
            left: 500,
            buttons: [
                {
                    text: '提交',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $("#toAddCustomerCard").contents().find("#add_customer_Card_form").submit();
                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#toAddCustomerCard").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });

    }


    function reloadCard(){
        $('#customer_card_list').datagrid('reload', {});
    }
    function statusFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == '0')
            return "无效";
        if(val == '1')
            return "有效";
    }

    function channelFormatter(val){
        console.log("------------------- "+val);
        if (val == undefined || val == "")
            return "";
        if(val == '0'){
            return "易宝";
		}else if(val == '1'){
            return "连连";
		}else {
            return "恒丰";
		}
    }
    function bindStatusFormatter(val){
        if (val == undefined || val == "")
            return "未绑定";
        if(val == '0')
            return "未绑定";
        if(val == '1')
            return "已绑定";
    }

    // 解除绑定操作
    function unbundling(customerCardId){
        $.messager.confirm("操作提示", "确定为该用户解绑该银行卡？", function (data) {
            if (data) {
                $.post("${ctx}/jsp/custom/customer/unbundling?customerCardId=" + customerCardId,
                    function(data){
                        if(data.result == 'success'){
                            $.messager.alert("操作提示", "操作成功！", "info");
                            $('#customer_card_list').datagrid('reload', {});
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
        });
    }
</script>



</body>
</html>