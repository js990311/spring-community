package com.toyproject.community.dto;

import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import lombok.Getter;

@Getter
public class CommentDto {
    String content;
    Member writer;
    Post post;
}
