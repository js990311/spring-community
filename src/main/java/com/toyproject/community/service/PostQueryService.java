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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @Transactional(readOnly = true)
    public List<ReadCommentDto> readAllCommentByPost(Long postId){
        return commentRepository.findByPostId(postId).stream().map(ReadCommentDto::new).toList();
    }
}
