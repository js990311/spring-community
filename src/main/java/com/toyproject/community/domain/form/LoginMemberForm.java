package com.toyproject.community.domain.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginMemberForm {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
