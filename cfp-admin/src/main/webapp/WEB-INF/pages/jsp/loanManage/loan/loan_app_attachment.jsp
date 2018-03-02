<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<body>
<div id="loan_app_attachment" class="container-fluid" style="padding: 0px 0px 0px 10px;width:50%;">
<a href="#" class="easyui-linkbutton" onclick="javascript:downloadImg()">附件下载</a>
<form class="form-horizontal" id="loan_add_part4_form" method="post">
	<table width="100%" id="showTable">
      <tr>
          <td>
<c:if test="${not empty isCredit &&  isCredit eq '1'}">
              <!-- 身份证 -->
              <span style="color: red;">*</span><span style="font-size: 24px;">身份证</span><span style="font-size: 12px;margin-left: 20px;">借款人身份证</span>
              <input style="margin-left: 600px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('0','identityList');">
              <br/><hr>
              <c:forEach items="${attachmentList }" var="data">
                  <c:if test="${data.attachment.type eq '0' }">
                      <div id="img_${data.fileName }_div" style="display : inline-block;position : relative;">
                          <img src="${picPath}${data.attachment.url }" id="img_${data.snapshotId }" onclick='showBig(${data.snapshotId })' width="100px" height="100px" >
                          <img src='${picPath}/picture/delImg.png' id='img_${data.snapshotId }_delete'  onclick='delImg(${data.snapshotId })'  style='position : absolute;top : 0px;right : 0px;width : 20px;height : 18px;'/>
                      </div>
                  </c:if>
              </c:forEach>
