package com.toyproject.community.service;

import com.toyproject.community.domain.Comment;
import com.toyproject.community.domain.Post;
import com.toyproject.community.domain.form.CommentForm;
import com.toyproject.community.domain.view.ReadCommentDto;
import com.toyproject.community.domain.view.ReadPostDto;
import com.toyproject.community.repository.CommentRepository;
import com.toyproject.community.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Cookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostQueryService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ReadPostDto readPostById(Long postId, boolean isRecentPost){
        /* 보여줄 post 가져오기 */
        Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);

        /* increase View가 필요한 경우 조치*/
        if(!isRecentPost){
            post.increasePostViewCount();
        }

        /* DTO에 넣기 */
        ReadPostDto readPostDto = new ReadPostDto(post);
        return readPostDto;
    }

    public List<ReadCommentDto> readAllCommentByPost(Long postId){
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<ReadCommentDto> readCommentDto = comments.stream().map(ReadCommentDto::new).collect(Collectors.toList());
        return readCommentDto;
    }
}
