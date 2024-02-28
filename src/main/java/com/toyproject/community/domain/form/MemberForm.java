package com.toyproject.community.domain.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
public class MemberForm {
    @Email @NotBlank
    private String email;
    @NotBlank
    private String password;
}
