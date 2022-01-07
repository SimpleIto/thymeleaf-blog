package org.ito.blog.controller;

import org.ito.blog.dto.EditingArticleDTO;
import org.ito.blog.service.ArticleService;
import org.ito.blog.service.CommentService;
import org.ito.blog.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/blog/admin")
public class AdminController {

    private static String tempBackupMarkdown = "";

    private final ArticleService articleService;
    private final CommentService commentService;

    @Autowired
    public AdminController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @RequestMapping
    public ModelAndView adminMainPage(){ return new ModelAndView("admin/admin_main"); }
    @RequestMapping("admin_nav")
    public ModelAndView adminNav(){ return new ModelAndView("admin/admin_nav"); }
    @RequestMapping("article_mgt")
    public ModelAndView articleMgt(Model model){
        model.addAttribute("types",articleService.getAllArticleTypes());
        model.addAttribute("tempBackupMarkdown",tempBackupMarkdown);
        return new ModelAndView("admin/article_mgt");
    }
    @RequestMapping("comment_mgt")
    public ModelAndView commentMgt(Model model){
        return new ModelAndView("admin/comment_mgt");
    }

    @PostMapping("article/update")
    public JsonResult updateArticle(@RequestParam(value = "id",required = true) Integer id,
                                    @RequestParam(value = "typeId") Integer typeId,
                                    @RequestParam(value = "title") String title, @RequestParam("content")String markdown){
        if(id==null)
            return new JsonResult(0,"更新失败！未指明文章id");
        else{
            if (articleService.updateArticle(id,typeId,title,markdown))
                return new JsonResult(1,"更新成功");
            else
                return new JsonResult(0,"更新失败！写入时出错");
        }
    }
    @PostMapping("article/write")
    public JsonResult writeArticle(@RequestParam("title") String title,
                                   @RequestParam(value = "typeId") Integer typeId, @RequestParam("content") String markdown){
        if(articleService.addArticle(title,typeId,markdown))
            return new JsonResult(1,"发表成功");
        else
           return  new JsonResult(0,"发表失败");
    }

    @PostMapping("article/delete")
    public JsonResult deleteArticle(@RequestParam(value = "id")Integer id){
        if(articleService.deleteArticleById(id))
            return new JsonResult(1,"文章删除成功");
        return new JsonResult(0,"文章删除失败");
    }


    @PostMapping("comment/reply")
    public JsonResult replyCommentByHost(@RequestParam(value = "replyId")Integer replyId, @RequestParam("content") String content){
        if(commentService.replyCommentByHost(replyId,content))
            return new JsonResult(1,"评论回复成功");
        return new JsonResult(0,"评论回复失败！写入出错");
    }

    @PostMapping("comment/write")
    public JsonResult writeNewHostComment(@RequestParam("articleId") Integer articleId,@RequestParam("content") String content){
        if(commentService.addCommentByHost(articleId,content)!=null)
            return new JsonResult(1,"发表评论成功");
        return new JsonResult(0,"发表评论失败！写入出错");
    }

    @PostMapping("comment/delete")
    public JsonResult delete(@RequestParam("id") Integer id){
        if(commentService.deleteComment(id))
            return new JsonResult(1,"评论删除成功");
        return new JsonResult(0,"评论删除失败");
    }

    //此处就显示出来了DTO以及需要一个统一的JsonResult(内容字段需为泛型，而不是上面那些JsonResult)的作用了
    //TODO：懒，以后有机会再重构，将返回值统一起来
    /**
     * 返回根据id查询到的编辑文章时所需的一些字段，未查到返回null
     * @param id
     * @return
     */
    @GetMapping("article/getContent")
    public EditingArticleDTO getMarkdownByArticleId(@RequestParam("id") Integer id){
        if(id==null)
            return null;
        return articleService.getEditingArticleDTOById(id);
    }

    @PostMapping("article/backupEditor")
    public JsonResult backupEditor(@RequestParam("test-editormd-markdown-doc") String content){
        tempBackupMarkdown = content;
        return new JsonResult(1,"已自动保存");
    }
}
