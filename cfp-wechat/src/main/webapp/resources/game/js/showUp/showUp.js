/***$(function(){
	
	is_weixn();
	function is_weixn(){  
	    var ua = navigator.userAgent.toLowerCase();  
	    if(ua.match(/MicroMessenger/i)=="micromessenger") {  
	        return true;  
	    } else {  
	    	$("#sxjh").hide();
	    	$("#shouwup").show();
	    
	        return false; 
	        
	    }  
	    //$("#shouwup").hide(); 控制首页 推荐框中散标的显示 如果app端全部验证通过 要么启用这个 要么删除页面中的散标推荐模块 保证推荐模块只显示
	}  
	
	
	is_weixn1();	
	function is_weixn1(){  
	    var ua = navigator.userAgent.toLowerCase();  
	    if(ua.match(/MicroMessenger/i)=="micromessenger") {  
	        return true;  
	    } else {  
	    	$(".xsjh").hide();
	    	$(".tab p").width(40+"%");
	        return false; 
	        
	    }  
	}  
	
	is_weixn2();
	function is_weixn2(){  
	    var ua = navigator.userAgent.toLowerCase();  
	    if(ua.match(/MicroMessenger/i)=="micromessenger") {  
	        return true;  
	    } else {  
	    	$("#sxjh").hide();
	    	$(".list li").width(34+"%");
	        return false; 
	        
	    }  
	}  	
	
	
})
****/