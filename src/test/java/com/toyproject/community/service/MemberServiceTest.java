package com.toyproject.community.service;

import com.toyproject.community.domain.Member;
import com.toyproject.community.dto.MemberDto;
import com.toyproject.community.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.plaf.metal.MetalMenuBarUI;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;

    @Test
    public void memberRegistLogin(){
        Long memberId = memberService.registMember("testEmail", "pw");

        assertThrows(EntityNotFoundException.class,()->{
           memberService.loginMember("not found email", "123");
        });

        assertThrows(RuntimeException.class, () ->{
            memberService.loginMember("testEmail", "invalid password");
        });

        MemberDto memberDto = memberService.loginMember("testEmail", "pw");
        assertEquals(memberId, memberDto.getId());
    }

}