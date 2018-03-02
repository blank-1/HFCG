<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="show_big_picture" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="show_big_picture_form" method="post">
	<input type="hidden"  id="cusId" value="${cusId }"/>
    
    <div class="control-group">
        <label class="control-label" id="imgName">${imgName }</label>
    </div>
    
    <div id="preview">
    	<img id="imghead"  border=0 src='${picPath }${url }'  style="width: 398px;height: 300px;">
	</div>
    <div style="text-align:center;">
    	<a href="#" class="easyui-linkbutton" plain="true" onclick="imgPaging('0')">上一张</a>
    	<a href="#" class="easyui-linkbutton" plain="true" onclick="imgPaging('1')">下一张</a>
    </div>
</form>
</div>
<script type="text/javascript">
function imgPaging(pageState)
{	
   	$.post("${ctx}/jsp/enterprise/imgPaging?pageState="+pageState+"&cusId="+$('#cusId').val(),
           	
			function(data){				
        		if(data.resultState == "success"){
        			$('#imgName').html(data.imgName);
        			$('#imghead').attr("src", '${picPath }'+data.url);
        			$('#cusId').attr("value", data.cusId);
        		}
        		if(data.resultState == "noUpper"){
            			$.messager.alert("系统提示", "已是第一张", "info");
        			return;
        		}
        		if(data.resultState == "noNext"){
        				$.messager.alert("系统提示", "已是最后一张", "info");

        			}
	  		}); 
}
</script>
</body>
</html>