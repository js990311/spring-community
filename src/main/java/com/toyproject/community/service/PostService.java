package com.toyproject.community.service;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.domain.dto.CreatePostDto;
import com.toyproject.community.domain.form.PostForm;
import com.toyproject.community.repository.BoardRepository;
import com.toyproject.community.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;

    public Long createPost(PostForm postForm, Member member){
        Board board = boardRepository.findById(postForm.getBoardId()).orElseThrow(
                ()->new EntityNotFoundException("board not found")
        );
        CreatePostDto createPostDto = new CreatePostDto(
                member,
                board,
                postForm.getTitle(),
                postForm.getContent()
        );
        return createPost(createPostDto);
    }

    @Transactional
    public Long createPost(CreatePostDto postDto){
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


    public List<Post> readPostByBoardName(String boardName){
        return postRepository.findByBoardName(boardName);
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
