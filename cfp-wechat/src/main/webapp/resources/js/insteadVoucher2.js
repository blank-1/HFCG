//初始化函数	
$(function(){
	var swiper = new Swiper('.swiper-container', {
	    pagination: '.swiper-pagination',
	    paginationClickable: true,
	    onSlideChangeEnd:function(mySwiper){
	       
	    }
	});
	new iScroll('noavailable',{
	      onBeforeScrollStart:function(e){
	        var target=e.target;
	        while(target.nodeType!=1){
	            target=target.parentNode;
	        }
	         var ele=e.target.tagName.toLowerCase();
	         if(ele!='input' && ele!='textarea' && ele!='select'){
	            e.preventDefault();
	         }
	    },
	    checkDOMChanges:true,
	    fadeScrollbar:true,
	    onScrollLimit:function(){
	        //ajax
	    }
	});
	new iScroll('all',{
	      onBeforeScrollStart:function(e){
	        var target=e.target;
	        while(target.nodeType!=1){
	            target=target.parentNode;
	        }
	         var ele=e.target.tagName.toLowerCase();
	         if(ele!='input' && ele!='textarea' && ele!='select'){
	            e.preventDefault();
	         }
	    },
	    checkDOMChanges:true,
	    fadeScrollbar:true,
	    onScrollLimit:function(){
	        //ajax
	    }
	});
	new iScroll('available',{
	      onBeforeScrollStart:function(e){
	        var target=e.target;
	        while(target.nodeType!=1){
	            target=target.parentNode;
	        }
	         var ele=e.target.tagName.toLowerCase();
	         if(ele!='input' && ele!='textarea' && ele!='select'){
	            e.preventDefault();
	         }
	    },
	    checkDOMChanges:true,
	    fadeScrollbar:true,
	    onScrollLimit:function(){
	        //ajax
	    }
	});
})
