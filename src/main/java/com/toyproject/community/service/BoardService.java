package com.toyproject.community.service;

import com.toyproject.community.domain.Board;
import com.toyproject.community.form.BoardForm;
import com.toyproject.community.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public void createBoard(String name, String description){
        Board board = Board.createBoard(name, description);
        boardRepository.save(board);
    }

    public List<Board> findAllBoard(){
        return boardRepository.findAll();
    }

    @Transactional
    public void deleteBoard(Long boardId){
        boardRepository.deleteById(boardId);
    }
}
