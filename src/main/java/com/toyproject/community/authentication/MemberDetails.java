package com.toyproject.community.authentication;

import com.toyproject.community.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class MemberDetails extends User {

    private Member member;

    public MemberDetails(Member member, Collection<? extends GrantedAuthority> authorities){
        super(member.getEmail(), member.getPassword(), authorities);
        this.member = member;
    }

}
