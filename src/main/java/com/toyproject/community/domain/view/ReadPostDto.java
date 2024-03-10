package com.toyproject.community.domain.view;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Post;
import com.toyproject.community.domain.dto.BoardDto;
import com.toyproject.community.domain.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Getter
@AllArgsConstructor
public class ReadPostDto {
    private Long id;
    private String title;
    private String content;
    private MemberDto writer;
    private BoardDto board;
    private String creationDateTime;
    private String modifiedDateTime;

    public ReadPostDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        if(post.getCreationDateTime() != null)
            this.creationDateTime = post.getCreationDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
        if(post.getModificationDateTime() != null)
            this.modifiedDateTime = post.getModificationDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));

        this.writer = new MemberDto(post.getMember());
        this.board = new BoardDto(post.getBoard());
    }
}
