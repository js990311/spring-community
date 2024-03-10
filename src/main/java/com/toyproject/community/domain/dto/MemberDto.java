package com.toyproject.community.domain.dto;

import com.toyproject.community.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String nickname;
    private LocalDate registDate;
    private LocalDate nicknameModifiedDate;

    public MemberDto(Member member){
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.registDate = member.getRegistDate();
        this.nicknameModifiedDate = member.getNicknameModifiedDate();
    }
}
