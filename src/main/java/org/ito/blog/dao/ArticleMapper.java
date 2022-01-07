package org.ito.blog.dao;

import org.apache.ibatis.annotations.Param;
import org.ito.blog.entity.Article;

import java.util.List;

/** 此注解也可不加，但使用处idea将提示无法自动装配（其实@MapperScan已经将所有mapperj接口注册为bean，已经由Spring容器管理）*/
//@Repository

public interface ArticleMapper {

    List<Article> selectArticleSummaryByPage( @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    Article selectById(Integer id);

    List<Article> selectArticleSummaryByPageAndTypeId(@Param("typeId") Integer typeId, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    Integer selectAllArticleCount();

    Integer selectArticleCountByTypeId(Integer typeId);

    List<Article> selectLatestArticles();

    List<Article> selectArticleBySearch(@Param("key")String key, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    int insert(@Param("title") String title,@Param("typeId") Integer typeId, @Param("content") String content);

    int update(@Param("id") Integer id,@Param("typeId") Integer typeId, @Param("title") String title, @Param("content") String content);

    int delete(Integer id);

    Article selectEditingArticleById(Integer id);
}
