package com.toyproject.community.dto.response;

import com.toyproject.community.domain.Post;
import com.toyproject.community.dto.BoardDto;
import com.toyproject.community.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Getter
@AllArgsConstructor
public class ResponsePostDto {
    private Long id;
    private String title;
    private String content;
    private Long viewCount;
    private String creationDateTime;
    private String modifiedDateTime;

    // member
    private MemberDto writer;

    // board
    private BoardDto board;


    public ResponsePostDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();

        if(post.getCreationDateTime() != null)
            this.creationDateTime = post.getCreationDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
        if(post.getModificationDateTime() != null)
            this.modifiedDateTime = post.getModificationDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));

        this.writer = new MemberDto(post.getMember());
        this.board = new BoardDto(post.getBoard());
    }
}
