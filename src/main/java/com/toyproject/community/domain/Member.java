package com.toyproject.community.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @OneToMany(mappedBy = "member")
    private List<BoardManager> managedBoards;

    @OneToMany(mappedBy = "member")
    private List<Post> posts;

    @OneToMany(mappedBy = "member")
    private List<Comment> comments;

    Member(String email, String password){
        this.email = email;
        this.password = password;
    }

    public static Member registMember(String email, String password){
        return new Member(email, password);
    }
}
