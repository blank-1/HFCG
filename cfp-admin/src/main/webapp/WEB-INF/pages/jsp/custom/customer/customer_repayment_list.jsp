<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<%@ page import="com.xt.cfp.core.constants.PaymentMethodEnum" %>
<%@ page import="com.xt.cfp.core.constants.PaymentMode" %>
<head>
</head>
<body>

<div class="easyui-layout" style="width:100%;height:90%;">
	<%@ include file="customer_loan_header.jsp" %>
	<div id="content" region="center" title="查询结果" >
		<div style="padding: 10px 20px 20px 20px;">
		    <fieldset style="height:80px">
		        <legend>查询条件</legend>
		        <form name="repaymentQuery" id="" action="" class="fitem" autocomplete="off">
		            <table>
		                <tbody>
		                <tr>
		                	<td nowrap="nowrap"><label>支付方式：</label>
		                    </td>
		                    <td>
		                        <select class="easyui-combobox" id="channelType" style="width:150px" name="channelType" >
		                            <option value='-1'>全部</option>
				                	<option value='<%=PaymentMode.DEDUCTION.getValue()%>'>划扣</option>
				                	<option value='<%=PaymentMode.TRANSFER.getValue()%>'>转账</option>
				                	<option value='<%=PaymentMode.CASH.getValue()%>'>现金</option>
		                        </select>
		                    </td>
		                </tr>
		                <tr>
		                 	<td nowrap="nowrap"><label>还款时间：</label>
		                    </td>
		                    <td>
		                        <input id="startDate" name="startDate" style="width:100px;" class="easyui-datebox"/>至<input id="endDate" style="width:100px;" name="endDate" class="easyui-datebox"/>
		                        <a onclick="selDate(0)" style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">今天</a>
		                        <a onclick="selDate(1)" style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">近7天</a>
		                        <a onclick="selDate(2)" style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">近3个月</a>
		                        <a onclick="selDate(3)" style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">近6个月</a>
		                        <a onclick="selDate(4)" style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">近12个月</a>
		                    </td>
		                    <td nowrap="nowrap">
		                        <div style="margin:10px;">
		                            <a href="javascript:void(0);" class="easyui-linkbutton"
		                               onclick="javascript:toQueryList();" iconCls="icon-search">查询</a>
		                        </div>
		                    </td>
		                </tr>
		                </tbody>
		            </table>
		        </form>
		    </fieldset>
		</div>
		<div id="loan" class="container-fluid" style="width: 1100px;">
		    <table id="customer_repayment_list"></table>
		</div>
	</div>
</div>


<script language="javascript">
	
	function selDate(val){
		Utils.selectDate(val,'startDate','endDate',toQueryList); 
	}
	
	 /**
     * 执行：列表加载。
     */
    function loadRepaymentList(){
        $("#customer_repayment_list").datagrid({
            title: '充值列表',
            url: '${ctx}/jsp/custom/customer/getRepaymentList',
            pagination: true,
            pageSize: 10,
            queryParams: {userId:'${userId }'},
            singleSelect: true,
            rownumbers: true,
            columns:[[
                {field:'opertionDate', width:150,title:'还款时间',formatter:dateTimeFormatter },
                {field:'payId', width:150,title:'支付单号'},
                {field:'factBalance', width:100,title:'本次还款金额'},
                {field:'noRepaymentPercent', width:100,title:'待还款占比',formatter:noRepaymentPercentFormatter},
                {field:'repaymentMethod', width:100,title:'还款方式' ,formatter:repaymentMethodFormatter},
                {field:'channelType', width:100,title:'支付方式' ,formatter:typeFormatter},
                {field:'applicationState', width:100,title:'操作',formatter:formatterHandle}
            ]]
        });
    }
	
    function repaymentMethodFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == <%=PaymentMethodEnum.AVERAGE_CAPITAL_PLUS_INTEREST.getValue()%>)
            return "等额本息";
        if(val == <%=PaymentMethodEnum.AVERAGE_CAPITAL_PLUS_INTEREST.getValue()%>)
            return "等额本金";
    }
    function typeFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == <%=PaymentMode.DEDUCTION.getValue()%>)
            return "划扣";
        if(val == <%=PaymentMode.TRANSFER.getValue()%>)
            return "转账";
        if(val == <%=PaymentMode.CASH.getValue()%>)
            return "现金";
    }
    function noRepaymentPercentFormatter(val){
        if (val == undefined || val == ""){        	
            return "";
        }else{        	
            return val + '%';
        }
    }
    function formatterHandle(value, row, index) {
    	return '<a class="label label-info" onclick="toView(' + index + ')">详情</a>';
    }
    
    function toView(index) {
        $("#customer_repayment_list").datagrid("selectRow", index);
        var selRow = $("#customer_repayment_list").datagrid("getSelected");
        $("#customer_repayment_list").after("<div id='toRepaymentDetail' style=' padding:10px;'></div>");
        if (selRow) {
            $("#toRepaymentDetail").dialog({
	            resizable: false,
	            href: '${ctx}/jsp/custom/customer/toRepaymentDetail?loanApplicationId=' + selRow.loanApplicationId,
	            width: 1000,
	            modal: true,
	            height: 500,
	            onClose: function () {
	                $(this).dialog('destroy');
	            }
	        });
        } else {
            $.messager.alert("系统提示", "请选择要查看的客户信息!", "info");
        }
    }
    
  //格式化时间
    function dateTimeFormatter(val) {

        if (val == undefined || val == "")
            return "";
        var date;
        if(val instanceof Date){
            date = val;
        }else{
            date = new Date(val);
        }
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();

        var h = date.getHours();
        var mm = date.getMinutes();
        var s = date.getSeconds();

        var dateStr = y + '-' + (m < 10 ? ('0' + m) : m) + '-'
                + (d < 10 ? ('0' + d) : d);
        var TimeStr = h + ':' + (mm < 10 ? ('0' + mm) : mm) + ':'
                + (s < 10 ? ('0' + s) : s);

        return dateStr + ' ' + TimeStr;
    }
	
    function toQueryList(){
        $('#customer_repayment_list').datagrid('reload', {
            'startDate' : Trim($('#startDate').datebox('getValue'),"g"),
            'endDate' : Trim($('#endDate').datebox('getValue'),"g"),
            'channelType':Trim($("#channelType").combobox("getValue"),"g"),
            'userId':'${userId }'
        });
    }
    
    function showcontent(url,userId){
    	var tabPanel = $('#detailTabs').tabs('getSelected');   
		tabPanel.panel({
			href : url
		});
    }
    
    $(function(){
        loadRepaymentList();
    });
</script>

</body>