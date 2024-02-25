package com.toyproject.community.controller;

import com.toyproject.community.domain.Board;
import com.toyproject.community.dto.BoardDto;
import com.toyproject.community.form.BoardForm;
import com.toyproject.community.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/b")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/{boardName}")
    public String boardName(@PathVariable("boardName") String boardName, Model model){
        return "redirect:/";
    }

    @GetMapping("/list")
    public String readBoardList(Model model){
        List<Board> boardList = boardService.findAllBoard();
        List<BoardDto> boards = boardList.stream().map(b -> new BoardDto(b.getName(),b.getDescription())).collect(Collectors.toList());
        model.addAttribute("boards", boards);
        return "boardList";
    }

    @GetMapping("/create")
    public String createBoardView(Model model){
        BoardForm boardForm = new BoardForm();
        model.addAttribute(boardForm);
        return "createBoard";
    }

    @PostMapping("/create")
    public String createBoard(@ModelAttribute BoardForm boardForm){
        boardService.createBoard(boardForm.getName(), boardForm.getDescription());
        return "redirect:/b/list";
    }
}
