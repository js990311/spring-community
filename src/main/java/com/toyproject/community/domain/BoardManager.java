package com.toyproject.community.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class BoardManager {
    @Id
    @GeneratedValue
    @Column(name = "board_manager_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
}
