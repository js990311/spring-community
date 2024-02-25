package com.toyproject.community.service;

import com.toyproject.community.domain.Comment;
import com.toyproject.community.domain.Post;
import com.toyproject.community.dto.PostDto;
import com.toyproject.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Long createPost(PostDto postDto){
        Post post = Post.createPost(postDto);
        postRepository.save(post);
        return post.getId();
    }

    public List<Post> readAllPost(){
        List<Post> postList = postRepository.findAll();
        return postList;
    }

    public Post readPostById(Long postId){
        Post post = postRepository.findById(postId).orElseThrow();
        return post;
    }

    public List<Post> readPostByBoardName(String postName){
        return postRepository.findAll();
    }

    public List<Post> readPostByBoardId(Long boardId){
        return postRepository.findByBoardId(boardId);
    }

    @Transactional
    public void updatePost(Long postId, String title, String content){
        Post post = postRepository.findById(postId).orElseThrow();
        post.updatePost(title,content);
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId){
        postRepository.deleteById(postId);
    }

}
