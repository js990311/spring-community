package com.toyproject.community.controller;

import com.toyproject.community.domain.Member;
import com.toyproject.community.form.CommentForm;
import com.toyproject.community.security.MemberAuthenticationToken;
import com.toyproject.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public String createComment(@ModelAttribute CommentForm commentForm, Authentication authentication){
        MemberAuthenticationToken memberInfo = null;
        if(authentication instanceof MemberAuthenticationToken){
            memberInfo = (MemberAuthenticationToken) authentication;
        }else{
            return "redirect:/member/login";
        }
        Member member = memberInfo.getMember();
        commentService.createComment(commentForm, member);
        return "redirect:/p/"+commentForm.getPostId();
    }
}
