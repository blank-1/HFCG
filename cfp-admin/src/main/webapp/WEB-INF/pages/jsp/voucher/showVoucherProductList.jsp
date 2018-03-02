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
    VoucherConstants.VoucherProductStatus[] voucherProductStatus = VoucherConstants.VoucherProductStatus.values();
    request.setAttribute("voucherProductStatus",voucherProductStatus);
    //使用场景
    VoucherConstants.UsageScenario[] usageScenario = VoucherConstants.UsageScenario.values();
    request.setAttribute("usageScenario",usageScenario);
%>
<body>

    <div class="" style="float:left; width:1200px;margin:0 10px 5px 10px;">
        <fieldset style="height:65px">
            <legend>查询条件</legend>
            <form name="voucherProductList" id="voucherProductList" action="${ctx}/voucher/list" class="fitem"
                  autocomplete="off">
                <table>
                    <tbody>
                    <tr>
                        <td nowrap="nowrap"><label>名称：</label>
                        </td>
                        <td align="center"><input id="voucher_Name"
                                   name="voucherName" value=""
                                   type="text" />
                        </td>
                        <td nowrap="nowrap"><label>类型：</label>
                        </td>
                        <td>
                            <select id="voucher_Type" class="easyui-combobox" name="voucherType" style="width:100px;">
                                <option value="">请选择</option>
                                <c:forEach items="${voucherType}" var="type">
                                    <option value="${type.value}"
                                            <c:if test="${voucherProduct.voucherType eq type.value}">selected="selected"</c:if>>${type.desc}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td nowrap="nowrap"><label>使用场景：</label>
                        </td>
                        <td>
                            <select id="usage_Scenario" class="easyui-combobox" name="usageScenario"
                                    style="width:180px;">
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
                            <select id="v_status" class="easyui-combobox" name="usageScenario"
                                    style="width:160px;">
                                <option value="">全部</option>
                                <c:forEach items="${voucherProductStatus}" var="type">
                                    <option value="${type.value}"
                                            <c:if test="${voucherProduct.usageScenario eq type.value}">selected="selected"</c:if>>${type.desc}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>

                        <td nowrap="nowrap"><label>有效时间：</label>
                        </td>
                        <td>
                            <input id="v_startDate" name="startDate" style="width:100px;" class="easyui-datebox"/>至
                            <input id="v_endDate" style="width:100px;" name="endDate" class="easyui-datebox"/>
                        </td>

                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQueryVoucherProduct();" iconCls="icon-search">查询</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>

    <div id="voucher_product_list" class="container-fluid" style="padding: 5px 0px 0px 10px">
        <div id="voucher_product_list_toolbar" style="height:auto">

            <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addVoucherProduct();">新增</a>
        </div>

        <table id="voucher_product_list_list"></table>
    </div>


    <div id="detail"></div>
    <script language="javascript">

        function toQueryVoucherProduct(){
            $('#voucher_product_list_list').datagrid('reload', {
                'startDateStr' : Trim($('#v_startDate').datebox('getValue'),"g"),
                'endDateStr' : Trim($('#v_endDate').datebox('getValue'),"g"),
                'status' : Trim($("#v_status").combobox("getValue"),"g"),
                'usageScenario' : Trim($("#usage_Scenario").combobox("getValue"),"g"),
                'voucherName' : Trim($("#voucher_Name").val(),"g"),
                'voucherType':Trim($("#voucher_Type").combobox("getValue"),"g")
            });
        }
        function loadList(){
            $("#voucher_product_list_list").datagrid({
                idField: 'voucherProductId',
                title: '财富券列表',
                url: '${ctx}/voucher/voucherProductList',
                pagination: true,
                pageSize: 10,
                width: document.body.clientWidth * 0.97,
                height: document.body.clientHeight * 0.6,
                singleSelect: true,
                rownumbers: true,
                toolbar: '#voucher_product_list_toolbar',
                columns:[[
                    {field:'voucherName', width:120,title:'名称'},
                    {field:'voucherTypeStr', width:100,title:'类型'},
                    {field:'amountStr', width:100,title:'金额'},
                    {field:'effictiveDate', width:200,title:'有效期'},
                    {field:'effectiveCountStr', width:80,title:'有效时长(天)'},
                    {field:'conditionAmountStr', width:120,title:'使用条件'},
                    {field:'usageScenarioStr', width:100,title:'使用场景'},
                    {field:'isOverlyStr', width:70,title:'叠加使用'},
                    {field:'statusStr', width:80,title:'状态'},
                    {field:'operateName', width:80,title:'创建人'},
                    {field:'action',title:'操作',width:200,align:'center',
                        formatter:function(value,row,index){

                            var result = "";
                            result += "<a class='label label-info' onclick='detail(" + row.voucherProductId + ")'>查看</a> &nbsp;";
                            if(row.status == '1')
                                result += "<a class='label label-info' onclick='stopUse(" + row.voucherProductId + ")'>停用</a> &nbsp;";
                            else
                                result += "<a class='label label-info' onclick='startUse(" + row.voucherProductId + ")'>启用</a> &nbsp;";
                            return result;
                        }
                    }
                ]]
            });
        }

        /**
         * 停用
         * */
        function stopUse(id){
            $.ajax({
                url:'${ctx}/voucher/stopUse?voucherProductId='+id,
                type:"POST",
                success:function(msg){
                    if(msg=='success'){
                        $.messager.alert('提示', '操作成功！！', 'info',function(){
                            toQueryVoucherProduct();
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
                url:'${ctx}/voucher/startUse?voucherProductId='+id,
                type:"POST",
                success:function(msg){
                    if(msg=='success'){
                        $.messager.alert('提示', '操作成功！！', 'info',function(){
                            toQueryVoucherProduct();
                        });
                    }else {
                        $.messager.alert('提示', '操作失败！！', 'info');
                    }
                }
            });
        }

      function addVoucherProduct() {
          $("#voucher_product_list").after("<div id='addVoucherProduct' style=' padding:10px; '></div>");
          $("#addVoucherProduct").dialog({
              resizable: false,
              title: '创建财富券',
              href: '${ctx}/voucher/add',
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
                          var form = $("#addVoucherProduct").contents().find("#addVoucherProduct_form");
                          var validate = form.form('validate');

                          if(validate){
                              $.ajax({
                                  url:'${ctx}/voucher/save',
                                  data:form.serialize(),
                                  type:"POST",
                                  success:function(msg){
                                      if(msg=='success'){
                                          $.messager.alert('提示', '保存成功！！', 'info',function(){
                                              $("#addVoucherProduct").dialog('close');
                                              toQueryVoucherProduct();
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
                          $("#addVoucherProduct").dialog('close');
                      }
                  }
              ],
              onClose: function () {
                  $(this).dialog('destroy');
              }
          });
      }

        function detail(id){
            $("#detail").after("<div id='product_detail' style=' padding:10px; '></div>");
            $("#product_detail").dialog({
                resizable: false,
                title: '财富券详情',
                href: '${ctx}/voucher/detail?voucherProductId='+id,
                width: 500,
                height: 400,
                modal: true,
                top: 50,
                left: 400,
                buttons: [
                    {
                        text: '确定',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $("#product_detail").dialog('close');
                        }
                    }
                ],
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
