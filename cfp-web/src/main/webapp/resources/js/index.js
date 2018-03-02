// JavaScript Document
$(document).ready(function(e) {
	/*
    $(".idbanner").hover(function(){
		console.log("a");
		$(this).addClass("round").removeClass("round2");
	},function(){
		console.log("b");
		$(this).addClass("round2").removeClass("round");
		
	});
	
	*/
	
	$(function(){
    	 $('html').resize(
            function(){
               var $this = $(this);
               $this.animate({opacity:'1.0',height:'+=40px',width:'+=15px'});
            }
        );
    });
	
	
});

