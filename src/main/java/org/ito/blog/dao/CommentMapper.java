package org.ito.blog.dao;

import org.apache.ibatis.annotations.Param;
import org.ito.blog.entity.Comment;

import java.util.List;

public interface CommentMapper {

    Integer insert(Comment comment);

    Integer insertReply(Comment comment);

    List<Comment> selectCommentsByArticleIdAndPage(@Param("articleId") Integer articleId, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    Integer selectCommentCountByArticleId(Integer articleId);

    String selectAuthorNameById(Integer id);

    List<Comment> selectLatestComments();

    int delete(Integer id);

    int insertHostComment(Comment comment);

    int insertHostReply(Comment comment);

    Integer selectArticleIdById(Integer id);
}
