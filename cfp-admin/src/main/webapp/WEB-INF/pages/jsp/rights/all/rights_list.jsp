<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.constants.CreditorRightsConstants" %>
<%@include file="../../../common/common.jsp" %>
<%
    //债权状态
    CreditorRightsConstants.CreditorRightsStateEnum[] creditorRightsState = CreditorRightsConstants.CreditorRightsStateEnum.values();
    request.setAttribute("creditorRightsState", creditorRightsState);
%>
<body>
<div id="rights_rightsList" class="container-fluid" style="padding: 5px 0px 0px 10px">

	<div id="rights_rightsList_toolbar" style="height:auto">
        <div class="container-fluid" style="padding: 5px 0px 0px 10px">
            <form action="${ctx}/jsp/rights/all/list" method="post" class="form-inline">
            	<span>借款标题:</span><input type="text" id="searchLoanName" size=6/>
            	<span>借款人姓名:</span><input type="text" id="searchLoanUserName" size=6/>
            	<span>出借人姓名:</span><input type="text" id="searchLeanUserName" size=6/>
            	
                <span>债权状态:</span>
                <select id="searchState" class="easyui-combobox" style="width: 130px;" >
                    <option value="">全部</option>
                    <c:forEach items="${creditorRightsState}" var="rightState">
                        <option value="${rightState.value}">${rightState.desc}</option>
                    </c:forEach>
                </select>
                
                <br/>
                
                <span>出借日期:</span>
              	<input type="text" class="easyui-datebox" editable="false" style="width: 150px" id="searchBeginTime">至
				<input type="text" class="easyui-datebox" editable="false" style="width: 150px" id="searchEndTime">&nbsp;&nbsp;&nbsp;
				
				<input type="hidden" id="searchT">
				<a style="cursor:pointer;color: blue;" class="T" id="T_1" onclick="onSearchT('T_1');">今天</a>&nbsp;&nbsp;
				<a style="cursor:pointer;color: blue;" class="T" id="T_7" onclick="onSearchT('T_7');">近7天</a>&nbsp;&nbsp;
				<a style="cursor:pointer;color: blue;" class="T" id="T_3" onclick="onSearchT('T_3');">近3个月</a>&nbsp;&nbsp;
				<a style="cursor:pointer;color: blue;" class="T" id="T_6" onclick="onSearchT('T_6');">近6个月</a>&nbsp;&nbsp;
				<a style="cursor:pointer;color: blue;" class="T" id="T_12" onclick="onSearchT('T_12');">近12个月</a>&nbsp;&nbsp;
				
                <a onclick="doSearch();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
            </form>
        </div>
    </div>
	
    <table id="rights_rightsList_list"></table>
</div>

