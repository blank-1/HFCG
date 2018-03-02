<%@ page import="com.xt.cfp.core.constants.VoucherConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<head>
</head>
<body>
<div class="" style="float:left; width:1175px;margin:0 10px 5px 10px;">

</div>

<div id="coltd_list" class="container-fluid" style="padding: 5px 0px 0px 10px">
	<div id="coltd_list_toolbar" style="height:auto">

		<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addColtd(${param.enterpriseId});">新增</a>
	</div>

	<table id="coltd_list_list"></table>
</div>

<script language="javascript">

	function toQueryColtd(){
		$('#coltd_list_list').datagrid('reload', {});
	}

	/**
	 * 执行：列表加载。
	 */
	function loadColtdList(){
		$("#coltd_list_list").datagrid({
			title: '合作公司列表',
			url: '${ctx}/jsp/enterprise/loan/coLtdList?enterpriseId='+'${param.enterpriseId}',
			pagination: true,
			pageSize: 10,
			singleSelect: true,
			rownumbers: true,
			toolbar: '#coltd_list_toolbar',
			columns:[[
				{field:'companyName', width:100,title:'公司名称'},
				{field:'organizationCode', width:100,title:'组织机构编码'},
				{field:'legalPerson', width:60,title:'法人'},
				{field:'contactPerson', width:60,title:'联系人'},
				{field:'contactInformation', width:60,title:'联系方式'},
				{field:'addressStr', width:160,title:'公司所在地'},
				{field:'address', width:160,title:'公司地址'},
				{field:'state', width:60,title:'状态' ,formatter:statusFormatter},
				{field:'action',title:'操作',width:80,align:'center',
					formatter:function(value,row,index){

						var result = "";
						result += "<a class='label label-info' onclick='editColtd(${param.enterpriseId}," + row.coId + ")'>编辑</a> &nbsp;";
						if(row.state == '0')
							result += "<a class='label label-info' onclick='delColtd(" + row.coId + ")'>禁用</a> &nbsp;";
						else
							result += "<a class='label label-info' onclick='startUseColtd(" + row.coId + ")'>启用</a> &nbsp;";
						return result;
					}
				}
			]],
			onBeforeLoad: function (value, rec) {
				var adminState = $(this).datagrid("getColumnOption", "status");
				if (adminState) {
					adminState.formatter = function (value, rowData, rowIndex) {
						if (value == '0') {
							return "<font style='color: green;'>正常</font>";
						} else if(value == '2') {
							return "<font style='color: red;'>禁用</font>";
						}
					}
				}
			}
		});
	}

	function statusFormatter(val){
		if (val == undefined || val == "")
			return "";
		if(val == '0')
			return "正常";
		else
			return '禁用';
	}

	/**
	 * 禁用
	 * */
	function delColtd(id){
		$.ajax({
			url:'${ctx}/jsp/enterprise/loan/deleteColtd?colId='+id,
			type:"POST",
			success:function(msg){
				if(msg=='success'){
					$.messager.alert('提示', '操作成功！！', 'info',function(){
						toQueryColtd();
					});
				}else {
					$.messager.alert('提示', '操作失败！！', 'info');
				}
			}
		});
	}

	/**
	 * 解禁
	 * */
	function startUseColtd(id){
		$.ajax({
			url:'${ctx}/jsp/enterprise/loan/free?colId='+id,
			type:"POST",
			success:function(msg){
				if(msg=='success'){
					$.messager.alert('提示', '操作成功！！', 'info',function(){
						toQueryColtd();
					});
				}else {
					$.messager.alert('提示', '操作失败！！', 'info');
				}
			}
		});
	}

	/**
	 * 执行：弹出添加层。
	 */
	function addColtd(enterpriseId) {
		$("#coltd_list").after("<div id='addColtd' style=' padding:10px; '></div>");
		$("#addColtd").dialog({
			resizable: false,
			title: '新增合作公司',
			href: '${ctx}/jsp/enterprise/loan/toAddCoLtd?enterpriseId='+enterpriseId,
			width: 700,
			height: 480,
			modal: true,
			top: 80,
			left: 200,
			buttons: [
				{
					text: '保存',
					iconCls: 'icon-ok',
					handler: function () {
						var form = $("#addColtd").contents().find("#addColtd_form");
						//企业类型（如果为资管公司则不做表单验证）
						var type = $("#d_enterpriseType").val();
						var validate = false;
						if(type == 2){
							validate = true;
						}else {
							validate = form.form('validate');
						}
						if(validate){
							$.ajax({
								url:'${ctx}/jsp/enterprise/loan/saveColtd',
								data:form.serialize(),
								type:"POST",
								success:function(msg){
									if(msg=='success'){
										$.messager.alert('提示', '保存成功！！', 'info',function(){
											$("#addColtd").dialog('close');
											toQueryColtd();
										});
									}else{
										$.messager.alert('提示', '保存失败！！'+msg, 'info');
									}
								}
							});
						}
					}
				},
				{
					text: '取消',
					iconCls: 'icon-cancel',
					handler: function () {
						$("#addColtd").dialog('close');
					}
				}
			],
			onClose: function () {
				$(this).dialog('destroy');
			}
		});
	}
	/**
	 * 执行：修改添加层。
	 */
	function editColtd(enterpriseId,id) {
		$("#coltd_list").after("<div id='addColtd' style=' padding:10px; '></div>");
		$("#addColtd").dialog({
			resizable: false,
			title: '编辑合作公司',
			href: '${ctx}/jsp/enterprise/loan/toAddCoLtd?colId='+id+'&enterpriseId='+enterpriseId,
			width: 700,
			height: 480,
			modal: true,
			top: 80,
			left: 200,
			buttons: [
				{
					text: '保存',
					iconCls: 'icon-ok',
					handler: function () {
						var form = $("#addColtd").contents().find("#addColtd_form");
						var validate = form.form('validate');
						if(validate){
							$.ajax({
								url:'${ctx}/jsp/enterprise/loan/saveColtd',
								data:form.serialize(),
								type:"POST",
								success:function(msg){
									if(msg=='success'){
										$.messager.alert('提示', '保存成功！！', 'info',function(){
											$("#addColtd").dialog('close');
											toQueryColtd();
										});
									}else{
										$.messager.alert('提示', '保存失败！！'+msg, 'info');
									}
								}
							});
						}
					}
				},
				{
					text: '取消',
					iconCls: 'icon-cancel',
					handler: function () {
						$("#addColtd").dialog('close');
					}
				}
			],
			onClose: function () {
				$(this).dialog('destroy');
			}
		});
	}

	$(function(){
		loadColtdList();
	});
</script>

</body>