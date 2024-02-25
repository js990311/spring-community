package com.toyproject.community.dto;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadPostDto {
    private String title;
    private String content;
    private String memberName;

    public ReadPostDto(Post post){
        this.title = post.getTitle();
        this.content = post.getContent();
        this.memberName = post.getMember().getEmail();
    }
}
