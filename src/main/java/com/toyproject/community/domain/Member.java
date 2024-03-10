package com.toyproject.community.domain;

import com.toyproject.community.domain.role.MemberRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column
    private String password;

    @NotNull
    @Column(unique = true)
    private String nickname;

    @OneToMany(mappedBy = "member")
    private List<Post> posts;

    @OneToMany(mappedBy = "member")
    private List<Comment> comments;

    @OneToMany(mappedBy = "member")
    private List<MemberRole> memberRoles;

    Member(String email, String password,String nickname){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public static Member registMember(String email, String password, String nickname){
        return new Member(email, password,nickname);
    }
}
