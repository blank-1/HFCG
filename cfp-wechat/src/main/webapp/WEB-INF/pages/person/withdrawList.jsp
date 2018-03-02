<%@ page language="java"  pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />    
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<title>提现记录</title>
<%@include file="../common/common_js.jsp"%>
<script type="text/javascript">
$(document).on('scroll',function(){
	var scrollTop=$(document).scrollTop(),
		selfH=$(document).height()-$(window).height();
	if( scrollTop>=selfH){
		pageNo+=1;
		if(pageNo<=totalPage){
			searchHtml(pageNo,20);
		}
	}
})
$(function() { 
	searchHtml(1,20); 
	
});
var pageNo=1,pageSize=20,totalPage; 
function searchHtml(page,rows){
	   var thtml="";
	   var now=dateTimeFormatter(new Date());
	   now=now.substr(0,16);
	   $.ajax({
		     url:rootPath+"/person/withDrawList",
		     type:"post",
		     data: {
		       "pageSize": rows,
		       "pageNo": page
		     },
		     success: function (data) {
		       var d_rows = data.rows;
		           totalPage = data.totalPage;
		           pageNo = data.currentPage;
		       for(var i=0;i<d_rows.length;i++){
		         var data = d_rows[i];
		         var time=data.createTime!=null?dateTimeFormatter(data.createTime):"";
		         time=time.substr(0,16);
		         time=calcDate(time,now);
		         var card=(data.cardNo==null?"":data.cardNo)+"";
		         card=card.substr(-4);
		         thtml +='<tr>';
		         thtml +='<td>'+time+'</td>';
		         thtml +='<td>'+data.withdrawAmount.toFixed(2)+'</td>';
		         thtml +='<td>'+card+'</td>';
		         thtml +='</tr>';
		       };
		       $('#income_list').append(thtml);
		       var trs=$("#income_list").find("tr");
		       if(trs.length==0){
		   		   $("#empty_div").show();
		   	   }
		     }
	   });
}
</script>
<style>
table.gridtable {
	font-family: verdana,arial,sans-serif;
	font-size:12px;
	color:#333333;
	border-collapse: collapse;
	width: 100%;
	margin: 3rem 0;
}
.gridtable tr{
	width: 100%;
	border-bottom:1px solid #ccc;
}
table.gridtable tr td{
	width: 33.3%;
	text-align: center;
	line-height: 3rem;
	color: #4b4b4b;
}
table.gridtable tr td:nth-child(1),.w_tit li:nth-child(1){
	width: 25%;
}
table.gridtable tr td:nth-child(2),.w_tit li:nth-child(2){
	width: 45%;
}
table.gridtable tr td:nth-child(3),.w_tit li:nth-child(3){
	width: 30%;
}
.w_tit{
	width: 100%;
	height: 3rem;
	background: #e2f3fa;
	display: -webkit-box;
	position: fixed;
	top: 0;
	left: 0;
}
.w_tit li{
	width: 33.3%;
	text-align: center;
	line-height: 3rem;
	color: #0199dc;
	font-size: 1.4rem;
}
.w_empty{
	width: 50%;
	height: auto;
	margin: 5rem auto;
}
.w_empty img{
	width: 100%;
	height: 100%;

}
</style>
</head>
<body>
	<div class="w_messageBox">
		<ul class="w_tit">
			<li>时间</li>
			<li>金额（元）</li>
			<li>银行卡</li>
		</ul>
		<div id="empty_div" class="w_empty" style="display: none;"><!--  如果内容为空此盒子显示  -->
			<img src="${ctx }/images/icon_noData.png" alt="">
		</div>
		<table id="income_list" class="gridtable" cellspacing="0"><!--  有数据渲染tr  --></table>
	</div>
</body>

</html>