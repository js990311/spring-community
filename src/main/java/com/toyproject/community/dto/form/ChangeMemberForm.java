package com.toyproject.community.dto.form;

import com.toyproject.community.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ChangeMemberForm {
    private String email;
    @NotBlank
    private String nickname;
    @NotBlank
    private String password;

    private LocalDate nicknameModifiedDate;

    public ChangeMemberForm(){}

    public ChangeMemberForm(Member member){
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.nicknameModifiedDate = member.getNicknameModifiedDate();
    }
}
