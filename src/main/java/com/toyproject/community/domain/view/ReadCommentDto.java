package com.toyproject.community.domain.view;

import com.toyproject.community.domain.Comment;
import lombok.Data;
import lombok.Getter;

@Getter
public class ReadCommentDto {
    Long id;
    String writer;
    String content;

    public ReadCommentDto(Comment comment){
        this.id = comment.getId();
        this.writer = comment.getMember().getEmail();
        this.content = comment.getContent();
    }
}
