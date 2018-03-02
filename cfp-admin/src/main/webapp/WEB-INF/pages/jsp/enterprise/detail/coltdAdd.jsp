<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="addColtd" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="addColtd_form" method="post" action="${ctx}/jsp/enterprise/loan/saveColtd">
    <c:if test="${not empty coLtd}">
        <input type="hidden" name="enterpriseId" id="enterpriseId" value="${coLtd.enterpriseId}"/>
    </c:if>
    <c:if test="${empty coLtd}">
        <input type="hidden" name="enterpriseId" id="enterpriseId" value="${param.enterpriseId}"/>
    </c:if>
	<input type="hidden" name="coId" id="coId" value="${coLtd.coId}"/>

    <div class="control-group">
    
        <label class="control-label">公司名称：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
             class="easyui-validatebox" required="true" missingMessage="公司名称不能为空"
                   name="companyName" id="companyName" value="${coLtd.companyName}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" >组织机构编码：</label>
        <div class="controls">
            <input type="text" style="width: 200px;"
                   class="easyui-validatebox" required="true" missingMessage="组织机构编码不能为空"
                   name="organizationCode" id="organizationCode" value="${coLtd.organizationCode}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">公司法人：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage=公司法人不能为空"
                   name="legalPerson" id="legalPerson" value="${coLtd.legalPerson}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">注册资金：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage="注册资金不能为空"
                   validtype="amount"
                   name="registeredCapital" id="registeredCapital" value="${coLtd.registeredCapital}"><span style="color: red">*</span>
        </div>
    </div>
  
    <div class="control-group" id="status">
        <label class="control-label">所属行业：</label>
        <div class="controls">
        	<div id="removeWhenUpdate">

               <input  id="industry"
                       editable="false" required="true"
                       style="width: 100px"
                       name="industry" style="width:160px;"/>

               <input id="occupation" editable="false"  style="width: 100px"   editable="false" required="true"
                       name="occupation" style="width:160px;"/>

			</div>
        </div>
    </div>

    <div class="control-group" id="">
        <label class="control-label">联系人：</label>
        <div class="controls">
        	<div id="">
                <input type="text" style="width: 200px"
                       class="easyui-validatebox" required="true" missingMessage="联系人不能为空"
                       name="contactPerson" id="contactPerson" value="${coLtd.contactPerson}"><span style="color: red">*</span>
			</div>
        </div>
    </div>

    <div class="control-group" id="">
        <label class="control-label">联系方式：</label>
        <div class="controls">
        	<div id="">
                <input type="text" style="width: 200px"
                       class="easyui-validatebox" required="true" missingMessage="联系人不能为空"
                       validtype="mobile"
                       name="contactInformation" id="contactInformation" value="${coLtd.contactInformation}"><span style="color: red">*</span>
			</div>
        </div>
    </div>

    <div class="control-group" id="">
        <label class="control-label">所在城市：</label>
        <div class="controls">
            <div id="ddd">
                <input id="province"  editable="false"  style="width: 100px"  editable="false" required="true"
                        name="province" style="width:160px;"/>
                <input id="city" editable="false"  style="width: 100px"  editable="false" required="true"
                        name="city" style="width:160px;"/>
            </div>
        </div>
    </div>
    <div class="control-group" id="" style="display: none;">
        <div class="controls">
            <div id="">
                <input type="text" style="width: 200px;display: none;" >
            </div>
        </div>
    </div>
    <div class="control-group" id="">
        <label class="control-label">公司地址：</label>
        <div class="controls">
            <div id="">
                <input type="text" style="width: 200px"
                       name="address" id="address" value="${coLtd.address}">
            </div>
        </div>
    </div>

    <div class="control-group" id="">
        <label class="control-label">公司描述：</label>
        <div class="controls">
            <div id="">
                <textarea name="coDesc" autocomplete="off"  placeholder="" style="margin-left: 0px; margin-right: 0px; height: 75px; width: 250px;">${coLtd.coDesc}</textarea>
            </div>
        </div>
    </div>

</form>
</div>
<script language="JAVASCRIPT">

   $("#industry").combobox({
       url: '${ctx}/jsp/enterprise/loan/condifine?code=industry',
       textField: 'constantName',
       valueField: 'constantValue',
       onSelect: function (rec) {
           $("#occupation").combobox("clear");
           $("#occupation").combobox({
               url: '${ctx}/jsp/enterprise/loan/condifineChild?code=industry&constantValue='+rec.constantValue,
               textField: 'constantName',
               valueField: 'constantValue'

           });
       }
   });

   $("#occupation").combobox({
       valueField: 'constantValue',
       textField: 'constantName',
   });

    //加载省份、城市
    $("#province").combobox({
        url: '${ctx}/jsp/enterprise/loan/province',
        textField: 'provinceName',
        valueField: 'provinceId',

        onSelect: function (record) {
            $("#city").combobox("clear");
            $("#city").combobox({
                url: '${ctx}/jsp/enterprise/loan/city?provinceId=' + record.provinceId,
                textField: 'cityName',
                valueField: 'cityId'

            });
        }
    });

   $("#city").combobox({
       textField: 'cityName',
       valueField: 'cityId'
   });

    if($("#coId").val()!=""){

        $("#industry").combobox("setValue", "${coLtd.industry}");
        $("#occupation").combobox("reload",
                '${ctx}/jsp/enterprise/loan/condifineChild?code=industry&constantValue=${coLtd.industry}');
        $("#occupation").combobox("setValue", "${coLtd.occupation}");

        $("#province").combobox("setValue", "${coLtd.province}");
        $("#city").combobox("reload",
                '${ctx}/jsp/enterprise/loan/city?provinceId=${coLtd.province}');
        $("#city").combobox("setValue", "${coLtd.city}");

        $('#industry').validatebox('selectedRemove'); //删除
        $('#occupation').validatebox('selectedRemove'); //删除
        $('#province').validatebox('selectedRemove'); //删除
        $('#city').validatebox('selectedRemove'); //删除
    }


</script>
</body>
</html>