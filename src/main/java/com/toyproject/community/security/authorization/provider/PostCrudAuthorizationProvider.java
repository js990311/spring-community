package com.toyproject.community.security.authorization.provider;

import com.toyproject.community.domain.Post;
import com.toyproject.community.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PostCrudAuthorizationProvider implements MethodCrudAuthorizationProvider{
    private final PostRepository postRepository;
    private final String objectType;

    @Autowired
    public PostCrudAuthorizationProvider(PostRepository postRepository) {
        this.postRepository = postRepository;
        this.objectType = "post";
    }

    @Override
    public boolean support(String objectType) {
        return this.objectType.equals(objectType);
    }

    @Override
    public boolean create(Long objectId, Authentication authentication) {
        return true;
    }

    @Override
    public boolean read(Long objectId, Authentication authentication) {
        return true;
    }

    @Override
    public boolean update(Long objectId, Authentication authentication) {
        Post post = postRepository.findById(objectId).orElse(null);
        if(post == null){
            return false;
        }

        return post.getMember().getEmail().equals(authentication.getName());
    }

    @Override
    public boolean delete(Long objectId, Authentication authentication) {
        Post post = postRepository.findById(objectId).orElse(null);
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
}
