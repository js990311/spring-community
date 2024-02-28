package com.toyproject.community.domain.dto;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePostDto {
    private Member member;
    private Board board;
    private String title;
    private String content;
}
