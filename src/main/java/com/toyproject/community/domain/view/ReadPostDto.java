package com.toyproject.community.domain.view;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Post;
import com.toyproject.community.domain.dto.BoardDto;
import com.toyproject.community.domain.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Member;

@Getter
@AllArgsConstructor
public class ReadPostDto {
    private Long id;
    private String title;
    private String content;
    private MemberDto writer;
    private BoardDto board;

    public ReadPostDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = new MemberDto(post.getMember());
        this.board = new BoardDto(post.getBoard());
    }
}
