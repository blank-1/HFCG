<%@ page import="com.xt.cfp.core.constants.RateEnum"%>
<%@ page import="com.xt.cfp.core.constants.VoucherConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<head>
</head>
<%
    //状态
    RateEnum.RateProductStatusEnum[] rateProductStatus = RateEnum.RateProductStatusEnum.values();
    request.setAttribute("rateProductStatus", rateProductStatus);
    
    //使用场景
    RateEnum.RateProductScenarioEnum[] rateProductScenario = RateEnum.RateProductScenarioEnum.values();
    request.setAttribute("rateProductScenario", rateProductScenario);
%>
<body>

    <div class="" style="float:left; width:1200px;margin:0 10px 5px 10px;">
        <fieldset style="height:65px">
            <legend>查询条件</legend>
            <form name="rateProductList" id="rateProductList" action="${ctx}/jsp/rate/product/list" class="fitem" autocomplete="off">
                <table>
                    <tbody>
                    <tr>
                        <td nowrap="nowrap"><label>名称：</label>
                        </td>
                        <td align="center"><input id="rateProductName" name="rateProductName" value="" type="text" maxlength="16"/>
                        </td>
                        
                        <td nowrap="nowrap"><label>使用场景：</label>
                        </td>
                        <td>
                            <select id="usageScenario" class="easyui-combobox" name="usageScenario" style="width:180px;">
                                <option value="">全部</option>
                                <c:forEach items="${rateProductScenario}" var="v">
                                    <option value="${v.value}">${v.desc}</option>
                                </c:forEach>
                            </select>
                        </td>
                        
                        <td nowrap="nowrap"><label>状态：</label>
                        </td>
                        <td>
                            <select id="status" class="easyui-combobox" name="status" style="width:160px;">
                                <option value="">全部</option>
                                <c:forEach items="${rateProductStatus}" var="v">
                                    <option value="${v.value}">${v.desc}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>

                        <td nowrap="nowrap"><label>有效时间：</label>
                        </td>
                        <td>
                            <input id="startDate" name="startDate" style="width:100px;" class="easyui-datebox"/>
							<font style="font-size: 12px;">至</font>
                            <input id="endDate" name="endDate" style="width:100px;" class="easyui-datebox"/>
                        </td>

                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQueryRateProduct();" iconCls="icon-search">查询</a>
                            </div>
                        </td>
                        
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>

    <div id="rate_product_list" class="container-fluid" style="padding: 5px 0px 0px 10px">
        <div id="rate_product_list_toolbar" style="height:auto">
            <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addRateProduct();">新增加息券</a>
        </div>

        <table id="rate_product_list_list"></table>
    </div>

    <div id="detail"></div>
    
    <script language="javascript">

    	/**
         * 查询
         */
        function toQueryRateProduct(){
            $('#rate_product_list_list').datagrid('reload', {
                'startDateStr' : Trim($('#startDate').datebox('getValue'),"g"),
                'endDateStr' : Trim($('#endDate').datebox('getValue'),"g"),
                'status' : Trim($("#status").combobox("getValue"),"g"),
                'usageScenario' : Trim($("#usageScenario").combobox("getValue"),"g"),
                'rateProductName' : Trim($("#rateProductName").val(),"g")
            });
        }
        
    	/**
         * 列表加载
         */
        function loadList(){
            $("#rate_product_list_list").datagrid({
                idField: 'rateProductId',
                title: '加息券列表',
                url: '${ctx}/jsp/rate/product/list',
                pagination: true,
                pageSize: 10,
                width: document.body.clientWidth * 0.97,
                height: document.body.clientHeight * 0.6,
                singleSelect: true,
                rownumbers: true,
                toolbar: '#rate_product_list_toolbar',
                columns:[[
                    {field:'rateProductName', width:140, title:'名称'},
                    {field:'rateValue', width:80, align:'center', title:'加息利率(%)'},
                    {field:'startAmount', width:80, align:'center', title:'起投金额(元)'},
                    {field:'conditionDesc', width:410, align:'center', title:'使用条件'},
                    {field:'totalRateUser', width:80, align:'center', title:'发放张数'},
                    {field:'usedRateUser', width:80, align:'center', title:'已用张数'},
                    {field:'endDate', width:180, align:'center', title:'有效期',
                    	formatter:function(value,row,index){
                    		if(value != null && value != "" && row.startDate != null && row.startDate != "")
                        	return getDateStr(new Date(row.startDate)) + " ~ " + getDateStr(new Date(value));
                    	}
                    },
                    {field:'statusStr', width:80, align:'center', title:'状态',
                    	styler: function (value, row, index) {
                    		if(row.status != null && row.status != ""){
                    			if(row.status == '0'){
                    				return 'color:green';
                    			}else if(row.status == '1'){
                    				return 'color:blue';
                    			}else{
                    				return 'color:red';
                    			}
                    		}
                         }	
                    },
                    {field:'action', title:'操作', width:200, align:'center',
                        formatter:function(value,row,index){
                        	// 0.有效；1.已使用；2.已过期；3.无效
                            var result = "";
                            result += "<a class='label label-info' onclick='detail(" + row.rateProductId + ")'>查看</a> &nbsp;";
                            if(row.status == '0' || row.status == '1'){
                            	result += "<a class='label' onclick='stopUse(" + row.rateProductId + ")'>停用</a> &nbsp;";
                            }else if(row.status == '3'){
                            	result += "<a class='label label-important' onclick='startUse(" + row.rateProductId + ")'>启用</a> &nbsp;";
                            }
                            return result;
                        }
                    }
                ]]
            });
        }

        /**
         * 停用
         */
        function stopUse(id){
            $.ajax({
                url:'${ctx}/jsp/rate/product/stopUse?rateProductId='+id,
                type:"POST",
                success:function(msg){
                    if(msg=='success'){
                        $.messager.alert('提示', '操作成功！！', 'info',function(){
                            toQueryRateProduct();
                        });
                    }else {
                        $.messager.alert('提示', '操作失败！！', 'info');
                    }
                }
            });
        }

        /**
         * 启用
         */
        function startUse(id){
            $.ajax({
                url:'${ctx}/jsp/rate/product/startUse?rateProductId='+id,
                type:"POST",
                success:function(msg){
                    if(msg=='success'){
                        $.messager.alert('提示', '操作成功！！', 'info',function(){
                            toQueryRateProduct();
                        });
                    }else {
                        $.messager.alert('提示', '操作失败！！', 'info');
                    }
                }
            });
        }
		
     /**
      * 添加
      */
      function addRateProduct() {
          $("#rate_product_list").after("<div id='addRateProduct' style=' padding:10px; '></div>");
          $("#addRateProduct").dialog({
              resizable: false,
              title: '创建加息券',
              href: '${ctx}/jsp/rate/product/to_add',
              width: 800,
              height: 550,
              modal: true,
              top: 100,
              left: 200,
              buttons: [
                  {
                      text: '保存',
                      iconCls: 'icon-ok',
                      handler: function () {
                          var form = $("#addRateProduct").contents().find("#addRateProduct_form");
                          var validate = form.form('validate');

                          if(validate){
                              $.ajax({
                                  url:'${ctx}/jsp/rate/product/add',
                                  data:form.serialize(),
                                  type:"POST",
                                  success:function(msg){
                                      if(msg=='success'){
                                          $.messager.alert('提示', '保存成功！', 'info',function(){
                                              $("#addRateProduct").dialog('close');
                                              toQueryRateProduct();
                                          });
                                      }else{
                                    	  $.messager.alert('提示', '保存失败！'+msg, 'info');
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
                          $("#addRateProduct").dialog('close');
                      }
                  }
              ],
              onClose: function () {
                  $(this).dialog('destroy');
              }
          });
      }
		
   /**
    * 详情
    */
	function detail(id){
	    $("#detail").after("<div id='product_detail' style=' padding:10px; '></div>");
	    $("#product_detail").dialog({
	        resizable: false,
	        title: '加息券详情',
	        href: '${ctx}/jsp/rate/product/detail?rateProductId='+id,
	        width: 800,
	        height: 550,
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
