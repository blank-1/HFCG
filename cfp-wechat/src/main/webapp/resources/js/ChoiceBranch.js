var pid = '';//省id
var cid = '';//市id
	
$(function(){
	
	$("#Province").html("");//清理省列表
	$('#market').html("");//清空市列表

	$(".showProvince").on("click",function(){//省选择事件
		$(this).next('div').show();
		$.ajax({
			url:rootPath + "/person/loadLianLianCitySelect?pProvinceCityId=0",
			type: "POST",
	        dataType: "json",
	        success:function(data){
	        	html = "";
	        	$.each(data,function(key,val){
	  				html+="<li id='" + val.id + "'>"+val.text+"</li>";
	  			})
	  			$("#Province").html(html);
	        },
	        error:function(){
	        	alert("错误")
	        }
       })
	})
	
	$("#Province").on("click",'li',function(){//省选中事件
			
			var valu=$(this).text();//名称
			pid=$(this).attr("id");//赋值pid
			
			$(this).parent("div").prev("label").html(valu);//赋值名称
			
			$("#Province").hide();
			$('#market').html("");//清空市列表
			$(".showCity").html("市区");
			cid = '';
			
	})
	
	$(".showCity").on("click",function(){//市选择事件
		if(pid != ''){
			$(this).next('div').show();
			$.ajax({
				url:rootPath + "/person/loadLianLianCitySelect?pProvinceCityId=" + pid,
				type: "POST",
		        dataType: "json",
		        success:function(data){
		        	html = "";
		        	$.each(data,function(key,val){
		  				html+="<li id='" + val.id + "'>"+val.text+"</li>";
		  			})
		  			$("#market").html(html);
		        },
		        error:function(){
		        	alert("错误")
		        }
	       })
		}
	})
	
	$("#market").on("click","li",function(){//市选中事件
	  	var valu=$(this).text();
	  	cid=$(this).attr("id");//赋值cid
	  	
	  	$(this).parent("div").prev("label").html(valu);//赋值名称
	  	$("#market").hide();
	})
	
	$("#w_nextBtn").on("click",function(){//下一步事件
		$("#w_seachPid").attr("value",pid);
		$("#w_seachCid").attr("value",cid);
		$("#queryForm").submit();
	})
	
	$("#w_seachBtn").on("click",function(){//搜索事件
		$("#w_seachPid").attr("value",pid);
		$("#w_seachCid").attr("value",cid);
		$("#queryForm").submit();
	})
	
})


