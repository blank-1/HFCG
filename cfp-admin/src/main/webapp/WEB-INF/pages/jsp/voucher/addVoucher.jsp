<%@ page import="com.xt.cfp.core.constants.VoucherConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<%
    //财富券来源类型
    VoucherConstants.SourceType[] sourceType = VoucherConstants.SourceType.values();
    request.setAttribute("sourceType",sourceType);

    //使用场景
    VoucherConstants.UsageScenario[] usageScenario = VoucherConstants.UsageScenario.values();
    request.setAttribute("usageScenario",usageScenario);
%>
<html>
<head>
    <title></title>
</head>
<body>
<iframe name="_test" style="display:none;"></iframe>
<div id="addVoucher" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <form class="form-horizontal" id="addVoucher_form" method="post" action="${ctx}/voucher/saveVoucher">
        <input type="hidden" name="voucherId" id="voucherId" value="${voucher.voucherId}"/>

        <div class="control-group">

            <label class="control-label" style="height: 50px;;">目标用户：</label>

           <div class="controls">
                <input type="radio" checked name="targetUser" value="0" onclick="userTarget()" id="userTarget1"/>
                        <input  type="text" id="user_Name" value=""/>
                        <select id="userId" name="userId" class="easyui-combobox" ><option value="">请选择</option></select><br/>

                <input type="radio" name="targetUser" value="1"  onclick="userTarget()"  id="userTarget2"/>
                    <input id="targetFile" name="importFile" type="file" disabled/> <a id="importFile" href="javascript:void(0);" class="easyui-linkbutton" onclick="javascript:toImportUser();" >导入</a><br/>
                <input type="radio" name="targetUser" value="2"   onclick="userTarget()"/>所有用户<br/>
            </div>

        </div>
        <div class="control-group">
            <label class="control-label">选择财富券：</label>

            <div class="controls">
                <select id="voucherList" class="easyui-combobox" name="voucherProductId" style="width:160px;">
                    <option value="-1">请选择</option>
                    <c:forEach items="${voucherList}" var="product">
                        <option value="${product.voucherProductId}"
                                <c:if test="${voucher.voucherProductId eq product.voucherProductId}">selected="selected"</c:if>>${product.voucherName}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">选择财富券来源：</label>

            <div class="controls">
                <select id="sourceType" class="easyui-combobox" name="sourceType" style="width:160px;">
                    <option value="">请选择</option>
                    <c:forEach items="${sourceType}" var="type">
                        <option value="${type.value}"
                                <c:if test="${voucher.sourceType eq type.value}">selected="selected"</c:if>>${type.desc}
                        </option>
                    </c:forEach>
                </select>

                <input type="text" class="easyui-validatebox" required="true" missingMessage="不能为空" id="sourceTypeDesc" name="sourceDesc" disabled/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">财富券金额：</label>

            <div class="controls">
                <span id="amount"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">有效期(天)：</label>

            <div class="controls">
                <span id="effectiveCountStr"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">使用场景：</label>

            <div class="controls">
                <span id="usageScenarioStr"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">使用条件：</label>
            <div class="controls">
                <span id="conditionAmountStr"></span>
            </div>
        </div>


        <div class="control-group">
            <label class="control-label">叠加使用：</label>

            <div class="controls">
                <span id="isOverlyStr"></span>
            </div>
        </div>
        <div class="control-group" id="status">
            <label class="control-label">体验型财富券：</label>

            <div class="controls">
                <input id="isExperience" name="isExperience" type="checkbox" disabled value="1">体验型财富券，投标回款将由平台收回，用户仅获得利息收益
            </div>
        </div>

        <input type="hidden" name="userIdList" id="userIdList">
    </form>
    <input type="hidden" id="users" value="1"/>
       <form method="post" action="${ctx}/voucher/importExcel" id="importExcel" enctype="multipart/form-data" target="_test">
       </form>