<script language="javascript">

	/**
	 * 时间段单击事件。
	 */
	function onT(t){
		if($("#searchT").val() == t){
			$(".T").css("color","blue");
			$("#searchT").attr("value","");
		}else{
			$(".T").css("color","blue");
			$("#"+t).css("color","red");
			$("#searchT").attr("value",t);
		}
	}
	
	 //日期-天
	 function SubDays(d, n) {
	     var t = new Date(d);//复制并操作新对象，避免改动原对象
	     t.setDate(t.getDate() - n);
	     return t;
	 }
	 
	 //日期-月。日对日，若目标月份不存在该日期，则置为最后一日
	 function SubMonths(d, n) {
	     var t = new Date(d);
	     t.setMonth(t.getMonth() - n);
	     if (t.getDate() != d.getDate()) { t.setDate(0); }
	     return t;
	 }
	 
	 //设置日历控件
	 function setDateBox(t){
		 var newD = new Date();
		 var resultD = '';
		 
		 if(t == 'T_1'){
			 resultD = newD;
		 }else if(t == 'T_7'){
			 resultD = SubDays(newD,7);
		 }else if(t == 'T_3'){
			 resultD = SubMonths(newD,3);
		 }else if(t == 'T_6'){
			 resultD = SubMonths(newD,6);
		 }else if(t == 'T_12'){
			 resultD = SubMonths(newD,12);
		 }
		 
		 // 设置日历控件
		 $("#searchBeginTime").datebox("setValue",resultD.format('yyyy-MM-dd'));
		 $("#searchEndTime").datebox("setValue",newD.format('yyyy-MM-dd'));
		 
	 }
	
	/**
	 * 时间段单击事件。
	 */
	function onSearchT(t){
		setDateBox(t);
		var searchInfo = {};
        searchInfo.searchT=t;
        $('#rights_rightsList_list').datagrid('load',searchInfo);
	}

	/**
	 * 执行：列表加载。
	 */
    function loadList(){
        $("#rights_rightsList_list").datagrid({
            idField: 'creditorRightsId',
            title: '所有债权列表',
            url: '${ctx}/jsp/rights/all/list',
            queryParams: {userId:'${userId }'},
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.8,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#rights_rightsList_toolbar',
            columns:[[
                      {field:'orderCode', width:140,title:'订单号'},
                      {field:'loanApplicationId', width:80, align:'center', title:'借款ID'},
                      {field:'loanType', width:90, align:'center', title:'借款类型'},
                      {field:'loanApplicationName', width:260,title:'借款标题'},
                      {field:'loanRealName', width:100, align:'center', title:'借款人姓名'},
                      {field:'lenderRealName', width:100, align:'center', title:'出借人姓名'},
                      
                      {field:'buyBalance', width:100, align:'right', title:'出借金额'},
                      {field:'sum_factBalance', width:100, align:'right', title:'已收回款'},
                      {field:'waitBackBalance', width:100, align:'right', title:'待收回款'},
                      {field:'shouldBalance2', width:100, align:'right', title:'总计应收回款'},
                      
                      {field:'lendTime', width:100, align:'center', title:'出借日期'},
                      {field:'originalUserId', width:110, align:'center', title:'原始债权人用户名'},
                      {field:'rightsStateDesc', width:100, align:'center', title:'债权状态'},
                      {field:'turnStateDesc', width:80, align:'center', title:'转让状态'}
            ]],
           	onBeforeLoad: function (value, rec) {
               var lendTime = $(this).datagrid("getColumnOption", "lendTime");
               if (lendTime) {
            	   lendTime.formatter = function (value, rowData, rowIndex) {
            			var d = new Date(value);
                       	return getDateStr(d);
                   }
               }
               var sum_shouldCapital2 = $(this).datagrid("getColumnOption", "sum_shouldCapital2");
               if (sum_shouldCapital2) {
            	   sum_shouldCapital2.formatter = function (value, rowData, rowIndex) {
            			return formatNum(value,2);
                   }
               }
               var sum_factBalance = $(this).datagrid("getColumnOption", "sum_factBalance");
               if (sum_factBalance) {
            	   sum_factBalance.formatter = function (value, rowData, rowIndex) {
            			return formatNum(value,2);
                   }
               }
               var sum_shouldBalance2_factBalance = $(this).datagrid("getColumnOption", "sum_shouldBalance2_factBalance");
               if (sum_shouldBalance2_factBalance) {
            	   sum_shouldBalance2_factBalance.formatter = function (value, rowData, rowIndex) {
            			return formatNum(value,2);
                   }
               }
               var shouldBalance2 = $(this).datagrid("getColumnOption", "shouldBalance2");
               if (shouldBalance2) {
            	   shouldBalance2.formatter = function (value, rowData, rowIndex) {
            			return formatNum(value,2);
                   }
               }
               
           }
        
        // --b
        ,
        view: detailview
        ,
        detailFormatter:function(index,row){
        	var currentPayDate = "";
        	var expectProfit = "";
        	var params = {"loanApplicationId" : row.loanApplicationId,"creditorRightsId":row.creditorRightsId,"lendOrderId" :row.lendOrderId,"lendUserId" :row.lendUserId};
        	/* $.ajax({
                url:'${ctx}/jsp/custom/customer/getLoanInfo',
                data: params,
                type:"POST",
                async: false,
                success:function(data){
                	currentPayDate = data.currentPayDate;
                	expectProfit = data.expectProfit;
                }
            }); */
        	
           return "<div style='padding: 10px 0px 10px 0px;font-size: 12px;'>"
            + "借款人:<span>"+row.loanRealName+"</span> &nbsp;&nbsp;&nbsp;&nbsp;"
            + "借款期限:<span>"+row.timeLimit+ (row.timeLimitType==1?"个月":"天") +"</span> &nbsp;&nbsp;&nbsp;&nbsp;"
            + "最近回款日:<span id='"+row.creditorRightsId+"_currentPayDate_span'>"+getDateStr(new Date(currentPayDate))+"</span> &nbsp;&nbsp;&nbsp;&nbsp;"
            + "年化利率:<span>"+row.annualRate+"%</span> &nbsp;&nbsp;&nbsp;&nbsp;"
            + "预期收益:<span id='"+row.creditorRightsId+"_expectProfit_span'>"+expectProfit+"元</span> &nbsp;&nbsp;&nbsp;&nbsp;"
            + "还款方式:<span>"+repaymentTypeFormatter(row.repaymentType)+"</span> <table class='ddv'></table></div>";
            
	        }
        ,
       onExpandRow: function(index,row){
    	   //详情头信息
        	var params = {"loanApplicationId" : row.loanApplicationId,"creditorRightsId":row.creditorRightsId,"lendOrderId" :row.lendOrderId,"lendUserId" :row.lendUserId};
        	$.ajax({
                url:'${ctx}/jsp/custom/customer/getLoanInfo',
                data: params,
                type:"POST",
                async: false,
                success:function(data){
                	$("#"+row.creditorRightsId+"_currentPayDate_span").html(getDateStr(new Date(data.currentPayDate)));
                	$("#"+row.creditorRightsId+"_expectProfit_span").html(data.expectProfit+"元");
                }
            });
        	
    	   //详情列表
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
               		
                   $('#rights_rightsList_list').datagrid('fixDetailRowHeight',index);
               },
               onLoadSuccess:function(){
                   setTimeout(function(){
                       $('#rights_rightsList_list').datagrid('fixDetailRowHeight',index);
                   },0);
               }
           });
           $('#rights_rightsList_list').datagrid('fixDetailRowHeight',index);
       }
        // --n
        
       });
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
	
    /**
     * 执行查询。
     */
    function doSearch(){
        var searchInfo = {};
        searchInfo.searchLoanName=$("#searchLoanName").val();
        searchInfo.searchLoanUserName=$("#searchLoanUserName").val();
        searchInfo.searchLeanUserName=$("#searchLeanUserName").val();
        searchInfo.searchT=$("#searchT").val();
        searchInfo.searchBeginTime=$("#searchBeginTime").datebox("getValue");
        searchInfo.searchEndTime=$("#searchEndTime").datebox("getValue");
        if('' != $('#searchState').combobox('getValue')){
        	searchInfo.searchState=$('#searchState').combobox('getValue');
        }
        $('#rights_rightsList_list').datagrid('load',searchInfo);
    }
    
    $(function(){
    	loadList();	
    });
</script>
    
</body>