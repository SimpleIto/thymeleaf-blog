var isLoadingComment = false;
var currentCommentPage = 1;
var isReplyOpening = [];
var isSendingComment = false;

function getCurrentArticleId(){
    var temp = $(".article-content").attr("id");
    return temp.substr(temp.indexOf("-")+1);
}

function loadMoreComment() {
    if(!isLoadingComment) {
        isLoadingComment = true;
        $.ajax({
            url: window.location.pathname + "/more_comments",
            dataType: "html",
            async: true,
            data:{"currentCommentPage":currentCommentPage},
            type:"GET",
            success:function (res) {
                var $newNode = $($.parseHTML(res))
                $newNode.find(".comment-control-reply").click(function () {
                    clickReplyEvent($(this));
                })
                $(".comments-content-container").append($newNode);
                currentCommentPage += 1;
                isLoadingComment = false;
                loadIsMoreCommentLeft();
            },
            error:function () {
                isLoadingComment = false;
            }
        })
    }
}
function loadIsMoreCommentLeft(){
    $.ajax({
        url: window.location.pathname + "/is_comment_left",
        dataType: "text",
        async: true,
        data:{"currentCommentPage":currentCommentPage},
        type:"GET",
        success:function (res) {
            if(res=="false")
                $(".load-more-comments").hide();
        },
    })
}
function clickReplyEvent(clickedjQueryNode) {
    var replyTo = clickedjQueryNode.attr("id");
    if(isReplyOpening[replyTo]==null){
        var $writeDom = $($.parseHTML($(".write-comment").prop("outerHTML"))[0]);
        $writeDom.find(".write-comment-reply-id").attr("value", replyTo);
        clickedjQueryNode.parentsUntil(".comments-content-container").find(".comment-right").append($writeDom);
        clickedjQueryNode.parents(".comment-right").find(".comment-submit-button").click(function () {
            submitComment($(this).parents(".write-comment"));
        })
        isReplyOpening[replyTo] = true;
    }
    else if(isReplyOpening[replyTo]==true){
        clickedjQueryNode.parentsUntil(".comment").find(".write-comment").hide();
        isReplyOpening[replyTo] = false;
    }
    else{
        clickedjQueryNode.parentsUntil(".comment").find(".write-comment").show();
        isReplyOpening[replyTo] = true;
    }
}

$(".comment-control-reply").click(function() {
    clickReplyEvent( $(this) );
});

function refresh() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/blog/util/code', true);
    xhr.responseType = "blob";
    xhr.setRequestHeader("client_type", "DESKTOP_WEB");
    xhr.onload = function() {
        if (this.status == 200) {
            var blob = this.response;
            // img.onload = function(e) {
            //     window.URL.revokeObjectURL(img.src);
            // };
            var imgSrc = window.URL.createObjectURL(blob);
            $(".comment-code").attr("src",imgSrc);
        }
    }
    xhr.send();
}
refresh();

$(".comment-submit-button").click(function () {
    submitComment($(this).parents(".write-comment"));
})

function submitComment(formNode){
    var emptyInput = [];
    var length = 0; //空input节点数
    formNode.find(".required-field").each(function(){
        if($(this).val()==null||$(this).val()=='')
            emptyInput[length++] = $(this);
    })
    if(length>0){
        for(var i=0;i<length;i++){
            emptyInput[i].animate({borderColor: '#f00',borderWidth:'1.5px'},250).animate({borderColor: '#ccc'},250
            ).animate({borderColor: '#f00'},250).animate({borderColor: '#ccc',borderWidth:'1px'},250);
        }
        return;
    }
    if(isSendingComment)
        return;
    isSendingComment = true;
    $.ajax({
        url: window.location.pathname + "/writeComment",
        dataType: "text",
        async: true,
        data: formNode.serialize(),
        type:"POST",
        success:function (data) {
            if(data=='false') {
                formNode.find(".comment-code-failed").fadeIn().delay(1500).fadeOut(500);
            }
            else {
                location.reload(true);
            }
            isSendingComment = false;
            refresh();
        },
        error:function () {
            isSendingComment = false;
            alert("网络异常！请稍后重试");
            refresh();
        }
    })
}
function changeWritingNotify(jsNode){
    var $valueNode = $(jsNode).parent().find(".writing-notify-value");
    var current = $valueNode.val();
    if(current=='false'){
        $(jsNode).attr("src","/img/notify.png");
        $(jsNode).css({
            backgroundColor: '#F6FFFD',
            borderColor:'#b5e5db'
        });
        $valueNode.val('true');
    }else{
        $(jsNode).attr("src","/img/notify-x.png");
        $(jsNode).css({
            backgroundColor: '#F9F9F9',
            borderColor:'#ccc'
        });
        $valueNode.val('false');
    }
}
//为什么开头会产生“不必要”的<br>呢？因为我想实现"文章是否显示目录是根据我是否书写某个字段来决定的"这个需求，这又涉及到了editor.md的目录解析逻辑
//editor.md 若想解析到目录到自定义容器，则需要开启toc:true 且使用tocContainer字段，//而只要指定了tocContainer，无论是否书写[TOC]，都会解析目录。（且是否书写,在解析后的文章中都不会留下痕迹）
//而若在指定了tocContainer的文章中，开启tocm:true且又书写了[TOCM]的话，虽然目录被解析到了指定容器，[tocm]也是无效的，但直接留下一行<br>
//因此可根据是否有这行<br>来判断文章中是否使用了[TOCM]，从而来判断目录的显示与否（更新：改成是否直接删除目录，因为显示与否配合大小屏切换容易出问题）
var $emptyLine = $(".article-content>br:first-child");
if($emptyLine.length==0){
    $("#article-toc-container").remove();
    $(".article-toc-toggle").remove();
}
else{
    $emptyLine.remove();
}
$(".article-toc-toggle").click(function () {
    $("#article-toc-container").toggle();
})