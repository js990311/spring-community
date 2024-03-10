package com.toyproject.community.service;

import com.toyproject.community.domain.Comment;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.domain.view.ReadPostDto;
import com.toyproject.community.domain.role.MemberRole;
import com.toyproject.community.exception.EntityDuplicateException;
import com.toyproject.community.repository.CommentRepository;
import com.toyproject.community.repository.MemberRepository;
import com.toyproject.community.repository.PostRepository;
import com.toyproject.community.security.authentication.MemberDetails;
import com.toyproject.community.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Transactional
    public Long registMember(String email, String password, String nickname) throws EntityDuplicateException {
        if(checkRegistMemberDuplicate(email,password)){
            if(memberRepository.existsByEmail(email)){
                throw new EntityDuplicateException("email");
            }else {
                throw new EntityDuplicateException("nickname");
            }
        }
        String encodedPassword = passwordEncoder.encode(password);
        Member member = Member.registMember(email, encodedPassword, nickname);
        memberRepository.save(member);
        roleService.registMember(member);
        return member.getId();
    }

    public boolean checkRegistMemberDuplicate(String email, String nickname){
        return memberRepository.existsByEmailOrNickname(email, nickname);
    }

    public List<ReadPostDto> readAllPostByMember(Long memberId){
        List<Post> memberPost = postRepository.findByMemberId(memberId);
        List<ReadPostDto> readPostDtos = memberPost.stream().map(ReadPostDto::new).collect(Collectors.toList());
        return readPostDtos;
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
        List<MemberRole> memberRoles = member.getMemberRoles();

        // TODO
        member.getMemberRoles().forEach(role->{
            String auth = role.getRole().getRoleName().toString();
            authorities.add(
                    new SimpleGrantedAuthority(auth)
            );
        });

        // User user = new User(member.getEmail(), member.getPassword(), authorities);
        User user = new MemberDetails(member, authorities);
        return user;
    }
}
