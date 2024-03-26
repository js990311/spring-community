package com.toyproject.community.security.authentication;

import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.role.MemberRole;
import com.toyproject.community.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public MemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow(
                ()-> {
                    return new UsernameNotFoundException("user not found" + username);
                }
        );
        List<GrantedAuthority> authorities = new ArrayList<>();

        member.getMemberRoles().forEach(role->{
            String auth = role.getRole().getRoleName().toString();
            authorities.add(
                    new SimpleGrantedAuthority(auth)
            );
        });

        User user = new MemberDetails(member, authorities);
        return user;
    }
}
