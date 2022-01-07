package org.ito.blog.serviceImpl;

import org.ito.blog.dao.CommentMapper;
import org.ito.blog.dto.CommentDTO;
import org.ito.blog.dto.LatestCommentDTO;
import org.ito.blog.entity.Comment;
import org.ito.blog.service.CommentService;
import org.ito.blog.utils.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private static final Integer PAGE_SIZE = 5;

    private final CommentMapper commentMapper;
    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    /**
     *
     * @param comment
     * @return 新插入评论的id
     */
    @Override
    public Integer addComment(Comment comment) {
        if(comment.getReplyTo()==0)
            commentMapper.insert(comment);
        else{
            comment.setIsReply(true);
            commentMapper.insertReply(comment);
        }
        return comment.getId();
    }

    @Override
    public List<CommentDTO> getInitCommentDTOByArticleId(Integer articleId) {
        return getCommentDTOByArticleIdAndPage(articleId,1);
    }

    @Override
    public List<CommentDTO> getCommentDTOByArticleIdAndPage(Integer articleId, Integer commentPage) {
        List<Comment> list = commentMapper.selectCommentsByArticleIdAndPage(articleId,PAGE_SIZE,getOffset(commentPage));
        List<CommentDTO> dtoList = BeanMapper.mapList(commentMapper.selectCommentsByArticleIdAndPage(articleId,PAGE_SIZE,getOffset(commentPage)),CommentDTO.class);
        for(int i=0;i<dtoList.size();i++){
            dtoList.get(i).setRepliedName(commentMapper.selectAuthorNameById(list.get(i).getReplyTo()));
        }
        return dtoList;
    }

    @Override
    public String getAuthorNameById(Integer id) {
        return commentMapper.selectAuthorNameById(id);
    }

    /**
     * 此处给Comment添加文章标题字段，然后在sql中直接多表查询
     * @return
     */
    @Override
    public List<LatestCommentDTO> getLatestComments() {
        return BeanMapper.mapList(commentMapper.selectLatestComments(),LatestCommentDTO.class);
    }

    @Override
    public boolean replyCommentByHost(Integer replyId, String content) {
        Comment comment = getBaseHostComment();
        comment.setArticleId(commentMapper.selectArticleIdById(replyId));
        comment.setIsReply(true);
        comment.setReplyTo(replyId);
        comment.setContent(content);
        return commentMapper.insertHostReply(comment) > 0;
    }

    @Override
    public Integer addCommentByHost(Integer articleId, String content) {
        Comment comment = getBaseHostComment();
        comment.setArticleId(articleId);
        comment.setContent(content);
        commentMapper.insertHostComment(comment);
        return comment.getId();
    }

    @Override
    public boolean deleteComment(Integer id) {
        return commentMapper.delete(id) > 0;
    }

    private Comment getBaseHostComment(){
        Comment comment = new Comment();
        comment.setIsHost(true);
        comment.setIsNotified(true);
        comment.setAuthorEmail("401092068@qq.com");
        comment.setAuthorName("伊东");
        return comment;
    }
    private Integer getOffset(Integer page){
        return (page-1) * PAGE_SIZE;
    }
}
