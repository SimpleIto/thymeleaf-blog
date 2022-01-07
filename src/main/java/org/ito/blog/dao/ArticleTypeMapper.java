package org.ito.blog.dao;

import org.apache.ibatis.annotations.Param;
import org.ito.blog.entity.ArticleType;

import java.util.List;
public interface ArticleTypeMapper {
    List<ArticleType> selectAllArticleType();

    Integer selectIdByName(String name);

    Integer selectIdByArticleId(@Param("id") Integer articleId);
}
