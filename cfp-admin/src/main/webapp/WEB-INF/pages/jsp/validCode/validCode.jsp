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
                        <td nowrap="nowrap"><label>用户手机号：</label>
                        </td>
                        <td align="center"><input id="userMobile"
                                   name="userMobile" value=""
                                   type="text" />
                        </td>
                        <!-- <td nowrap="nowrap"><label>发送时间：</label>
                        </td>
                        <td align="center">
                        	<input type="text" class="easyui-datebox" editable="false" style="width: 150px" id="startTime">
                        </td> -->
                        <td nowrap="nowrap"><label>验证码类型：</label>
                        </td>
                        <td>
                        	<select class="easyui-combobox" id="codeType" style="width:150px" name="codeType" >
                                <option value="-1" >全部</option>
                                <!-- <option value="0" >邀请好友</option> -->
                                <option value="1">修改手机</option>
                                <option value="2">用户注册</option>
                                <!-- <option value="3">重设交易密码</option>
                                <option value="4">找回密码</option> -->
                                <option value="5">提现短信</option>
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

        <table id="guaranteeCompany_list_list"></table>
    </div>

    <div id="detailGuaranteeCompany">

    </div>
    <script language="javascript">

        function toQueryGuaranteeCompany(){
            $('#guaranteeCompany_list_list').datagrid('reload', {
                'userMobile' : Trim($("#userMobile").val(),"g"),
                'codeType':Trim($("#codeType").combobox("getValue"),"g")
            });
        }
        /**
         * 执行：列表加载。
         */
        function loadList(){
            $("#guaranteeCompany_list_list").datagrid({
                idField: 'companyId',
                title: '验证码列表',
                url: '${ctx}/validCode/showCodesList',
                pagination: true,
                pageSize: 10,
                width: document.body.clientWidth * 0.97,
                height: document.body.clientHeight * 0.6,
                singleSelect: true,
                rownumbers: true,
                columns:[[
                    {field:'mobile', width:100,title:'用户手机'},
                    {field:'type', width:120,title:'验证码类型'},
                    {field:'startTime', width:150,title:'验证码开始时间'},
                    {field:'endTime', width:150,title:'验证码失效时间'},
                    {field:'code', width:100,title:'验证码'}
                ]]
            });
        }

        $(function(){
            loadList();
        });
    </script>

</body>