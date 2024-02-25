package com.toyproject.community.form;

import lombok.Data;
import lombok.Getter;

@Data
public class CommentForm {
    private Long postId;
    private String commentContent;
}
