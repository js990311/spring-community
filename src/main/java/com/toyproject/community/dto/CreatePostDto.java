package com.toyproject.community.dto;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 외부로 나가지 않고 Service 계층에서 Post 작성을 위해 사용됨
 */
@Getter
@AllArgsConstructor
public class CreatePostDto {
    private Member member;
    private Board board;
    private String title;
    private String content;
}
