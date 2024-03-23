package com.toyproject.community.dto.response;

import com.toyproject.community.domain.Comment;
import com.toyproject.community.dto.BoardDto;
import lombok.Getter;

@Getter
public class ResponseMyPageCommentDto {
    private Long id;
    private Long parentId;
    private String parentWriter;
    private String writer;
    private String content;
    private Long postId;
    private BoardDto board;

    public ResponseMyPageCommentDto(Comment comment){
        this.id = comment.getId();
        if(comment.getParentComment()!=null) {
            this.parentId = comment.getParentComment().getId();
            this.parentWriter = comment.getParentComment().getMember().getEmail();
        }
        this.writer = comment.getMember().getEmail();
        this.content = comment.getContent();
        this.postId = comment.getPost().getId();
        this.board = new BoardDto(comment.getPost().getBoard());
    }

}
