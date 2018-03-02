<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<head>
</head>
<body>

    <div class="cf" style="float:left; width:900px;margin:0 10px 5px 10px;">
        <fieldset style="height:65px">
            <legend>查询条件</legend>
            <form name="bondSourceQuery" id="bondSourceQuery" action="${ctx}/bondSource/list" class="fitem"
                  autocomplete="off">
                <table>
                    <tbody>
                    <tr>
                        <td nowrap="nowrap"><label>担保公司名称：</label>
                        </td>
                        <td align="center"><input id="companyName"
                                   name="guaranteeCompany.companyName" value=""
                                   type="text" />
                        </td>
                        <td nowrap="nowrap"><label>担保公司所在地：</label>
                        </td>
                        <td><input id="companyLocation"
                                   name="guaranteeCompany.companyLocation" value=""
                                   type="text" />
                        </td>
                        <td nowrap="nowrap"><label>状态：</label>
                        </td>
                        <td>
                            <select class="easyui-combobox" id="status" style="width:150px" name="guaranteeCompany.status" >
                                <option value="" >全部</option>
                                <option value="0" >正常</option>
                                <option value="1">禁用</option>
                            </select>
                        </td>
                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQueryGuaranteeCompany();" iconCls="icon-search">查询</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>

    <div id="guaranteeCompany_list" class="container-fluid" style="padding: 5px 0px 0px 10px">
        <div id="guaranteeCompany_list_toolbar" style="height:auto">

            <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addGuaranteeCompany();">新增</a>
        </div>

        <table id="guaranteeCompany_list_list"></table>
    </div>

    <div id="detailGuaranteeCompany">

    </div>
    <script language="javascript">

        function withDraw(id){
            $("#guaranteeCompany_list").after("<div id='withDraw' style=' padding:10px; '></div>");
            $("#withDraw").dialog({
                resizable: false,
                title: '提现',
                href: '${ctx}/guaranteeCompany/toWithDraw?companyId='+id,
                width: 700,
                height: 520,
                modal: true,
                top: 100,
                left: 200,
                buttons: [
                    {
                        text: '提交申请',
                        iconCls: 'icon-ok',
                        handler: function () {
                            var form = $("#withDraw").contents().find("#withDraw_form");
                            var validate = form.form('validate');
                            if(validate){
                                $.ajax({
                                    url:'${ctx}/bondSource/withDraw',
                                    data:form.serialize(),
                                    type:"POST",
                                    success:function(msg){
                                        if(msg=='success'){
                                            $.messager.alert('提示', '提交成功！！', 'info',function(){
                                                $("#withDraw").dialog('close');
                                                toQueryGuaranteeCompany();
                                            });
                                        }else{
                                            $.messager.alert('提示', '提交失败！'+msg, 'info');
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
                            $("#withDraw").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }

        function income(id){
            $("#guaranteeCompany_list").after("<div id='guaranteeCompany_income' style=' padding:10px; '></div>");
            $("#guaranteeCompany_income").dialog({
                resizable: false,
                title: '充值',
                href: '${ctx}/guaranteeCompany/toIncome?companyId='+id,
                width: 700,
                height: 400,
                modal: true,
                top: 100,
                left: 200,
                buttons: [
                    {
                        text: '充值',
                        iconCls: 'icon-ok',
                        handler: function () {
                            var form = $("#guaranteeCompany_income").contents().find("#guaranteeCompany_income_form");
                            var validate = form.form('validate');
                            if(validate){
                                 $.ajax({
                                    url:'${ctx}/bondSource/income',
                                    data:form.serialize(),
                                    type:"POST",
                                    success:function(msg){
                                        if(msg=='success'){
                                            $.messager.alert('提示', '充值成功！！', 'info',function(){
                                                $("#guaranteeCompany_income").dialog('close');
                                                toQueryGuaranteeCompany();
                                            });
                                        }else{
                                            $.messager.alert('提示', '充值失败！'+msg, 'info');
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
                            $("#guaranteeCompany_income").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }


        function toQueryGuaranteeCompany(){
            $('#guaranteeCompany_list_list').datagrid('reload', {
                'companyName' : Trim($("#companyName").val(),"g"),
                'companyLocation' : Trim($("#companyLocation").val(),"g"),
                'status':Trim($("#status").combobox("getValue"),"g")
            });
        }
        /**
         * 执行：列表加载。
         */
        function loadList(){
            $("#guaranteeCompany_list_list").datagrid({
                idField: 'companyId',
                title: '渠道列表',
                url: '${ctx}/guaranteeCompany/list',
                pagination: true,
                pageSize: 10,
                width: document.body.clientWidth * 0.97,
                height: document.body.clientHeight * 0.6,
                singleSelect: true,
                rownumbers: true,
                toolbar: '#guaranteeCompany_list_toolbar',
                columns:[[
                    {field:'companyName', width:100,title:'担保公司名称'},
                    {field:'companyLocation', width:120,title:'担保公司所在地'},
                    {field:'contactPersion', width:80,title:'联系人'},
                    {field:'contactPhone', width:80,title:'联系电话'},
                    {field:'avilableValue', width:60,title:'可用金额'},
                    {field:'freezeValue', width:60,title:'冻结金额'},
                    {field:'value', width:60,title:'账户余额'},
                    {field:'borrowCount', width:60,title:'借款个数'},
                    {field:'status', width:80,title:'状态'},
                    {field:'action',title:'操作',width:200,align:'center',
                        formatter:function(value,row,index){

                            var result = "";
                            if(row.status == '0'){
                                result+="<a id='withDrawButton' class='label label-info' onclick='income(" +  row.companyId + ")'>充值</a> &nbsp;";
                                result += "<a id='rechargeButton' class='label label-info' onclick='withDraw(" + row.companyId  + ")'>提现</a> &nbsp;";
                                result += "<a class='label label-important' onclick='editGuaranteeCompany(" + row.companyId + ")'>编辑</a> &nbsp;";
                            }
                            if(row.status == '0')
                                result += "<a class='label' onclick='delGuaranteeCompany(" + row.companyId + ")'>禁用</a> &nbsp;";
                            else
                                result += "<a class='label label-success' onclick='startUse(" + row.companyId + ")'>启用</a> &nbsp;";
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
                            } else if(value == '1') {
                                return "<font style='color: red;'>禁用</font>";
                            }
                        }
                    }
                    var sourceName = $(this).datagrid("getColumnOption", "companyName");
                    if (sourceName) {
                        sourceName.formatter = function (value, rowData, rowIndex) {
                            return "<a onclick='showDetails(" + rowData.companyId  + ")'>"+value+"</a>";
                        }
                    }
                }
            });
        }

        /**
         * 禁用
         * */
        function delGuaranteeCompany(id){
            $.ajax({
                url:'${ctx}/guaranteeCompany/delete?companyId='+id,
                type:"POST",
                success:function(msg){
                    if(msg=='success'){
                        $.messager.alert('提示', '操作成功！！', 'info',function(){
                            toQueryGuaranteeCompany();
                        });
                    }else {
                        $.messager.alert('提示', '操作失败！！', 'info');
                    }
                }
            });
        }

        /**
         * 启用
         * */
        function startUse(id){
            $.ajax({
                url:'${ctx}/guaranteeCompany/startUse?companyId='+id,
                type:"POST",
                success:function(msg){
                    if(msg=='success'){
                        $.messager.alert('提示', '操作成功！！', 'info',function(){
                            toQueryGuaranteeCompany();
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
        function addGuaranteeCompany() {
            $("#guaranteeCompany_list").after("<div id='addGuaranteeCompany' style=' padding:10px; '></div>");
            $("#addGuaranteeCompany").dialog({
                resizable: false,
                title: '新增担保公司',
                href: '${ctx}/guaranteeCompany/add',
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
                            var form = $("#addGuaranteeCompany").contents().find("#addGuaranteeCompany_form");
                            var validate = form.form('validate');
                            if(validate){
                                $.ajax({
                                    url:'${ctx}/guaranteeCompany/save',
                                    data:form.serialize(),
                                    type:"POST",
                                    success:function(msg){
                                        if(msg=='success'){
                                            $.messager.alert('提示', '保存成功！！', 'info',function(){
                                                $("#addGuaranteeCompany").dialog('close');
                                                toQueryGuaranteeCompany();
                                            });
                                        }

                                        else
                                            $.messager.alert('提示', '保存失败！！', 'info');
                                    }
                                });
                            }
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#addGuaranteeCompany").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }


        /**
         * 执行：弹出修改层。
         */
        function editGuaranteeCompany(index) {
            $("#guaranteeCompany_list").after("<div id='addGuaranteeCompany' style=' padding:10px; '></div>");
            $("#addGuaranteeCompany").dialog({
                resizable: false,
                title: '编辑担保公司',
                href: '${ctx}/guaranteeCompany/add?companyId='+index,
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
                            var form = $("#addGuaranteeCompany").contents().find("#addGuaranteeCompany_form");
                            var validate = form.form('validate');
                            if(validate){
                                $.ajax({
                                    url:'${ctx}/guaranteeCompany/save',
                                    data:form.serialize(),
                                    type:"POST",
                                    success:function(msg){
                                        if(msg=='success'){
                                            $.messager.alert('提示', '保存成功！！', 'info',function(){
                                                $("#addGuaranteeCompany").dialog('close');
                                                toQueryGuaranteeCompany();
                                            });
                                        }

                                        else
                                            $.messager.alert('提示', '保存失败！！', 'info');
                                    }
                                });
                            }
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#addGuaranteeCompany").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }

        /**
         * 详情：弹出详情层。
         */
        function showDetails(id){
            $("#detailGuaranteeCompany").after("<div id='detailGuaranteeCompany_1' style=' padding:10px; '></div>");
            $('#detailGuaranteeCompany_1').dialog({
                title : '担保公司详情',
                href: '${ctx}/guaranteeCompany/detail?companyId='+id,
                width: 1000,
                height: 500,
                modal:true,
                maximizable : true,
                minimizable : false,
                collapsible : false,
                resizable : true,
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }

        $(function(){
            loadList();
        });
    </script>

</body>