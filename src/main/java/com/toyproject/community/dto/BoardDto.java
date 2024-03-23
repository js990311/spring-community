package com.toyproject.community.dto;

import com.toyproject.community.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardDto {
    private Long id;
    private String name;
    private String description;

    public BoardDto(Board board){
        this.id = board.getId();
        this.name = board.getName();
        this.description = board.getDescription();
    }
}
