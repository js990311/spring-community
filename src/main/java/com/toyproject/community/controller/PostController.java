package com.toyproject.community.controller;

import com.toyproject.community.controller.util.RecentPostCookie;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.dto.response.ResponseCommentDto;
import com.toyproject.community.dto.response.ResponsePostDto;
import com.toyproject.community.dto.form.CommentForm;
import com.toyproject.community.dto.form.PostForm;
import com.toyproject.community.dto.form.UpdatePostForm;
import com.toyproject.community.security.authentication.MemberAuthenticationToken;
import com.toyproject.community.security.authorization.annotation.IsUser;
import com.toyproject.community.security.authorization.annotation.PostUpdateAuthorize;
import com.toyproject.community.service.CommentService;
import com.toyproject.community.service.PostQueryService;
import com.toyproject.community.service.PostService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/p")
@Slf4j
public class PostController {

    private final PostService postService;
    private final PostQueryService postQueryService;
    private final CommentService commentService;

    @IsUser
    @GetMapping("/create")
    public String createPostView(@RequestParam("boardid") Long boardid, Model model){
        PostForm postForm = new PostForm();
        postForm.setBoardId(boardid);
        model.addAttribute("postForm", postForm);
        return "createPost";
    }

    @IsUser
    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute PostForm postForm, BindingResult bindingResult, Authentication authentication, Model model){
        if(bindingResult.hasErrors()){
            log.debug("errors={}",bindingResult);
            return "createPost";
        }
        MemberAuthenticationToken memberInfo = (MemberAuthenticationToken) authentication;
        Member member = memberInfo.getMember();
        postService.createPost(postForm, member);
        return "redirect:/b/" + postForm.getBoardId();
    }

    @GetMapping("/{postID}")
    public String readPost(@PathVariable("postID") Long postId, Model model,
                           HttpServletRequest request, HttpServletResponse response
    ){
        // 조회수 중복 체크를 위한 cookie
        RecentPostCookie recentPostCookie = new RecentPostCookie(request);
        boolean isRecentPost = recentPostCookie.isRecentPost(postId);

        // Post
        ResponsePostDto responsePostDto = postQueryService.readPostById(postId, isRecentPost);
        model.addAttribute("post", responsePostDto);

        // Comment
        List<ResponseCommentDto> responseCommentDto = postQueryService.readAllCommentByPost(postId);
        model.addAttribute("comments", responseCommentDto);

        // comment Form
        CommentForm commentForm = new CommentForm();
        commentForm.setPostId(postId);
        model.addAttribute("commentForm", commentForm);

        // 조회수 중복 체크를 위한 cookie 설정
        Cookie cookie = recentPostCookie.addPostInRecentPost(postId);
        response.addCookie(cookie);

        return "post";
    }

    @PreAuthorize("@post_authz.delete(#postId, authentication)")
    @GetMapping("/{postId}/delete")
    public String deletePost(@P("postId") @PathVariable("postId") Long postId){
        postService.deletePost(postId);
        return "redirect:/";
    }

    @PostUpdateAuthorize
    @GetMapping("/{postID}/update")
    public String updatePostView(
            @P("postId") @PathVariable("postID") Long postId, Model model){
        Post post = postService.readPostById(postId);
        UpdatePostForm postForm = new UpdatePostForm(post);
        model.addAttribute("postForm", postForm);
        return "updatePost";
    }

    @PostUpdateAuthorize
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
