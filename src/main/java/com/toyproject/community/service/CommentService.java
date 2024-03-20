package com.toyproject.community.service;

import com.toyproject.community.domain.Comment;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.domain.dto.CommentDto;
import com.toyproject.community.domain.form.CommentForm;
import com.toyproject.community.repository.CommentRepository;
import com.toyproject.community.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
     * Group 번호를 root 댓글의 id와 동일하게 설정
     * @param commentDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void createComment(CommentDto commentDto){
        Comment comment = Comment.createComment(commentDto);
        Long availableGroupId = commentRepository.getAvailableGroupId(commentDto.getPost().getId());
        if(availableGroupId==null) availableGroupId = 0l;
        comment.initializeGroupId(availableGroupId+1);
        commentRepository.save(comment);
    }

    /**
     * 대댓글
     * @param parentCommentId 부모 댓글의 아이디
     * @param commentForm 댓글 form
     * @param member 댓글 작성자
     */
    @Transactional(rollbackFor = Exception.class)
    public void createSubComment(Long parentCommentId, CommentForm commentForm, Member member){
        // create Comment
        Post post = postRepository.findById(commentForm.getPostId()).orElseThrow(EntityNotFoundException::new);
        CommentDto commentDto = new CommentDto(commentForm.getCommentContent(), member, post);

        // 그룹, 시퀀스, Depth 계산
        // 1. 부모 comment 불러오기
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(EntityNotFoundException::new);

        // 2. 시퀀스(동일 그룹 내 순서) 계산하기
        List<Comment> sequencePair = commentRepository.getAvailableSequence(parentComment);
        Long sequence, childCount;
        if(sequencePair.size() == 0){
            sequence = parentComment.getSequence();
            childCount = 0l;
        }else {
            sequence = sequencePair.get(0).getSequence();
            childCount = sequencePair.get(0).getChildCount();
        }

        // 현재 추가될 자식 댓글이 도착할 시퀀스 값
        Long childSequence = sequence+childCount+1;
        commentRepository.increaseSequence(parentComment.getGroupId(), childSequence);

        Comment comment = Comment.createComment(commentDto);
        // group, Depth 정보는 이미 parent entity가 가지고 있음
        Comment.setParentComment(comment, parentComment, childSequence);
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
