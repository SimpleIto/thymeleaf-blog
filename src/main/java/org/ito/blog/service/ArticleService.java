package org.ito.blog.service;

import org.ito.blog.dto.ArticleDTO;
import org.ito.blog.dto.EditingArticleDTO;
import org.ito.blog.dto.LatestArticleDTO;
import org.ito.blog.entity.ArticleType;

import java.util.List;

public interface ArticleService {
    List<ArticleType> getAllArticleTypes();

    List<ArticleDTO> getArticleSummaryByPage(Integer page);

    ArticleDTO getArticleDTOById(Integer id);

    List<ArticleDTO> getArticleSummaryByPageAndType(Integer page, String category);

    Boolean isArticleLeft(Integer currentPage);
    Boolean isArticleLeft(Integer currentPage,String typeName);

    Integer getArticleCommentCount(Integer articleId);
    Boolean isCommentLeft(Integer articleId,Integer commentPage);

    List<LatestArticleDTO> getLatestArticleList();

    Integer getArticleTypeIdByName(String name);
    Integer getArticleTypeIdByArticleId(Integer articleId);

    List<ArticleDTO> searchArticle(String key, Integer page);

    boolean addArticle(String title, Integer typeId, String markdown);

    boolean updateArticle(Integer id, Integer typeId, String title, String markdown);

    boolean deleteArticleById(Integer id);

    EditingArticleDTO getEditingArticleDTOById(Integer articleId);
}
