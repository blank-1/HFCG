<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.constants.CreditorRightsConstants" %>
<%@include file="../../../common/common.jsp" %>
<%
    //债权状态
    CreditorRightsConstants.CreditorRightsStateEnum[] creditorRightsState = CreditorRightsConstants.CreditorRightsStateEnum.values();
    request.setAttribute("creditorRightsState", creditorRightsState);
    
    CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus[] creditorRightsTransferApp = CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus.values();
    request.setAttribute("creditorRightsTransferApp", creditorRightsTransferApp);
%>
<body>
<div id="transferApplyList" class="container-fluid" style="padding: 5px 0px 0px 10px">

	<div id="transferApplyList_toolbar" style="height:auto">
        <div class="container-fluid" style="padding: 5px 0px 0px 10px">
            <form action="${ctx}/jsp/rights/transferApply/transferApplyList" method="post" class="form-inline">
            	<span>借款标题:</span><input type="text" id="searchLoanApplicationName" />
            	<span>转让人用户名:</span><input type="text" id="searchLoginName" />
            	<span>转让人手机号:</span><input type="text" id="searchMobileNo" />
                
                <br/>
                
                <span>债权状态:</span>
                <select id="searchRightsState" class="easyui-combobox" style="width: 130px;" >
                    <option value="">全部</option>
                    <c:forEach items="${creditorRightsState}" var="rightState">
                        <option value="${rightState.value}">${rightState.desc}</option>
                    </c:forEach>
                </select>
                
                <span>转让状态:</span>
                <select id="searchTurnState" class="easyui-combobox" style="width: 130px;" >
                    <option value="">全部</option>
                    <c:forEach items="${creditorRightsTransferApp}" var="rightTransferState">
                        <option value="${rightTransferState.value}">${rightTransferState.desc}</option>
                    </c:forEach>
                </select>
                
                <span>转让日期:</span>
              	<input type="text" class="easyui-datebox" editable="false" style="width: 150px" id="searchBeginTime">至
				<input type="text" class="easyui-datebox" editable="false" style="width: 150px" id="searchEndTime">&nbsp;&nbsp;&nbsp;
				
                <a onclick="doSearch();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
            </form>
        </div>
    </div>
	
    <table id="transferApplyList_list"></table>
</div>

<script language="javascript">

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
	 * 执行：列表加载。
	 */
    function loadList(){
        $("#transferApplyList_list").datagrid({
            idField: 'creditorRightsApplyId',
            title: '债权转让列表',
            url: '${ctx}/jsp/rights/transferApply/transferApplyList',
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.8,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#transferApplyList_toolbar',
            columns:[[
                      {field:'loanApplicationName', width:260, align:'left', title:'借款标题',
							formatter:function(value,row,index){
								if(value != null){
									var result = '<a href="#" onclick="detail('+index+')">'+ value +'</a> ';
									return result;
								}
							}
                      },
                      {field:'periodsNumber', width:80, align:'center', title:'当前期数'},
                      {field:'whenWorth', width:100, align:'right', title:'待收本金(元)',
                    	  formatter:function(value,row,index){
                    		  if(value != null && value != ""){
                    			  return formatNum(value,2);
                    		  }
                          }
                      },
                      {field:'applyPrice', width:100, align:'right', title:'转出价格(元)',
                    	  formatter:function(value,row,index){
                    		  if(value != null && value != ""){
                    			  return formatNum(value,2);
                    		  }
                          }
                      },
                      {field:'discount', width:100, align:'center', title:'转让折扣(%)'},
                      {field:'applyTime', width:100, align:'center', title:'发起转让日期',
                    	  formatter:function(value,row,index){
                    		  if(value != null && value != ""){
	                  	      	  return getDateStr(new Date(value));
	                  		  }
                  		  }
                      },
                      {field:'applyEndTime', width:100, align:'center', title:'转让截止日',
                    	  formatter:function(value,row,index){
                    		  if(value != null && value != ""){
	                  	      	  return getDateStr(new Date(value));
	                  		  }
                  		  }
                      },
                      {field:'loginName', width:120, align:'center', title:'转让人用户名'},
                      {field:'realName', width:120, align:'center', title:'转让人姓名'},
                      {field:'mobileNo', width:130, align:'center', title:'转让人手机号'},
                      {field:'rightsState', width:100, align:'center', title:'债权状态'},
                      {field:'turnState', width:100, align:'center', title:'转让状态'},
                      {field:'action',align:'center',title:'操作',width:100,
                          formatter:function(value,row,index){
                              var value = '<a class="label label-info" onclick="detail('+index+')">详情</a> &nbsp;&nbsp;';
                              return value;
                          }
                      }
            ]]
            
       });
    }
	
    /**
     * 执行查询。
     */
    function doSearch(){
        var searchInfo = {};
        searchInfo.searchLoanApplicationName=$("#searchLoanApplicationName").val();
        searchInfo.searchLoginName=$("#searchLoginName").val();
        searchInfo.searchMobileNo=$("#searchMobileNo").val();
        searchInfo.searchBeginTime=$("#searchBeginTime").datebox("getValue");
        searchInfo.searchEndTime=$("#searchEndTime").datebox("getValue");
        if('' != $('#searchRightsState').combobox('getValue')){
        	searchInfo.searchRightsState=$('#searchRightsState').combobox('getValue');
        }
        if('' != $('#searchTurnState').combobox('getValue')){
        	searchInfo.searchTurnState=$('#searchTurnState').combobox('getValue');
        }
        $('#transferApplyList_list').datagrid('load',searchInfo);
    }
    
    //详情
    function detail(index) {
    	$("#transferApplyList_list").datagrid("selectRow",index);
    	var selRow = $("#transferApplyList_list").datagrid("getSelected");
    	if (selRow) {
            $("#transferApplyList").after("<div id='financialPlanDetail' style=' padding:10px; '></div>");
            $("#financialPlanDetail").dialog({
                resizable: false,
                title: '转让详情',
                href: '${ctx}/jsp/rights/transferApply/showRightTransferApplyDetail?loanApplicationId='+selRow.loanApplicationId,
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
    
    $(function(){
    	loadList();	
    });
</script>
    
</body>