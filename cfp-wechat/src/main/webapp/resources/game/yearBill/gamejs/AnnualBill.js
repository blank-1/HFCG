var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        onSlideChangeEnd:function(swiper){ 
            var thisInd = swiper.activeIndex;
            if(thisInd == 0){
                $(".animated1").addClass("ani_1");
                $(".animated2").addClass("ani_2");
            }else{
                $(".animated1").removeClass("ani_1");
                $(".animated2").removeClass("ani_2");
            }
            if(thisInd == 1){
                $(".animated3").addClass("ani_3");
                $(".animated4").addClass("ani_4");
            }else{
                $(".animated3").removeClass("ani_3");
                $(".animated4").removeClass("ani_4");
            }
            if(thisInd == 2){
                $(".animated5").addClass("ani_5");
                $(".animated6").addClass("ani_6");
            }else{
                $(".animated5").removeClass("ani_5");
                $(".animated6").removeClass("ani_6");
            }
            if(thisInd == 3){
                $(".animated7").addClass("ani_7");
                $(".animated8").addClass("ani_8");
            }else{
                $(".animated7").removeClass("ani_7");
                $(".animated8").removeClass("ani_8");
            }
            if(thisInd == 4){
                $(".animated9").addClass("ani_9");
                $(".animated10").addClass("ani_10");
                $(".animate13").addClass("Soar");
            }else{
                $(".animated9").removeClass("ani_9");
                $(".animated10").removeClass("ani_10");
                 $(".animate13").removeClass("Soar");
            }
            if(thisInd == 5){
                $(".animated11").addClass("ani_11");
                $(".animated12").addClass("ani_12");
            }else{
                $(".animated11").removeClass("ani_11");
                $(".animated12").removeClass("ani_12");
            }
        }
    });

$("#audioCtl").on("touchend",function(e) {
    e.stopPropagation();
    e.preventDefault();
    $(this).toggleClass("audioCtl2");
    if ($(this).hasClass("audioCtl2")) {
        $("#media")[0].pause();
    }else {
        $("#media")[0].play();
    }
})