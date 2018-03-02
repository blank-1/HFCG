
(function($,window){
    var con = $(".container .wrapper");
    var aitems = $(".container .wrapper .pages");
    var page = 0;
    var pages = $(".wrapper .pages");
    var len=aitems.length;
    init(con, aitems, 30, 1, $(".container"));
    $(".container .wrapper .pages").eq(0).css({
        display: "block"
    });
    $(".container .wrapper .pages").eq(1).find(".animate").css({
        'display':'none'
    });

    con.on("page", function(e) {

        var curPage = e.data;
        if (curPage >= 0 && curPage <= len) {
            
            $(".container .wrapper .pages").find(".animate").css({
                'display':'none'
            });
            $(".container .wrapper .pages").eq(curPage).find(".animate").css({
                'display':'block'
            });
            
        }
        if(curPage==4){
            $("#arr").hide() 
        }else{
            $("#arr").show() 
        }
    });
    $(function() {
        $(".wrapper li").each(function() {
            var thisi = $(this).index();
            $(this).find(".span").on("click", function() {
                swipepage.goPage(thisi + 1);
            });
        });
        
    });
    var height = $(window).height();
    $(".fixTxt").css({
        height: height
    });
    $(document).ready(function() {
        //alert('c');
        var _height = parseInt(document.documentElement.clientHeight);
        //var _height=$(window).height();
        var _width = parseInt($(document.body).css("width"));
        var _sheight = parseInt(window.screen.height);
        var _swidth = parseInt(window.screen.width);
        //alert(_width);
        $(".wrapper .pages").each(function() {
            var thisi = $(this).index();
            $(this).find(".span").on("tap", function() {
                init.goPage(thisi + 1);
            });
        });
    });
})(Zepto,window);

$(document).ready(function() {
    //设置基准响应比率；
    var _height = parseInt(document.documentElement.clientHeight);
    var _width = parseInt($(document.body).css("width"));
    var _sheight = parseInt(window.screen.height);
    var _swidth = parseInt(window.screen.width);
    $(window).bind("orientationchange", function() {
        page_reor();
    });
    function page_reor() {
        if (window.orientation == -90 || window.orientation == 90) {
            if (_width < _height) {
                _new_width = _width * _sheight / _swidth;
                _new_height = _swidth;
            } else {
                //alert('b');
                _new_width = _width;
                _new_height = _height;
            }
        } else {
            if (_width > _height) {
                //alert('c');
                if (_swidth > _sheight) {
                    _new_width = _width * _sheight / _swidth;
                    _new_height = _height * _swidth / _sheight;
                } else {
                    _new_width = _width * _swidth / _sheight;
                    _new_height = _height * _sheight / _swidth;
                }
            } else {
                //alert(_swidth+'d'+_sheight);
                _new_height = _height;
                _new_width = _width;
            }
        }
        //var _new_width=parseInt(window.screen.width);
        // }
        //alert(_new_width);
        //var _new_width=parseInt($(window).width());
        //alert(_new_width);
        $("body").css({
            "font-size": 32 * _new_height / 1136 + "px"
        });
    }
    $("body").css({
        "font-size": 32 * _height / 1136 + "px"
    });
})

