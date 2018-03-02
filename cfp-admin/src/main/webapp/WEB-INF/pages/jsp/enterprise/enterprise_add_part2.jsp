<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<body>
<div id="enterprise_add_part2" class="container-fluid" style="padding: 50px 0px 0px 10px;width:50%;">
<form class="form-horizontal" id="enterprise_add_part2_form" method="post">
	<table width="100%" id="showTable">
      <tr>
          <td>
          		<!-- 法人身份证 -->
          		<span style="font-size: 24px;">法人身份证</span>
          		<input style="margin-left: 820px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('0','legalIdentityCard');">
          		<br/><hr>
          		<div id="legalIdentityCard">
					
				</div>
				<div id="legalIdentityCard_name">
					
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 法人个人征信 -->
				<span style="font-size: 24px;">法人个人征信</span>
          		<input style="margin-left: 820px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('1','legalPersonalCredit');">
          		<br/><hr>
          		<div id="legalPersonalCredit">
				
				</div>
				<div id="legalPersonalCredit_name">
					
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 税务登记证 -->
				<span style="font-size: 24px;">税务登记证</span>
          		<input style="margin-left: 820px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('2','taxRegistrationCertificate');">
          		<br/><hr>
          		<div id="taxRegistrationCertificate">
					
				</div>
				<div id="taxRegistrationCertificate_name">
					
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 营业执照 -->
				<span style="font-size: 24px;">营业执照</span>
          		<input style="margin-left: 820px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('3','businessLicense');">
          		<br/><hr>
          		<div id="businessLicense">
					
				</div>
				<div id="businessLicense_name">
					
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 组织机构代码证 -->
				<span style="font-size: 24px;">组织机构代码证</span>
          		<input style="margin-left: 820px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('4','organizationCodeCertificate');">
          		<br/><hr>
          		<div id="organizationCodeCertificate">
					
				</div>
				<div id="organizationCodeCertificate_name">
					
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 开户许可证 -->
				<span style="font-size: 24px;">开户许可证</span>
          		<input style="margin-left: 820px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('5','openingPermit');">
          		<br/><hr>
          		<div id="openingPermit">
					
				</div>
				<div id="openingPermit_name">
					
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 验资报告 -->
				<span style="font-size: 24px;">验资报告</span>
          		<input style="margin-left: 820px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('6','theCapitalVerificationReport');">
          		<br/><hr>
          		<div id="theCapitalVerificationReport">
					
				</div>
				<div id="theCapitalVerificationReport_name">
					
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 经营场所租凭合同 -->
				<span style="font-size: 24px;">经营场所租凭合同</span>
          		<input style="margin-left: 820px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('7','businessPremisesLeaseContract');">
          		<br/><hr>
          		<div id="businessPremisesLeaseContract">
					
				</div>
				<div id="businessPremisesLeaseContract_name">
					
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 近三年的财务报表 -->
				<span style="font-size: 24px;">近三年的财务报表</span>
          		<input style="margin-left: 820px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('8','nearlyThreeYearsOfFinancialStatements');">
          		<br/><hr>
          		<div id="nearlyThreeYearsOfFinancialStatements">
					
				</div>
				<div id="nearlyThreeYearsOfFinancialStatements_name">
					
				</div>
				<hr style="margin-bottom: 20px;">

          </td>
      </tr>
	</table>
	
</form>
</div>

<script type="text/javascript">

// 是否在前台显示
function onIsDisplay(cusId){
	var isDisplay = 1;
	if($('#isDisplay_'+snapshotId).is(':checked')){
		isDisplay = 0
	}
	
	$.post('${ctx}/jsp/enterprise/doIsDisplay',
         {
			snapshotId:snapshotId,
			isDisplay:isDisplay
         },
         function(data){
             if(data.result == 'success'){
                 $.messager.alert("操作提示", "操作成功！", "info");
             }else if(data.result == 'error'){
                 if(data.errCode == 'check'){
                     $.messager.alert("验证提示", data.errMsg, "info");
                 }else{
                     $.messager.alert("系统提示", data.errMsg, "info");
                 }
             }else{
                 $.messager.alert("系统提示", "网络异常，请稍后操作！", "info");
             }
     },'json');
}

// 执行:上传附件。
function upButton(state,typeList){
	var enterpriseId = $("#enterpriseId").val();
	if(enterpriseId == ""){
		$.messager.alert("系统提示", "需要先保存企业的基本信息!", "info");
		return;
	}
	
	$("#enterprise_add_part2_form").after("<div id='upload_snapshot_add' style=' padding:10px; '></div>");
	$("#upload_snapshot_add").dialog({
        resizable: false,
        title: '上传图片',
        href: '${ctx}/jsp/enterprise/uploadSnapshotAdd?state='+ state + '&typeList=' + typeList + '&enterpriseId=' + enterpriseId + '&isCode=1',
        width: 400,
        height: 420,
        modal: true,
        top: 100,
        left: 200,
        buttons: [
            {
                text: '提交',
                iconCls: 'icon-ok',
                handler: function () {
                    $("#upload_snapshot_add").contents().find("#upload_snapshot_add_form").submit();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#upload_snapshot_add").dialog('close');
                }
            }
        ],
        onClose: function () {
            $(this).dialog('destroy');
        }
    });
}

// 显示大图片
function showBig(cusId){
	$("#showTable").after("<div id='show_big_picture' style=' padding:10px; '></div>");
	 $("#show_big_picture").dialog({
	        resizable: false,
	        title: '大图',
	        href: '${ctx}/jsp/enterprise/toShowBigPicture?cusId='+cusId,
	        width: 400,
	        height: 420,
	        modal: true,
	        top: 100,
	        left: 200,
	        
	        onClose: function () {
	            $(this).dialog('destroy');
	        }
	    });
}

// 删除图片
function delImg(cusId,id){
	$.messager.confirm('确认信息','是否确认删除该张图片？',function(r){
		if(r){
			try {
            	$.post("${ctx}/jsp/enterprise/delImg?cusId="+cusId,
                    	function(data){
                    		if(data.result == "success"){
                    			$.messager.alert("系统提示", "操作成功!", "info");
                    			 var myDiv = document.getElementById("img_"+data.divId+"_div");
                    			 var mySpan = document.getElementById("img_"+data.divId+"_span");
                    			    if (myDiv != null)
                    			    	myDiv.parentNode.removeChild(myDiv);
                    			    if (mySpan != null)
                    			    	mySpan.parentNode.removeChild(mySpan); 
                    		}
                    		if(data.result == "error"){
                    			$.messager.alert("系统提示", "操作失败!", "info");

                    		}
            	  		});
            } catch (e) {
                alert("删除失败！"+e);

            }
		} 
	});
}
</script>
</body>
</html>
