<%@ page import="com.xt.cfp.core.util.ApplicationContextUtil" %>
<%@ page import="com.xt.cfp.core.service.ConstantDefineService" %>
<%@ page import="com.xt.cfp.core.pojo.ConstantDefine" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<body>

<%
	ConstantDefineService constantDefineService = (ConstantDefineService)ApplicationContextUtil.getBean("constantDefineServiceImpl");
	List<ConstantDefine> bankList = constantDefineService.getConstantDefinesByType("bank");
	request.setAttribute("bankList",bankList);
%>
<div id="rechargeOrderList" class="container-fluid" style="padding: 5px 0px 0px 10px">

	<div id="rechargeOrderList_toolbar" style="height:auto">
        <div class="container-fluid"  style="padding: 5px 0px 0px 10px" >
            <form action="${ctx}/rechargeOrder/rechargeOrderList" id="rechargeOrderForm" method="post" class="form-inline">
            	<span>充值用户名:</span><input type="text" id="searchUserName" name="searchUserName" size=6/>&nbsp;&nbsp;&nbsp;
            	<span>用户姓名:</span><input type="text" id="searchRealName" name="searchRealName" size=6/>&nbsp;&nbsp;&nbsp;
                <span>充值时间:</span>
              	<input type="text" class="easyui-datebox" editable="false" style="width: 150px" id="searchBeginTime" name="searchBeginTime">至
				<input type="text" class="easyui-datebox" editable="false" style="width: 150px" id="searchEndTime" name="searchEndTime">&nbsp;&nbsp;&nbsp;
				
				<span>对账状态:</span>
				<select id="searchState" name="searchState" class="easyui-combobox" style="width: 130px;" >
                    <option value="">全部</option>
                    <option value="0">待处理</option>
                    <option value="1">充值成功</option>
                    <option value="2">充值失败</option>
                </select>&nbsp;&nbsp;&nbsp;
                  
		<mis:PermisTag code="4000301">
               		<a onclick="doSearch();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                </mis:PermisTag>
                <mis:PermisTag code="04000302">
	                <a href="javascript:doExport()" class="easyui-linkbutton"
	               		data-options="iconCls:'icon-save'">导出Excel</a>
               	</mis:PermisTag>
            </form>
        </div>
    </div>
	
    <table id="rechargeOrderList_list"></table>
</div>

