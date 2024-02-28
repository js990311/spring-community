package com.toyproject.community.authentication;

import com.toyproject.community.domain.Member;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class MemberAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final Member member;

    public MemberAuthenticationToken(MemberDetails memberDetails){
        super(
                memberDetails.getMember().getEmail(),
                null, //memberDetails.getMember().getPassword(),
                memberDetails.getAuthorities()
        );
        this.member = memberDetails.getMember();
    }
}
