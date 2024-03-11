package com.toyproject.community.controller;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.dto.BoardDto;
import com.toyproject.community.domain.view.PageNumberInfo;
import com.toyproject.community.domain.view.ReadPostDto;
import com.toyproject.community.domain.form.BoardForm;
import com.toyproject.community.service.BoardService;
import com.toyproject.community.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @GetMapping("/{boardId}")
    public String boardName(
            @PathVariable("boardId") Long boardId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model){
        Board board;
        try{
            board = boardService.findById(boardId);
        }catch (Exception e){
            // TODO error 페이지
            return "redirect:/b/list";
        }

        Page<ReadPostDto> readPostDtos = postService.readPostByBoardId(boardId, page);
        PageNumberInfo pageNumberInfo = new PageNumberInfo(readPostDtos.getNumber(), readPostDtos.getTotalPages() - 1);

        model.addAttribute("posts",readPostDtos.getContent());
        model.addAttribute("board", board);
        model.addAttribute("pages", pageNumberInfo);

        return "postListInBoard";
    }

    @GetMapping({"/list", "/"})
    public String readBoardList(Model model){
        List<Board> boardList = boardService.findAllBoard();
        List<BoardDto> boards = boardList.stream().map(BoardDto::new).collect(Collectors.toList());
        model.addAttribute("boards", boards);
        if(!boards.isEmpty()){
            log.info(boards.get(0).getDescription());
        }
        return "boardList";
    }

    @GetMapping("/create")
    @PreAuthorize("@authz.decide(#root)")
    public String createBoardView(Model model){
        BoardForm boardForm = new BoardForm();
        model.addAttribute(boardForm);
        return "createBoard";
    }

    @PostMapping("/create")
    public String createBoard(@Valid @ModelAttribute BoardForm boardForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.debug("errors = {}", bindingResult);
            return "createBoard";
        }
        try{
            boardService.createBoard(boardForm.getName(), boardForm.getDescription());
        }catch (Exception e){
            bindingResult.rejectValue("name","Duplicate");
            return "createBoard";
        }

        return "redirect:/b/list";
    }
}
