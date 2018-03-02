<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>

<!doctype html>
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

	
	<%@include file="../common/common_js.jsp"%>
	<link rel="stylesheet" href="${ctx }/css/style.css">
<title>代金券支付</title>
</head>
<body>
	<div class="w_Voucher" id="w_vouPag" style="overflow-y:scroll;"><!--w_vouPag为可用代金券-->
		
	</div>

</body>
<script type="text/javascript">
window.onload=function(){
	seachHtml(10,1,"0");
}
function seachHtml(rows,page,state){
	var thtml="";
	var thtmldiv="";
	$.ajax({
		url:rootPath+"/person/voucherList",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"state":state,
		},
		success: function (data) {
			var d_rows = data.rows;
			totalPage = data.totalPage;
			for(var i=0;i<d_rows.length;i++){
				var _data = d_rows[i];
				var rmk = _data.voucherRemark==null?"——":_data.voucherRemark;
				if(_data.status=='0'){
					thtml += '<dl class="w_vouList">';	
					thtml += '<dt><span>'+_data.amountStr+'</span></dt>';
					thtml += '<dd>';
					thtml += '<h2>'+_data.voucherName+'</h2>';
					thtml += '<p>'+rmk+'</p>';
					thtml += '<p>有效期：'+dateTimeFormatter(_data.createDate)+' 至 '+getDateStr(new Date(_data.endDate))+'</p>';
					thtml += '</dd></dl>';
				}
			}
			$("#w_vouPag").append(thtml);
	}
});
}
</script>
</html>