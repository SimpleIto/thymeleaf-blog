$(".sidebar-toggle").click(switchSidebar);
$(".shadow").click(function(){
    $(".sidebar-toggle img").attr("src","/img/sidebar_toggle_open.png");
    cleanContentShadow();
    $(".sidebar").toggleClass("hidden-xs");
})
function switchSidebar(){
    if($(".sidebar").css("display")=="none"){
        $(".sidebar-toggle img").attr("src","/img/sidebar_toggle_close.png");
        addContentShadow();
    }else{
        $(".sidebar-toggle img").attr("src","/img/sidebar_toggle_open.png");
        cleanContentShadow();
    }
    $(".sidebar").toggleClass("hidden-xs");
}
//初始化小屏幕判断变量
var screen= {};
isSmall=false;
//监听小屏幕判断变量，改变时执行相应逻辑
Object.defineProperty(screen,'isSmall',{
    set:function(newVal){
        isSmall = newVal;
        if(isSmall==false){
            bigScreenStatus();
        }
        else{
            smallScreenStatus();
        }
    },
    get:function(){
        return isSmall;
    }
});
if(window.innerWidth<768){
    screen.isSmall = true;
}
//页面大小变化事件
$(window).resize(function(){
    if(window.innerWidth<768&&screen.isSmall==false)
        screen.isSmall = true;
    else if(window.innerWidth>=768&&screen.isSmall==true)
        screen.isSmall = false;
});
function bigScreenStatus(){
    resetSidebarStatus();
    $(".sidebar-content").css("padding","20px 40px 0 40px").css("width","300px");
    $(".article-content").css("padding","20px");
    $(".article-title").css("margin","0 -40px").css("padding","15px 40px");
    $(".page-controls").css("margin","0 -40px").css("padding","20px 40px");
    $("main").css("padding","0 40px");
    $("#article-toc-container").css({
        "display":"block",
        "left":"1200px",
        "right":"unset",
        "top":"0"
    });
    $(".markdown-toc").css("width","325px");
    $(".article-category").css("margin","0 -40px 40px -40px");
}
function smallScreenStatus(){
    $(".sidebar-content").css("padding","20px 30px 0 30px").css("width","250px");
    $(".article-content").css("padding","20px 10px");
    $(".article-title").css("margin","0 -15px").css("padding","15px 20px");
    $(".page-controls").css("margin","0 -15px").css("padding","20px 20px");
    $("main").css("padding","0 15px");
    $("#article-toc-container").css({
        "display":"none",
        "left":"initial",
        "right":"0",
        "top":"55px"
    });
    $(".markdown-toc").css("width","245px");
    $(".article-category").css("margin","0 -15px 40px -40px");
}
function resetSidebarStatus(){
    cleanContentShadow();
    $(".sidebar").addClass("hidden-xs");
    $(".sidebar-toggle img").attr("src","/img/sidebar_toggle_open.png");
}
function addContentShadow(){
    $(".xs-header-container").css("filter","blur(3px)");
    $(".content").css("filter","blur(3px)");
    $(".shadow").show();
}
function cleanContentShadow(){
    $(".xs-header-container").css("filter","none");
    $(".content").css("filter","none");
    $(".shadow").hide();
}
