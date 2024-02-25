package com.toyproject.community.form;

import lombok.Data;
import lombok.Getter;

@Data
public class PostForm {
    private String title;
    private String content;
    private Long boardId;
}
