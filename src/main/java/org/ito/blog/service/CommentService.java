package org.ito.blog.service;

import org.ito.blog.dto.CommentDTO;
import org.ito.blog.dto.LatestCommentDTO;
import org.ito.blog.entity.Comment;

import java.util.List;

public interface CommentService {

    Integer addComment(Comment comment);

    List<CommentDTO> getInitCommentDTOByArticleId(Integer articleId);

    List<CommentDTO> getCommentDTOByArticleIdAndPage(Integer articleId, Integer commentPage);

    String getAuthorNameById(Integer id);

    List<LatestCommentDTO> getLatestComments();

    boolean replyCommentByHost(Integer replyId, String content);

    Integer addCommentByHost(Integer articleId, String content);

    boolean deleteComment(Integer id);
}
