package com.toyproject.community.controller;

import com.toyproject.community.domain.dto.MemberDto;
import com.toyproject.community.domain.form.LoginMemberForm;
import com.toyproject.community.domain.form.RegistMemberForm;
import com.toyproject.community.domain.view.PageNumberInfo;
import com.toyproject.community.domain.view.ReadPostDto;
import com.toyproject.community.exception.EntityDuplicateException;
import com.toyproject.community.security.authentication.MemberAuthenticationToken;
import com.toyproject.community.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final SecurityContextLogoutHandler logoutHandler;

    @GetMapping("/regist")
    public String getRegistMember(Model model){
        model.addAttribute("memberForm", new RegistMemberForm());
        return "member/registMember";
    }

    @PostMapping("/regist")
    public String postRegistMember(@Valid @ModelAttribute("memberForm") RegistMemberForm memberForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.debug("errors={}", bindingResult);
            return "member/registMember";
        }
        try {
            memberService.registMember(memberForm.getEmail(), memberForm.getPassword(), memberForm.getNickname());
        } catch (EntityDuplicateException e) {
            String duplicateColumn = e.getDuplicateColumn();
            bindingResult.rejectValue(duplicateColumn,"Duplicate");
            return "member/registMember";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLogin(Model model, HttpServletRequest request){
        model.addAttribute("memberForm", new LoginMemberForm());
        return "member/login";
    }

    @PostMapping("/login")
    public String postLogin(@Valid @ModelAttribute("memberForm") LoginMemberForm memberForm, BindingResult bindingResult, HttpServletRequest request){
        AuthenticationException exception = (AuthenticationException) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        if(exception instanceof BadCredentialsException){
            bindingResult.reject("BadCredentials");
        }else if(exception instanceof UsernameNotFoundException){
            bindingResult.reject("UserNotFound");
        }else if(exception != null){
            bindingResult.reject("AuthenticationException");
        }

        if(bindingResult.hasErrors()){
            log.warn("errors={}",bindingResult.getAllErrors());
            return "member/login";
        }

        return "forward:/member/login_process";
    }

    @GetMapping("/logout")
    public String getLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        logoutHandler.logout(request, response, authentication);
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String myPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model,Authentication authentication){

        // 멤버 정보 추출
        MemberAuthenticationToken memberAuthenticationToken = (MemberAuthenticationToken) authentication;
        MemberDto memberDto = new MemberDto(memberAuthenticationToken.getMember());

        // 멤버 작성글 조회
        Page<ReadPostDto> readPostDtos = memberService.readAllPostByMember(memberDto.getId(),page);

        model.addAttribute("member", memberDto);
        model.addAttribute("posts", readPostDtos.getContent());
        PageNumberInfo pageNumberInfo = new PageNumberInfo(readPostDtos.getNumber(), readPostDtos.getTotalPages() - 1);
        model.addAttribute("pages", pageNumberInfo);

        return "member/myPage";
    }

}
