package com.toyproject.community.service;

import com.toyproject.community.domain.Comment;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.dto.MemberDto;
import com.toyproject.community.repository.CommentRepository;
import com.toyproject.community.repository.MemberRepository;
import com.toyproject.community.repository.PostRepository;
import com.toyproject.community.security.MemberDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long registMember(String email, String password){
        String encodedPassword = passwordEncoder.encode(password);
        Member member = Member.registMember(email, encodedPassword);
        memberRepository.save(member);
        return member.getId();
    }

    public MemberDto loginMember(String email, String password){
        Member member = memberRepository.findByEmail(email).orElseThrow(
                ()-> new EntityNotFoundException("member not found"+email)
        );
        if(member.getPassword().equals(password)){
           return new MemberDto(member.getId(),email);
        }else{
            throw new RuntimeException();
        }
    }

    public List<Post> readAllPostByMember(Long memberId){
        return postRepository.findByMemberId(memberId);
    }

    public List<Comment> readAllCommentByMember(Long memberId){
        return commentRepository.findByMemberId(memberId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow(
                ()-> {
                    return new UsernameNotFoundException("user not found" + username);
                }
        );
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        // User user = new User(member.getEmail(), member.getPassword(), authorities);
        User user = new MemberDetails(member, authorities);
        return user;
    }
}
