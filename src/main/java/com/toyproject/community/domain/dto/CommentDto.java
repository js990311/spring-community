package com.toyproject.community.domain.dto;

import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDto {
    String content;
    Member writer;
    Post post;
}
