package com.toyproject.community.dto;

import com.toyproject.community.domain.Comment;
import lombok.Data;
import lombok.Getter;

@Getter
public class ReadCommentDto {
    String writer;
    String content;

    public ReadCommentDto(Comment comment){
        this.writer = comment.getMember().getEmail();
        this.content = comment.getContent();
    }
}
