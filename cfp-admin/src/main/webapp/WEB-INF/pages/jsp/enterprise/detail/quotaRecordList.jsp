<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="../../../common/common.jsp"%>
<html>
<head>
<title></title>
</head>
<body>
	<div id="quotaRecordList" class="container-fluid" style="padding: 5px 0px 0px 10px">

		<table id="quota_record_list"></table>
		
	</div>

	<script type="text/javascript">
		$(function(){
			loadCardList();
		});

		function loadCardList(){
			$("#quota_record_list").datagrid({
				idField: 'quotaRecordId',
				title: '额度记录列表',
				url: '${ctx}/jsp/enterprise/quotaRecordList?enterpriseId=${enterpriseId}',
				width: document.body.clientWidth * 0.97,
				height: document.body.clientHeight * 0.6,
				singleSelect: true,
				rownumbers: true,
				columns:[[
					{field:'contractBegin', width:150,title:'合同开始期限'},
					{field:'contractEnd', width:150,title:'合同结束期限'},
					{field:'singleMaximumAmount', width:150,title:'单笔最大额度'},
					{field:'annualMaximumLimit', width:150,title:'年度最大限额'},
					{field:'lastUpdateTime', width:150,title:'最后修改时间'}
					
					/*
					,
					{field:'mobile1',title:'操作',width:200,align:'center',
						formatter:function(value,row,index){
							var result = "<a class='label label-info' onclick='toEditQuotaRecord(" + row.quotaRecordId + ")'>编辑</a> &nbsp;";
							return result;
						}
					}
					*/
					
				]],
	           	onBeforeLoad: function (value, rec) {
	         	   
		               // 单笔最大额度
		               var singleMaximumAmount = $(this).datagrid("getColumnOption", "singleMaximumAmount");
		               if (singleMaximumAmount) {
		            	   singleMaximumAmount.formatter = function (value, rowData, rowIndex) {
		            		   if(value){
		            			   return formatNum(value,2);
		            		   }
		                   }
		               }
		               
		               // 年度最大限额
		               var annualMaximumLimit = $(this).datagrid("getColumnOption", "annualMaximumLimit");
		               if (annualMaximumLimit) {
		            	   annualMaximumLimit.formatter = function (value, rowData, rowIndex) {
		            		   if(value){
		            			   return formatNum(value,2);
		            		   }
		                   }
		               }
		               
		               // 合同开始期限
		               var contractBegin = $(this).datagrid("getColumnOption", "contractBegin");
		               if (contractBegin) {
		            	   contractBegin.formatter = function (value, rowData, rowIndex) {
		            		   if(value != null && value != ""){
		            			   return getDateStr(new Date(value));
		            		   }
		                   }
		               }
		               
		               // 合同结束期限
		               var contractEnd = $(this).datagrid("getColumnOption", "contractEnd");
		               if (contractEnd) {
		            	   contractEnd.formatter = function (value, rowData, rowIndex) {
		            		   if(value != null && value != ""){
		            			   return getDateStr(new Date(value));
		            		   }
		                   }
		               }
		               
		               // 最后修改时间
		               var lastUpdateTime = $(this).datagrid("getColumnOption", "lastUpdateTime");
		               if (lastUpdateTime) {
		            	   lastUpdateTime.formatter = function (value, rowData, rowIndex) {
		            		   if(value != null && value != ""){
		            			   return getDateTimeStr(new Date(value));
		            		   }
		                   }
		               }
		              
		               
	           }
			});
		}

	</script>
</body>
</html>