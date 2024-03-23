package com.toyproject.community.dto.form;

import com.toyproject.community.domain.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
public class PostForm {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Long boardId;
}