</c:if>
              <div id="identityList"></div>
              <div id="identityList_name"></div>
              <hr style="margin-bottom: 20px;">
			  <c:if test="${empty isCredit }">
                  <!-- 工资流水 -->
                  <span style="font-size: 24px;">工资流水</span><span style="font-size: 12px;margin-left: 20px;">如房产抵押选择二抵，则必须提交</span>
                  <input style="margin-left: 600px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('1','wagesFlowList');">
                  <br/><hr>
                  <c:forEach items="${attachmentList }" var="data">
                      <div id="img_${data.fileName }_div" style="display : inline-block;position : relative;">
                          <c:if test="${data.attachment.type eq '1' }">
                              <img src="${picPath}${data.attachment.url }" id="img_${data.snapshotId }" onclick='showBig(${data.snapshotId })' width="100px" height="100px" >
                              <img src='${picPath}/picture/delImg.png' id='img_${data.snapshotId }_delete'  onclick='delImg(${data.snapshotId })'  style='position : absolute;top : 0px;right : 0px;width : 20px;height : 18px;'/>
                          </c:if>
                      </div>
                  </c:forEach>
                  <div id="wagesFlowList">

                  </div>
                  <div id="wagesFlowList_name">

                  </div>
                  <hr style="margin-bottom: 20px;">

                  <!-- 个人信用报告 -->
                  <span style="color: red;">*</span><span style="font-size: 24px;">个人信用报告</span><span style="font-size: 12px;margin-left: 20px;">如房产抵押选择二抵，则必须提交</span>
                  <input style="margin-left: 600px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('2','creditReportList');">
                  <br/><hr>
                  <c:forEach items="${attachmentList }" var="data">
                      <div id="img_${data.fileName }_div" style="display : inline-block;position : relative;">
                          <c:if test="${data.attachment.type eq '2' }">
                              <img src="${picPath}${data.attachment.url }" id="img_${data.snapshotId }" onclick='showBig(${data.snapshotId })' width="100px" height="100px" >
                              <img src='${picPath}/picture/delImg.png' id='img_${data.snapshotId }_delete'  onclick='delImg(${data.snapshotId })'  style='position : absolute;top : 0px;right : 0px;width : 20px;height : 18px;'/>
                          </c:if></div>
                  </c:forEach>
                  <div id="creditReportList">

                  </div>
                  <div id="creditReportList_name">

                  </div>
                  <hr style="margin-bottom: 20px;">

                  <!-- 结婚证 -->
                  <span style="font-size: 24px;">结婚证</span><span style="font-size: 12px;margin-left: 20px;">如借款人为已婚，则必须提交结婚证</span>
                  <input style="margin-left: 600px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('3','marriageList');">
                  <br/><hr>
                  <c:forEach items="${attachmentList }" var="data">
                      <div id="img_${data.fileName }_div" style="display : inline-block;position : relative;">
                          <c:if test="${data.attachment.type eq '3' }">
                              <img src="${picPath}${data.attachment.url }" id="img_${data.snapshotId }" onclick='showBig(${data.snapshotId })' width="100px" height="100px" >
                              <img src='${picPath}/picture/delImg.png' id='img_${data.snapshotId }_delete'  onclick='delImg(${data.snapshotId })'  style='position : absolute;top : 0px;right : 0px;width : 20px;height : 18px;'/>
                          </c:if></div>
                  </c:forEach>
                  <div id="marriageList">

                  </div>
                  <div id="marriageList_name">

                  </div>
                  <hr style="margin-bottom: 20px;">

                  <!-- 社保截图 -->
                  <span style="font-size: 24px;">社保截图</span><span style="font-size: 12px;margin-left: 20px;">此项为加分项，用于提高批贷额度</span>
                  <input style="margin-left: 600px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('4','socialSecurityList');">
                  <br/><hr>
                  <c:forEach items="${attachmentList }" var="data">
                      <div id="img_${data.fileName }_div" style="display : inline-block;position : relative;">
                          <c:if test="${data.attachment.type eq '4' }">
                              <img src="${picPath}${data.attachment.url }" id="img_${data.snapshotId }" onclick='showBig(${data.snapshotId })' width="100px" height="100px" >
                              <img src='${picPath}/picture/delImg.png' id='img_${data.snapshotId }_delete'  onclick='delImg(${data.snapshotId })'  style='position : absolute;top : 0px;right : 0px;width : 20px;height : 18px;'/>
                          </c:if></div>
                  </c:forEach>
                  <div id="socialSecurityList">

                  </div>
                  <div id="socialSecurityList_name">

                  </div>
                  <hr style="margin-bottom: 20px;">

                  <!-- 收入证明 -->
                  <span style="font-size: 24px;">收入证明</span><span style="font-size: 12px;margin-left: 20px;">如房产抵押选择二抵，则必须提交</span>
                  <input style="margin-left: 600px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('5','proofIncomeList');">
                  <br/><hr>
                  <c:forEach items="${attachmentList }" var="data">
                      <div id="img_${data.fileName }_div" style="display : inline-block;position : relative;">
                          <c:if test="${data.attachment.type eq '5' }">
                              <img src="${picPath}${data.attachment.url }" id="img_${data.snapshotId }" onclick='showBig(${data.snapshotId })' width="100px" height="100px" >
                              <img src='${picPath}/picture/delImg.png' id='img_${data.snapshotId }_delete'  onclick='delImg(${data.snapshotId })'  style='position : absolute;top : 0px;right : 0px;width : 20px;height : 18px;'/>
                          </c:if></div>
                  </c:forEach>
                  <div id="proofIncomeList">

                  </div>
                  <div id="proofIncomeList_name">

                  </div>
                  <hr style="margin-bottom: 20px;">

                  <!-- 房产证 -->
                  <span style="font-size: 24px;">房产证</span><span style="font-size: 12px;margin-left: 20px;">如房产抵押则必须提交</span>
                  <input style="margin-left: 600px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('6','houseCertificateList');">
                  <br/><hr>
                  <c:forEach items="${attachmentList }" var="data">
                      <div id="img_${data.fileName }_div" style="display : inline-block;position : relative;">
                          <c:if test="${data.attachment.type eq '6' }">
                              <img src="${picPath}${data.attachment.url }" id="img_${data.snapshotId }" onclick='showBig(${data.snapshotId })' width="100px" height="100px" >
                              <img src='${picPath}/picture/delImg.png' id='img_${data.snapshotId }_delete'  onclick='delImg(${data.snapshotId })'  style='position : absolute;top : 0px;right : 0px;width : 20px;height : 18px;'/>
                          </c:if></div>
                  </c:forEach>
                  <div id="houseCertificateList">

                  </div>
                  <div id="houseCertificateList_name">

                  </div>
                  <hr style="margin-bottom: 20px;">

                  <!-- 土地证 -->
                  <span style="font-size: 24px;">土地证</span><span style="font-size: 12px;margin-left: 20px;">如有则提交</span>
                  <input style="margin-left: 600px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('7','landCertificateList');">
                  <br/><hr>
                  <c:forEach items="${attachmentList }" var="data">
                      <div id="img_${data.fileName }_div" style="display : inline-block;position : relative;">
                          <c:if test="${data.attachment.type eq '7' }">
                              <img src="${picPath}${data.attachment.url }" id="img_${data.snapshotId }" onclick='showBig(${data.snapshotId })' width="100px" height="100px" >
                              <img src='${picPath}/picture/delImg.png' id='img_${data.snapshotId }_delete'  onclick='delImg(${data.snapshotId })'  style='position : absolute;top : 0px;right : 0px;width : 20px;height : 18px;'/>
                          </c:if></div>
                  </c:forEach>
                  <div id="landCertificateList">

                  </div>
                  <div id="landCertificateList_name">

                  </div>
                  <hr style="margin-bottom: 20px;">
			  </c:if>
              <c:if test="${not empty isCredit &&  isCredit eq '1'}">
                  <!-- 合同协议 -->
                  <span style="font-size: 24px;">合同协议</span><span style="font-size: 12px;margin-left: 20px;"></span>
                  <input style="margin-left: 600px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('10','wagesFlowList');">
                  <br/><hr>
                  <c:forEach items="${attachmentList }" var="data">
                      <div id="img_${data.fileName }_div" style="display : inline-block;position : relative;">
                          <c:if test="${data.attachment.type eq '1' }">
                              <img src="${picPath}${data.attachment.url }" id="img_${data.snapshotId }" onclick='showBig(${data.snapshotId })' width="100px" height="100px" >
                              <img src='${picPath}/picture/delImg.png' id='img_${data.snapshotId }_delete'  onclick='delImg(${data.snapshotId })'  style='position : absolute;top : 0px;right : 0px;width : 20px;height : 18px;'/>
                          </c:if>
                      </div>
                  </c:forEach>
                  <div id="wagesFlowList">

                  </div>
                  <div id="wagesFlowList_name">

                  </div>
                  <hr style="margin-bottom: 20px;">

                  <!-- 车辆照片 -->
                  <span style="color: red;">*</span><span style="font-size: 24px;">车辆照片</span><span style="font-size: 12px;margin-left: 20px;"></span>
                  <input style="margin-left: 600px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('11','creditReportList');">
                  <br/><hr>
                  <c:forEach items="${attachmentList }" var="data">
                      <div id="img_${data.fileName }_div" style="display : inline-block;position : relative;">
                          <c:if test="${data.attachment.type eq '2' }">
                              <img src="${picPath}${data.attachment.url }" id="img_${data.snapshotId }" onclick='showBig(${data.snapshotId })' width="100px" height="100px" >
                              <img src='${picPath}/picture/delImg.png' id='img_${data.snapshotId }_delete'  onclick='delImg(${data.snapshotId })'  style='position : absolute;top : 0px;right : 0px;width : 20px;height : 18px;'/>
                          </c:if></div>
                  </c:forEach>
                  <div id="creditReportList">

                  </div>
                  <div id="creditReportList_name">

                  </div>
                  <hr style="margin-bottom: 20px;">

                  <!-- 证件相关 -->
                  <span style="font-size: 24px;">证件相关</span><span style="font-size: 12px;margin-left: 20px;"></span>
                  <input style="margin-left: 600px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('12','marriageList');">
                  <br/><hr>
                  <c:forEach items="${attachmentList }" var="data">
                      <div id="img_${data.fileName }_div" style="display : inline-block;position : relative;">
                          <c:if test="${data.attachment.type eq '3' }">
                              <img src="${picPath}${data.attachment.url }" id="img_${data.snapshotId }" onclick='showBig(${data.snapshotId })' width="100px" height="100px" >
                              <img src='${picPath}/picture/delImg.png' id='img_${data.snapshotId }_delete'  onclick='delImg(${data.snapshotId })'  style='position : absolute;top : 0px;right : 0px;width : 20px;height : 18px;'/>
                          </c:if></div>
                  </c:forEach>
                  <div id="marriageList">

                  </div>
                  <div id="marriageList_name">

                  </div>
                  <hr style="margin-bottom: 20px;">
              </c:if>
