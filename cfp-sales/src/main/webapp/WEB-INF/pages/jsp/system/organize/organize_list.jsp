<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="../../../common/common.jsp"%>

<body>
<%@include file="../../../common/header.jsp" %>
<div style="width: 100%;margin-left: 10px;">系统管理>>组织管理</div>

	<!-- easyUI-layout begin -->
	<div class="easyui-layout" style="width:100%;height:85%;">
		<div data-options="region:'west',split:true" title="组织树" style="width:480px;">
			<!-- 内嵌的layout，用来区分按钮和树结构 -->
			<div data-options="region:'north',split:true,border:false" style="height:30px">
				<div id="function_functionsList_toolbar" style="height:auto">
		<%--			<a href="javascript:void(0);" class="easyui-linkbutton"
						iconCls="icon-add" plain="true" onclick="addOrganize(1);">新增同级</a>--%>
					<a href="javascript:void(0);" class="easyui-linkbutton"
						iconCls="icon-add" plain="true" onclick="addOrganize(2);">新增子级</a>
				</div>
			</div>
			<!-- easyUI-TreeGrid -->
			<div data-options="region:'center',border:false">
				<table id="showOrganizeTree" style="width:800px;">
					<thead>
						<th data-options="field:'name'" width="auto">&nbsp;</th>
					</thead>
				</table>
			</div>
		</div>
		<!-- 节点详情 -->
		<div data-options="region:'center'" id="functionDetail" title="详细信息" style="padding:15px">
			 
			<div id="detail" class="container-fluid" style="padding: 5px 0px 0px 10px">
			    <div id="showOrganize" class="easyui-tabs" style="width:auto;height:auto">
			    	<div title="组织机构信息" data-options="href:''" style="padding:10px"></div>
			    </div>
			</div> 
		</div>
	</div>
	<!-- easyUI-layout end -->

<script language="javascript">
		//获取当前正在操作的权限信息
		var editingId;
		var editingName;
		var editingPid;
		
		//单击事件(添加+详情+编辑)
		function getEditingInfo() {
			var root = $('#showOrganizeTree').treegrid('getRoot');
			var row = $('#showOrganizeTree').treegrid('getSelected');
			if(root != null) {	//初始化的时候，无根节点
				if (row) {
					editingId = row.id;
					editingName = row.name;
					editingPid = row._parentId;
					if(editingPid == null) {
						editingPid = 0;
					}
				} else {
					editingId = -1;
				}
			}else {
				editingId = 0;
				editingName = '';
				editingPid = 0;
			}
		}
		
		//TreeGrid的相关配置
		$('#showOrganizeTree')
			.treegrid(
				{
					url : '${ctx}/jsp/system/organize/tree',
					method : 'get',
					collapsible : true,
					fitColumns : true,
					animate : true,
					idField : 'id',
					treeField : 'name',
					onClickRow : function() {
						getEditingInfo();
						 //权限信息
						 $("#showOrganize").tabs('update', {
						 tab: $("#showOrganize").tabs('getTab',"组织机构信息"),
						 options: {
						 href: "${ctx}/jsp/system/organize/to_organize_show?functionId="+editingId
						 }
						 });
					}

				});
		// 添加事件
		function addOrganize(flag) {
			getEditingInfo();
			//判断是否选中节点 
			if(editingId==-1){
				alert("请先选择权限节点");
				return;
			}
			$("#showOrganize").tabs('update', {
				tab: $("#showOrganize").tabs('getTab',"组织机构信息"),
				options: {
					href: "${ctx}/jsp/system/organize/to_organize_add?editingId="+editingId+"&editingPid="+editingPid+"&flag="+flag
				}
			});
			$("#showOrganize").tabs('select',"组织机构信息");
		}

</script>

</body>