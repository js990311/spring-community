package com.toyproject.community.service;

import com.toyproject.community.domain.dto.MemberDto;
import com.toyproject.community.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void addMember(){
        // !주의!
//        memberService.registMember("testEmail@com", "pw", "nickname");
    }

    @Test
    public void memberRegistLogin(){
        try{
            memberService.registMember("testEmail1@com", "pw", "nickname");
        }catch (Exception e){
            System.out.println("check");
            System.out.println(e.toString());
            fail(e);
        }
    }

}