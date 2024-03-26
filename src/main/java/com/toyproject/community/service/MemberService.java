package com.toyproject.community.service;

import com.toyproject.community.domain.Comment;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.dto.MemberDto;
import com.toyproject.community.dto.form.ChangeMemberForm;
import com.toyproject.community.dto.response.ResponseMyPageCommentDto;
import com.toyproject.community.dto.response.ResponsePostDto;
import com.toyproject.community.domain.role.MemberRole;
import com.toyproject.community.exception.EntityDuplicateException;
import com.toyproject.community.repository.CommentRepository;
import com.toyproject.community.repository.MemberRepository;
import com.toyproject.community.repository.PostRepository;
import com.toyproject.community.security.authentication.MemberDetails;
import com.toyproject.community.service.role.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
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
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final PostRepository postRepository;
        private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;


    /**
     * 회원가입 처리
     * @param email 이메일
     * @param password 비밀번호
     * @param nickname 닉네임
     * @return 회원가입이 성공할 시 해당 회원의 id return함
     * @throws EntityDuplicateException 해당 이메일로 등록한 회원이 이미 존재
     */
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

    /**
     * 해당 이메일 혹은 닉네임을 가지고 있는 지 체크
     * @param email 이메일
     * @param nickname 닉네임
     * @return 중복회원존재여부
     */
    public boolean checkRegistMemberDuplicate(String email, String nickname){
        return memberRepository.existsByEmailOrNickname(email, nickname);
    }

    /**
     * mypage에서 회원이 작성한 게시글 목록
     * @param memberId
     * @param page
     * @return
     */
    public Page<ResponsePostDto> readAllPostByMember(Long memberId, int page){
        PageRequest pageRequest = PageRequest.of(
                page,
                15,
                Sort.by(
                        Sort.Direction.DESC,
                        "creationDateTime"
                )
        );

        Page<Post> memberPosts = postRepository.findPageByMemberId(memberId, pageRequest);
        Page<ResponsePostDto> readPostDtos = memberPosts.map(ResponsePostDto::new);
        return readPostDtos;
    }

    public Page<ResponsePostDto> readAllPostByMember(Long memberId){
        return readAllPostByMember(memberId,0);
    }

    public Page<ResponseMyPageCommentDto> readAllCommentByMember(Long memberId, int page){
        PageRequest pageRequest = PageRequest.of(
                page,
                15,
                Sort.by(
                        Sort.Direction.DESC,
                        "creationDateTime"
                )
        );
        Page<Comment> comments = commentRepository.findPageByMemberId(memberId, pageRequest);
        Page<ResponseMyPageCommentDto> commentDtos = comments.map(ResponseMyPageCommentDto::new);
        return commentDtos;
    }

    @Transactional
    public void changeMemberInfo(ChangeMemberForm changeMemberForm) throws EntityDuplicateException {
        Member member = memberRepository.findByEmail(changeMemberForm.getEmail()).orElseThrow(EntityNotFoundException::new);

        if(!passwordEncoder.matches(changeMemberForm.getPassword(), member.getPassword())){
            throw new BadCredentialsException("password not matches");
        }

        boolean isDuplicateNickname = memberRepository.existsByNickname(changeMemberForm.getNickname());
        if(isDuplicateNickname){
            throw new EntityDuplicateException("nickname duplicate");
        }

        member.changeNickname(changeMemberForm.getNickname());
    }

    public MemberDto findMemberDtoByUsername(String username) {
        Member member = memberRepository.findByEmail(username).orElseThrow(EntityNotFoundException::new);
        MemberDto memberDto = new MemberDto(member);
        return memberDto;
    }
}
