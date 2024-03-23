package com.toyproject.community.dto.response;

import com.toyproject.community.domain.Comment;
import lombok.Getter;

@Getter
public class ResponseCommentDto {
    Long id;
    Long parentId;
    String parentWriter;
    String writer;
    String content;

    public ResponseCommentDto(Comment comment){
        this.id = comment.getId();
        if(comment.getParentComment()!=null) {
            this.parentId = comment.getParentComment().getId();
            this.parentWriter = comment.getParentComment().getMember().getNickname();
        }
        this.writer = comment.getMember().getNickname();
        this.content = comment.getContent();
    }
}
