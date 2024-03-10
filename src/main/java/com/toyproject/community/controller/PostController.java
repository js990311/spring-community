package com.toyproject.community.controller;

import com.toyproject.community.domain.Comment;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.domain.view.ReadCommentDto;
import com.toyproject.community.domain.view.ReadPostDto;
import com.toyproject.community.domain.form.CommentForm;
import com.toyproject.community.domain.form.PostForm;
import com.toyproject.community.domain.form.UpdatePostForm;
import com.toyproject.community.security.authentication.MemberAuthenticationToken;
import com.toyproject.community.service.CommentService;
import com.toyproject.community.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/p")
@Slf4j
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/create")
    public String createPostView(@RequestParam("boardid") Long boardid, Model model){
        PostForm postForm = new PostForm();
        postForm.setBoardId(boardid);
        model.addAttribute("postForm", postForm);
        return "createPost";
    }

    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute PostForm postForm, BindingResult bindingResult, Authentication authentication, Model model){
        if(bindingResult.hasErrors()){
            log.debug("errors={}",bindingResult);
            return "createPost";
        }
        MemberAuthenticationToken memberInfo = (MemberAuthenticationToken) authentication;
        Member member = memberInfo.getMember();
        postService.createPost(postForm, member);
        StringBuilder sb = new StringBuilder();
        return "redirect:/b/" + postForm.getBoardId();
    }

    @GetMapping("/{postID}")
    public String readPost(@PathVariable("postID") Long postId, Model model){
        Post post;
        try{
            post = postService.readPostById(postId);
        }catch (Exception e){
            // TODO error 페이지
            return "redirect:/";
        }

        ReadPostDto readPostDto = new ReadPostDto(post);
        model.addAttribute("post",readPostDto);

        List<Comment> comments = commentService.readAllCommentByPost(postId);
        List<ReadCommentDto> readCommentDto = comments.stream().map(ReadCommentDto::new).collect(Collectors.toList());
        model.addAttribute("comments", readCommentDto);

        CommentForm commentForm = new CommentForm();
        commentForm.setPostId(postId);
        model.addAttribute("commentForm", commentForm);

        return "post";
    }

    @GetMapping("/{postID}/delete")
    public String deletePost(@PathVariable("postID") Long postId){
        postService.deletePost(postId);
        return "redirect:/";
    }

    @GetMapping("/{postID}/update")
    public String updatePostView(@PathVariable("postID") Long postId, Model model, Authentication authentication){
        MemberAuthenticationToken memberInfo = (MemberAuthenticationToken) authentication;
        Member member = memberInfo.getMember();
        Post post = postService.readPostById(postId);
        UpdatePostForm postForm = new UpdatePostForm(post);
        model.addAttribute("postForm", postForm);
        return "updatePost";
    }

    @PutMapping("/{postID}/update")
    public String updatePostView(@PathVariable("postID") Long postId, @Valid @ModelAttribute(name = "postForm") UpdatePostForm updatePostForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.debug("errors={}", bindingResult);
            return "updatePost";
        }
        postService.updatePost(updatePostForm.getId(), updatePostForm.getTitle(), updatePostForm.getContent());
        return "redirect:/p/" + postId;
    }
}