<c:if test="${not empty isCredit &&  isCredit eq '2'}">
              <!--债权清单 -->
              <span style="color: red;">*</span><span style="font-size: 24px;">债权清单</span>
              <input style="margin-left: 520px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('0','identityList');">
              <br/><hr>
              <hr style="margin-bottom: 20px;">

              <!-- 其它 -->
              <span style="font-size: 24px;">其它</span><span style="font-size: 12px;margin-left: 20px;"></span>
              <input style="margin-left: 520px;" type="button" class="btn btn-primary" value="上传附件" onclick="upButton('1','wagesFlowList');">
              <br/><hr>
              <div id="wagesFlowList">

              </div>
              <div id="wagesFlowList_name">

              </div>
              <hr style="margin-bottom: 20px;">

</c:if>

          </td>
      </tr>
	</table>
	
</form>
</div>
<script type="text/javascript">
//执行:上传附件。
function upButton(state,typeList){
	var divNum = document.getElementById("identityList").getElementsByTagName("div").length;
	if(state == 0)
	{
		if(divNum>=2)
		{
			$.messager.alert("系统提示", "身份证照片最多放2张!", "info");
			return;
		}
	}
	
	$("#loan_add_part4_form").after("<div id='upload_snapshot_add' style=' padding:10px; '></div>");
	$("#upload_snapshot_add").dialog({
        resizable: false,
        title: '上传图片',
        href: '${ctx}/jsp/loanManage/loan/uploadSnapshotAdd?state='+ state + '&typeList=' + typeList + '&loanApplicationId=${loanApplicationId}' + '&isCode=0',
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
function showBig(cusId)
{
	$("#showTable").after("<div id='show_big_picture' style=' padding:10px; '></div>");
	 $("#show_big_picture").dialog({
	        resizable: false,
	        title: '大图',
	        href: '${ctx}/jsp/loanManage/loan/toShowBigPicture?cusId='+cusId,
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

function delImg(cusId,id)
{
	$.messager.confirm('确认信息','是否确认删除该张图片？',function(r){
		if(r){
			try {
            	$.post("${ctx}/jsp/loanManage/loan/delImg?cusId="+cusId,
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
                    			return;
                    		}
            	  		});
            } catch (e) {
                alert("删除失败！"+e);
                return;
            }
		} 
	});
}

//下载图片
function downloadImg(){
	$.post("${ctx}/jsp/loanPublish/loan/getcustomerUploadAttachment",{loanApplicationId:'${loanApplicationId }'},function(data){
		if(0 == data){
			$.messager.alert("系统提示", "该用户未上传图片", "info");
		}else{
			window.location.href="${ctx}/jsp/loanPublish/loan/download?loanApplicationId=${loanApplicationId }";		
		}		
	});
}
</script>
</body>
</html>
