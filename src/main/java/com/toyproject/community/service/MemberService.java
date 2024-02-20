package com.toyproject.community.service;

import com.toyproject.community.domain.Comment;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.repository.CommentRepository;
import com.toyproject.community.repository.MemberRepository;
import com.toyproject.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private PostRepository postRepository;
    private MemberRepository memberRepository;
    private CommentRepository commentRepository;

    @Transactional
    public void registMember(String email, String password){
        Member member = Member.registMember(email, password);
        memberRepository.save(member);
    }

    public List<Post> readAllPostByMember(Long memberId){
        return postRepository.findByMemberId(memberId);
    }

    public List<Comment> readAllCommentByMember(Long memberId){
        return commentRepository.findByMemberId(memberId);
    }

}
