package org.ito.blog.serviceImpl;

import org.ito.blog.dao.ArticleMapper;
import org.ito.blog.dao.ArticleTypeMapper;
import org.ito.blog.dao.CommentMapper;
import org.ito.blog.dto.ArticleDTO;
import org.ito.blog.dto.EditingArticleDTO;
import org.ito.blog.dto.LatestArticleDTO;
import org.ito.blog.entity.Article;
import org.ito.blog.entity.ArticleType;
import org.ito.blog.service.ArticleService;
import org.ito.blog.utils.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Integer PAGE_SIZE = 5;
    private static final Integer SEARCH_PAGE_SIZE = 8;
    private final ArticleTypeMapper articleTypeMapper;
    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;
    @Autowired
    public ArticleServiceImpl(ArticleTypeMapper articleTypeMapper, ArticleMapper articleMapper, CommentMapper commentMapper) {
        this.articleTypeMapper = articleTypeMapper;
        this.articleMapper = articleMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public List<ArticleType> getAllArticleTypes() {
        return articleTypeMapper.selectAllArticleType();
    }

    @Override
    public Integer getArticleCommentCount(Integer articleId) {
        return commentMapper.selectCommentCountByArticleId(articleId);
    }

    @Override
    public Boolean isCommentLeft(Integer articleId,Integer commentPage) {
        return commentMapper.selectCommentCountByArticleId(articleId) > commentPage * 5;
    }

    @Override
    public List<LatestArticleDTO> getLatestArticleList() {
       List<Article> articles = articleMapper.selectLatestArticles();
       return BeanMapper.mapList(articles,LatestArticleDTO.class);
    }

    @Override
    public Integer getArticleTypeIdByName(String name) {
        if(StringUtils.isEmpty(name))
            return null;
        else
            return articleTypeMapper.selectIdByName(name);
    }

    @Override
    public Integer getArticleTypeIdByArticleId(Integer articleId) {
        return articleTypeMapper.selectIdByArticleId(articleId);
    }

    @Override
    public List<ArticleDTO> searchArticle(String key, Integer page) {
        String trueKey = "";
        for(String singleKey:key.split("\\s+")){
            trueKey += "+"+singleKey+" ";
        }
        List<ArticleDTO> articleDTOs = BeanMapper.mapList(articleMapper.selectArticleBySearch(trueKey,SEARCH_PAGE_SIZE,getSearchOffset(page)),ArticleDTO.class);
        for(ArticleDTO articleDTO:articleDTOs){
            articleDTO.setCommentNumber(commentMapper.selectCommentCountByArticleId(articleDTO.getId()));
        }
        for(ArticleDTO articleDTO: articleDTOs){
            String content = articleDTO.getContent().trim();
            articleDTO.setContent(content.replaceAll("</?[a-zA-Z]+[^><]*>","")
                    .replaceAll("[\\*\\+\\>\\<#\\-\\=`]+"," ")
                    .replaceAll("br+"," ")
                    .replaceAll("&emsp;+","")
                    .replaceFirst("\\[TOCM\\]+",""));
        }
        return articleDTOs;
    }

    @Override
    public boolean addArticle(String title, Integer typeId, String markdown) {
       return articleMapper.insert(title,typeId,markdown) > 0;
    }
    @Override
    public boolean updateArticle(Integer id, Integer typeId, String title, String markdown) {
        return articleMapper.update(id,typeId,title,markdown) >0;
    }
    @Override
    public boolean deleteArticleById(Integer id) {
        return articleMapper.delete(id) > 0;
    }

    @Override
    public EditingArticleDTO getEditingArticleDTOById(Integer articleId) {
        Article temp = articleMapper.selectEditingArticleById(articleId);
        if(temp == null)
            return null;
        return BeanMapper.map(temp,EditingArticleDTO.class);
    }

    private Integer getSearchOffset(Integer page){
        return (page-1) * SEARCH_PAGE_SIZE;
    }


    /** 这里不把直接调用下面的ByPageAndType，是因为需要搜索所有分类的文章*/
    @Override
    public List<ArticleDTO> getArticleSummaryByPage(Integer page) {
        List<Article> articles = articleMapper.selectArticleSummaryByPage(PAGE_SIZE,getOffset(page));
        List<ArticleDTO> articleDTOs = BeanMapper.mapList(articles,ArticleDTO.class);
        for(ArticleDTO articleDTO:articleDTOs){
            articleDTO.setCommentNumber(commentMapper.selectCommentCountByArticleId(articleDTO.getId()));
        }
        return articleDTOs;
    }
    @Override
    public List<ArticleDTO> getArticleSummaryByPageAndType(Integer page, String typeName) {
        Integer typeId = articleTypeMapper.selectIdByName(typeName);
        List<Article> articles = articleMapper.selectArticleSummaryByPageAndTypeId(typeId,PAGE_SIZE,getOffset(page));
        List<ArticleDTO> articleDTOs = BeanMapper.mapList(articles,ArticleDTO.class);
        for(ArticleDTO articleDTO:articleDTOs){
            articleDTO.setCommentNumber(commentMapper.selectCommentCountByArticleId(articleDTO.getId()));
        }
        return articleDTOs;
    }

    @Override
    public ArticleDTO getArticleDTOById(Integer id) {
        ArticleDTO result = BeanMapper.map(articleMapper.selectById(id),ArticleDTO.class);
        result.setCommentNumber(getArticleCommentCount(id));
        return result;
    }

    /** 这里不直接调用下面的多参数重载方法，原因同上面文章查询*/
    public Boolean isArticleLeft(Integer currentPage){
        Integer articleCount = articleMapper.selectAllArticleCount();
        return articleCount > currentPage*5;
    }
    public Boolean isArticleLeft(Integer currentPage,String typeName){
        Integer typeId = articleTypeMapper.selectIdByName(typeName);
        Integer articleCount = articleMapper.selectArticleCountByTypeId(typeId);
        return articleCount > currentPage*5;
    }
    private Integer getOffset(Integer page){
        return (page-1) * PAGE_SIZE;
    }
}
