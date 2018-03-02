<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<%@ page import="com.xt.cfp.core.constants.LoanApplicationStateEnum" %>
<head>
</head>
<body>

<div class="easyui-layout" style="width:100%;height:90%;">
	<%@ include file="customer_lend_header.jsp" %>
	<div id="content" region="center" title="查询结果" >
		<div style="padding: 10px 20px 20px 20px;">
		    <fieldset style="height:80px">
		        <legend>查询条件</legend>
		        <form name="lendListQuery" id="" action="" class="fitem" autocomplete="off">
		            <table>
		                <tbody>
		                <tr>
		                	<td nowrap="nowrap"><label>当前状态：</label>
		                    </td>
		                    <td>
		                        <select class="easyui-combobox" id="orderState" style="width:150px" name="orderState" >
		                            <option value='-1'>全部</option>
		                            <option value='0'>未支付</option>
				                	<option value='1'>理财中（还款中）</option>
				                	<option value='2'>已结清</option>
				                	<option value='3'>已过期</option>
				                	<option value='4'>已撤销</option>
				                	<option value='5'>匹配中（已支付）</option>
				                	<option value='6'>退出中</option>
				                	<option value='7'>流标</option>
		                        </select>
		                    </td>
		                </tr>
		                <tr>
		                 	<td nowrap="nowrap"><label>出借时间：</label>
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
		                               onclick="toQueryList();" iconCls="icon-search">查询</a>
		                        </div>
		                    </td>
		                </tr>
		                </tbody>
		            </table>
		        </form>
		    </fieldset>
		</div>
		<div id="loan" class="container-fluid" style="width: 1100px;">
		    <table id="lend_list"></table>
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
    function lendLoanList(){
        $("#lend_list").datagrid({
            title: '出借列表',
            url: '${ctx}/jsp/custom/customer/getLendList',
            pagination: true,
            pageSize: 10,
            queryParams: {userId:'${userId }'},
            singleSelect: true,
            rownumbers: true,
            width: document.body.clientWidth * 0.8,
            height: document.body.clientHeight * 0.5,
            columns:[[
                {field:'loanApplicationCode', width:100,title:'借款ID' },
                {field:'loanType', width:100,title:'借款类型' ,formatter:loanTypeFormatter},
                {field:'loanTitle', width:250,title:'借款标题'},
                {field:'buyBalance', width:80,title:'出借金额'},
                {field:'factBalance', width:80,title:'已收回款'},
                {field:'waitBackBalance', width:80,title:'待收回款'},
                {field:'shouldBalance', width:80,title:'总计应收回款'},
                {field:'buyDate', width:150,title:'出借日期',formatter:dateTimeFormatter},
                {field:'applicationState', width:80,title:'当前状态',formatter:appStateFormatter}
            ]],
            view: detailview,
            detailFormatter:function(index,row){
            	var currentPayDate = "";
            	var expectProfit = "";
            	var params = {"loanApplicationId" : row.loanApplicationId,"creditorRightsId":row.creditorRightsId,"lendOrderId" :row.lendOrderId,"lendUserId" :row.lendUserId};
            	$.ajax({
                    url:'${ctx}/jsp/custom/customer/getLoanInfo',
                    data: params,
                    type:"POST",
                    async: false,
                    success:function(data){
                    	currentPayDate = data.currentPayDate;
                    	expectProfit = data.expectProfit;
                    }
                });
                return "<div style='padding: 10px 0px 10px 0px;font-size: 12px;'>"
                + "借款人:<span>"+row.realName+"</span> &nbsp;&nbsp;&nbsp;&nbsp;"
                + "借款期限:<span>"+row.cycleCounts+ (row.cycleValue==1?"个月":"天") +"</span> &nbsp;&nbsp;&nbsp;&nbsp;"
                + "最近回款日:<span>"+getDateStr(new Date(currentPayDate))+"</span> &nbsp;&nbsp;&nbsp;&nbsp;"
                + "年化利率:<span>"+row.annualRate+"%</span> &nbsp;&nbsp;&nbsp;&nbsp;"
                + "预期收益:<span>"+expectProfit+"元</span> &nbsp;&nbsp;&nbsp;&nbsp;"
                + "还款方式:<span>"+repaymentTypeFormatter(row.repaymentType)+"</span> <table class='ddv'></table></div>";
            },  
            onExpandRow: function(index,row){
                var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
                var params = {"creditorRightsId":row.creditorRightsId,"lendOrderId":row.lendOrderId,"loanProductId":row.loanProductId,"lendUserId":row.lendUserId};
                ddv.datagrid({
                    url:'${ctx}/jsp/custom/customer/getLendDetail',
                    queryParams : params,
                    fitColumns:true,
                    singleSelect:true,
                    rownumbers:true,
                    loadMsg:'',
                    height:'auto',
                    columns:[[
                        {field:'sectionCode',title:'期数',width:40},
                        {field:'repaymentDayPlanned',title:'应回款日期',width:100},
                        {field:'shouldCapital2',title:'应回本金',width:100},
                        {field:'shouldInterest2',title:'应回利息',width:100},
                        /* {field:'AAAAAAAAAA',title:'罚息',width:100}, */
                        {field:'shouldFee',title:'应缴费用',width:100},
                        {field:'shouldBalance2',title:'应回款总额',width:100},
                        {field:'factBalance',title:'已回款总额',width:100},
                        {field:'rightsDetailState',title:'状态',width:100}
                    ]],
                    onBeforeLoad: function (value, rec) {
                 	   var repaymentDayPlanned = $(this).datagrid("getColumnOption", "repaymentDayPlanned");
                        if (repaymentDayPlanned) {
                     	   repaymentDayPlanned.formatter = function (value, rowData, rowIndex) {
                     			var d = new Date(value);
                                	return getDateStr(d);
                            }
                        }
                        var shouldCapital2 = $(this).datagrid("getColumnOption", "shouldCapital2");
                        if (shouldCapital2) {
                     	   shouldCapital2.formatter = function (value, rowData, rowIndex) {
                     		   return formatNum(value,2);
                            }
                        }
                        var shouldInterest2 = $(this).datagrid("getColumnOption", "shouldInterest2");
                        if (shouldInterest2) {
                     	   shouldInterest2.formatter = function (value, rowData, rowIndex) {
                     		   return formatNum(value,2);
                            }
                        }
                        var shouldBalance2 = $(this).datagrid("getColumnOption", "shouldBalance2");
                        if (shouldBalance2) {
                     	   shouldBalance2.formatter = function (value, rowData, rowIndex) {
                     		   return formatNum(value,2);
                            }
                        }
                        var factBalance = $(this).datagrid("getColumnOption", "factBalance");
                        if (factBalance) {
                     	   factBalance.formatter = function (value, rowData, rowIndex) {
                     		   return formatNum(value,2);
                            }
                        }
                        var rightsDetailState = $(this).datagrid("getColumnOption", "rightsDetailState");
                        if (rightsDetailState) {
                     	   rightsDetailState.formatter = function (value, rowData, rowIndex) {
                     		   if(value == 0){return '未还款';}
                     		   else if(value == 1){return '部分还款';}
                     		   else if(value == 2){return '已还清';}
                     		   else if(value == 3){return '逾期';}
                     		   else if(value == 4){return '提前还款';}
                     		   else if(value == 5){return '已转出';}
                     		   else if(value == 6){return '平台垫付本金';}
                     		   else if(value == 7){return '平台垫付利息';}
                     		   else if(value == 8){return '平台垫付本息';}
                     		   else if(value == 9){return '已删除';}
                            }
                        }
                        
                        
                    },
                    onResize:function(){
                    		$.post('${ctx}/jsp/rights/detail/detail',
                    			{
                     			creditorRightsId:row.creditorRightsId       		
                    			},
                    			function(data){
                     	    	if(data.result == 'success'){
                     	    		$('#annualRate_' + index).html(data.annualRate);
                     	    	}
                     	 }),'json';
                    		
                        $('#lend_list').datagrid('fixDetailRowHeight',index);
                    },
                    onLoadSuccess:function(){
                        setTimeout(function(){
                            $('#lend_list').datagrid('fixDetailRowHeight',index);
                        },0);
                    }
                });
                $('#lend_list').datagrid('fixDetailRowHeight',index);
            }
        });
    }
	
    function loanTypeFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == '0')
            return "信贷";
        if(val == '1')
            return "房贷";
        if(val == '7')
            return "房产直投";
    }
    function repaymentTypeFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == '1')
            return "周期还本息";
        if(val == '2')
            return "周期还利息,到期还本金";
        if(val == '3')
            return "周期还本金,到期还利息";
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
        $('#lend_list').datagrid('reload', {
            'startDate' : Trim($('#startDate').datebox('getValue'),"g"),
            'endDate' : Trim($('#endDate').datebox('getValue'),"g"),
            'orderState':Trim($("#orderState").combobox("getValue"),"g"),
            'userId':'${userId }'
        });
    }
    
    function showcontent(url){
    	var tabPanel = $('#detailTabs').tabs('getSelected');   
		tabPanel.panel({
			href : url
		});
    }
    
    $(function(){
        lendLoanList();
    });
</script>

</body>