<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<body>
<div id="orderList" class="container-fluid" style="padding: 5px 0px 0px 10px">

	<div id="orderList_toolbar" style="height:auto">
        <div class="container-fluid" style="padding: 5px 0px 0px 10px">
            <form action="${ctx}/jsp/lendList/order_list" method="post" class="form-inline">
            	<span>订单号:</span><input type="text" id="searchOrderCode" size=6/>&nbsp;&nbsp;&nbsp;&nbsp;
            	<span>出借/购买人用户名:</span><input type="text" id="searchLendUserName" size=6/>&nbsp;&nbsp;&nbsp;&nbsp;
            	<span>订单支付状态:</span>
				<select id="searchOrderPayState" class="easyui-combobox" style="width: 130px;" >
                    <option value="">全部</option>
                    <option value="1">已支付</option>
                    <option value="0">未支付</option>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;
                <br/>
            	<span>标题:</span><input type="text" id="searchLendOrderName" size=6/>&nbsp;&nbsp;&nbsp;&nbsp;
                <span>订单时间:</span>
              	<input type="text" class="easyui-datebox" editable="false" style="width: 150px" id="searchBeginTime">至
				<input type="text" class="easyui-datebox" editable="false" style="width: 150px" id="searchEndTime">&nbsp;&nbsp;&nbsp;&nbsp;
                <span>订单类型:</span>
				<select id="searchProductType" class="easyui-combobox" style="width: 130px;" >
                    <option value="">全部</option>
                    <option value="1">债权</option>
                    <option value="2">省心计划</option>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;
                <mis:PermisTag code="3000101">
                <a onclick="doSearch();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                </mis:PermisTag>
            </form>
        </div>
    </div>
	
    <table id="orderList_list"></table>
</div>