</div>
<script language="javascript">

    function inputUserIds(v,length,error){
        if(error!=null&&error!=''){
            $.messager.alert('提示', error, 'info');
        }else{
            $("#userIdList").val(v);
            $("#users").val(length);
            $.messager.alert('提示', '成功导入'+length+'个用户', 'info');
        }
    }

    function toImportUser(){
        var file = $("#targetFile");
        if(file.val()==null||file.val()==''){
            $.messager.alert('提示', '请选择导入文件', 'info');
        }else{
            $("#importExcel").html("").append(file.clone(true).hide());
            $("#importExcel").submit();
        }
    }

    function userTarget(){
        $("#voucherList").combobox("setValue","-1");


        var checked1 = $("#userTarget1").attr("checked");
        var checked2 = $("#userTarget2").attr("checked");
        if(checked1=='checked'){
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
        if(checked2=='checked'){
            $("#userIdList").val("");//历史导入数据清空
            $("#user_Name").attr("disabled","disabled");
            $("#userId").combobox({
                disabled: true
                ,required: false,});
            $('#userId').validatebox('selectedRemove'); //删除
            $("#targetFile").removeAttr("disabled");
        }

        if(checked1!='checked' && checked2!='checked'){
            $.ajax({
                url:'${ctx}/voucher/getAllUser',
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

    function selectType(v) {
        if (v == '1') {
            $("#sourceTypeDesc").removeAttr("disabled");
            $('#sourceTypeDesc').validatebox('reduce'); //恢复
        }else{
            $("#sourceTypeDesc").attr("disabled","disabled");
            $('#sourceTypeDesc').validatebox('remove'); //删除
        }
    }
    function doChange() {
        $("#sourceType").combobox({
            onChange: function (rec) {
                selectType(rec);
            },
            required: true,
            validType: 'selectV'
        });

        $("#voucherList").combobox({
            onChange: function (rec) {
                //后台返回产品数据
                $.ajax({
                    url:'${ctx}/voucher/detailProduct?voucherProductId='+rec,
                    type:"POST",
                    success:function(msg){
                        var length = $("#users").val();
                        if(length!=null){
                            var mount = msg.amountStr;
                            if(mount==null){
                                $("#amount").html("");
                                $("#effectiveCountStr").html("");
                                $("#conditionAmountStr").html("");
                                $("#usageScenarioStr").html("");
                                $("#isOverlyStr").html("");
                                $("#isExperience").removeAttr("checked");
                            }
                            if(mount.indexOf("元")==-1){
                                if(length==1){
                                    $("#amount").html(msg.amountStr+"(发放财富券总额:"+mount+")");
                                }else{
                                    $("#amount").html(msg.amountStr+"(发放财富券总额:"+mount+"*"+length+")");
                                }
                            }else{
                                mount = mount.replace("元","");
                                $("#amount").html(msg.amountStr+"(发放财富券总额"+parseFloat(mount)*length+"元)");
                            }
                        }else{
                            $("#amount").html(msg.amountStr);
                        }
                        if(msg.voucherEffictiveDate==null){
                            $("#effectiveCountStr").html(msg.effectiveCountStr);
                        }else{
                            $("#effectiveCountStr").html(msg.effectiveCountStr+"天"+msg.voucherEffictiveDate);
                        }
                        $("#conditionAmountStr").html(msg.conditionAmountStr);
                        $("#usageScenarioStr").html(msg.usageScenarioStr);
                        $("#isOverlyStr").html(msg.isOverlyStr);
                        if(msg.isExperience=='1')
                            $("#isExperience").attr("checked","checked");
                        else
                            $("#isExperience").removeAttr("checked");
                    }
                });
            },
            required: true,
            validType: 'selectV'
        });



    }

    function userList(){
        var userName = $("#user_Name").val();
        $.ajax({
            url:'${ctx}/voucher/getUser',
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
        doChange();
        $("#user_Name").bind("blur",function(){
            userList();
        });
        userTarget();
    });
</script>
</body>
</html>