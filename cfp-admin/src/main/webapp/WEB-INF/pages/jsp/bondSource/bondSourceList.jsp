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
                        <td nowrap="nowrap"><label>渠道名称：</label>
                        </td>
                        <td align="center"><input id="sourceName"
                                   name="bondSource.sourceName" value=""
                                   type="text" />
                        </td>
                        <td nowrap="nowrap"><label>渠道公司所在地：</label>
                        </td>
                        <td><input id="location"
                                   name="bondSource.locatioin" value=""
                                   type="text" />
                        </td>
                        <td nowrap="nowrap"><label>渠道状态：</label>
                        </td>
                        <td>
                            <select class="easyui-combobox" id="status" style="width:150px" name="bondSource.status" >
                                <option value="" >全部</option>
                                <option value="0" >正常</option>
                                <option value="1">禁用</option>
                            </select>
                        </td>
                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQueryBondSource();" iconCls="icon-search">查询</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>

    <div id="bondSource_list" class="container-fluid" style="padding: 5px 0px 0px 10px">
        <div id="bondSource_list_toolbar" style="height:auto">

            <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addBondSource();">新增</a>
        </div>

        <table id="bondSource_list_list"></table>
    </div>

    <div id="detailBondSource">

    </div>
    <script language="javascript">

        function withDraw(id){
            $("#bondSource_list").after("<div id='withDraw' style=' padding:10px; '></div>");
            $("#withDraw").dialog({
                resizable: false,
                title: '提现',
                href: '${ctx}/bondSource/toWithDraw?bondSourceId='+id,
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
                                                toQueryBondSource();
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
            $("#bondSource_list").after("<div id='income' style=' padding:10px; '></div>");
            $("#income").dialog({
                resizable: false,
                title: '充值',
                href: '${ctx}/bondSource/toIncome?bondSourceId='+id,
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
                            var form = $("#income").contents().find("#income_form");
                            var validate = form.form('validate');
                            if(validate){
                                 $.ajax({
                                    url:'${ctx}/bondSource/income',
                                    data:form.serialize(),
                                    type:"POST",
                                    success:function(msg){
                                        if(msg=='success'){
                                            $.messager.alert('提示', '充值成功！！', 'info',function(){
                                                $("#income").dialog('close');
                                                toQueryBondSource();
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
                            $("#income").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }


        function toQueryBondSource(){
            $('#bondSource_list_list').datagrid('reload', {
                'sourceName' : Trim($("#sourceName").val(),"g"),
                'locatioin' : Trim($("#location").val(),"g"),
                'status':Trim($("#status").combobox("getValue"),"g")
            });
        }
        /**
         * 执行：列表加载。
         */
        function loadList(){
            $("#bondSource_list_list").datagrid({
                idField: 'bondSourceId',
                title: '渠道列表',
                url: '${ctx}/bondSource/list',
                pagination: true,
                pageSize: 10,
                width: document.body.clientWidth * 0.97,
                height: document.body.clientHeight * 0.6,
                singleSelect: true,
                rownumbers: true,
                toolbar: '#bondSource_list_toolbar',
                columns:[[
                    {field:'sourceName', width:100,title:'渠道名称'},
                    {field:'locatioin', width:120,title:'渠道公司所在地'},
                    {field:'contactPersion', width:80,title:'联系人'},
                    {field:'contactPhone', width:80,title:'联系电话'},
                    {field:'avilableValue', width:60,title:'可用金额'},
                    {field:'freezeValue', width:60,title:'冻结金额'},
                    {field:'value', width:60,title:'账户余额'},
                    {field:'borrowCount', width:60,title:'借款个数'},
                    {field:'bondUserCount', width:70,title:'原始债权人'},
                    {field:'status', width:80,title:'渠道状态'},
                    {field:'action',title:'操作',width:200,align:'center',
                        formatter:function(value,row,index){

                            var result = "";
                            if(row.status == '0'){
                                result+="<a id='withDrawButton' class='label label-info' onclick='income(" +  row.bondSourceId + ")'>充值</a> &nbsp;";
                                result += "<a id='rechargeButton' class='label label-info' onclick='withDraw(" + row.bondSourceId  + ")'>提现</a> &nbsp;";
                                result += "<a class='label label-important' onclick='editBondSource(" + row.bondSourceId + ")'>编辑</a> &nbsp;";
                            }
                            if(row.status == '0')
                                result += "<a class='label' onclick='delBondSource(" + row.bondSourceId + ")'>禁用</a> &nbsp;";
                            else
                                result += "<a class='label label-success' onclick='startUseBondSource(" + row.bondSourceId + ")'>启用</a> &nbsp;";
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
                    var sourceName = $(this).datagrid("getColumnOption", "sourceName");
                    if (sourceName) {
                        sourceName.formatter = function (value, rowData, rowIndex) {
                            return "<a onclick='showDetails(" + rowData.bondSourceId  + ")'>"+value+"</a>";
                        }
                    }
                }
            });
        }


        /**
         * 禁用
         * */
        function delBondSource(id){
            $.ajax({
                url:'${ctx}/bondSource/delete?bondSourceId='+id,
                type:"POST",
                success:function(msg){
                    if(msg=='success'){
                        $.messager.alert('提示', '操作成功！！', 'info',function(){
                            toQueryBondSource();
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
        function startUseBondSource(id){
            $.ajax({
                url:'${ctx}/bondSource/startUse?bondSourceId='+id,
                type:"POST",
                success:function(msg){
                    if(msg=='success'){
                        $.messager.alert('提示', '操作成功！！', 'info',function(){
                            toQueryBondSource();
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
        function addBondSource() {
            $("#bondSource_list").after("<div id='addBondSource' style=' padding:10px; '></div>");
            $("#addBondSource").dialog({
                resizable: false,
                title: '新增渠道',
                href: '${ctx}/bondSource/add',
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
                            var form = $("#addBondSource").contents().find("#addBindSource_form");
                            var validate = form.form('validate');
                            if(validate){
                                $.ajax({
                                    url:'${ctx}/bondSource/save',
                                    data:form.serialize(),
                                    type:"POST",
                                    success:function(msg){
                                        if(msg=='success'){
                                            $.messager.alert('提示', '保存成功！！', 'info',function(){
                                                $("#addBondSource").dialog('close');
                                                toQueryBondSource();
                                            });
                                        }else
                                            $.messager.alert('提示', '保存失败！！'+msg, 'info');
                                    }
                                });
                            }
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#addBondSource").dialog('close');
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
        function editBondSource(index) {
            $("#bondSource_list").after("<div id='addBondSource' style=' padding:10px; '></div>");
            $("#addBondSource").dialog({
                resizable: false,
                title: '编辑渠道',
                href: '${ctx}/bondSource/add?bondSourceId='+index,
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
                            var form = $("#addBondSource").contents().find("#addBindSource_form");
                            var validate = form.form('validate');
                            if(validate){
                                $.ajax({
                                    url:'${ctx}/bondSource/save',
                                    data:form.serialize(),
                                    type:"POST",
                                    success:function(msg){
                                        if(msg=='success'){
                                            $.messager.alert('提示', '保存成功！！', 'info',function(){
                                                $("#addBondSource").dialog('close');
                                                toQueryBondSource();
                                            });
                                        }

                                        else
                                            $.messager.alert('提示', '保存失败！！'+msg, 'info');
                                    }
                                });
                            }
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#addBondSource").dialog('close');
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
            $("#detailBondSource").after("<div id='detailBondSource_1' style=' padding:10px; '></div>");
            $('#detailBondSource_1').dialog({
                title : '渠道详情',
                href: '${ctx}/bondSource/detail?bondSourceId='+id,
                width: 1200,
                height: 620,
                maximizable : true,
                minimizable : false,
                collapsible : false,
                modal:true,
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