<script language="javascript">

	/**
	 * 执行：列表加载。
	 */
    function loadList(){
        $("#orderList_list").datagrid({
            idField: 'lendOrderId',
            title: '订单列表',
            url: '${ctx}/jsp/lendList/order_list',
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.8,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#orderList_toolbar',
            columns:[[
					  {field:'buyTime',width:140,align:'center',title:'订单时间'},
                      {field:'orderCode',width:150,align:'center',title:'订单号'},
                      {field:'productType',width:120,align:'center',title:'订单类型'},
                      {field:'lendOrderName',width:300,align:'center',title:'借款/省心计划标题',
                    	  formatter:function(value,row,index){
                    		  	var result = "";
                    		    <mis:PermisTag code="030004">
            		    	     return value;
            		    	    </mis:PermisTag >
                    		  <mis:PermisTag code="03000103">
                    		    if(row.productType == 1){
                    		    	result = "<a style='cursor:pointer' onclick='loanApplicationDetail(" + index + ")'>" + value + "</a>";
   	            		   		}else if(row.productType == 2){
   	            			   		result = "<a style='cursor:pointer' onclick='financialPlanDetail(" + index + ")'>" + value + "</a>";
   	            		   		}
                    		  </mis:PermisTag >
                                return result;
                          }
                      },
                      {field:'buyBalance',width:120,align:'right',title:'订单金额'},
                      {field:'lendUserName',width:120,align:'center',title:'出借/购买人用户名'},
                      {field:'orderPayState',width:120,align:'center',title:'订单支付状态'},
                      {field:'rightingState',width:120,align:'center',title:'标的状态'},
                      {field:'orderState',width:120,align:'center',title:'理财状态'},
                      {field:'action',width:80,align:'center',title:'操作',
                          formatter:function(value,row,index){
                              <mis:PermisTag code="03000102">
                          		var result = "<a class='label label-info' onclick='showDetail(" + row.lendOrderId + ")'>详情</a>";
                                return result;
                              </mis:PermisTag >
                          }
                      }
            ]],
           	onBeforeLoad: function (value, rec) {
         	   
        		   // 订单时间
                   var buyTime = $(this).datagrid("getColumnOption", "buyTime");
	               if (buyTime) {
	            	   buyTime.formatter = function (value, rowData, rowIndex) {
	            			if(value){
	            				var d = new Date(value);
	                           	return getDateTimeStr(d);
	            			}
	                   }
	               }
	               
	               // 订单类型
	               var productType = $(this).datagrid("getColumnOption", "productType");
	               if (productType) {
	            	   productType.formatter = function (value, rowData, rowIndex) {
	            		   if(value == 1){
	            			   return "<font style='color: black;'>债权</font>";
	            		   }else if(value == 2){
	            			   return "<font style='color: blue;'>省心计划</font>";
	            		   }
	                   }
	               }
	               
	               // 订单金额
	               var buyBalance = $(this).datagrid("getColumnOption", "buyBalance");
	               if (buyBalance) {
	            	   buyBalance.formatter = function (value, rowData, rowIndex) {
	            		   if(value){
	            			   return formatNum(value,2);
	            		   }
	                   }
	               }
	               
	               // 订单支付状态
	               var orderPayState = $(this).datagrid("getColumnOption", "orderPayState");
	               if (orderPayState) {
	            	   orderPayState.formatter = function (value, rowData, rowIndex) {
	            		   if(value == 1){
	            			   return "<font style='color: green;'>已支付</font>";
	            		   }else {
	            			   return "<font style='color: black;'>未支付</font>";
	            		   }
	                   }
	               }
	               
	               // 标的状态
	               var rightingState = $(this).datagrid("getColumnOption", "rightingState");
	               if (rightingState) {
	            	   rightingState.formatter = function (value, rowData, rowIndex) {
	            		   if(rowData.productType == 1||rowData.productType == 3){//投标类
                               if(value == '0'){
								   return "草稿";
	            			   }else if(value == '1'){
	            				   return "风控审核中";
	            			   }else if(value == '2'){
	            				   return "发标审核中";
	            			   }else if(value == '3'){
	            				   return "投标中";
	            			   }else if(value == '4'){
	            				   return "放款审核中";
	            			   }else if(value == '5'){
	            				   return "待放款";
	            			   }else if(value == '6'){
	            				   return "还款中";
	            			   }else if(value == '7'){
	            				   return "已结清";
	            			   }else if(value == '8'){
	            				   return "已结清(提前还贷)";
	            			   }else if(value == '9'){
	            				   return "取消";
	            			   }else if(value == 'A'){
	            				   return "流标";
	            			   }
	            		   }else if(rowData.productType == 2){//省心计划
	            			   return "--";
	            		   }
	                   }
	               }
	               
	               // 理财状态
	               var orderState = $(this).datagrid("getColumnOption", "orderState");
	               if (orderState) {
	            	   orderState.formatter = function (value, rowData, rowIndex) {
	            		   if(rowData.productType == 1){//投标类
	            				return "--";
	            		   }else if(rowData.productType == 2){//省心计划
							   if(value == 0){
								   return "未支付";
	            			   }else if(value == 1){
	            				   return "理财中";
	            			   }else if(value == 2){
	            				   return "已结清";
	            			   }else if(value == 3){
	            				   return "已过期";
	            			   }else if(value == 4){
	            				   return "已撤销";
	            			   }else if(value == 5){
	            				   return "匹配中";
	            			   }else if(value == 6){
	            				   return "退出中";
	            			   }
	            		   }
	                   }
	               }
	               
           }
        
       });
    }
	
	// 省心计划-详情
    function financialPlanDetail(index) {
    	$("#orderList_list").datagrid("selectRow",index);
    	var selRow = $("#orderList_list").datagrid("getSelected");
    	if (selRow) {
            $("#orderList_list").after("<div id='financialPlanDetail' style=' padding:10px; '></div>");
            $("#financialPlanDetail").dialog({
                resizable: false,
                title: '理财明细',
                href: '${ctx}/jsp/lendList/tofinancialPlanDetail?lendOrderId='+selRow.lendOrderId,
                width: 1000,
                height: 550,
                modal: true,
                top: 100,
                left: 300,
                buttons: [
                    {
                        text: '关闭',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#financialPlanDetail").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要查看的消息!", "info");
        }
    }
	
	// 借款申请-详情
    function loanApplicationDetail(index) {

    	$("#orderList_list").datagrid("selectRow",index);
        var selRow = $("#orderList_list").datagrid("getSelected");
        if (selRow) {
			$("#orderList_list").after("<div id='loan_detail_d' style=' padding:5px; '></div>");
            $('#loan_detail_d').dialog({
                title : '借款申请详情',
                href: '${ctx}/jsp/loanManage/loan/showLoanDetailByLendOrderId?lendOrderId='+selRow.lendOrderId,
                width: 1000,
                height:550,
                maximizable : true,
                minimizable : false,
                collapsible : false,
                modal:true,
                resizable : true,
				onClose: function () {
					$(this).dialog('destroy');
				}
            });

        } else {
            $.messager.alert("系统提示", "请选择要查看的借款合同详细信息!", "info");
        }
	}
	
	/**
	 * 执行：详情。
	 */
	function showDetail(lendOrderId){
		var url = '${ctx}/jsp/lendList/to_order_detail?lendOrderId=' + lendOrderId;
	    window.open(url,'_blank',"top=0,left=0,width=" + (document.body.clientWidth*0.9) + ",height=" + (document.body.clientHeight*0.9));
	}
	
    /**
     * 执行：查询。
     */
    function doSearch(){
        var searchInfo = {};
        searchInfo.searchOrderCode=$("#searchOrderCode").val();
        searchInfo.searchLendUserName=$("#searchLendUserName").val();
        if('' != $('#searchOrderPayState').combobox('getValue')){
        	searchInfo.searchOrderPayState=$('#searchOrderPayState').combobox('getValue');
        }
        searchInfo.searchLendOrderName=$("#searchLendOrderName").val();
        searchInfo.searchBeginTime=$("#searchBeginTime").datebox("getValue");
        searchInfo.searchEndTime=$("#searchEndTime").datebox("getValue");
        if('' != $('#searchProductType').combobox('getValue')){
        	searchInfo.searchProductType=$('#searchProductType').combobox('getValue');
        }
        $('#orderList_list').datagrid('load',searchInfo);
    }
    
    $(function(){
    	loadList();	
    });
</script>
    
</body>