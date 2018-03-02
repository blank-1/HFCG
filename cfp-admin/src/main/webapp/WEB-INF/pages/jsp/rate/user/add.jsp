<%@ page import="com.xt.cfp.core.constants.RateEnum"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<%
    //加息券来源
    RateEnum.RateUserSourceEnum[] rateUserSource = RateEnum.RateUserSourceEnum.values();
    request.setAttribute("rateUserSource", rateUserSource);
%>
<body>
<iframe name="_test" style="display:none;"></iframe>
<div id="addVoucher" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <form class="form-horizontal" id="addRateUser_form" method="post" action="${ctx}/jsp/rate/user/add">

        <div class="control-group">

            <label class="control-label" style="height: 50px;;">目标用户：</label>

           <div class="controls">
                <input type="radio" checked name="targetUser" value="0" onclick="userTarget()" id="userTarget1"/>
                    <input  type="text" id="user_Name" value=""/>
                    <select id="userId" name="userId" class="easyui-combobox" ><option value="">请选择</option></select><br/>

                <input type="radio" name="targetUser" value="1"  onclick="userTarget()"  id="userTarget2"/>
                    <input id="targetFile" name="importFile" type="file" disabled/>
					<a id="importFile" href="javascript:void(0);" class="easyui-linkbutton" onclick="javascript:toImportUser();" >导入</a>
					<a href="${ctx }/file/UserImportTemplate.xls">下载模板</a>
					<br/>
                    
                <input type="radio" name="targetUser" value="2"   onclick="userTarget()"/>所有用户<br/>
            </div>

        </div>
        
        <div class="control-group">
            <label class="control-label">选择加息券：</label>

            <div class="controls">
                <select id="rateProductList_select" class="easyui-combobox" name="rateProductId" style="width:160px;">
                    <option value="-1">请选择</option>
                    <c:forEach items="${rateProductList}" var="product">
                        <option value="${product.rateProductId}">${product.rateProductName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">选择加息券来源：</label>

            <div class="controls">
                <select id="rateUserSource_select" class="easyui-combobox" name="rateUserSource" style="width:160px;" >
                    <option value="">请选择</option>
                    <c:forEach items="${rateUserSource}" var="v">
                        <option value="${v.value}">${v.desc}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">使用场景：</label>

            <div class="controls">
                <span id="usage_scenario"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">使用条件：</label>

            <div class="controls">
                <span id="condition"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">使用次数：</label>

            <div class="controls">
                <span id="usage_times"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">有效时长：</label>
            <div class="controls">
                <span id="usage_duration"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">有效截止日：</label>

            <div class="controls">
                <span id="end_date"></span>
            </div>
        </div>

        <input type="hidden" name="userIdList" id="userIdList">
    </form>
    
    <input type="hidden" id="users" value="1"/>
    <form method="post" action="${ctx}/jsp/rate/user/importExcel" id="importExcel" enctype="multipart/form-data" target="_test"></form>
    
</div>
<script language="javascript">

	//导入结果后台回调（后台importExcel）
    function inputUserIds(v,length,error){
        if(error!=null&&error!=''){
            $.messager.alert('提示', error, 'info');
        }else{
            $("#userIdList").val(v);
            $("#users").val(length);
            $.messager.alert('提示', '成功导入'+length+'个用户', 'info');
        }
    }

    //导入事件
    function toImportUser(){
        var file = $("#targetFile");
        if(file.val()==null||file.val()==''){
            $.messager.alert('提示', '请选择导入文件', 'info');
        }else{
            $("#importExcel").html("").append(file.clone(true).hide());
            $("#importExcel").submit();
        }
    }

    //目标用户，单选事件
    function userTarget(){
        $("#rateProductList_select").combobox("setValue","-1");//清空选中的加息券

        var checked1 = $("#userTarget1").attr("checked");//一个用户
        var checked2 = $("#userTarget2").attr("checked");//导入用户
        if(checked1=='checked'){//一个用户
            $("#targetFile").attr("disabled","disabled");
            $("#user_Name").removeAttr("disabled");
            $("#userId").combobox({
                disabled: false
                ,required: true,
                required: true,
                validType: 'selectV'
            });
            $("#users").val(1);
        }
        if(checked2=='checked'){//导入用户
            $("#userIdList").val("");//历史导入数据清空
            $("#user_Name").attr("disabled","disabled");
            $("#userId").combobox({
                disabled: true
                ,required: false,});
            $('#userId').validatebox('selectedRemove'); //删除
            $("#targetFile").removeAttr("disabled");
        }

        if(checked1!='checked' && checked2!='checked'){//所有用户
            $.ajax({
                url:'${ctx}/voucher/getAllUser',//此方法共用财富券的
                type:"POST",
                success:function(msg){
                    if(msg!=null){
                       $("#users").val(msg);
                    }
                }
            });

            $("#userIdList").val("");//历史导入数据清空
            $("#user_Name").attr("disabled","disabled");
            $("#userId").combobox({
                disabled: true
                ,required: false,});
            $('#userId').validatebox('selectedRemove'); //删除
            $("#targetFile").removeAttr("disabled");
        }

    }
	
    //加息券下拉框，选择事件
    function doChange() {
        $("#rateProductList_select").combobox({
            onChange: function (rec) {
                //后台请求（开始）
                if(rec == -1){
                	$("#usage_scenario").html("");
                	$("#condition").html("");
                	$("#usage_times").html("");
                	$("#usage_duration").html("");
                	$("#end_date").html("");
                }
                $.ajax({
                    url:'${ctx}/jsp/rate/user/getRateProductDetail?rateProductId='+rec,
                    type:"POST",
                    dataType:'json',
                    success:function(data){
                    	
                    	$("#usage_scenario").html(data.usage_scenario);
                    	$("#condition").html(data.condition);
                    	$("#usage_times").html(data.usage_times);
                    	$("#usage_duration").html(data.usage_duration);
                    	$("#end_date").html(data.end_date);
                    	
                    }
                });
                //后台请求(结束)
            },
            required: true,
            validType: 'selectV'
        });
    }
	
    //根据用户名模糊查询出用户列表
    function userList(){
        var userName = $("#user_Name").val();
        $.ajax({
            url:'${ctx}/voucher/getUser',//此方法共用财富券的
            type:"POST",
            data:{"userName":userName},
            success:function(msg){
                if(msg!=null){
                    $('#userId').combobox({
                        valueField:'id',
                        textField:'text',
                        data:eval(msg),
                    });
                }
            }
        });
    }

    $(function () {
        doChange();//加息券下拉框，选择事件
        $("#user_Name").bind("blur",function(){
            userList();
        });
        userTarget();//目标用户，单选事件
        $("#rateUserSource_select").combobox({
            required: true,
            validType: 'selectV'
        });
    });
</script>
</body>
</html>