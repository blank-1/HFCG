<%@ page import="com.xt.cfp.core.constants.LendProductTypeEnum" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>

</head>
<body>
<div id="financialPlan" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div style="padding:3px;">
    		<span >借款标题：${loanApplication.loanApplicationName }</span>&nbsp;&nbsp;&nbsp;<span>借款ID:${loanApplication.loanApplicationCode }</span>&nbsp;&nbsp;&nbsp;
    		<span>标的类型：
    			<c:if test="${loanApplication.subjectType == 1 }">借款标</c:if>
    			<c:if test="${loanApplication.subjectType == 2 }">债权标</c:if>
    		</span>&nbsp;&nbsp;&nbsp;<span>状态:满标待复审</span>
            <a href="javascript:doExport()" class="easyui-linkbutton"
               		data-options="iconCls:'icon-save'">导出Excel</a>   
            <form action="" method="post" id="exportForm"></form>   		
    </div>
    <hr style="height:1px;border:none;border-top:1px solid #555555;" />
    <fieldset style="height:65px;width: 1135px;">       
        <table align="center" border="0" width="100%">
            <tr>
                <td align="right" style="font-size: 12px;">借款人：</td>
                <td align="left" style="font-size: 12px;" width="20%">${cbs.realName }</td>
                <td align="right" style="font-size: 12px;" >手机：</td>
                <td align="left" style="font-size: 12px;" width="20%">${cbs.mobileNo }</td>
                <td align="right" style="font-size: 12px;">身份证号：</td>   
                <td align="left" style="font-size: 12px;" width="20%">${cbs.idCard }</td>        
            </tr>
             <tr>
                <td align="right" style="font-size: 12px;">借款金额：</td>
                <td align="left" style="font-size: 12px;" width="20%"><fmt:formatNumber value="${loanApplication.confirmBalance }" pattern="#,#00.00"/></td>
                <td align="right" style="font-size: 12px;" >创建时间：</td>
                <td align="left" style="font-size: 12px;" width="20%"> <fmt:formatDate value="${loanApplication.createTime }" type="both"/></td> 
                <td align="right" style="font-size: 12px;">满标时间：</td>
                <td align="left" style="font-size: 12px;" width="20%"><fmt:formatDate value="${loanApplication.fullTime }" type="both"/></td>
            </tr>
        </table>
   </fieldset>
   <br>
	<fieldset style="height:65px;width: 1135px;">
        <table align="center" border="0" width="100%">
            <tr>
                <td align="right" style="font-size: 12px;">来源类型：</td>
                <td align="left" style="font-size: 12px;" width="20%">
                	<c:if test="${loanApplication.channel == 1 }">
                		渠道
                	</c:if>
                	<c:if test="${loanApplication.channel == 2 }">
                		门店
                	</c:if>
                </td> 
                <td align="right" style="font-size: 12px;" >打款卡开户行：</td>   
                <td align="left" style="font-size: 12px;" width="20%">${customerCard.registeredBank }</td>   
            </tr>
			<tr>
                <td align="right" style="font-size: 12px;">开户名：</td>
                <td align="left" style="font-size: 12px;" width="20%">${customerCard.cardcustomerName }</td>
                <td align="right" style="font-size: 12px;" >卡号：</td> 
                <td align="left" style="font-size: 12px;" width="20%">${customerCard.cardCode }</td>       
            </tr>
        </table>
   </fieldset>
	
		<table id="lendList_list"></table>
		
		<fieldset style="height:150px;width: 1135px;">  
		<legend>满标复审</legend>     
        <table align="center" border="0" width="100%">
            <tr>
                <td align="right" style="font-size: 12px;">复审意见：</td>
                <td align="left" style="font-size: 12px;">
                	<div class="controls">
            			<textarea id="fullContent" name="fullContent" autocomplete="off" class="textbox-text" placeholder="" style="margin-left: 0px; margin-right: 0px; height: 100px; width: 250px;"></textarea>
        			</div>
                </td>
                
            </tr>
             
        </table>
   </fieldset>
	</div>
	<script language="javascript">
    /**
	 * 执行：列表加载。
	 */
    function loadList(){
        $("#lendList_list").datagrid({
        	idField: 'lendOrderId',         
            url: '${ctx}/jsp/financial/lenderInformation?loanApplicationId=${loanApplication.loanApplicationId}',
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.593,
            height: document.body.clientHeight * 0.3,
            singleSelect: true,
            rownumbers: true,
            columns:[[
					  {field:'productType', align:'center',width:80,title:'来源'},
                      {field:'loginName', align:'center', width:150,title:'出借人用户名'},
                      {field:'realName', align:'center', width:190,title:'出借人姓名'},
                      {field:'idCard', align:'center', width:260,title:'出借人身份证号'},
                      {field:'mobileNo', align:'center',width:150,title:'出借人手机号'},
                      {field:'buyBalance', align:'center',width:150,title:'总出借金额'}                  
                      
            ]],
           	onBeforeLoad: function (value, rec) {
               var productType = $(this).datagrid("getColumnOption", "productType");
               if (productType) {
            	   productType.formatter = function (value, rowData, rowIndex) {
            			if(value == '<%=LendProductTypeEnum.RIGHTING.getValue()%>'){
            				return "投标";
            			}else if(value == '<%=LendProductTypeEnum.FINANCING.getValue()%>'){
            				return "理财";
            			}
                   }
               }
               var buyBalance = $(this).datagrid("getColumnOption", "buyBalance");
               if (buyBalance) {
            	   buyBalance.formatter = function (value, rowData, rowIndex) {
            		   return formatNum(value,2);	
                   }
               }
           }

        ,
        view: detailview
        ,
        detailFormatter:function(index,row){
        	if(row.productType == '<%=LendProductTypeEnum.RIGHTING.getValue()%>')
            return "<div style='padding:2px;font-size: 12px;'><table class='ddv'></table></div>";
        }
        ,
       onExpandRow: function(index,row){
           var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
           ddv.datagrid({
               url:'${ctx}/jsp/financial/paymentOrderDetail?lendOrderId=' + row.lendOrderId+"&productType=" + row.productType,
               fitColumns:true,
               singleSelect:true,
               rownumbers:true,
               loadMsg:'',
               height:'auto',
               columns:[[
                   {field:'extendNo',title:'交易流水号',width:200},
                   {field:'rechargeCode',title:'支付单号',width:210},
                   {field:'amountType',title:'支付方式',width:100},
                   {field:'channelName',title:'支付平台',width:150},
                   {field:'amount',title:'支付金额',width:100},
                   {field:'status',title:'支付结果',width:100,
                	   formatter:function(value, row, index)
               			{
               				if(value == '1'){
               					return "成功";
               				}
               			}   
                   },
                   {field:'resultTime',title:'出借时间',width:150,
                		formatter:function(value, row, index)
                		{
                			if(value != null && value != "")
                        	return getDateTimeStr(new Date(value));
                		}
                   },
                   {field:'action',title:'操作',width:100,
                	   formatter:function(value, row, index)
               			{
               				return "";
               			}   
                   },
               ]],
               onBeforeLoad: function (value, rec) {
            	   var amount = $(this).datagrid("getColumnOption", "amount");
                   if (amount) {
                	   amount.formatter = function (value, rowData, rowIndex) {
                			
                           	return formatNum(value,2);	
                       }
                   }
                   var amountType = $(this).datagrid("getColumnOption", "amountType");
                   if (amountType) {
                	   amountType.formatter = function (value, rowData, rowIndex) {
                		   if(value == '0'){
                			   return "余额";
                		   }else if(value == '1'){
                			   return "充值";
                		   }else if(value == '2'){
                			   return "财富券";
                		   }
                       }
                   }
                   var channelName = $(this).datagrid("getColumnOption", "channelName");
                   if (channelName) {
                	   channelName.formatter = function (value, rowData, rowIndex) {
                		   //alert(rowData.amountType);
                		   if(rowData.amountType == '1'){
                			   return value;
                		   }else {
                			   return "--";
                		   }
                       }
                   }
                   var rechargeCode = $(this).datagrid("getColumnOption", "rechargeCode");
                   if (rechargeCode) {
                	   rechargeCode.formatter = function (value, rowData, rowIndex) {
                		   //alert(rowData.amountType);
                		   if(rowData.amountType == '1'){
                			   return value;
                		   }else {
                			   return "-----";
                		   }
                       }
                   }
               },
               onResize:function(){
               		
               },
               onLoadSuccess:function(){
                   setTimeout(function(){
                       $('#lendList_list').datagrid('fixDetailRowHeight',index);
                   },0);
               }
           });
           $('#lendList_list').datagrid('fixDetailRowHeight',index);
       }
        // --n
        
       });
    }
    
    function doExport(){
    	$("#exportForm").attr("action", "${ctx}/jsp/financial/exportExcel?loanApplicationId=${loanApplication.loanApplicationId}");
        $("#exportForm").submit();
    }
    
    $(function(){
    	loadList();	
    });
</script>
</body>
</html>