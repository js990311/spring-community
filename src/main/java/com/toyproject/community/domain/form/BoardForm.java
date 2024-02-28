package com.toyproject.community.domain.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BoardForm {
    @NotBlank
    private String name;
    private String description;
}
