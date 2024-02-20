package com.toyproject.community.service;

import com.toyproject.community.domain.Comment;
import com.toyproject.community.dto.CommentDto;
import com.toyproject.community.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> readAllCommentByPost(Long postId){
        return commentRepository.findByPostId(postId);
    }

    @Transactional
    public void createComment(CommentDto commentDto){
        Comment comment = Comment.createComment(commentDto);
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long commentId, String content){
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.updateComment(content);
    }

    @Transactional
    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }

}