<script language="javascript">

	/**
	 * 执行：列表加载。
	 */
    function loadList(){
        $("#rechargeOrderList_list").datagrid({
            idField: 'rechargeId',
            title: '充值单列表',
            url: '${ctx}/rechargeOrder/rechargeOrderList',
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.8,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#rechargeOrderList_toolbar',
            columns:[[
                      {field:'rechargeCode',width:240,align:'center',title:'充值单号'},
                      {field:'externalNo',width:240,align:'center',title:'交易流水号'},
                      {field:'amount',width:120,align:'center',title:'充值金额(元)'},
                      {field:'detailId',width:120,align:'center',title:'充值渠道明细单号'},
                      {field:'channelName',width:100,align:'center',title:'充值渠道名称',    
                    	formatter:function(value,row,index){
                            if(null != value){
                                if(value.indexOf("TZT") > 0 ){
                                    //	var v =value.replace("TZT","快捷支付");
                                    return value.replace("TZT","快捷支付");
                                }else if(value.indexOf("EBK") > 0){
                                    return value.replace("EBK","网银支付");
                                }
							}
                    		return value;
                     }},
                      {field:'loginName',width:120,align:'center',title:'用户名/公司名称'},
                      {field:'realName',width:120,align:'center',title:'充值人姓名'},
                      {field:'bankCode',width:120,align:'center',title:'银行',formatter:bankShow},
                      {field:'cardNo',width:120,align:'center',title:'充值卡号'},
                      {field:'status',width:120,align:'center',title:'对账状态'},
                      {field:'createTime',width:120,align:'center',title:'充值时间'},
                      {field:'resultTime',width:120,align:'center',title:'对账完成时间'},
                      {field:'action',width:80,align:'center',title:'操作',
                          formatter:function(value,row,index){
                        	  <mis:PermisTag code="04000303">
                        	  if(row.status != 1){
                        		  var result = "<a class='label label-info' onclick='contrastRechange(" + row.rechargeId + ")'>对账</a>";
                                  return result;
                        	  }else{
                        		  return "";
                        	  }
                        	  </mis:PermisTag>
                         }
                      }
                      
            ]],
           	onBeforeLoad: function (value, rec) {
           			
                   var createTime = $(this).datagrid("getColumnOption", "createTime");
	               if (createTime) {
	            	   createTime.formatter = function (value, rowData, rowIndex) {
	            			if(value){
	            				var d = new Date(value);
	                           	return getDateTimeStr(d);
	            			}
	                   }
	               }
	               var resultTime = $(this).datagrid("getColumnOption", "resultTime");
	               if (resultTime) {
	            	   resultTime.formatter = function (value, rowData, rowIndex) {
	            		   if(value){
	            			   var d = new Date(value);
	            			   return getDateTimeStr(d);
	            		   }
	                   }
	               }
	               var amount = $(this).datagrid("getColumnOption", "amount");
	               if (amount) {
	            	   amount.formatter = function (value, rowData, rowIndex) {
	            		   if(value){
	            			   return formatNum(value,2);
	            		   }
	                   }
	               }
	               var status = $(this).datagrid("getColumnOption", "status");
	               if (status) {
	            	   status.formatter = function (value, rowData, rowIndex) {
	            		   if(value == 0){
	            			   return "<font style='color: black;'>待处理</font>";
	            		   }else if(value == 1){
	            			   return "<font style='color: green;'>充值成功</font>";
	            		   }else if(value == 2){
	            			   return "<font style='color: red;'>充值失败</font>";
	            		   }
	                   }
	               }
               
           }
        
       });
    }

	function bankShow(v){
		<c:forEach items="${bankList}" var="bank">
		if(v=='${bank.constantDefineId}') return '${bank.constantName}';
		</c:forEach>
	}

	/**
	 * 执行：对账。
	 */
	function contrastRechange(rechargeId){
		$.post('${ctx}/rechargeOrder/contrastRechange',
       		{
				rechargeId:rechargeId
       		},
       		function(data){
       	    	if(data.result == 'success'){
       	    		$.messager.alert("系统提示", "已提交查询，稍后请刷新页面获取最新对账状态。", "info");
       	    	}else if(data.result == 'error'){
       	    		if(data.errCode == 'check'){
       	    			$.messager.alert("验证提示", data.errMsg, "info");
       	    		}else{
       	    			$.messager.alert("系统提示", data.errMsg, "info");
       	    		}
       	    	}else{
       	    		$.messager.alert("系统提示", "网络异常，请稍后操作！", "info");
       	    	}
       	    	$('#rechargeOrderList_list').datagrid('reload');
       	}),'text';
	}
	
    /**
     * 执行：查询。
     */
    function doSearch(){
        var searchInfo = {};
        searchInfo.searchUserName=$("#searchUserName").val();
        searchInfo.searchRealName=$("#searchRealName").val();
        searchInfo.searchBeginTime=$("#searchBeginTime").datebox("getValue");
        searchInfo.searchEndTime=$("#searchEndTime").datebox("getValue");
        if('' != $('#searchState').combobox('getValue')){
        	searchInfo.searchState=$('#searchState').combobox('getValue');
        }
        $('#rechargeOrderList_list').datagrid('load',searchInfo);
    }
    
    function doExport(){
    	$("#rechargeOrderForm").attr("action", "${ctx}/rechargeOrder/exportExcel");
        $("#rechargeOrderForm").submit();
    }
    
    $(function(){
    	loadList();	
    });
</script>
    
</body>
