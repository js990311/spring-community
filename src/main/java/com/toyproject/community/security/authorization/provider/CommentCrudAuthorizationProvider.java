package com.toyproject.community.security.authorization.provider;

import com.toyproject.community.domain.Comment;
import com.toyproject.community.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CommentCrudAuthorizationProvider implements MethodCrudAuthorizationProvider{
    private final CommentRepository commentRepository;
    private final String objectType;

    @Autowired
    public CommentCrudAuthorizationProvider(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        this.objectType = "comment";
    }

    @Override
    public boolean support(String objectType) {
        return objectType.equals(this.objectType);
    }

    @Override
    public boolean create(Long objectId, Authentication authentication) {
        return false;
    }

    @Override
    public boolean read(Long objectId, Authentication authentication) {
        return false;
    }

    @Override
    public boolean update(Long objectId, Authentication authentication) {
        Comment comment = commentRepository.findById(objectId).orElse(null);
        if(comment == null){
            return false;
        }

        return comment.getMember().getEmail().equals(authentication.getName());
    }

    @Override
    public boolean delete(Long objectId, Authentication authentication) {
        Comment comment = commentRepository.findById(objectId).orElse(null);

        if(comment == null){
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities){
            if(grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
                // Admin이면 삭제를 허용함
                return true;
            }
        }

        return comment.getMember().getEmail().equals(authentication.getName());
    }
}
