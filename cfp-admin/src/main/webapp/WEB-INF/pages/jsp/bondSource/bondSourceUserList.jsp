<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<body>

    <div class="cf" style="float:left; width:1100px;margin:0 10px 5px 10px;">
        <fieldset style="height:65px">
            <legend>查询条件</legend>
            <form name="bondSourceUserQuery" id="bondSourceUserQuery" action="${ctx}/bondSource/bondSourceUserlist" class="fitem"
                  autocomplete="off">
                <input type="hidden" id="bondSourceId"  name="bondSourceId" value="${bondSourceId}">
                <table>
                    <tbody>
                    <tr>
                        <td nowrap="nowrap"><label>原始债权人姓名：</label>
                        </td>
                        <td align="center"><input id="bondName"
                                   name="bondName" value=""
                                   type="text" />
                        </td>
                        <td nowrap="nowrap"><label>原始债权人用户名：</label>
                        </td>
                        <td><input id="loginName" name="loginName"
                                    value=""
                                   type="text" />
                        </td>
                        <td nowrap="nowrap"><label>状态：</label>
                        </td>
                        <td>
                            <select class="easyui-combobox" id="user_status" style="width:150px" name="bondSource.status" >
                                <option value="" >全部</option>
                                <option value="0" >正常</option>
                                <option value="2">禁用</option>
                            </select>
                        </td>
                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQueryBondSourceUser();" iconCls="icon-search">查询</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>
    
    <!-- test -->
      	<div class="easyui-window" title="增加原始债权人" id="addSubareaWindow"  closed="true" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px" >
		<table id="newdatag"></table>
	    </div>
	    
	    <div id="dd11" class="easyui-dialog" title="增加原始债权人" closed="true" style="width:400px;height:200px;" 
	    data-options="iconCls:'icon-save',resizable:true,modal:true" >  
	  	      <form name="bondSourceUserQuery" id="bondSourceUserQueryDialog" action="${ctx}/bondSource/bondSourceUserlist" class="fitem" autocomplete="off">
              <%--   <input type="hidden" id="bondSourceId"  name="bondSourceId" value="${bondSourceId}"> --%>
                <table> <tbody> <tr>
                        <td nowrap="nowrap"><label>原始债权人姓名：</label> </td>
                        <td align="center"><input id="bondNames" name="bondNames" value="" type="text" /> </td>
                        <td nowrap="nowrap"><label>原始债权人用户名：</label></td>
                        <td><input id="loginNames" name="loginNames" value="" type="text" /> <td nowrap="nowrap">
                        <div style="margin:10px;"><a href="javascript:void(0);" class="easyui-linkbutton" onclick="javascript:toQueryBondSourceUserDialog();" iconCls="icon-search">查询</a></div>
                        </td> </tr></tbody>
                </table>
                		  <table id="newdatagDialog"></table>
     		    </form>
       </div>  
    <!-- end test -->

    <div id="bondSource_user_list" class="container-fluid" style="padding: 5px 0px 0px 10px">
   
        <div id="bondSource_user_list_toolbar" style="height:auto">

            <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addBondSourceUser(${bondSourceId});">新增</a>
              <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addBondSourceUserListByDialog();">增加原始债权人列表</a>
            </a>
        </div>

    </div>
    <table id="bondSource_user_list_list"></table>

    <script language="javascript">
        function toQueryBondSourceUser(){
            $('#bondSource_user_list_list').datagrid('reload', {
                'bondName' : Trim($("#bondName").val(),"g"),
                'loginName' : Trim($("#loginName").val(),"g"),
                'bondSourceId' : Trim($("#bondSourceId").val(),"g"),
                'status':Trim($("#user_status").combobox("getValue"),"g")
            });
        }
        
        function toQueryBondSourceUserDialog(){
        /* 	 $("#bondSourceUserQueryDialog").form('clear'); */
            $('#newdatagDialog').datagrid('reload', {
                'bondNames' : Trim($("#bondNames").val(),"g"),
                'loginNames' : Trim($("#loginNames").val(),"g"),
               /*  'bondSourceId' : Trim($("#bondSourceIdsss").val(),"g"), */
                'status':Trim($("#user_status").combobox("getValue"),"g")
            });
            $('#newdatagDialog').datagrid('clearSelections'); 
        }
        /**
         * 执行：列表加载。
         */
        function loadUserList(){
            $("#bondSource_user_list_list").datagrid({
                title: '原始债权人列表',
                url: '${ctx}/bondSource/bondSourceUserlist?bondSourceId='+'${bondSourceId}',
                pagination: true,
                pageSize: 10,
//                height: document.body.clientHeight * 0.5,
                singleSelect: true,
                rownumbers: true,
                toolbar: '#bondSource_user_list_toolbar',
                columns:[[
                    {field:'bondName', width:100,title:'原始债权人姓名'},
                    {field:'loginName', width:100,title:'原始债权人用户名'},
                    {field:'avilableValue', width:60,title:'可用金额'},

                    {field:'freezeValue', width:60,title:'冻结金额'},
                    {field:'value', width:60,title:'账户余额'},
                    {field:'borrowCount', width:60,title:'借款个数'},
                    {field:'mobileNo', width:80,title:'联系电话'},
                    {field:'contactPhone', width:100,title:'邮箱'},
                    {field:'status', width:60,title:'状态'},
                    {field:'action',title:'操作',width:80,align:'center',
                        formatter:function(value,row,index){

                            var result = "";
                            result += "<a class='label label-info' onclick='editBondSourceUser(${bondSourceId}," + row.userSourceId + ")'>编辑</a> &nbsp;";
                            if(row.status == '0')
                                result += "<a class='label label-info' onclick='delBondSourceUser(" + row.userId + ")'>禁用</a> &nbsp;";
                            else
                                result += "<a class='label label-info' onclick='startUseBondSourceUser(" + row.userId + ")'>启用</a> &nbsp;";
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


        /**
         * 禁用
         * */
        function delBondSourceUser(id){
            $.ajax({
                url:'${ctx}/bondSource/deleteUser?userId='+id,
                type:"POST",
                success:function(msg){
                    if(msg=='success'){
                        $.messager.alert('提示', '操作成功！！', 'info',function(){
                            toQueryBondSourceUser();
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
        function startUseBondSourceUser(id){
            $.ajax({
                url:'${ctx}/bondSource/free?userId='+id,
                type:"POST",
                success:function(msg){
                    if(msg=='success'){
                        $.messager.alert('提示', '操作成功！！', 'info',function(){
                            toQueryBondSourceUser();
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
        function addBondSourceUser(bondSourceId) {
            $("#bondSource_user_list").after("<div id='addBondSourceUser' style=' padding:10px; '></div>");
            $("#addBondSourceUser").dialog({
                resizable: false,
                title: '新增原始债权人',
                href: '${ctx}/bondSource/addBondUser?bondSourceId='+bondSourceId,
                width: 700,
                height: 350,
                modal: true,
                top: 100,
                left: 200,
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-ok',
                        handler: function () {
                            var form = $("#addBondSourceUser").contents().find("#addBindSourceUser_form");
                            var validate = form.form('validate');
                            if(validate){
                                $.ajax({
                                    url:'${ctx}/bondSource/saveUser',
                                    data:form.serialize(),
                                    type:"POST",
                                    success:function(msg){
                                        if(msg=='success'){
                                            $.messager.alert('提示', '保存成功！！', 'info',function(){
                                                $("#addBondSourceUser").dialog('close');
                                                toQueryBondSourceUser();
                                                toQueryBondSource();
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
                            $("#addBondSourceUser").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }


        function addBondSourceUserFormHF(bondSourceId) {
            $("#bondSource_user_list").after("<div id='addBondSourceUserFormHF' style=' padding:10px; '></div>");
            $("#addBondSourceUserFormHF").dialog({
                resizable: false,
                title: '新增原始债权人',
                href: '${ctx}/bondSource/addBondUserformHF?bondSourceId='+bondSourceId,
                width: 700,
                height: 350,
                modal: true,
                top: 100,
                left: 200,
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-ok',
                        handler: function () {
                            var form = $("#addBondSourceUser").contents().find("#addBindSourceUser_form");
                            var validate = form.form('validate');
                            if(validate){
                                $.ajax({
                                    url:'${ctx}/bondSource/saveUserToLocal',
                                    data:form.serialize(),
                                    type:"POST",
                                    success:function(msg){
                                        if(msg=='success'){
                                            $.messager.alert('提示', '保存成功！！', 'info',function(){
                                                $("#addBondSourceUser").dialog('close');
                                                toQueryBondSourceUser();
                                                toQueryBondSource();
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
                            $("#addBondSourceUser").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }




        /**
        * newdatagDialog
        *dilog
        **/
     function   addBondSourceUserListByDialog(){
        	
    	 
    	 $("#bondSourceUserQueryDialog").form('clear');
    	  $('#dd11').dialog({   
    	         title: '原始债权人列表',   
    	         width: 705,   
    	         height: 400,   
    	         closed: false,   
    	         cache: false,   
    	         modal: true  
    	     });   
        	
    	  getDataGridByDailog();
        }
        /**
         *显示原始债权人列表
         *选中并新增在新的渠道中
         **/
      function  getDataGridByDailog(){
    	  $("#newdatagDialog").datagrid({
              /*  title: '原始债权人列表', */
               url: '${ctx}/bondSource/bondUserSelect?randnum='+Math.floor(Math.random()*1000000),
               pagination: true,
               pageSize: 10,
               /* fit:true, */
               width:680,
             /*   height: 350,  */
              height: document.body.clientHeight * 0.5,
              queryParams:{ bondNames: Trim($("#bondNames").val(),"g"),loginNames:Trim($("#loginNames").val(),"g")},
                 singleSelect: true,
           //    rownumbers: true, 
          //     toolbar: '#bondSource_user_list_toolbar',
               columns:[[
                   {field:'userId', width:100,title:'userId',hidden:true},    
                   {field:'userSourceId', width:100,title:'userSourceId',hidden:true},    
                   {field:'bondNames', width:100,title:'原始债权人姓名'},
                   {field:'loginNames', width:100,title:'原始债权人用户名'},
                   {field:'avilableValue', width:60,title:'可用金额'},

                   {field:'freezeValue', width:60,title:'冻结金额'},
                   {field:'value', width:60,title:'账户余额'},
                   {field:'borrowCount', width:60,title:'借款个数'},
                   {field:'mobileNo', width:80,title:'联系电话'},
                   {field:'contactPhone', width:100,title:'邮箱'},
                   {field:'status', width:60,title:'状态'}
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
               },
    	   onDblClickRow: function (rowIndex, rowData) {  
    		     $.ajax({
                    url:'${ctx}/bondSource/saveUserRelation',
                    data:{userId:rowData.userId,bondSourceId:'${bondSourceId}',userSourceId:rowData.userSourceId},  
                    type:"POST",
                    success:function(msg){
                        if(msg=='SCUESS'){
                            $.messager.alert('提示', '保存成功！！', 'info',function(){
                           	 
                           	    $("#dd11").dialog('close');

                                toQueryBondSourceUser();
                                toQueryBondSource();
                            });
                        }else if(msg=='ALEADY'){
                            $.messager.alert('提示', '保存失败！！该用户已添加', 'info');
                        }
                        else
                            $.messager.alert('提示', '保存失败！！', 'info');
                         /*   $("#newdatagDialog").dialog('close'); */
                    }
                }); 
    		  } 
           });
        }
        /**
         * 执行：修改添加层。
         */
        function editBondSourceUser(bondSourceId,id) {
            $("#bondSource_user_list").after("<div id='addBondSourceUser' style=' padding:10px; '></div>");
            $("#addBondSourceUser").dialog({
                resizable: false,
                title: '编辑原始债权人',
                href: '${ctx}/bondSource/addBondUser?userSourceId='+id+'&bondSourceId='+bondSourceId,
                width: 700,
                height: 350,
                modal: true,
                top: 100,
                left: 200,
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-ok',
                        handler: function () {
                            var form = $("#addBondSourceUser").contents().find("#addBindSourceUser_form");
                            var validate = form.form('validate');
                            if(validate){
                                $.ajax({
                                    url:'${ctx}/bondSource/saveUser',
                                    data:form.serialize(),
                                    type:"POST",
                                    success:function(msg){
                                        if(msg=='success'){
                                            $.messager.alert('提示', '保存成功！！', 'info',function(){
                                                $("#addBondSourceUser").dialog('close');
                                                toQueryBondSourceUser();
                                                toQueryBondSource();
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
                            $("#addBondSourceUser").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }

        $(function(){
            loadUserList();
        });
    </script>

</body>