package com.toyproject.community.service;

import com.toyproject.community.domain.Comment;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.dto.CommentDto;
import com.toyproject.community.form.CommentForm;
import com.toyproject.community.repository.CommentRepository;
import com.toyproject.community.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<Comment> readAllCommentByPost(Long postId){
        return commentRepository.findByPostId(postId);
    }

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
