package org.ito.blog.controller;

import org.ito.blog.dto.ArticleDTO;
import org.ito.blog.entity.Comment;
import org.ito.blog.service.ArticleService;
import org.ito.blog.service.CommentService;
import org.ito.blog.utils.CaptchaUtil;
import org.ito.blog.utils.FileUtils;
import org.ito.blog.utils.MD5Util;
import org.ito.blog.utils.UploadImgJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import java.util.List;

@Controller
@RequestMapping("/blog")
public class GeneralController {

    private final ArticleService articleService;
    private final CommentService commentService;
    @Autowired
    public GeneralController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @ModelAttribute
    public void addBaseData(Model model){
        model.addAttribute("articleTypes",articleService.getAllArticleTypes());
        model.addAttribute("latestArticles",articleService.getLatestArticleList());
        model.addAttribute("latestComments",commentService.getLatestComments());
    }

    @RequestMapping
    public String showHomePage(@RequestParam(name = "category",required = false) String category,
                               @RequestParam(name = "page",required = true,defaultValue = "1") Integer page, Model model){
        model.addAttribute("category",category);
        model.addAttribute("categoryId",articleService.getArticleTypeIdByName(category));
        model.addAttribute("page",page);
        List<ArticleDTO> articles;
        Boolean isArticleLeft;
        if(category==null) {
            articles = articleService.getArticleSummaryByPage(page);
            isArticleLeft = articleService.isArticleLeft(page);
        }
        else {
            articles = articleService.getArticleSummaryByPageAndType(page, category);
            isArticleLeft = articleService.isArticleLeft(page,category);
        }
        model.addAttribute("articles",articles);
        model.addAttribute("isArticleLeft",isArticleLeft);

        return "index";
    }

    @RequestMapping("search")
    public String searchArticle(@RequestParam(name="key",defaultValue = "") String key, @RequestParam(value = "page",defaultValue = "1") Integer page, Model model){
        List<ArticleDTO> articleDTOs = articleService.searchArticle(key,page);
        model.addAttribute("key",key);
        model.addAttribute("page",page);
        model.addAttribute("articles",articleDTOs);
        return "normal/search";
    }
    @ExceptionHandler
    @GetMapping("article/{id}")
    public String showArticle(@PathVariable("id") Integer articleId, Model model){
        ArticleDTO articleDTO  = articleService.getArticleDTOById(articleId);
        model.addAttribute("categoryId",articleService.getArticleTypeIdByArticleId(articleId));
        model.addAttribute("article",articleDTO);
        model.addAttribute("comments",commentService.getInitCommentDTOByArticleId(articleId));
        model.addAttribute("isCommentLeft",articleService.getArticleCommentCount(articleId)>5);
        return "normal/article_detail";
    }

    //TODO:特别注意，属性中以is开头的，自动生成getter setter时记得手动添加上is！！！！！！！！！！！！！！！

    @ResponseBody
    @PostMapping("article/{id}/writeComment")
    public String writeComment (@PathVariable("id") Integer articleId, Comment comment,
                               @RequestParam("commentCode") String inputCaptcha, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String captcha = (String) request.getSession().getAttribute("commentCaptcha");
        if(!StringUtils.isEmpty(captcha)&&!StringUtils.isEmpty(inputCaptcha)&&captcha.equals(inputCaptcha)){
            comment.setArticleId(articleId);
            comment.setId(null);
            Integer newCommentId = commentService.addComment(comment);
            return "/blog/article/"+articleId+"#comment-"+newCommentId.toString();
        }
        else
            return "false";
    }

    @GetMapping("article/{id}/more_comments")
    public String getMoreComments(@PathVariable("id") Integer articleId,@RequestParam(name = "currentCommentPage",required = true) Integer currentPage, Model model){
        model.addAttribute("comments",commentService.getCommentDTOByArticleIdAndPage(articleId,currentPage+1));
        return "template/more_comments";
    }

    @ResponseBody
    @GetMapping("article/{id}/is_comment_left")
    public String isCommentLeft(@PathVariable("id") Integer articleId,@RequestParam(name = "currentCommentPage",required = true) Integer commentPage){
        return articleService.isCommentLeft(articleId,commentPage).toString();
    }

    @RequestMapping("util/code")
    public void getCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
        CaptchaUtil util = CaptchaUtil.Instance();
        // 将验证码输入到session中，用来验证
        String code = util.getString();
        request.getSession().setAttribute("commentCaptcha", code);
        // 输出打web页面
        ImageIO.write(util.getImage(),"jpg",response.getOutputStream());
    }

    @ResponseBody
    @PostMapping("util/uploadImg")
    public UploadImgJsonResult uploadImage(@RequestParam("editormd-image-file")MultipartFile file,
                              @Value("${blog.upload-path}") String basePath, @Value("${blog.upload-url}") String baseUrl) throws IOException {
        UploadImgJsonResult result;
        if(file.getContentType().startsWith("image")) {
            if (file.getSize() <= 10485760) {
                String imgPath = basePath+"img/";   //图片文件路径，基础路径加上img/
                String imgUrl = baseUrl.substring(0,baseUrl.length()-2)+"img/";     //跳过后两个**字符，获取图片url路径
                String originalName = file.getOriginalFilename();
                byte[] fileData = file.getBytes();
                String fileType = originalName.substring(originalName.lastIndexOf("."),originalName.length());
                File saveFile = FileUtils.CreateAndSaveFile(imgPath+MD5Util.getMD5(fileData)+fileType,fileData);
                result = new UploadImgJsonResult(1,"上传成功",imgUrl+saveFile.getName());
                return result;
            }
        }
        result = new UploadImgJsonResult();
        result.setSuccess(0);
        result.setMessage("上传失败，文件类型不符或超过15M");
        return result;
    }
}
