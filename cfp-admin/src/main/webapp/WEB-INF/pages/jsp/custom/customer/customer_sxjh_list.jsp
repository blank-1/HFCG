<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<body>
<div id="financialPlanList" class="container-fluid" style="padding: 5px 0px 0px 10px">

	<div id="financialPlanList_toolbar" style="height:auto">
        <div class="container-fluid" style="padding: 5px 0px 0px 10px">
            <form action="${ctx}/jsp/lendList/financialPlanList" method="post" class="form-inline">
            	<input type="hidden" id="userId" name="userId" value="${userId}">
            	<span>省心计划名称:</span><input type="text" id="searchFinancialName" size=6/>
            	<span>期数:</span><input type="text" id="searchPeriods" size=6/>
            	
                <span>理财状态:</span>
                <select id="searchState" class="easyui-combobox" style="width: 130px;" >
                    <option value="">全部</option>
                    <option value="1">理财中</option>
                    <option value="6">授权期结束</option>
                    <option value="2">已结清</option>
                </select>
                
                <br/>
                
                <span>购买日期:</span>
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
	
    <table id="financialPlanList_list"></table>
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
        $('#financialPlanList_list').datagrid('load',searchInfo);
	}

	/**
	 * 执行：列表加载。
	 */
    function loadList(){
		var userId = $("#userId").val();
        $("#financialPlanList_list").datagrid({
            idField: 'lendOrderId',
            title: '所有省心列表',
            url: '${ctx}/jsp/lendList/financialPlanList?userId='+ userId,
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.8,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#financialPlanList_toolbar',
            columns:[[
                      {field:'lendOrderId',checkbox:true},
                      {field:'orderCode', width:140,title:'订单号'},
                      {field:'publishName', width:150,title:'省心计划名称',
                    	  formatter:function(value,row,index){
                      		if(value != null)
                              var value = '<a href="#" onclick="detail('+index+')">'+ value +'</a> ';
                             	return value;
                             }  
                      },
                      {field:'publishCode', width:80,align:'center',title:'期数'},
                      {field:'timeLimit', width:70,align:'center',title:'时长(月)'},
                      {field:'closingDate', width:70,align:'center',title:'封闭期(月)'},
                      {field:'profitRate', width:100,align:'center',title:'预期年化收益率',
                    	  formatter:function(value,row,index){
                    		  if(value != null && value != "")
                              return value+"-"+row.profitRateMax+"%";
                          }	  
                      },
                      
                      {field:'loginName', width:130,title:'购买人用户名'},
                      {field:'buyBalance', width:100,align:'right',title:'购买金额',
                    	  formatter:function(value,row,index){
                    		  if(value != null && value != "")
                              return formatNum(value,2);
                              }	  
                      },
                      {field:'forLendBalance', width:100,align:'right',title:'待理财金额',
                     	   formatter:function(value,row,index){
                   		  	  if(value != null && value != "")
                              return formatNum(value,2);
                              }	  
                      },
                      /*
                      {field:'expectProfit', width:100,align:'right',title:'预期收益',
                    	  formatter:function(value,row,index){
                    		  if(value != null && value != "")
                              return formatNum(value,2);
                              }	  
                      },
                      */
                      {field:'totalPayMent', width:100,align:'right',title:'总计应收回款',
                    	  formatter:function(value,row,index){
                    		  if(value != null && value != "")
                              return formatNum(value,2);
                              }	  
                      },
                      {field:'buyTime', width:130,align:'center',title:'购买日期',
                    		formatter:function(value,row,index){
                    			if(value != null && value != "")
                    			return getDateTimeStr(new Date(value));
                    		}  
                      },
                      {field:'agreementStartDate', width:130,align:'center',title:'起息日期',
                    	  formatter:function(value,row,index){
                    		if(value != null && value != "")
                  			return getDateTimeStr(new Date(value));
                  		}   
                      },
                      {field:'agreementEndDate', width:130,align:'center',title:'到期日期',
                    	  formatter:function(value,row,index){
                    		if(value != null && value != "")
                  			return getDateTimeStr(new Date(value));
                  		}   
                      },
                      {field:'orderState', width:100,align:'center',title:'理财状态',
                    		formatter:function(value,row,index){
                    			if(value == "1"){
                    				return "理财中";
                    			}else if(value == "2"){
                    				return "已结清";
                    			}else if(value == "6"){
                    				return "授权期结束";
                    			}
                    		}  
                      },
                      {field:'action',align:'left',title:'操作',width:100,
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
        searchInfo.searchFinancialName=$("#searchFinancialName").val();
        searchInfo.searchPeriods=$("#searchPeriods").val();
        searchInfo.searchLeanUserName=$("#searchLeanUserName").val();
        searchInfo.searchT=$("#searchT").val();
        searchInfo.searchBeginTime=$("#searchBeginTime").datebox("getValue");
        searchInfo.searchEndTime=$("#searchEndTime").datebox("getValue");
        if('' != $('#searchState').combobox('getValue')){
        	searchInfo.searchState=$('#searchState').combobox('getValue');
        }
        $('#financialPlanList_list').datagrid('load',searchInfo);
    }
    
    function detail(index) {
    	$("#financialPlanList_list").datagrid("selectRow",index);
    	var selRow = $("#financialPlanList_list").datagrid("getSelected");
    	if (selRow) {
            $("#financialPlanList").after("<div id='financialPlanDetail' style=' padding:10px; '></div>");
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
    
    $(function(){
    	loadList();	
    });
</script>
    
</body>