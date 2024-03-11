package com.toyproject.community.security.authorization.bean;

import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.repository.MemberRepository;
import com.toyproject.community.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Component("post_authz")
public class PostAuthorizationLogic {

    private final PostRepository postRepository;

    private Post findPostById(Long postId){
        return postRepository.findById(postId).orElse(null);
    }

    /* Controller level */

    public boolean decide_delete(Long postId, Authentication authentication){
        Post post = findPostById(postId);
        if(post == null){
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities){
            if(grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
                // Admin이면 삭제를 허용함
                return true;
            }
        }

        return post.getMember().getEmail().equals(authentication.getName());
    }

    public boolean decide_update(Long postId, Authentication authentication){
        Post post = findPostById(postId);
        if(post == null){
            return false;
        }

        return post.getMember().getEmail().equals(authentication.getName());
    }

    /* Service level */

    public boolean decide(Post post, Member member){
        if(post == null){
            return false;
        }
        return post.getMember().getId()
                .equals(member.getId());
    }

    public boolean decide(Long postId, Long memberId){
        Post post = findPostById(postId);
        return decide(post, memberId);
    }

    public boolean decide(Post post, Long memberId){
        if(post == null){
            return false;
        }
        return post.getMember().getId().equals(memberId);
    }

    public boolean decide(Long postId, Member member){
        Post post = findPostById(postId);
        return decide(post, member);
    }
}
