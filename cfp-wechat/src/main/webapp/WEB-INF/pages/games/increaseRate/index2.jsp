<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes" />    
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
	<meta name="format-detection" content="telephone=no"/>
	<meta name="msapplication-tap-highlight" content="no" />
	<title>加息券来了</title>	
</head>
<body>
	<div>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</div>
	<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		var ints=[0,0,0,0,0];
		for(var i=0;i<10000;i++){
			setTimeout(function() {
				$.ajax({
					url:'test',
					type:"post",
					dataType:"json",
					success:function(data){
						if(data==1){
							ints[0]=ints[0]+1;
						}else if(data==2){
							ints[1]=ints[1]+1;
						}else if(data==3){
							ints[2]=ints[2]+1;
						}else if(data==4){
							ints[3]=ints[3]+1;
						}else if(data==5){
							ints[4]=ints[4]+1;
						}
					},
					error:function(data){
						console.log("error");
					}
				});
			}, 2);
		}
		setTimeout(function() {
			console.log(ints[0]+","+ints[1]+","+ints[2]+","+ints[3]+","+ints[4]);
		}, 30000);
		
	});
	
	</script>
</body>
</html>