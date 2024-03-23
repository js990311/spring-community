package com.toyproject.community.service;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.dto.CreatePostDto;
import com.toyproject.community.dto.form.PostForm;
import com.toyproject.community.dto.response.ResponsePostDto;
import com.toyproject.community.repository.BoardRepository;
import com.toyproject.community.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
        return postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void increateViewCount(Post post){
        post.increasePostViewCount();
    }


    public List<Post> readPostByBoardName(String boardName){
        return postRepository.findByBoardName(boardName);
    }

    public Page<ResponsePostDto> readPostByBoardId(Long boardId){
        return readPostByBoardId(boardId,0);
    }

    public Page<ResponsePostDto> readPostByBoardId(Long boardId, int page){
        PageRequest pageRequest = PageRequest.of(
                page,
                15,
                Sort.by(
                        Sort.Direction.DESC,
                        "creationDateTime"
                )
        );
        Page<Post> posts = postRepository.findPageByBoardId(boardId, pageRequest);
        Page<ResponsePostDto> readPostDtos = posts.map(ResponsePostDto::new);
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
