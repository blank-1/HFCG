<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<%@ page import="com.xt.cfp.core.constants.LoanApplicationStateEnum" %>
<head>
</head>
<body>

<div class="easyui-layout" style="width:100%;height:90%;">
	<%@ include file="customer_loan_header.jsp" %>
	<div id="content" region="center" title="查询结果" >
		<div style="padding: 10px 20px 20px 20px;">
		    <fieldset style="height:80px">
		        <legend>查询条件</legend>
		        <form name="bondSourceUserQuery" id="" action="" class="fitem" autocomplete="off">
		            <table>
		                <tbody>
		                <tr>
		                	<td nowrap="nowrap"><label>支付方式：</label>
		                    </td>
		                    <td>
		                        <select class="easyui-combobox" id="applicationState" style="width:150px" name="applicationState" >
		                            <option value='-1'>全部</option>
				                	<option value='<%=LoanApplicationStateEnum.DRAFT.getValue()%>'>草稿</option>
				                	<option value='<%=LoanApplicationStateEnum.AUDITING.getValue()%>'>风控审核中</option>
				                	<option value='<%=LoanApplicationStateEnum.PUBLISHAUDITING.getValue()%>'>发标审核中</option>
				                	<option value='<%=LoanApplicationStateEnum.BIDING.getValue()%>'>投标中</option>
				                	<option value='<%=LoanApplicationStateEnum.LOANAUDIT.getValue()%>'>放款审核中</option>
				                	<option value='<%=LoanApplicationStateEnum.WAITMAKELOANAUDIT.getValue()%>'>待放款</option>
				                	<option value='<%=LoanApplicationStateEnum.REPAYMENTING.getValue()%>'>还款中</option>
				                	<option value='<%=LoanApplicationStateEnum.COMPLETED.getValue()%>'>已结清</option>
				                	<option value='<%=LoanApplicationStateEnum.EARLYCOMPLETE.getValue()%>'>提前还贷</option>
				                	<option value='<%=LoanApplicationStateEnum.DELETED.getValue()%>'>取消</option>
				                	<option value='<%=LoanApplicationStateEnum.FAILURE.getValue()%>'>流标</option>
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
		    <table id="loan_list"></table>
		</div>
	</div>
</div>


<script language="javascript">
	
	function selDate(val){
		Utils.selectDate(val,'startDate','endDate',toQueryList); 
	}
	
	function toQueryCrashFlowList(){
	    var boxes = $('input[type="checkbox"]');
	    var changeType="";
	    for(var i=0;i<boxes.length;i++){
	        if(boxes[i].checked)
	            changeType +=boxes[i].value+",";
	    }
	    if(changeType.indexOf(0)!=-1){
	        changeType='0';
	    }
	    $('#crash_flow_list${userId}').datagrid('reload', {
	        'startDate' : Trim($('#startDate').datebox('getValue'),"g"),
	        'endDate' : Trim($('#endDate').datebox('getValue'),"g"),
	        'changeType' : Trim(changeType,"g"),
	        'userId':'${userId}'
	    });
	}
	
	 /**
     * 执行：列表加载。
     */
    function loadLoanList(){
        $("#loan_list").datagrid({
            title: '充值列表',
            url: '${ctx}/jsp/custom/customer/getLoanList',
            pagination: true,
            pageSize: 10,
            queryParams: {userId:'${userId }'},
            singleSelect: true,
            rownumbers: true,
            columns:[[
                {field:'loanApplicationCode', width:100,title:'借款ID' },
                {field:'loanType', width:100,title:'借款类型' ,formatter:loanTypeFormatter},
                {field:'loanApplicationName', width:250,title:'借款标题'},
                {field:'loanBalance', width:80,title:'申请金额'},
                {field:'confirmBalance', width:80,title:'审批金额'},
                {field:'1231', width:80,title:'申请期限'},
                {field:'123', width:80,title:'审批期限'},
                {field:'annualRate', width:80,title:'利率'},
                {field:'createTime', width:150,title:'申请时间',formatter:dateTimeFormatter},
                {field:'applicationState', width:80,title:'当前状态',formatter:appStateFormatter}
            ]]
        });
    }
	
    function loanTypeFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == '0')
            return "信贷";
        if(val == '1')
            return "放贷";
        if(val == '7')
            return "房产直投";
    }
    
    function appStateFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == <%=LoanApplicationStateEnum.DRAFT.getValue()%>)
            return "草稿";
        if(val == <%=LoanApplicationStateEnum.AUDITING.getValue()%>)
            return "风控审核中";
        if(val == <%=LoanApplicationStateEnum.PUBLISHAUDITING.getValue()%>)
            return "发标审核中";
        if(val == <%=LoanApplicationStateEnum.BIDING.getValue()%>)
            return "投标中";
        if(val == <%=LoanApplicationStateEnum.LOANAUDIT.getValue()%>)
            return "放款审核中";
        if(val == <%=LoanApplicationStateEnum.WAITMAKELOANAUDIT.getValue()%>)
            return "待放款";
        if(val == <%=LoanApplicationStateEnum.REPAYMENTING.getValue()%>)
            return "还款中";
        if(val == <%=LoanApplicationStateEnum.COMPLETED.getValue()%>)
            return "已结清";
        if(val == <%=LoanApplicationStateEnum.EARLYCOMPLETE.getValue()%>)
            return "提前还贷";
        if(val == <%=LoanApplicationStateEnum.DELETED.getValue()%>)
            return "取消";
        if(val == <%=LoanApplicationStateEnum.FAILURE.getValue()%>)
            return "流标";
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
    	//console.log($('#startDate').datebox('getValue')+'=='+$('#endDate').datebox('getValue')+'=='+$("#applicationState").combobox("getValue")+'=='+$("#loanType").combobox("getValue"))
        $('#loan_list').datagrid('reload', {
            'startDate' : Trim($('#startDate').datebox('getValue'),"g"),
            'endDate' : Trim($('#endDate').datebox('getValue'),"g"),
            'applicationState':Trim($("#applicationState").combobox("getValue"),"g"),
            'loanType':Trim($("#loanType").combobox("getValue"),"g")
        });
    }
    
    $(function(){
        loadLoanList();
    });
   
    function showcontent(url){
    	var tabPanel = $('#detailTabs').tabs('getSelected');   
		tabPanel.panel({
			href : url
		});
    }
</script>

</body>