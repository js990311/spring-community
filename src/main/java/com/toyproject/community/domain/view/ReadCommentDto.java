package com.toyproject.community.domain.view;

import com.toyproject.community.domain.Comment;
import lombok.Data;
import lombok.Getter;

@Getter
public class ReadCommentDto {
    Long id;
    Long parentId;
    String parentWriter;
    String writer;
    String content;

    public ReadCommentDto(Comment comment){
        this.id = comment.getId();
        if(comment.getParentComment()!=null) {
            this.parentId = comment.getParentComment().getId();
            this.parentWriter = comment.getParentComment().getMember().getEmail();
        }
        this.writer = comment.getMember().getEmail();
        this.content = comment.getContent();
    }
}
