<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="upload_snapshot_add" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="upload_snapshot_add_form" method="post" enctype="multipart/form-data">
	<input type="hidden" name="state" value="${state }"/>
	<input type="hidden" name="typeList" value="${typeList }"/>
	<input type="hidden" name="userId" value="${userId }"/>
	<input type="hidden" name="isCode" value="${isCode }"/>
	<input type="hidden" name="enterpriseId" value="${enterpriseId }"/>
    <div class="control-group">

        <label class="control-label">附件类型:</label>
        <div class="controls">
            <c:if test="${state == 0}">法人身份证</c:if>
            <c:if test="${state == 1}">法人个人征信</c:if>
            <c:if test="${state == 2}">税务登记证</c:if>
            <c:if test="${state == 3}">营业执照 </c:if>
            <c:if test="${state == 4}">组织机构代码证 </c:if>
            <c:if test="${state == 5}">开户许可证</c:if>
            <c:if test="${state == 6}">验资报告</c:if>
            <c:if test="${state == 7}">经营场所租凭合同</c:if>
            <c:if test="${state == 8}">近三年的财务报表</c:if>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">附件标题:</label>
        <div class="controls">
			<input type="text" style="width: 200px"
             class="easyui-validatebox" required="true" missingMessage="标题不能为空"
                   name="msgName" >
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" >文件上传:<span style="color: red;">*</span></label>
        <div class="controls">
            <input id="imgFileId" type="file" name="imgFile" onchange="previewImage(this)" />   
        </div>
    </div>
    <div id="preview" style="display: none;">
    	<img id="imghead" border=0 src=''  style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image);width: 200px;height: 200px;">
	</div>
</form>
</div>
<script language="javascript" >
	// 执行：保存操作
    $("#upload_snapshot_add_form").form({
        
        onSubmit:function() {
        	if($("#imgFileId").val() != ''){
        		 var filepath=$("#imgFileId").val();
        		 var extStart=filepath.lastIndexOf(".");	
        		 var ext=filepath.substring(extStart,filepath.length).toUpperCase();
        	        if(ext!=".BMP"&&ext!=".PNG"&&ext!=".JPG"&&ext!=".JPEG"){
        	        	alert("图片限于bmp,png,jpeg,jpg格式");
        	        	return false;
        	        }
        	        
        			return $(this).form('validate');
        		}
        	else
        		{
        			$.messager.alert("系统提示", "请选择图片!", "info");
        		}
        	
        },
        url:'${ctx}/jsp/enterprise/saveUploadSnapshot',
        success:function(data) {
        	var resultData = eval('(' + data + ')');
            if (resultData.resultState == "success") {
                $.messager.alert("系统提示", "添加成功!", "info");
                parent.$("#upload_snapshot_add").dialog("close");
                $("<div id='img_"+resultData.id+"_div' style='display : inline-block;position : relative;margin-right: 15px;'><input type='checkbox' id='isDisplay_"+resultData.cusId+"' onclick='onIsDisplay("+resultData.cusId+");'><font style='font-size: 12px;'>前台显示</font><br/><img id='img_"+resultData.id+"' style='width: 100px;height: 100px;' alt='' src='' onclick='showBig("+resultData.cusId+")'> <img src='${picPath}/picture/delImg.png' id='img_"+resultData.id+"_delete'  onclick='delImg("+resultData.cusId+")'  style='position : absolute;top : 0px;right : 0px;width : 20px;height : 18px;'/> </div>").appendTo(parent.$("#"+resultData.typeList));
                parent.$("#img_"+resultData.id).attr("src", '${picPath}'+resultData.url);
				
             
                $("<span id='img_"+resultData.id+"_span' style='margin: 20px;'>"+resultData.msgName+"</span>").appendTo(parent.$("#"+resultData.typeList+"_name"));
            }else if(data.resultState == "error") {
            	$.messager.alert("系统提示", "添加失败!", "info");
            }
        }
    });
    
   		//图片上传预览    IE是用了滤镜。
        function previewImage(file)
        {
          var MAXWIDTH  = 260; 
          var MAXHEIGHT = 180;
          var div = document.getElementById('preview');
          if (file.files && file.files[0])
          {
              div.innerHTML ='<img id=imghead>';
              var img = document.getElementById('imghead');
              img.onload = function(){
                var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
                img.width  =  rect.width;
                img.height =  rect.height;
                img.style.marginTop = rect.top+'px';
              };
              var reader = new FileReader();
              reader.onload = function(evt){img.src = evt.target.result;};
              reader.readAsDataURL(file.files[0]);
          }
          else //兼容IE
          {
            var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
            file.select();
            var src = document.selection.createRange().text;
            div.innerHTML = '<img id=imghead>';
            var img = document.getElementById('imghead');
            img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
            var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
            status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
            div.innerHTML = "<div id=divhead style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
          }
          document.getElementById("preview").style.display = "inline";
        }
        function clacImgZoomParam( maxWidth, maxHeight, width, height ){
            var param = {top:0, left:0, width:width, height:height};
            if( width>maxWidth || height>maxHeight )
            {
                rateWidth = width / maxWidth;
                rateHeight = height / maxHeight;
                 
                if( rateWidth > rateHeight )
                {
                    param.width =  maxWidth;
                    param.height = Math.round(height / rateWidth);
                }else
                {
                    param.width = Math.round(width / rateHeight);
                    param.height = maxHeight;
                }
            }
             
            param.left = Math.round((maxWidth - param.width) / 2);
            param.top = Math.round((maxHeight - param.height) / 2);
            return param;
        }
</script>
</body>
</html>