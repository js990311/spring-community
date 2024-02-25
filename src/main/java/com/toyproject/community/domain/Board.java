package com.toyproject.community.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "board")
    private List<BoardManager> boardManagers;

    @OneToMany(mappedBy = "board")
    private List<Post> posts;

    Board(String name){
        this.name = name;
    }

    public static Board createBoard(String name){
        return new Board(name);
    }
}
