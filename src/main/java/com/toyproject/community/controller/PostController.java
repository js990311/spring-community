package com.toyproject.community.controller;

import com.toyproject.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/p")
public class PostController {

    @GetMapping("/create")
    public String createPostView(Model model){
        return "redirect:/";
    }
}
