package com.toyproject.community.dto.form;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
public class CommentForm {
    @NotNull
    private Long postId;
    @NotBlank
    private String commentContent;
}
