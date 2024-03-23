package com.toyproject.community.service;

import com.toyproject.community.domain.Post;
import com.toyproject.community.dto.response.ResponseCommentDto;
import com.toyproject.community.dto.response.ResponsePostDto;
import com.toyproject.community.repository.CommentRepository;
import com.toyproject.community.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostQueryService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ResponsePostDto readPostById(Long postId, boolean isRecentPost){
        /* 보여줄 post 가져오기 */
        Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);

        /* increase View가 필요한 경우 조치*/
        if(!isRecentPost){
            post.increasePostViewCount();
        }

        /* DTO에 넣기 */
        ResponsePostDto responsePostDto = new ResponsePostDto(post);
        return responsePostDto;
    }

    @Transactional(readOnly = true)
    public List<ResponseCommentDto> readAllCommentByPost(Long postId){
        return commentRepository.findByPostId(postId).stream().map(ResponseCommentDto::new).toList();
    }
}
