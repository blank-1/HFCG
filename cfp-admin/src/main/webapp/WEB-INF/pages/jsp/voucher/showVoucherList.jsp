<%@ page import="com.xt.cfp.core.constants.VoucherConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<head>
</head>
<%
    //财富券类型
    VoucherConstants.VoucherTypeEnum[] voucherType = VoucherConstants.VoucherTypeEnum.values();
    request.setAttribute("voucherType",voucherType);
    //状态
    VoucherConstants.VoucherStatus[] voucherStatus = VoucherConstants.VoucherStatus.values();
    request.setAttribute("voucherStatus",voucherStatus);
    //使用场景
    VoucherConstants.UsageScenario[] usageScenario = VoucherConstants.UsageScenario.values();
    request.setAttribute("usageScenario",usageScenario);
%>
<body>

    <div class="" style="float:left; width:1200px;margin:0 10px 5px 10px;">
        <fieldset style="height:65px;with:1000px;">
            <legend>查询条件</legend>
            <form method="post" id="voucher_form" action="${ctx}/voucher/exportExcel" class="form-inline">
                <table>
                    <tbody>
                    <tr>
                        <td nowrap="nowrap"><label>用户名：</label>
                        </td>
                        <td align="center"><input id="userName"
                                   name="userName" value=""
                                   type="text" />
                        </td>
                        <td nowrap="nowrap"><label>真实姓名：</label>
                        </td>
                        <td align="center"><input id="realName"
                                   name="realName" value=""
                                   type="text" />
                        </td>

                        <td nowrap="nowrap"><label>名称：</label>
                        </td>
                        <td align="center"><input id="voucherName"
                                   name="voucherName" value=""
                                   type="text" />
                        </td>

                        <td nowrap="nowrap"><label>使用场景：</label>
                        </td>
                        <td>
                            <select id="usageScenario" class="easyui-combobox" name="usageScenario"
                                    style="width:170px;">
                                <option value="">全部</option>
                                <c:forEach items="${usageScenario}" var="type">
                                    <option value="${type.value}"
                                            <c:if test="${voucherProduct.usageScenario eq type.value}">selected="selected"</c:if>>${type.desc}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td nowrap="nowrap"><label>状态：</label>
                        </td>
                        <td>
                            <select id="v_status" class="easyui-combobox" name="status"
                                    style="width:160px;">
                                <option value="">全部</option>
                                <c:forEach items="${voucherStatus}" var="type">
                                    <option value="${type.value}"
                                            <c:if test="${voucherProduct.usageScenario eq type.value}">selected="selected"</c:if>>${type.desc}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>

                        <td nowrap="nowrap"><label>获得时间：</label>
                        </td>
                        <td nowrap="nowrap">
                            <input id="v_startDate" name="startDateStr" style="width:100px;" class="easyui-datebox"/>至
                            <input id="v_endDate" style="width:100px;" name="endDateStr" class="easyui-datebox"/>
                        </td>

                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQueryVoucher();" iconCls="icon-search">查询</a>


                                <a href="javascript:doExport()" class="easyui-linkbutton"
                                   data-options="iconCls:'icon-save'">导出Excel</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>

    <div id="voucher_list" class="container-fluid" style="padding: 5px 0px 0px 10px">
        <div id="voucher_list_toolbar" style="height:auto">

            <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addVoucher();">发放财富券</a>
        </div>

        <table id="voucher_list_list"></table>
    </div>


    <div id="detail"></div>
    <script language="javascript">
        function doExport(){
            $("#voucher_form").submit();
        }


        function toQueryVoucher(){
            $('#voucher_list_list').datagrid('reload', {
                'startDateStr' : Trim($('#v_startDate').datebox('getValue'),"g"),
                'endDateStr' : Trim($('#v_endDate').datebox('getValue'),"g"),
                'userName' : Trim($('#userName').val(),"g"),
                'realName' : Trim($('#realName').val(),"g"),
                'status' : Trim($("#v_status").combobox("getValue"),"g"),
                'usageScenario' : Trim($("#usageScenario").combobox("getValue"),"g"),
                'voucherName' : Trim($("#voucherName").val(),"g")
            });
        }
        function loadList(){
            $("#voucher_list_list").datagrid({
                idField: 'voucherId',
                title: '财富券列表',
                url: '${ctx}/voucher/voucherList',
                pagination: true,
                pageSize: 10,
                width: document.body.clientWidth * 0.97,
                height: document.body.clientHeight * 0.6,
                singleSelect: true,
                rownumbers: true,
                toolbar: '#voucher_list_toolbar',
                columns:[[
                    {field:'voucherId', width:60,title:'ID'},
                    {field:'voucherName', width:80,title:'名称'},
                    {field:'userName', width:100,title:'持有人'},
                    {field:'realName', width:60,title:'真实姓名'},
                    {field:'amountStr', width:60,title:'金额'},
                    {field:'effictiveDate', width:160,title:'有效期'},
                    {field:'conditionAmountStr', width:110,title:'使用条件'},
                    {field:'usageScenarioStr', width:100,title:'使用场景'},
                    {field:'isOverlyStr', width:70,title:'叠加使用'},
                    {field:'sourceStr', width:70,title:'来源'},
                    {field:'createDate', width:140,title:'获得时间',formatter:dateTimeFormatter},
                    {field:'usageDate', width:140,title:'使用时间',formatter:dateTimeFormatter},
                    {field:'createTime', width:140,title:'注册时间',formatter:dateTimeFormatter},

                    {field:'loanTitle', width:160,title:'标的名称'},
                    {field:'buyBalance', width:100,title:'投标金额'},

                    {field:'statusStr', width:80,title:'状态'},
                    {field:'action',title:'操作',width:50,align:'center',
                        formatter:function(value,row,index){

                            var result = "";
                            result += "<a class='label label-info' onclick='detail(" + row.voucherId + ")'>查看</a> &nbsp;";
                            return result;
                        }
                    }
                ]]
            });
        }


      function addVoucher() {
          $("#voucher_list").after("<div id='addVoucher' style=' padding:10px; '></div>");
          $("#addVoucher").dialog({
              resizable: false,
              title: '发放财富券',
              href: '${ctx}/voucher/addVoucher',
              width: 900,
              height: 450,
              modal: true,
              top: 100,
              left: 200,
              buttons: [
                  {
                      text: '保存',
                      iconCls: 'icon-ok',
                      handler: function () {
                          var form = $("#addVoucher").contents().find("#addVoucher_form");
                          var validate = form.form('validate');

                          if(validate){
                              $.messager.progress({
                                  title:'请稍后',
                                  msg:'页面加载中...'
                              });

                              $.ajax({
                                  url:'${ctx}/voucher/saveVoucher',
                                  data:form.serialize(),
                                  type:"POST",
                                  success:function(msg){
                                      $.messager.progress('close');
                                      if(msg=='success'){
                                          $.messager.alert('提示', '保存成功！！', 'info',function(){
                                              $("#addVoucher").dialog('close');
                                              toQueryVoucher();
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
                          $("#addVoucher").dialog('close');
                      }
                  }
              ],
              onClose: function () {
                  $(this).dialog('destroy');
              }
          });
      }

        function detail(id){
            $("#detail").after("<div id='voucher_detail' style=' padding:10px; '></div>");
            $("#voucher_detail").dialog({
                resizable: false,
                title: '财富券详情',
                href: '${ctx}/voucher/detailVoucher?voucherId='+id,
                width: 500,
                height: 550,
                modal: true,
                top: 50,
                left: 400,
                buttons: [
                    {
                        text: '确定',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $("#voucher_detail").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }
    //格式化时间
    function dateTimeFormatter(val) {

        if (val == undefined || val == "")
            return "";
        var date;
        if(val instanceof Date){
            date = val;
        }else{
            date = new Date(val);
        }
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();

        var h = date.getHours();
        var mm = date.getMinutes();
        var s = date.getSeconds();

        var dateStr = y + '-' + (m < 10 ? ('0' + m) : m) + '-'
                + (d < 10 ? ('0' + d) : d);
        var TimeStr = h + ':' + (mm < 10 ? ('0' + mm) : mm) + ':'
                + (s < 10 ? ('0' + s) : s);

        return dateStr + ' ' + TimeStr;
    }

    $(function(){
        loadList();
    });
    </script>

</body>
