package com.toyproject.community.controller;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.dto.ReadPostDto;
import com.toyproject.community.form.CommentForm;
import com.toyproject.community.form.PostForm;
import com.toyproject.community.security.MemberAuthenticationToken;
import com.toyproject.community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/p")
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping("/create")
    public String createPostView(@RequestParam("boardid") Long boardid, Model model){
        PostForm postForm = new PostForm();
        postForm.setBoardId(boardid);
        model.addAttribute("postForm", postForm);
        return "createPost";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute PostForm postForm, Authentication authentication, Model model){
        log.info("PostController-createPost");
        MemberAuthenticationToken memberInfo = null;
        if(authentication instanceof MemberAuthenticationToken){
            memberInfo = (MemberAuthenticationToken) authentication;
        }else{
            return "redirect:/member/login";
        }
        Member member = memberInfo.getMember();
        postService.createPost(postForm, member);
        StringBuilder sb = new StringBuilder();
        return "redirect:/b/" + postForm.getBoardId();
    }

    @GetMapping("/{postID}")
    public String boardName(@PathVariable("postID") Long postId, Model model){
        Post post;
        try{
            post = postService.readPostById(postId);
        }catch (Exception e){
            // TODO error 페이지
            return "redirect:/";
        }

        ReadPostDto readPostDto = new ReadPostDto(post);
        model.addAttribute("post",readPostDto);

        CommentForm commentForm = new CommentForm();
        commentForm.setPostId(postId);
        model.addAttribute("commentForm", commentForm);

        return "post";
    }
}