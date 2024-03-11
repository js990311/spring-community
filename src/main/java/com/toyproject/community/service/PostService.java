package com.toyproject.community.service;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.domain.dto.CreatePostDto;
import com.toyproject.community.domain.form.PostForm;
import com.toyproject.community.domain.view.ReadPostDto;
import com.toyproject.community.repository.BoardRepository;
import com.toyproject.community.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
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

    public Page<ReadPostDto> readPostByBoardId(Long boardId){
        return readPostByBoardId(boardId,0);
    }

    public Page<ReadPostDto> readPostByBoardId(Long boardId, int page){
        PageRequest pageRequest = PageRequest.of(
                page,
                15,
                Sort.by(
                        Sort.Direction.DESC,
                        "creationDateTime"
                )
        );
        Page<Post> posts = postRepository.findPageByBoardId(boardId, pageRequest);
        Page<ReadPostDto> readPostDtos = posts.map(ReadPostDto::new);
        return readPostDtos;
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
