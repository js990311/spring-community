package com.toyproject.community.dto.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BoardForm {
    @NotBlank
    private String name;
    private String description;
}
