var pageNo=1,pageSize=20,totalPage;

$(function(){
	
	pushData(0);
	
	$("#w_vouList").on("click","li",function(){
        var thisIndex = $(this).index(),
            moveW = $(".w_page").width();
        $(this).find("a").addClass("w_active").parent().siblings("li").find("a").removeClass("w_active");
        $(".orderList").animate({
            "margin-left":-thisIndex*moveW+"px"
        },300)
        pageNo=1;
        for(var i=0;i<3;i++){
        	$(".pay_page"+i).html("");
		}
        pushData(thisIndex);
    })
    
})

function pushData(ind){
	$(".page"+ind).on("scroll",function(){
		var scrollTop= $(this).find("ul").outerHeight(true),
			selfH=$(this).scrollTop()+$(window).height();
		    if( scrollTop <= selfH){
		    	if(pageNo<=totalPage){
					pageNo++;
					seachHtml(pageNo,pageSize,ind);
				}
		    }
		
	});
	seachHtml(pageNo,pageSize,ind);
}

function seachHtml(page,rows,inx){
	var html="";
	var state="-1";
	if(inx==1){
		state="0";
	}else if(inx==2){
		state="1";
	}
	$.ajax({
		url:rootPath+"/person/voucherList",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"state":state,
			"couponType":"-1"
		},
		success: function (data) {
			var d_rows = data.rows;
			totalPage = data.totalPage;
			if(data.rows == 0 && page==1){
				html="<dl class='w_noDate'>"+
	    				"<dd>"+
	    					"<img src='"+rootPath+"/images/noVouer.png' />"+
	    				"</dd>"+
	    				"<dt>暂无优惠券</dt>"+
	    			"</dl>";
			}else{
				for(var i=0;i<d_rows.length;i++){
					var _data = d_rows[i];
					var type=" ";
					var content="";
					if(_data.voucherCouponType=='2'){
						type+="w_Interest ";
						content="<dd>"+
		                            "<p class='percent'>"+_data.voucherValue+"<span>%</span><br><span>加息券</span></p>"+
		                        "</dd>";
					}else{
						type+="w_Voucher ";
						var q="";
						if(_data.voucherCouponType=='3'){
							q="<br><span>提现券</span>";
						}else{
							q="<br><span>财富券</span>";
						}
						content="<dd>"+
		                            "<p>￥"+_data.amountStr.substr(0,_data.amountStr.length-1)+q+"</p>"+
		                        "</dd>";
					}
					if(_data.status!='0'){
						if(_data.voucherCouponType!='2'){
							type+="w_use ";
						}else{
							if(_data.status=='2'){
								type+="w_use ";
							}
						}
						if(_data.status=='3'){
							if(_data.voucherCouponType!='2'){
								type+="expire";
							}else{
								type+="w_use expire";
							}
							
						}
					}
					var vuser="";
					if(_data.status!='0'){
						vuser="class='w_limit'";
					}
					var r=_data.voucherRemark==null?"——":_data.voucherRemark;
					if(_data.voucherCouponType=='3'){
						r=_data.amountStr+"提现券";
					}
					var remark = "<p "+vuser+">"+r+"</p>";
					if(_data.voucherCouponType=='2'){
						remark="<ul class='percentList'>" +
									"<li>起投："+_data.amountStartStr+"元</li>" +
									"<li>限："+(_data.conditionStr==undefined||_data.conditionStr==null||_data.conditionStr==""?"无":_data.conditionStr)+"</li>";
							    "</ul>";
					}
					var deadline="有效期限：长期有效";
					if(_data.endDate!=null){
						deadline='有效期 '+getDateStr(new Date(_data.endDate));
					}
					html+="<li class='w_public "+type+"'>"+
			                    "<dl>"+
			                    	content+
			                        "<dt>"+
			                            "<p>"+remark+"</p>"+
			                        "</dt>"+
			                    "</dl>"+
			                    "<p class='w_date'>"+deadline+"</p>"+
			                "</li>";
				}
			}
			$(".pay_page"+inx).append(html);
		}
	});
}