<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<head>
<title></title>
</head>
<body>

    <div class="" style="float:left; width:1200px;margin:0 10px 5px 10px;">
        <fieldset style="height:65px">
            <legend>查询条件</legend>
            <form name="voucherProductList" id="voucherProductList111" action="${ctx}/report/toExportEccelProduct" class="fitem"
                  autocomplete="off">
                <table>
                    <tbody>
                    <input id="productId"   type="hidden" name="productId" value="${productId}"  />
                    <tr>
                        <td nowrap="nowrap"><label>姓名：</label>
                        </td>
                        <td align="center"><input id="name"
                                   name="name" value=""
                                   type="text" />
                        </td>
                        <td nowrap="nowrap"><label>手机号码：</label>
                        </td>
                        <td>
                         <td align="center"><input id="mobileNo"
                                   name="mobileNo" value=""
                                   type="text" />
                        </td>
                        </td>

                    

 						 
                        
                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQueryVoucherProduct();" iconCls="icon-search">查询</a>
                                   
                                      <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toExportEccelProduct();" iconCls="icon-save">导出EXCEL</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>

      <div id="dis_activity_list" class="container-fluid" style="padding: 5px 0px 0px 10px">
        <div id="dis_activity_list_toolbar" style="height:auto">

          <!--   <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addDisActivity();">新增</a> -->
        </div>

        <table id="dis_activity_list_list1"></table>
    </div>  


    <div id="detail"></div>
    <script language="javascript">

    function    toExportEccelProduct(){
    	
    	   $("#voucherProductList111").submit();
  /*       $.ajax({
            type: 'POST',
            url: "${ctx}/report/toExportEccelProduct" ,
            data:{  
            	   mobileNo :  $("#mobileNo").val(),  
            	   name :  $("#name").val() ,  
            	   productId : $("#productId").val()    
            	  },  
            type:'post',  

       });
         */
        
    }
    
    
    
        function toQueryVoucherProduct(){
            $('#dis_activity_list_list1').datagrid('reload', {
                'mobileNo' : $("#mobileNo").val() ,
                'name' : $("#name").val() ,
                //'productId': $("#productId").val(),
            });
        }
        function loadList(){
        var 	productId =$("#productId").val();
        	
            $("#dis_activity_list_list1").datagrid({
                idField: 'voucherProductId',//todo ID更改
                title: '分销产品列表',
                url: '${ctx}/disActivity/activitProductDetail?productId='+productId,
                pagination: true,
                pageSize: 10,
                width: document.body.clientWidth * 0.97,
                height: document.body.clientHeight * 0.6,
                singleSelect: true,
                rownumbers: true,
              /*   toolbar: '#dis_activity_list_toolbar', */
                columns:[[
                  /*   {field:'sectionCode', width:120,title:'当前期号'}, */
                    {field:'realName', width:120,title:'姓名'},
                    {field:'loginName', width:250,title:'用户名'},
                    {field:'mobileNo', width:200,title:'手机号'},
                    {field:'factProfit', width:100,title:'已获佣金'},
                    {field:'shoulProfit', width:100,title:'预期佣金'},
                    {field:'wasCommit', width:80,title:'投资金额'},
                    {field:'realNameCustom', width:80,title:'被邀请人姓名'},
                    {field:'turnTime', width:80,title:'订单时间',formatter:dateTimeFormatter},
                    {field:'level', width:80,title:'邀请层级'},
                ]]
            });
        }

      
    $(function(){
        loadList();
    });
    
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

        return dateStr;// + ' ' + TimeStr;
    }
  
  
    //查看详情(分销活动表ID)
    function detail(disId){
    	$("#detail").after("<div id='dis_activity_detail' style=' padding:10px; '></div>");
    	$("#dis_activity_detail").dialog({
    	    resizable: false,
    	    title: '分销产品详情',
    	    href: '${ctx}/disActivity/disActivityDetail?disId=' + disId,
    	    width: document.body.clientWidth * 0.6,
    	    height: document.body.clientHeight * 0.7,
    	    modal: true,
    	    buttons: [
    	        {
    	            text: '确定',
    	            iconCls: 'icon-ok',
    	            handler: function () {
    	                $("#dis_activity_detail").dialog('close');
    	            }
    	        }
    	    ],
    	    onClose: function () {
    	        $(this).dialog('destroy');
    	    }
    	});
    }
  
    
    </script>

</body>

