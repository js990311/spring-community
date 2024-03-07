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

    @Column
    private String description;

    @OneToMany(mappedBy = "board")
    private List<Post> posts;


    Board(String name, String description){
        this.name = name;
        this.description = description;
    }

    public static Board createBoard(String name, String description){
        return new Board(name, description);
    }
}
