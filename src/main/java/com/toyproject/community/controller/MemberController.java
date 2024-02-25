package com.toyproject.community.controller;

import com.toyproject.community.form.MemberForm;
import com.toyproject.community.repository.MemberRepository;
import com.toyproject.community.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Security;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final SecurityContextLogoutHandler logoutHandler;

    @GetMapping("/regist")
    public String getRegistMember(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "registMember";
    }

    @PostMapping("/regist")
    public String postRegistMember(@ModelAttribute MemberForm memberForm){
        memberService.registMember(memberForm.getEmail(), memberForm.getPassword());
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLogin(@RequestParam(name = "error", required = false) String error, Model model){
        model.addAttribute("memberForm", new MemberForm());
        model.addAttribute("exception", error);
        return "login";
    }

    @GetMapping("/logout")
    public String getLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        logoutHandler.logout(request, response, authentication);
        return "redirect:/";
    }

}
