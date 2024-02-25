package com.toyproject.community.controller;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Post;
import com.toyproject.community.dto.BoardDto;
import com.toyproject.community.dto.ReadPostDto;
import com.toyproject.community.form.BoardForm;
import com.toyproject.community.service.BoardService;
import com.toyproject.community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/b")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final PostService postService;

    @GetMapping("/{boardName}")
    public String boardName(@PathVariable("boardName") String boardName, Model model){
        Board board;
        try{
            board = boardService.findByName(boardName);
        }catch (Exception e){
            // TODO error 페이지
            return "redirect:/b/list";
        }
        List<Post> posts = postService.readPostByBoardName(boardName);
        List<ReadPostDto> postDtos = posts.stream().map(ReadPostDto::new).collect(Collectors.toList());
        model.addAttribute("posts",postDtos);
        model.addAttribute("board", board);
        return "postListInBoard";
    }

    @GetMapping({"/list", "/"})
    public String readBoardList(Model model){
        List<Board> boardList = boardService.findAllBoard();
        List<BoardDto> boards = boardList.stream().map(b -> new BoardDto(b.getName(),b.getDescription())).collect(Collectors.toList());
        model.addAttribute("boards", boards);
        if(!boards.isEmpty()){
            log.info(boards.get(0).getDescription());
        }
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
        log.info("POST /b/create?desc=",boardForm.getDescription());
        return "redirect:/b/list";
    }
}
