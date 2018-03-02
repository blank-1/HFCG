$(function(){
    /**
     * @Author                           CXM
     * @DateTime2015-12-11T10:49:07+0800
     * @param                            {鼠标点击值}
     * @param                            {鼠标点击值}
     * @return                           {鼠标点击值}
     * 侧边栏鼠标滑过时个人中心信息出现
     */
    $(".uSides .lPerson").hover(function(){
        $(this).children("ul.thPerson").show();
    },function(){
        $(this).children("ul.thPerson").hide();
    });
    /**
     * @Author                           CXM
     * @DateTime2015-12-11T10:51:18+0800
     * @param                            {mouse}
     * @return                           {mouse}
     * 平台新闻 常见问题 切换
     */
    $(".stab").click(function(){
        $(this).addClass("saction").siblings("span,li").removeClass("saction");
        var id=$(this).attr("data-id");
        $("#"+id).siblings("div,ul.subul").hide();
        $("#"+id).fadeIn(500);
    });
    /**
     * @Author                           CXM
     * @DateTime2015-12-11T10:47:12+0800
     * @param                            {无}
     * @return                           {无}
     * 当第一次点击视频前的播放按钮时
     */
    $("#iVedio").click(function(){
        $(this).hide();
        $("#emVedio").show();
        $(this).parent().css("line-height","0px");
    });
})
