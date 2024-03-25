package com.toyproject.community.controller;

import com.toyproject.community.dto.form.CommentForm;
import com.toyproject.community.security.authorization.annotation.IsUser;
import com.toyproject.community.security.authorization.annotation.comment.CommentDeleteAuthorize;
import com.toyproject.community.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @IsUser
    @PostMapping("/create")
    public String createComment(@ModelAttribute CommentForm commentForm, Authentication authentication){
        String username = authentication.getName();
        commentService.createComment(commentForm, username);
        return "redirect:/p/"+commentForm.getPostId();
    }

    @IsUser
    @PostMapping("/{commentId}/create")
    public String createSubComment(@PathVariable Long commentId, @ModelAttribute CommentForm commentForm, Authentication authentication){
        String username = authentication.getName();
        commentService.createSubComment(commentId, commentForm, username);
        return "redirect:/p/"+commentForm.getPostId();
    }


    @CommentDeleteAuthorize
    @GetMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, HttpServletRequest request){
        commentService.deleteComment(commentId);

        String referer = request.getHeader("referer");

        if(referer == null){
            return "redirect:/";
        }else{
            return "redirect:" + request.getHeader("referer");
        }
    }
}
