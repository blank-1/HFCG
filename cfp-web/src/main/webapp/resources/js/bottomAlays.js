// JavaScript Document

	//总在最底部
	function bottomB(){
	
		var Sys = {};

		var ua = navigator.userAgent.toLowerCase();
		
		var s;
		
		(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
		
		(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
		
		(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
		
		(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
		
		(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
		
		//以下进行测试
		
		var banben;//文档高度
		
		var liulanqi;//浏览器高度
		
		if (Sys.ie){
			
			banben = document.body.clientHeight;
			liulanqi =  document.documentElement.clientHeight ;
		}else if (Sys.firefox){
			
			banben =document.body.clientHeight ;
			liulanqi = document.documentElement.clientHeight ;
		}else if (Sys.chrome){
			
			banben = document.body.clientHeight;
			liulanqi =  document.documentElement.clientHeight ;
		}else if (Sys.opera){
			
			banben = document.documentElement.clientHeight ;
			liulanqi = document.body.clientHeight ;
		}else if (Sys.safari){
			
			banben = document.body.clientHeight;
			liulanqi =  document.documentElement.clientHeight ;
			
		}else{
			
			banben = document.body.clientHeight;
			liulanqi =  document.documentElement.clientHeight ;
			
		}
		$(".zhezhao1,.zhezhao,.zhezhao5,.zhezhao2").css("height",document.body.clientHeight);
		
		if($(".footer").html()===undefined){
			
		}else{
			if(banben>=liulanqi ){
				
				$(".footer").css({"position":"inherit"});
			}else{
				
				$(".footer").css({"position":"absolute","bottom":"0px","width":"100%"});
				
			}
		}
	}
