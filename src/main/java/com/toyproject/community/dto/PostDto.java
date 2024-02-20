package com.toyproject.community.dto;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostDto {
    private Member member;
    private Board board;
    private String title;
    private String content;

    public PostDto() {

    }
}
