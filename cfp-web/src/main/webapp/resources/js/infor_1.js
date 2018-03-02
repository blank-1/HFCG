		
		
//多选框效果
	function selectAll(checkbox) {
		$('input[class=a]').prop('checked', $(checkbox).prop('checked'));
		if(!checkbox.checked){
			$('input[class=a]').eq(0).prop('checked',true);
		}
	};
	function selectAll3(checkbox) {
		$('input[class=c]').prop('checked', $(checkbox).prop('checked'));
		if(!checkbox.checked){
			$('input[class=c]').eq(0).prop('checked',true);
		}
	};
	function selectAll4(checkbox) {
		$('input[class=e]').prop('checked', $(checkbox).prop('checked'));
		if(!checkbox.checked){
			$('input[class=e]').eq(0).prop('checked',true);
		}
	};
	function selectAllw(checkbox) {
		searchHtml(null,10,true);
	};
	function selectAlls(checkbox) {
		searchHtml(null,10,true);
	};
	$(function(){
		function checkp(xuan1){
			var falg=0;
			xuan1.each(function(){
				if($(this).is(":checked")){
					falg+=1;
				}
			});
			if(falg>0) return true; else return false;
		}
		$('.xuan1 .a').click(function(){
			if(!$(this).is(":checked")){
				$(".xuan1").find("input[type=checkbox]").eq(0).prop("checked",false);
			}
			if(!checkp($('.xuan1 .a'))){
				$(this).prop("checked",true);
			}
		});
		$('.xuan3 .c').click(function(){
			if(!$(this).is(":checked")){
				$(".xuan3").find("input[type=checkbox]").eq(0).prop("checked",false);
			}
			if(!checkp($('.xuan3 .c'))){
				$(this).prop("checked",true);
			}
		});
		$('.xuan4 .e').click(function(){
			if(!$(this).is(":checked")){
				$(".xuan4").find("input[type=checkbox]").eq(0).prop("checked",false);
			}
			if(!checkp($('.xuan4 .e'))){
				$(this).prop("checked",true);
			}
		});
		$('.xuan .w').click(function(){
			if(!$(this).is(":checked")){
				$(".xuan").find("input[type=checkbox]").eq(0).prop("checked",false);
			}
			if(!checkp($('.xuan .w'))){
				$(this).prop("checked",true);
			}
		});
		$('.xuan2 .d').click(function(){
			if(!$(this).is(":checked")){
				$(".xuan2").find("input[type=checkbox]").eq(0).prop("checked",false);
			}
			if(!checkp($('.xuan2 .d'))){
				$(this).prop("checked",true);
			}
		});
	});
//平台消息未读变已读
	$(function() {
		$(document).on("click",".infor_ullih_300",function(){
			$(this).next().text("已读").removeClass("pre_red");
			$("#imgAddress").attr("src", rootPath+"/images/pingt_ggao.jpg");
		});
	})
function searchLike(flag){	
	searchHtml(null,10,true);
} 
//订单查询
$(document).ready(function() {
	searchHtml(null,10,true);
 })
 //flag--加点击绑定事件分页
function searchHtml(page,rows,flag){
	var thtml="";
	if(flag){
		$("#userMessage").html("");
	}	 
	
	thtml =  '<li class="p-Right-ulli-bg">' +
						 '<ul class="p-Right-ulli-borb">' +
							'<li class="p-Right-xxgl-100 xxgl-hide">类型</li>' +
							'<li class="p-Right-xxgl-350">标题</li>' +
							'<li class="p-Right-xxgl-100 xxgl-hide">状态</li>' +
							'<li class="p-Right-xxgl-100 xxgl-hide">发送人</li>' +
							'<li class="p-Right-xxgl-225">日期</li>' +
						'</ul>' +
					'</li>';
	
		var w_value = [];//定义一个数组 
		var d_value = [];
		
		$(".msgtype.lcjh-state-one").each(function(){
			var value = $(this).attr("value");
			if(value=='-1')  {
				$(this).siblings(".msgtype").each(function(){
					w_value.push($(this).attr("value"));
				});
			}else{
				w_value.push(value);
			}
		});
		$(".msgstate.lcjh-state-one").each(function(){
			var value = $(this).attr("value");
			if(value=='-1')  {
				$(this).siblings(".msgstate").each(function(){
					d_value.push($(this).attr("value"));
				});
			}else{
				d_value.push(value);
			}
		});
       
		$.ajax({
			url:""+rootPath+"/message/userMessageList",
			type:"post",
			data:{"pageNo":page,"pageSize":rows,"messageType":w_value,"status":d_value},
			success:function(data){
				var _data = data.rows;
				var pageCount = data.totalPage;
				for(var i=0;i<_data.length;i++){
					var dataResult = _data[i];
						thtml += '<li>' +
						'<ul class="p-Right-ulli-borb">' +
							'<li class="p-Right-xxgl-100 xxgl-hide">'+getMessageType(dataResult.type)+'</li>' +
							'<li class="p-Right-xxgl-350" data-xxgl="'+dataResult.msgId+'" ><a href="javascript:readMessage('+dataResult.reciveId+','+dataResult.msgId+');" data-mask="mask"  data-id="pingt_ggao">'+dataResult.name+'</a></li>' ;
							if(dataResult.status == 1){
								thtml += '<li class="p-Right-xxgl-100 xxgl-hide">已读</li>' ;
							}else{
								thtml += '<li class="p-Right-xxgl-100 xxgl-hide pre_red">未读</li>' ;
							}
							thtml +='<li class="p-Right-xxgl-100 xxgl-hide">'+dataResult.senderName+'</li>' +
							'<li class="p-Right-xxgl-225">'+dateTimeFormatter(dataResult.sendTime)+'</li>' +
						'</ul>' +
					'</li>';
				} 
				$("#userMessage").html(thtml);
				bottomB();
				if(flag){
					 $(".tcdPageCode").createPage({
					        pageCount:pageCount,
					        current:1,
					        backFn:function(p){
					            //console.log(p);
							//点击分页效果
					            searchHtml(parseInt(p),10,false);
					        }
					    });
				}
			}
		});
}

function getMessageType(type){
	if(type == 0){
		return '资金通知';
	}else if(type == 1){
		return '平台公告';
	}else if(type == 2){
		return '站内信';
	}	
	return "";
}

//订单查询 zqlist end
function readMessage(reciveId,msgId){
	
	$.ajax({
		url:""+rootPath+"/message/readMessage",
		type:"post",
		data:{"msgId":msgId,"reciveId":reciveId},

		success:function(data){
				if(data != null){
					$("#messageName").text(data.name);
					$("#messageSendTime").text(dateTimeFormatter(data.sendTime));
					$("#imgAddress").attr("src", data.imgAddress);
					if(data.imgAddress == '' || data.imgAddress == null)
						$("#imgAddress").attr("src", rootPath+"/images/pingt_ggao.jpg"); 
					$("#messageContent").text("");
					$("#messageContent").append(data.content);
					messageCount();
					var obj = $("li[data-xxgl="+msgId+"]");
					obj.next().text("已读").removeClass("pre_red");
				}
			}
		});
}

	
//新版点击事件
$(function(){
	$('span.msgtype , span.msgstate').click(function(){
		$(this).addClass("lcjh-state-one").siblings().removeClass("lcjh-state-one");
		var value = $(this).attr("value");
		if(value == '-1'){
			selectAllw($(this));
		}else{
			searchLike($(this));
		}
	})
})
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
