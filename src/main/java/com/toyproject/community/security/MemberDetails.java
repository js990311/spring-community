package com.toyproject.community.security;

import com.toyproject.community.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class MemberDetails extends User {

    private Member member;

    public MemberDetails(Member member, Collection<? extends GrantedAuthority> authorities){
        super(member.getEmail(), member.getPassword(), authorities);
        this.member = member;
    }

}
