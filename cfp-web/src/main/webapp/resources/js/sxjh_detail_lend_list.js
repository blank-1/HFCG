	
	var lendProductPublishId;//省心产品发布ID
	//初始化
	$(function(){
		lendProductPublishId = $("#lendProductPublishId").val();
		searchHtml(1,10);
	});
	
	//省心投资列表首次查询
	function searchHtml(page,rows){
	    var thtml="";
	    $.ajax({
	      url:rootPath+"/finance/getSXJHLender",
	      type:"post",
	      data: {
	        "pageSize": rows,
	        "pageNo": page,
	        "lendProductPublishId": lendProductPublishId
	      },
	      success: function (data) {
	        var d_rows = data.rows;
	        var pageCount = data.totalPage;
	        for(var i=0;i<d_rows.length;i++){
	          var data = d_rows[i];
	          if(i%2==0){
	         	thtml += "<li>";
			  }else{
				thtml += "<li class='list-ul-li-bg'>";
			  }
	          thtml +="<ul>";
	          thtml +="<li class='w_worry'>"+data.lenderName+"</li>";
	          thtml +="<li>"+fmoney(data.lendAmount,2)+"</li>";
	          thtml +="<li  class='list-ul-line-height'>"+dateTimeFormatter(data.lendTime)+"</li>";
	          thtml +="</ul></li>";
	        }
	        $("#tbtable").html(thtml);
			$(".tcdPageCode").createPage({
			  pageCount:pageCount,
			  current:1,
			  backFn:function(p){
			    //点击分页效果
				  searchHtmlPage(parseInt(p),10);
			  }
			});
	        bottomB();
	      }
	    });
  }
  
	//省心投资列表分页查询
	function searchHtmlPage(page,rows){
		var thtml="";
		$.ajax({
			url:rootPath+"/finance/getSXJHLender",
			type:"post",
			data: {
			  "pageSize": rows,
			  "pageNo": page,
			  "lendProductPublishId": lendProductPublishId
		},
	    success: function (data) {
	      var d_rows = data.rows;
	      var pageCount = data.totalPage;
	      for(var i=0;i<d_rows.length;i++){
		        var data = d_rows[i];
		        if(i%2==0){
			       	thtml += "<li>";
				}else{
					thtml += "<li class='list-ul-li-bg'>";
				}
	        
		        thtml +="<ul>";
		        thtml +="<li class='w_worry'>"+data.lenderName+"</li>";
		        thtml +="<li>"+fmoney(data.lendAmount,2)+"</li>";
		        thtml +="<li class='list-ul-line-height'>"+dateTimeFormatter(data.lendTime)+"</li>";
		        thtml +="</ul></li>";
	      }
	      $('#tbtable').html(thtml);
	      bottomB();
	    }
	  });
	}