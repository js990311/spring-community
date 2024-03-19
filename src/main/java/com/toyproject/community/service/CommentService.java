package com.toyproject.community.service;

import com.toyproject.community.domain.Comment;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.domain.dto.CommentDto;
import com.toyproject.community.domain.form.CommentForm;
import com.toyproject.community.domain.query.InsertCommentClosure;
import com.toyproject.community.repository.CommentRepository;
import com.toyproject.community.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.MapsId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    public void createComment(CommentForm commentForm, Member member){
        Post post = postRepository.findById(commentForm.getPostId()).orElseThrow(
                ()->new EntityNotFoundException("entity not found")
        );
        CommentDto commentDto = new CommentDto(
                commentForm.getCommentContent(),
                member,
                post
        );
        createComment(commentDto);
    }

    /**
     * 대댓글이 아닌 일반 댓글 작성
     * @param commentDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void createComment(CommentDto commentDto){
        Comment comment = Comment.createComment(commentDto);
        commentRepository.save(comment);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createSubComment(Long parentCommentId, CommentForm commentForm, Member member){
        // create Comment
        Post post = postRepository.findById(commentForm.getPostId()).orElseThrow(EntityNotFoundException::new);
        CommentDto commentDto = new CommentDto(commentForm.getCommentContent(), member, post);

        Comment comment = Comment.createComment(commentDto);
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(EntityNotFoundException::new);
        Comment.setParentComment(comment, parentComment);
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
